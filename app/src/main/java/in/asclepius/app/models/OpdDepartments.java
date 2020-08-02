package in.asclepius.app.models;

import java.util.ArrayList;

public class OpdDepartments {
    private String departmentName;

    private ArrayList<AvailabeSpeciality> availabeSpeciality;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public ArrayList<AvailabeSpeciality> getAvailabeSpeciality() {
        return availabeSpeciality;
    }

    public void setAvailabeSpeciality(ArrayList<AvailabeSpeciality> availabeSpeciality) {
        this.availabeSpeciality = availabeSpeciality;
    }

    @Override
    public String toString() {
        return "ClassPojo [departmentName = " + departmentName + ", availabeSpeciality = " + availabeSpeciality + "]";
    }
}
