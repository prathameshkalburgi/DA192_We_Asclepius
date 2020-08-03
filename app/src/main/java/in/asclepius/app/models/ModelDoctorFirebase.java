package in.asclepius.app.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class ModelDoctorFirebase {

    private String experience;
    private int fees;
    private String fullName;
    private String hospital;
    private float rating;
    private int specialityId;
    private String speciality;

    public ModelDoctorFirebase() {

    }

    public ModelDoctorFirebase(String experience, int fees, String fullName, String hospital, float rating, int specialityId, String speciality) {
        this.experience = experience;
        this.fees = fees;
        this.fullName = fullName;
        this.hospital = hospital;
        this.rating = rating;
        this.specialityId = specialityId;
        this.speciality = speciality;
    }

    @PropertyName("experience")
    public String getExperience() {
        return experience;
    }


    @PropertyName("experience")
    public void setExperience(String experience) {
        this.experience = experience;
    }

    @PropertyName("fees")
    public int getFees() {
        return fees;
    }

    @PropertyName("fees")
    public void setFees(int fees) {
        this.fees = fees;
    }

    @PropertyName("fullName")
    public String getFullName() {
        return fullName;
    }

    @PropertyName("fullName")
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @PropertyName("hospital")
    public String getHospital() {
        return hospital;
    }

    @PropertyName("hospital")
    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    @PropertyName("rating")
    public float getRating() {
        return rating;
    }

    @PropertyName("rating")
    public void setRating(float rating) {
        this.rating = rating;
    }

    @PropertyName("specialityId")
    public int getSpecialityId() {
        return specialityId;
    }

    @PropertyName("specialityId")
    public void setSpecialityId(int specialityId) {
        this.specialityId = specialityId;
    }

    @PropertyName("speciality")
    public String getSpeciality() {
        return speciality;
    }

    @PropertyName("speciality")
    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
