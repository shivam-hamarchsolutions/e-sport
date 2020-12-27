package app.puretech.e_sport.model;

public class SchoolStudentListDTO {
    private String str_name;
    private String str_division;
    private String str_grade;
    private String str_school_id;
    private String str_student_id;

    public String getStr_student_id() {
        return str_student_id;
    }

    public void setStr_student_id(String str_student_id) {
        this.str_student_id = str_student_id;
    }

    public String getStr_school_id() {
        return str_school_id;
    }

    public void setStr_school_id(String str_school_id) {
        this.str_school_id = str_school_id;
    }

    public String getStr_name() {
        return str_name;
    }

    public void setStr_name(String str_name) {
        this.str_name = str_name;
    }

    public String getStr_grade() {
        return str_grade;
    }

    public void setStr_grade(String str_grade) {
        this.str_grade = str_grade;
    }

    public String getStr_division() {
        return str_division;
    }

    public void setStr_division(String str_division) {
        this.str_division = str_division;
    }
}
