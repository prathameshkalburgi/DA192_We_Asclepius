package in.asclepius.app.models;

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
}