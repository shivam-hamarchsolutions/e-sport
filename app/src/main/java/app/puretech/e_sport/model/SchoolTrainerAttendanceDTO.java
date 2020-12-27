package app.puretech.e_sport.model;

public class SchoolTrainerAttendanceDTO {
    String str_trainer_id;
    String str_name;
    String str_primary_game;
    String str_profile_photo;

    public String getStr_profile_photo() {
        return str_profile_photo;
    }

    public void setStr_profile_photo(String str_profile_photo) {
        this.str_profile_photo = str_profile_photo;
    }

    public String getStr_primary_game() {
        return str_primary_game;
    }

    public void setStr_primary_game(String str_primary_game) {
        this.str_primary_game = str_primary_game;
    }

    public String getStr_name() {
        return str_name;
    }

    public void setStr_name(String str_name) {
        this.str_name = str_name;
    }

    public String getStr_trainer_id() {
        return str_trainer_id;
    }

    public void setStr_trainer_id(String str_trainer_id) {
        this.str_trainer_id = str_trainer_id;
    }
}
