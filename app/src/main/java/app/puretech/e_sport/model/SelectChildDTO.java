package app.puretech.e_sport.model;

public class SelectChildDTO {
    String student_id;
    String student_name;
    String str_class;
    String school_id;
    String school_name;

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getStr_class() {
        return str_class;
    }

    public void setStr_class(String str_class) {
        this.str_class = str_class;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }
}
