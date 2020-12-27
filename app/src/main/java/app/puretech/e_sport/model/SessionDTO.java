package app.puretech.e_sport.model;

public class SessionDTO {
    String str_session_id;
    String str_sport_name;
    String str_time;

    public String getStr_sport_name() {
        return str_sport_name;
    }

    public void setStr_sport_name(String str_sport_name) {
        this.str_sport_name = str_sport_name;
    }

    public String getStr_time() {
        return str_time;
    }

    public void setStr_time(String str_time) {
        this.str_time = str_time;
    }

    public String getStr_session_id() {
        return str_session_id;
    }

    public void setStr_session_id(String str_session_id) {
        this.str_session_id = str_session_id;
    }

}
