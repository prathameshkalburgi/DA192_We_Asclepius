package in.asclepius.app.models;

import android.util.Log;

import java.util.ArrayList;

public class Doctors {
    private String availableOn;

    private String name;

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

}