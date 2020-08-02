package in.asclepius.app.models;

public class OpdDepartments {
    private String departmentName;

    private AvailabeSpeciality[] availabeSpeciality;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public AvailabeSpeciality[] getAvailabeSpeciality() {
        return availabeSpeciality;
    }

    public void setAvailabeSpeciality(AvailabeSpeciality[] availabeSpeciality) {
        this.availabeSpeciality = availabeSpeciality;
    }

    @Override
    public String toString() {
        return "ClassPojo [departmentName = " + departmentName + ", availabeSpeciality = " + availabeSpeciality + "]";
    }
}
