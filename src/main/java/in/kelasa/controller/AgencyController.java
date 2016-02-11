package in.kelasa.controller;


import com.google.maps.model.LatLng;
import in.kelasa.model.Agency;
import in.kelasa.model.CustomGeoLocation;
import in.kelasa.service.AgencyService;
import in.kelasa.service.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by rajeevguru on 25/11/15.
 */
@RestController
@RequestMapping(value = "/api/agency")
public class AgencyController extends AbstractController {


    @Autowired
    AgencyService agencyService;

    @Autowired
    GeocodingService geocodingService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createAgency(@Valid @RequestBody Agency agency) {




        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        agency.setOwningUser(auth.getName());

        if (!StringUtils.isEmpty(agency.getAddress().toFormattedAddress())) {

            CustomGeoLocation geoLocation = geocodingService.geocode(agency.getAddress());
            if (geoLocation != null) {

                agency.getAddress().setGeoLocation(geoLocation);

            }


        }





            agencyService.create(agency);


        return new ResponseEntity<Agency>(agency, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAgencyById(@PathVariable String id) {

        Agency agency = agencyService.getAgency(id);
        if (agency == null) {
            ErrorMessage errorMessage = new ErrorMessage("agency not found");
            return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Agency>(agency, HttpStatus.OK);


    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateAgency(@Valid @RequestBody Agency agency) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        agency.setOwningUser(auth.getName());
        agencyService.update(agency);
        return new ResponseEntity<Agency>(agency, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAgency(@PathVariable String id) {

        boolean result = agencyService.deleteAgency(id);
        if (!result) {
            ErrorMessage errorMessage = new ErrorMessage("agency not found");
            return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);


    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAgencies() {

        Iterable<Agency> list =  agencyService.getAgencies();

        List<Agency> listconv = StreamSupport.stream(list.spliterator(),
                false).collect(Collectors.toList());

        return new ResponseEntity<Iterable<Agency>>(listconv, HttpStatus.OK);


    }


    @RequestMapping(method = RequestMethod.GET,value = "/find")
    public ResponseEntity<?> findAgenciesWithLatLng(@RequestParam String skill, @RequestParam Double[] location) {

        LatLng l = new LatLng(location[1],location[0]);


        List<Agency> listconv = agencyService.findAgenciesForSkill(skill,l);

        return new ResponseEntity<Iterable<Agency>>(listconv, HttpStatus.OK);


    }


    @RequestMapping(method = RequestMethod.GET,value = "/getaverages")
    public ResponseEntity<?>  getAgencyAveragePrice(@RequestParam(required = false,defaultValue = "200000") Double maxDistance, @RequestParam Double[] location) {


        LatLng l = new LatLng(location[1],location[0]);

        Map<String,Double> averages =  agencyService.getAveragePrices(maxDistance,l);

        return new ResponseEntity<Map<String,Double>>(averages, HttpStatus.OK);
    }








}
