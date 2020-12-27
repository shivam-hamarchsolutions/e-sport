package app.puretech.e_sport.model;

public class SchoolTrainerDailyReportSubDTO {
    String title;
    String str_date;
    String str_time;
    String str_class;
    String str_sport_name;
    String str_day;
    String str_total_student;
    String str_present_student;
    String str_school_name;
    String str_session_name;
    String str_session_pic;
    String str_attendance_status;

    private int toatlStu;
    private int presentStu;

    public int getToatlStu() {
        return toatlStu;
    }

    public void setToatlStu(int toatlStu) {
        this.toatlStu = toatlStu;
    }

    public int getPresentStu() {
        return presentStu;
    }

    public void setPresentStu(int presentStu) {
        this.presentStu = presentStu;
    }

    public String getStr_attendance_status() {
        return str_attendance_status;
    }

    public void setStr_attendance_status(String str_attendance_status) {
        this.str_attendance_status = str_attendance_status;
    }

    public String getStr_session_pic() {
        return str_session_pic;
    }

    public void setStr_session_pic(String str_session_pic) {
        this.str_session_pic = str_session_pic;
    }

    public String getStr_session_name() {
        return str_session_name;
    }

    public void setStr_session_name(String str_session_name) {
        this.str_session_name = str_session_name;
    }

    public String getStr_school_name() {
        return str_school_name;
    }

    public void setStr_school_name(String str_school_name) {
        this.str_school_name = str_school_name;
    }

    public String getStr_present_student() {
        return str_present_student;
    }

    public void setStr_present_student(String str_present_student) {
        this.str_present_student = str_present_student;
    }

    public String getStr_total_student() {
        return str_total_student;
    }

    public void setStr_total_student(String str_total_student) {
        this.str_total_student = str_total_student;
    }

    public String getStr_day() {
        return str_day;
    }

    public void setStr_day(String str_day) {
        this.str_day = str_day;
    }

    public String getStr_sport_name() {
        return str_sport_name;
    }

    public void setStr_sport_name(String str_sport_name) {
        this.str_sport_name = str_sport_name;
    }

    public String getStr_class() {
        return str_class;
    }

    public void setStr_class(String str_class) {
        this.str_class = str_class;
    }

    public String getStr_time() {
        return str_time;
    }

    public void setStr_time(String str_time) {
        this.str_time = str_time;
    }

    public String getStr_date() {
        return str_date;
    }

    public void setStr_date(String str_date) {
        this.str_date = str_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
