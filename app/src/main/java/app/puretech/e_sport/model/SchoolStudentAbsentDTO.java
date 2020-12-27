package app.puretech.e_sport.model;

public class SchoolStudentAbsentDTO {

    String day;
    String attendances_data;
    String student_id;
    String attendance_month;
    String title;
    String description;
    String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAttendance_month() {
        return attendance_month;
    }

    public void setAttendance_month(String attendance_month) {
        this.attendance_month = attendance_month;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getAttendances_data() {
        return attendances_data;
    }

    public void setAttendances_data(String attendances_data) {
        this.attendances_data = attendances_data;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
            this.day = day;
    }

}
