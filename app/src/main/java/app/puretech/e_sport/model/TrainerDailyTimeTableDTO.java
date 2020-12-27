package app.puretech.e_sport.model;

public class TrainerDailyTimeTableDTO {
    private String str_time;
    private String str_division;
    private String str_class;
    private String str_session;
    private String str_school_name;
    private String str_present_student;
    private String str_absent_student;
    private String str_class_session_id;
    private String str_image;
    private String str_session_pdf;
    private String str_school_id;
    private int str_status;
    private String str_days;

    private int presetStu;
    private int totalStu;

    public int getPresetStu() {
        return presetStu;
    }

    public void setPresetStu(int presetStu) {
        this.presetStu = presetStu;
    }

    public int getTotalStu() {
        return totalStu;
    }

    public void setTotalStu(int totalStu) {
        this.totalStu = totalStu;
    }

    public String getStr_days() {
        return str_days;
    }

    public void setStr_days(String str_days) {
        this.str_days = str_days;
    }

    public int getStr_status() {
        return str_status;
    }

    public void setStr_status(int str_status) {
        this.str_status = str_status;
    }

    public String getStr_school_id() {
        return str_school_id;
    }

    public void setStr_school_id(String str_school_id) {
        this.str_school_id = str_school_id;
    }

    public String getStr_session_pdf() {
        return str_session_pdf;
    }

    public void setStr_session_pdf(String str_session_pdf) {
        this.str_session_pdf = str_session_pdf;
    }

    public String getStr_image() {
        return str_image;
    }

    public void setStr_image(String str_image) {
        this.str_image = str_image;
    }

    public String getStr_class_session_id() {
        return str_class_session_id;
    }

    public void setStr_class_session_id(String str_class_session_id) {
        this.str_class_session_id = str_class_session_id;
    }

    public String getStr_absent_student() {
        return str_absent_student;
    }

    public void setStr_absent_student(String str_absent_student) {
        this.str_absent_student = str_absent_student;
    }

    public String getStr_division() {
        return str_division;
    }

    public void setStr_division(String str_division) {
        this.str_division = str_division;
    }

    public String getStr_time() {
        return str_time;
    }

    public void setStr_time(String str_time) {
        this.str_time = str_time;
    }

    public String getStr_present_student() {
        return str_present_student;
    }

    public void setStr_present_student(String str_present_student) {
        this.str_present_student = str_present_student;
    }

    public String getStr_school_name() {
        return str_school_name;
    }

    public void setStr_school_name(String str_school_name) {
        this.str_school_name = str_school_name;
    }

    public String getStr_class() {
        return str_class;
    }

    public void setStr_class(String str_class) {
        this.str_class = str_class;
    }

    public String getStr_session() {
        return str_session;
    }

    public void setStr_session(String str_session) {
        this.str_session = str_session;
    }
}
