package app.puretech.e_sport.model;

public class AttendanceBarDTO {
    String str_month;
    String str_avg_attendance;
    String str_trainer_id;
    String str_school_id;

    public void setStr_trainer_id(String str_trainer_id) {
        this.str_trainer_id = str_trainer_id;
    }

    public String getStr_trainer_id() {
        return str_trainer_id;
    }

    public void setStr_school_id(String str_school_id) {
        this.str_school_id = str_school_id;
    }

    public String getStr_school_id() {
        return str_school_id;
    }

    public void setStr_avg_attendance(String str_avg_attendance) {
        this.str_avg_attendance = str_avg_attendance;
    }

    public String getStr_avg_attendance() {
        return str_avg_attendance;
    }

    public void setStr_month(String str_month) {
        this.str_month = str_month;
    }

    public String getStr_month() {
        return str_month;
    }
}
