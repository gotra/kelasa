package in.kelasa.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.maps.model.LatLng;


/**
 * Created by rajeevguru on 30/11/15.
 */

public class CustomGeoLocation  {

    @JsonSerialize(using = CustomLatLngSerializer.class)
    @JsonDeserialize(using = CustomLatLngDeserializer.class)

    private LatLng coordinates;

    private final String type = "Point";

    public CustomGeoLocation() {

    }

    public CustomGeoLocation(LatLng coordinates) {

        this.coordinates = coordinates;
    }

    public CustomGeoLocation(double longitude, double latitude) {

        this.coordinates = new LatLng(latitude,longitude);
    }


    public LatLng getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(LatLng coordinates) {

        this.coordinates = coordinates;
    }


    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(!(o instanceof CustomGeoLocation)) {
            return false;
        } else if(!super.equals(o)) {
            return false;
        } else {
            boolean var10000;
            label39: {
                CustomGeoLocation customGeoLocation = (CustomGeoLocation)o;
                if(this.coordinates != null) {
                    if(this.coordinates.equals(customGeoLocation.coordinates)) {
                        break label39;
                    }
                } else if(customGeoLocation.coordinates == null) {
                    break label39;
                }

                var10000 = false;
                return var10000;
            }

            var10000 = true;
            return var10000;
        }
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (this.coordinates != null?this.coordinates.hashCode():0);
        return result;
    }

    public String toString() {
        return "CustomGeoLocation{coordinates=" + this.coordinates + "} ";
    }



}
