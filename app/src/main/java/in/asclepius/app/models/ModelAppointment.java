package in.asclepius.app.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import org.jetbrains.annotations.NotNull;

@IgnoreExtraProperties
public class ModelAppointment {

    private AppUser patient;
    private String bookingDate;
    private boolean isFeesPaid;
    private Doctors doctor;
    private String department;
    private String status;
    private AppUser bookedBy;
    private Integer appointmentId;
    private boolean isOnlineConsultation;


    public ModelAppointment(AppUser patient, String bookingDate, boolean isFeesPaid, Doctors doctor, String department, String status, AppUser bookedBy, Integer appointmentId, boolean isOnlineConsultation) {
        this.patient = patient;
        this.bookingDate = bookingDate;
        this.isFeesPaid = isFeesPaid;
        this.doctor = doctor;
        this.department = department;
        this.status = status;
        this.bookedBy = bookedBy;
        this.appointmentId = appointmentId;
        this.isOnlineConsultation = isOnlineConsultation;
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
    public String getOpdDepartments() {
        return department;
    }

    @PropertyName("opdDepartment")
    public void setOpdDepartments(String department) {
        this.department = department;
    }

    @PropertyName("status")
    public String getStatus() {
        return status;
    }

    @PropertyName("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @PropertyName("bookedBy")
    public AppUser getBookedBy() {
        return bookedBy;
    }

    @PropertyName("bookedBy")
    public void setBookedBy(AppUser bookedBy) {
        this.bookedBy = bookedBy;
    }

    @NotNull
    public String getData() {
        return "Patient Name : " + patient.getFullName() + "\nAge : " + patient.getAge() + "\nBooked by : " + bookedBy.getFullName() + "\nStatus : " + status + "\n" + "Date : " + bookingDate;
    }

    @NotNull
    public String getDataForUser() {
        if (!isOnlineConsultation) {
            return "Patient Name : " + patient.getFullName() + "\nAge : " + patient.getAge() + "\n" + "Date : " + bookingDate + "\nAppointment Id : " +
                    appointmentId + "\n\nDoctor Name :  " + doctor.getName() + "\nDepartment : " + department;
        } else {
            return "Patient Name : " + patient.getFullName() + "\nAge : " + patient.getAge() + "\n" +
                    "Appointment Id : " + appointmentId + "\n\nDoctor Name :  " + doctor.getName() + "\nDepartment : " + department;
        }

    }

    @PropertyName("appointmentId")
    public Integer getAppointmentId() {
        return appointmentId;
    }

    @PropertyName("appointmentId")
    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    @PropertyName("isOnlineConsultation")
    public boolean isOnlineConsultation() {
        return isOnlineConsultation;
    }

    @PropertyName("isOnlineConsultation")
    public void setOnlineConsultation(boolean onlineConsultation) {
        isOnlineConsultation = onlineConsultation;
    }
}
