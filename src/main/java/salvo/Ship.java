package salvo;

import javax.persistence.*;
import javax.xml.ws.soap.Addressing;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Jeix4 on 09/05/2017.
 */

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    private String ShipName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="ship_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    private List<String> locations;




    public Ship(){}

    public Ship(String shipName, List<String> locations) {
        this.ShipName = shipName;
        this.locations = locations;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        this.Id = Id;
    }

    public String getShipName() {
        return ShipName;
    }

    public void setShipName(String shipName) {
        this.ShipName = shipName;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}
