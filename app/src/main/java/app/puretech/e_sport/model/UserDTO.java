package app.puretech.e_sport.model;


import java.io.Serializable;

/**
 * Created by Di9esh MisaRs on 11/04/2018.
 */

public class UserDTO implements Serializable {

    private String token;
    private String school;
    private String uid;
    private String name;
    private String company;
    private String phone;
    private String email;
    private String father;
    private String std;
    private String div;
    private Boolean isClassTeacher;
    private String avtarURL;
    private Boolean isActive;
    private Boolean approvalRequested;
    private String schoolCode;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;

    public String getTokenId() {
        return token;
    }

    public void setTokenId(String token) {
        this.token = token;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getUserid() {
        return uid;
    }

    public void setUserid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMobile() {
        return phone;
    }

    public void setMobile(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public String getDiv() {
        return div;
    }

    public void setDiv(String div) {
        this.div = div;
    }

    public Boolean getIsClassTeacher() {
        return isClassTeacher;
    }

    public void setIsClassTeacher(boolean isClassTeacher) {
        this.isClassTeacher = isClassTeacher;
    }

    public String getProfilepic() {
        return avtarURL;
    }

    public void setProfilepic(String avtarURL) {
        this.avtarURL = avtarURL;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getApprovalRequested() {
        return approvalRequested;
    }

    public void setApprovalRequested(boolean approvalRequested) {
        this.approvalRequested = approvalRequested;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}

