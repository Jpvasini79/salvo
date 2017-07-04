package salvo;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by Jeix4 on 28/04/2017.
 */
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long Id;

    @OneToMany(mappedBy = "game", fetch =FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    Set<Score> scores;


    private Date creationDate;

    public Game() {
        this.creationDate = new Date();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public long getId() {
        return Id;

    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Set<Score> getScores() { return scores;}

    @Override
    public String toString() {
        return "Game{" +
                "Id=" + Id +
                ", creationDate=" + creationDate +
                '}';
    }
}
