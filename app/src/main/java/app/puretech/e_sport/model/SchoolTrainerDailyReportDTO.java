package app.puretech.e_sport.model;

public class SchoolTrainerDailyReportDTO {
    String trainer_id;
    String name;
    String primary_game;
    String profile_photo;

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getPrimary_game() {
        return primary_game;
    }

    public void setPrimary_game(String primary_game) {
        this.primary_game = primary_game;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrainer_id() {
        return trainer_id;
    }

    public void setTrainer_id(String trainer_id) {
        this.trainer_id = trainer_id;
    }

}
