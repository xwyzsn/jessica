package jessca.study.entity;

import lombok.Data;

@Data
public class Study {
    public String date ;
    public int score;
    public int id ;
    public String gift_name;
    public String gift_finish;
    public String username;

    public Study(String date, int score, int id, String gift_name, String gift_finish) {
        this.date = date;
        this.score = score;
        this.id = id;
        this.gift_name = gift_name;
        this.gift_finish = gift_finish;
    }

    public Study(String date, int score, String gift_name, String gift_finish, String username) {
        this.date = date;
        this.score = score;
        this.gift_name = gift_name;
        this.gift_finish = gift_finish;
        this.username = username;
    }

    public Study(String date, int score, int id, String gift_name, String gift_finish, String username) {
        this.date = date;
        this.score = score;
        this.id = id;
        this.gift_name = gift_name;
        this.gift_finish = gift_finish;
        this.username = username;
    }

    public Study() {
    }

    public Study(String date, int score) {
        this.date = date;
        this.score = score;
    }

    public Study(String date, int score, int id) {
        this.date = date;
        this.score = score;
        this.id = id;
    }

    public Study(String date, int score, String gift_finish, String username) {
        this.date = date;
        this.score = score;
        this.gift_finish = gift_finish;
        this.username = username;
    }
}
