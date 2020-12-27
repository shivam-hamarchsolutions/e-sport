package app.puretech.e_sport.model;

public class SchoolStudentAttendanceListDTO {
    String title;
    String day;
    String trainer_id;
    String school_id;
    String attendance_date;
    String attendance_month;
    String student_id;

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getAttendance_date() {
        return attendance_date;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setTrainer_id(String trainer_id) {
        this.trainer_id = trainer_id;
    }

    public String getTrainer_id() {
        return trainer_id;
    }

    public void setAttendance_date(String attendance_date) {
        this.attendance_date = attendance_date;
    }

    public String getAttendance_month() {
        return attendance_month;
    }

    public void setAttendance_month(String attendance_month) {
        this.attendance_month = attendance_month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
