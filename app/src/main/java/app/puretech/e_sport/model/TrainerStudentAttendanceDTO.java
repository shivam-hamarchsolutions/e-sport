package app.puretech.e_sport.model;

public class TrainerStudentAttendanceDTO {
    private String str_serial_number;
    private String str_student_name;
    private String str_status;
    private String str_id;

    public String getStr_id() {
        return str_id;
    }

    public void setStr_id(String str_id) {
        this.str_id = str_id;
    }

    public String getStr_serial_number() {
        return str_serial_number;
    }

    public void setStr_serial_number(String str_serial_number) {
        this.str_serial_number = str_serial_number;
    }

    public String getStr_status() {
        return str_status;
    }

    public void setStr_status(String str_status) {
        this.str_status = str_status;
    }

    public String getStr_student_name() {
        return str_student_name;
    }

    public void setStr_student_name(String str_student_name) {
        this.str_student_name = str_student_name;
    }
}
