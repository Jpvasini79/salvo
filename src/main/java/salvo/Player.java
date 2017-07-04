package salvo;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Jeix4 on 27/04/2017.
 */

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long Id;
    private String userName;

    @OneToMany(mappedBy ="player", fetch =FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<Score> scores;

    private String password;

    public Player() { }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Player(String email) {
        userName = email;
    }

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Score getScores(Game game) {
        return scores.stream().filter(score -> score.getGame().equals(game)).findFirst().orElse(null);
    }

    public Set<Score> getAllScores(){
        return scores;
    }


    @Override
    public String toString() {
        return "Player{" +
                "id=" + getId() +
                ", userName='" + this.userName + '\'' +
                '}';
    }

}
