package salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Jeix4 on 22/05/2017.
 */

@Entity

public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    private Date finishDate = new Date();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="player_id")
    private Player player;

    public float Score;
    public Score(){}

    public Score(Player player, Game game,float Score, Date finishDate){
        this.Score = Score;
        this.finishDate = finishDate;
        this.player = player;
        this.game = game;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @JsonIgnore
    public float getScore() {
        return Score;
    }

    public void setScore(float score) {
        Score = score;
    }


}





