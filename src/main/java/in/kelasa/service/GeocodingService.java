package in.kelasa.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import in.kelasa.model.Address;
import in.kelasa.model.CustomGeoLocation;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by rajeevguru on 27/11/15.
 */
@Service
public class GeocodingService {

    private final String api_key = "AIzaSyCzyTdznyTD31DFTKXyp1Yjgwjo-ybMvq4";

    private GeoApiContext context;

    @PostConstruct
    public void init () {
        context = new GeoApiContext().setApiKey(api_key);
    }

    public CustomGeoLocation geocode(Address address) {

        try {
            GeocodingResult[] results =GeocodingApi.geocode(context,address.toFormattedAddress()).await();

            LatLng l = results[0].geometry.location;

           CustomGeoLocation point = new CustomGeoLocation();
            point.setCoordinates(l);


            return point;
        } catch (Exception e) {
            e.printStackTrace();

        }


        return null;
    }
}
