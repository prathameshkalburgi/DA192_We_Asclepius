package in.asclepius.app.models;

import android.util.Log;

import com.google.firebase.database.PropertyName;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Doctors {

    private String availableOn;
    private String name;
    private String experience;
    private int fees;
    private String fullName;
    private String hospital;
    private float rating;
    private int specialityId;
    private String speciality;
    private LocationClass location;
    private int distance;
    private List<ModelRating> ratings;


    public Doctors(@NotNull ModelDoctorFirebase doctor, LocationClass userLocation) {
        name = doctor.getFullName();
        availableOn = "Monday";
        rating = doctor.getRating();
        speciality = doctor.getSpeciality();
        fullName = doctor.getFullName();
        hospital = doctor.getHospital();
        location = doctor.getLocation();

        int count = 0;
        double sum = 0;

        if (doctor.getRatings() != null) {

            Iterator it = doctor.getRatings().entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                Object object = pair.getValue();
                ModelRating tempRate = new ModelRating(object);
                Gson gson = new Gson();
                ratings = new ArrayList<>();
                ratings.add(tempRate);
                count++;
                sum = (sum + tempRate.getRating());
            }

            if (sum >= 0) {
                rating = (float) (sum / count);
            }
        }

        distance = (int) distance(doctor.getLocation().getLat(), doctor.getLocation().getLongitude(), userLocation.getLat(), userLocation.getLongitude(), 'K');
    }

    public Doctors() {
    }

    @PropertyName("ratings")
    public List<ModelRating> getRatings() {
        return ratings;
    }

    @PropertyName("ratings")
    public void setRatings(List<ModelRating> ratings) {
        this.ratings = ratings;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(int specialityId) {
        this.specialityId = specialityId;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getAvailableOn() {
        return availableOn;
    }

    public void setAvailableOn(String availableOn) {
        this.availableOn = availableOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ClassPojo [availableOn = " + availableOn + ", name = " + name + "]";
    }

    public static Doctors[] getDoctorsAvailableOnDay(Doctors[] doctors, String availableOn) {
        ArrayList<Doctors> tempList = new ArrayList<>();
        for (int i = 0; i < doctors.length; i++) {
            Log.d("Doctors", "Comparing : required -> " + availableOn + " with actual : " + doctors[i].getAvailableOn());
            if (doctors[i].getAvailableOn().equalsIgnoreCase(availableOn)) {
                tempList.add(doctors[i]);
            }
        }
        return tempList.toArray(new Doctors[0]);
    }

    @NotNull
    public static List<Doctors> filterDoctorsByName(@NotNull List<Doctors> modelList, @NotNull String doctorName) {
        ArrayList<Doctors> doctors = new ArrayList<>();
        Log.d("DoctorFilter", "data : to search " + doctorName);
        for (int i = 0; i < modelList.size(); i++) {
            try {
                Log.d("DoctorFilter", "Comparing : " + modelList.get(i).getFullName().toLowerCase() + " result -> " + modelList.get(i).getFullName().toLowerCase().contains(doctorName.toLowerCase()));
                if (modelList.get(i).getFullName() != null) {
                    if (modelList.get(i).getFullName().toLowerCase().contains(doctorName.toLowerCase())) {
                        doctors.add(modelList.get(i));

                    }
                }

            } catch (Exception e) {
                Log.e("DoctorFilter", "Error while filtering: " + e);
            }

        }

        return doctors;
    }

    @PropertyName("location")
    public LocationClass getLocation() {
        return location;
    }

    @PropertyName("location")
    public void setLocation(LocationClass location) {
        this.location = location;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }


    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}