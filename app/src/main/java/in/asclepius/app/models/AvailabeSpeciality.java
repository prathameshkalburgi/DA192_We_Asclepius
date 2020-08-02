package in.asclepius.app.models;


public class AvailabeSpeciality {
    private String specialityName;

    private Doctors[] doctors;

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }

    public Doctors[] getDoctors() {
        return doctors;
    }

    public void setDoctors(Doctors[] doctors) {
        this.doctors = doctors;
    }

    @Override
    public String toString() {
        return "ClassPojo [specialityName = " + specialityName + ", doctors = " + doctors + "]";
    }
}