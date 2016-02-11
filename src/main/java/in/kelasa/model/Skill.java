package in.kelasa.model;

/**
 * Created by rajeevguru on 25/11/15.
 */
public class Skill {

    private String name;
    private double ratePerHour;
    private double minimumHours;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRatePerHour() {
        return ratePerHour;
    }

    public void setRatePerHour(double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    public double getMinimumHours() {
        return minimumHours;
    }

    public void setMinimumHours(double minimumHours) {
        this.minimumHours = minimumHours;
    }
}
