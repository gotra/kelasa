package in.kelasa.service;

import com.google.maps.model.LatLng;
import com.mongodb.WriteResult;
import in.kelasa.model.Agency;
import in.kelasa.model.CustomGeoLocation;
import in.kelasa.model.User;
import org.bson.types.ObjectId;
import org.jongo.Aggregate;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jongo.ResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by rajeevguru on 04/11/15.
 */
@Service
public class AgencyService {

    @Autowired
    private MongoCollection agencies;

    public Agency create(Agency agency) {


        agencies.save(agency);
        return agency;
    }

    public Agency getAgency(String id) {
        Agency agency = agencies.findOne(new ObjectId(id)).as(Agency.class);
        return agency;
    }

    public Iterable<Agency> getAgenciesByOwner(String username) {
        Iterable<Agency> agencyList = agencies.find("{owningUser: #}",username).as(Agency.class);

        return agencyList;
    }

    public Iterable<Agency> getAgencies() {
        Iterable<Agency> agencyList = agencies.find().as(Agency.class);
        return agencyList;
    }

    public Agency update(Agency agency) {

       WriteResult result =  agencies.save(agency);
       return agency;

    }

    public boolean deleteAgency(String id) {
        WriteResult result = agencies.remove(new ObjectId(id));
        boolean res = result.getN() > 0 ? true : false;
        return res;
    }

    public List<Agency> findAgenciesForSkill(String skill, LatLng location) {



        Iterable<Agency> agenciesCursor = agencies.find(
        "{\"address.geoLocation\": { $near: { $geometry: { type: \"Point\",  coordinates: #  },$maxDistance: #}},"
                + " \"skills.name\" : #}}"
                , Arrays.asList(location.lng,location.lat),   10000,Pattern.compile(skill)).as(Agency.class);

        List<Agency> agencyList = StreamSupport.stream(agenciesCursor.spliterator(),false)
                .collect(Collectors.toList());

        return agencyList;
    }

    /**
     * db.agencies.aggregate([{ $geoNear : { near : { type: "Point",coordinates: [ 4.3227469, 50.877559 ]},
     * distanceField:"address.distance",spherical: true,maxDistance:150000}},{ $project : { "skills":1,_id:0}},
     * {$unwind:"$skills"},{$group:{_id:"$skills.name",avg:{$avg:"$skills.ratePerHour"}}}])
     */


    private static class AggregateResult {
        String _id;
        double avg;
    }

    public Map<String,Double> getAveragePrices(Double maxDistance, LatLng location) {

     Iterable<AggregateResult> aggregateResults =  agencies.aggregate("{ $geoNear : { near : { type: \"Point\",coordinates: # }," +
                "distanceField:\"address.distance\",spherical: true,maxDistance:#}}",Arrays.asList(location.lng,location.lat),maxDistance)
                .and("{ $project : { skills : 1, _id : 0}}")
             .and("{$unwind: \"$skills\"}")
                .and("{$group:{_id:\"$skills.name\",avg:{$avg:\"$skills.ratePerHour\"}}}")
                .as(AggregateResult.class);


        Map<String,Double> results = new HashMap<>();

     aggregateResults.forEach((aggregateResult) -> results.put(aggregateResult._id,aggregateResult.avg));
        return results;
    }





}
