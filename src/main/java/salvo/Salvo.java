package salvo;

import salvo.GamePlayer;
import javax.persistence.*;
import java.util.List;

/**
 * Created by Jeix4 on 17/05/2017.
 */

@Entity
public class Salvo {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salvo_id")
    private GamePlayer gamePlayer;

    private long turnN;

    @ElementCollection
    private List<String> shotLocations;

    public Salvo(){}

    public Salvo(long turnN, List<String> shotLocations){
        this.turnN = turnN;
        this.shotLocations = shotLocations;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public long getTurnN() {
        return turnN;
    }

    public void setTurnN(long turnN) {
        this.turnN = turnN;
    }

    public List<String> getShotLocations() {
        return shotLocations;
    }

    public void setShotLocations(List<String> shotLocations) {
        this.shotLocations = shotLocations;
    }
}
