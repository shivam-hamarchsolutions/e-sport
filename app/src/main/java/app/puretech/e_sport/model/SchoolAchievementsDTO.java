package app.puretech.e_sport.model;

public class SchoolAchievementsDTO {
    private String str_competition_name;
    private String str_full_name;
    private String str_competition_level;
    private String str_competition_year;
    private String str_rank;
    private String set_image;

    public String getSet_image() {
        return set_image;
    }

    public void setSet_image(String set_image) {
        this.set_image = set_image;
    }

    public String getStr_rank() {
        return str_rank;
    }

    public void setStr_rank(String str_rank) {
        this.str_rank = str_rank;
    }

    public String getStr_competition_level() {
        return str_competition_level;
    }

    public void setStr_competition_level(String str_competition_level) {
        this.str_competition_level = str_competition_level;
    }

    public String getStr_competition_name() {
        return str_competition_name;
    }

    public void setStr_competition_name(String str_competition_name) {
        this.str_competition_name = str_competition_name;
    }

    public String getStr_competition_year() {
        return str_competition_year;
    }

    public void setStr_competition_year(String str_competition_year) {
        this.str_competition_year = str_competition_year;
    }

    public String getStr_full_name() {
        return str_full_name;
    }

    public void setStr_full_name(String str_full_name) {
        this.str_full_name = str_full_name;
    }
}
