package in.asclepius.app.models;


public class DepartmentModel {
    private String organizationName;

    private OpdDepartments[] opdDepartments;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public OpdDepartments[] getOpdDepartments() {
        return opdDepartments;
    }

    public void setOpdDepartments(OpdDepartments[] opdDepartments) {
        this.opdDepartments = opdDepartments;
    }

    @Override
    public String toString() {
        return "ClassPojo [organizationName = " + organizationName + ", opdDepartments = " + opdDepartments + "]";
    }

    public OpdDepartments getOPDDepartmentAt(int pos) {
        try {
            return opdDepartments[pos];
        } catch (Exception e) {
            return null;
        }

    }
}