package salvo;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Jeix4 on 02/05/2017.
 */
@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    private Date creationDate = new Date();


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    Set<Ship> ships = new LinkedHashSet<>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    Set<Salvo> salvoes = new LinkedHashSet<>();



    public GamePlayer(){}

    public GamePlayer(Player player, Game game){
        this.player = player;
        this.game = game;

;    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public void addShips(Ship ship){
        ships.add(ship);
        ship.setGamePlayer(this);
    }

    public void addSalvoes(Salvo salvo){
        salvoes.add(salvo);
        salvo.setGamePlayer(this);

    }

    public Score getScore(){
        return player.getScores(this.game);
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }
}