package in.asclepius.app.models;

import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.Random;

@IgnoreExtraProperties
public class AppUser {

    private String fullName;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private boolean isNumberVerified=false;
    private boolean isRegisteredAsDoctor=false;
    private int age;
    private String aadharNumber;
    private String gender;
    private String relationType;

    public AppUser() {
        //required default constructor
    }

    public AppUser(String fullName, String mobileNumber) {
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        try{
            String[] names=fullName.split(" ");
            firstName=names[0];
            lastName=names[1];
        }catch (Exception e) {
            Log.d("AppUser","Some error : "+e.toString());
        }
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        if(firstName!=null && !firstName.equals("null"))
            return firstName;
        return fullName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        if(lastName!=null && !lastName.equals("null"))
            return lastName;
        return fullName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @PropertyName("numberVerified")
    public boolean isNumberVerified() {
        return isNumberVerified;
    }

    @PropertyName("numberVerified")
    public void setNumberVerified(boolean numberVerified) {
        isNumberVerified = numberVerified;
    }

    @PropertyName("registeredAsDoctor")
    public boolean isRegisteredAsDoctor() {
        return isRegisteredAsDoctor;
    }

    @PropertyName("registeredAsDoctor")
    public void setRegisteredAsDoctor(boolean registeredAsDoctor) {
        isRegisteredAsDoctor = registeredAsDoctor;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "fullName='" + fullName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", isNumberVerified=" + isNumberVerified +
                ", isRegisteredAsDoctor=" + isRegisteredAsDoctor +
                '}';
    }

    @PropertyName("age")
    public int getAge() {
        if (age <= 0) {
            Random da = new Random();
            return da.nextInt(75);

        }
        return age;
    }

    @PropertyName("age")
    public void setAge(int age) {
        this.age = age;
    }

    @PropertyName("aadharId")
    public String getAadharNumber() {
        return aadharNumber;
    }

    @PropertyName("aadharId")
    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    @PropertyName("gender")
    public String getGender() {
        return gender;
    }

    @PropertyName("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @PropertyName("relationType")
    public String getRelationType() {
        return relationType;
    }

    @PropertyName("relationType")
    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }
}
