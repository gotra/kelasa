package in.kelasa.model;

import com.google.api.client.util.StringUtils;

/**
 * Created by rajeevguru on 25/11/15.
 */
public class Address {

    private String streetAddress;
    private String city;
    private String postal;
    private String region;
    private String country;
    private CustomGeoLocation geoLocation;

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public CustomGeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(CustomGeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    @Override
    public String toString() {
        return "Address{" +
                "streetAddress='" + streetAddress + '\'' +
                ", city='" + city + '\'' +
                ", postal='" + postal + '\'' +
                ", region='" + region + '\'' +
                ", country='" + country + '\'' +
                ", geoLocation=" + geoLocation +
                '}';
    }

    public String toFormattedAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(streetAddress != null ? streetAddress:"")
                .append(city != null ? city + " , ":"")
                .append(region!= null? region + " , ":"")
                .append(postal!= null ? postal + " , ":"")
                .append(country!=null ? country:"");
        return sb.toString();
    }


}
