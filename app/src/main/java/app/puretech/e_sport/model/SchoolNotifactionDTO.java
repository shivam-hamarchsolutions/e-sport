package app.puretech.e_sport.model;

public class SchoolNotifactionDTO {
    private String str_notifaction_name;
    private String str_grade;
    private String str_date;
    private String str_time;
    private String sir_description;

    public String getSir_description() {
        return sir_description;
    }

    public void setSir_description(String sir_description) {
        this.sir_description = sir_description;
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

    public String getStr_grade() {
        return str_grade;
    }

    public void setStr_grade(String str_grade) {
        this.str_grade = str_grade;
    }

    public String getStr_notifaction_name() {
        return str_notifaction_name;
    }

    public void setStr_notifaction_name(String str_notifaction_name) {
        this.str_notifaction_name = str_notifaction_name;
    }
}
