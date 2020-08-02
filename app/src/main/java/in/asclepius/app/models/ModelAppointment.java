package in.asclepius.app.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class ModelAppointment {

    private AppUser patient;
    private String bookingDate;
    private boolean isFeesPaid;
    private Doctors doctor;
    private OpdDepartments opdDepartments;
    private String status;

    public ModelAppointment(AppUser patient, String bookingDate, boolean isFeesPaid, Doctors doctor, OpdDepartments opdDepartments, String status) {
        this.patient = patient;
        this.bookingDate = bookingDate;
        this.isFeesPaid = isFeesPaid;
        this.doctor = doctor;
        this.opdDepartments = opdDepartments;
        this.status = status;
    }

    public ModelAppointment() {
    }

    @PropertyName("patient")
    public AppUser getPatient() {
        return patient;
    }

    @PropertyName("patient")
    public void setPatient(AppUser patient) {
        this.patient = patient;
    }

    @PropertyName("bookingDate")
    public String getBookingDate() {
        return bookingDate;
    }

    @PropertyName("bookingDate")
    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    @PropertyName("isFeesPaid")
    public boolean isFeesPaid() {
        return isFeesPaid;
    }

    @PropertyName("isFeesPaid")
    public void setFeesPaid(boolean feesPaid) {
        isFeesPaid = feesPaid;
    }

    @PropertyName("doctor")
    public Doctors getDoctor() {
        return doctor;
    }

    @PropertyName("doctor")
    public void setDoctor(Doctors doctor) {
        this.doctor = doctor;
    }

    @PropertyName("opdDepartment")
    public OpdDepartments getOpdDepartments() {
        return opdDepartments;
    }

    @PropertyName("opdDepartment")
    public void setOpdDepartments(OpdDepartments opdDepartments) {
        this.opdDepartments = opdDepartments;
    }

    @PropertyName("status")
    public String getStatus() {
        return status;
    }

    @PropertyName("status")
    public void setStatus(String status) {
        this.status = status;
    }
}
