package in.asclepius.app.models;

import com.google.firebase.database.PropertyName;

public class ModelRating {

    private double rating;
    private AppUser ratedBy;
    private String ratedOn;
    private String description;

    public ModelRating(double rating, AppUser ratedBy, String ratedOn, String description) {
        this.rating = rating;
        this.ratedBy = ratedBy;
        this.ratedOn = ratedOn;
        this.description = description;
    }

    public ModelRating() {

    }

    public ModelRating(Object object) {
        ModelRating temp = (ModelRating) object;
        this.ratedBy = ((ModelRating) object).getRatedBy();
        this.ratedOn = ((ModelRating) object).ratedOn;
        this.description = ((ModelRating) object).description;
        this.rating = ((ModelRating) object).rating;
    }

    @PropertyName("rating")
    public double getRating() {
        return rating;
    }

    @PropertyName("rating")
    public void setRating(double rating) {
        this.rating = rating;
    }

    @PropertyName("ratedBy")
    public AppUser getRatedBy() {
        return ratedBy;
    }

    @PropertyName("ratedBy")
    public void setRatedBy(AppUser ratedBy) {
        this.ratedBy = ratedBy;
    }

    @PropertyName("ratedOn")
    public String getRatedOn() {
        return ratedOn;
    }

    @PropertyName("ratedOn")
    public void setRatedOn(String ratedOn) {
        this.ratedOn = ratedOn;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    @PropertyName("description")
    public void setDescription(String description) {
        this.description = description;
    }
}
