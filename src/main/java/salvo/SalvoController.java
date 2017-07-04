package salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.management.PlatformLoggingMXBean;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static sun.audio.AudioPlayer.player;

/**
 * Created by Jeix4 on 03/05/2017.
 */

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository GameRepo; //TODO nombres de variables son camelCase empiezan con minusculas

    @Autowired //El autowired solo aplica a la linia inmediatamente siguiente
    private GamePlayerRepository GamePlRepo;

    @Autowired
    private PlayerRepository PlayerRepo;

    @Autowired
    private ShipRepository ShipRepo;

    @Autowired
    private  SalvoRepository SalvoRepo;

    @Autowired
    private ScoreRepository scoreRepo;

    @RequestMapping("/games")
    private Map<String, Object> makeGameListDto(Authentication authentication) {

        Map<String, Object> gameListDto = new LinkedHashMap<>();
/**
 * si la autotenticacion es null o que no este logeado me vas a aparecer con el username
 * en null, si no me haces el gameInfoAuth que lo que hace es imprimir el current player que
 * esta logeado.
 */
        if(authentication == null){
            gameListDto.put("username","null");
        }
        else{
            gameListDto.put("currentPlayer", gamesInfoAuth(authentication));
        }

        gameListDto.put("games", GameRepo.findAll()
                .stream()
                .map(game -> gamesMapDto(game))
                .collect(Collectors.toList()));
       /* Map<String,Object> leaderListDto = new LinkedHashMap<>();
        leaderListDto.put("leaderList", scoreRepo.findAll()
                .stream()
                .map(leaderb ->(leaderb))
                .collect(Collectors.toList()));*/
        return gameListDto;
    }

    @RequestMapping("/leaderboard")
    private Map<String, Object> makeLeaderBoard(Authentication authentication) {
        Map<String,Object> leaderListDto = new LinkedHashMap<>();

        if(authentication == null){
            leaderListDto.put("username","null");
        }
        else{
            leaderListDto.put("currentPlayer", gamesInfoAuth(authentication));
        }


        leaderListDto.put("leaderboard", PlayerRepo.findAll().stream().map(player1 -> leaderBoardDto(player1)).collect(Collectors.toList()));

        return  leaderListDto;
    }


    private Map<String, Object> makeGamePlayerListDto(GamePlayer gamePlayer) {
        Map<String, Object> gamePlayerDto = new LinkedHashMap<>();
        gamePlayerDto.put("gpid", gamePlayer.getId());
        gamePlayerDto.put("player", playerMapDto(gamePlayer.getPlayer()));
        if(gamePlayer.getScore() != null) {
            gamePlayerDto.put("score", gamePlayer.getScore().getScore());
        }
        return gamePlayerDto;
    }

    private Map<String, Object> playerMapDto(Player player) {
        Map<String, Object> playerDto = new LinkedHashMap<String, Object>();
        playerDto.put("id", player.getId());
        playerDto.put("email", player.getUserName());

        return playerDto;
    }

    private Map<String, Object> gamesMapDto(Game game) {
        Map<String, Object> gameDto = new LinkedHashMap<String, Object>();
        gameDto.put("id", game.getId());
        gameDto.put("date", game.getCreationDate());
        gameDto.put("gamePlayers", game.getGamePlayers()
                .stream()
                .map(gamePlayer -> makeGamePlayerListDto(gamePlayer))
                .collect(Collectors.toList()));

        return gameDto;

    }

    private Map<String, Object> gamesInfoAuth(Authentication authentication){
        Map<String, Object> authDto = new LinkedHashMap<>();
        authDto.put("id", getAll(authentication).getId());
        authDto.put("username", getAll(authentication).getUserName());

        return authDto;
    }

    private Player getAll(Authentication authentication){
        return PlayerRepo.findByUserName(authentication.getName());
    }

    @RequestMapping(path = "/games",method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
        Game game = new Game();
        GameRepo.save(game);
        Player player = PlayerRepo.findByUserName(authentication.getName());

        GamePlayer gamePlayer = new GamePlayer(player, game);
        GamePlRepo.save(gamePlayer);

        return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);


    }




    @RequestMapping(path = "/player",method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestParam String name, @RequestParam String pwd) {
        if (name.isEmpty()){
            return new ResponseEntity<>("no game given", HttpStatus.FORBIDDEN);
        }

        Player player = PlayerRepo.findByUserName(name);
        if (player != null){
            return new ResponseEntity<>("Name already in use", HttpStatus.CONFLICT);
        }

        Player p1 = new Player(name);
        p1.setPassword(pwd);
        PlayerRepo.save(p1);
        return new ResponseEntity<>("Name Added",HttpStatus.CREATED);
    }



    @RequestMapping(path = "/games/players/{gamePlayerId}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> placeSalvoes(@PathVariable Long gamePlayerId, @RequestBody Set<Salvo> salvos, Authentication auth){

        Player playerSalvo = PlayerRepo.findByUserName(auth.getName());

        GamePlayer gpSalvoes = GamePlRepo.findOne(gamePlayerId);

        if (playerSalvo == null){
            new ResponseEntity<>(makeMap("error", "no player connected"), HttpStatus.UNAUTHORIZED);
        }

        if(!gpSalvoes.getSalvoes().isEmpty()){
            new ResponseEntity<>(makeMap("error", "all salvoes has been shotted"), HttpStatus.FORBIDDEN);
        }

        for(Salvo salvo : salvos){
            gpSalvoes.addSalvoes(salvo);
            SalvoRepo.save(salvo);

        }

        return  new ResponseEntity<>(makeMap("success", "OK"), HttpStatus.CREATED);

    }

    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public  ResponseEntity<Map<String, Object>> placeShips(@PathVariable Long gamePlayerId, @RequestBody Set<Ship> ships, Authentication auth) {

        Player playerShip = PlayerRepo.findByUserName(auth.getName());

        GamePlayer gpShips = GamePlRepo.findOne(gamePlayerId);

        if (playerShip == null) {
            new ResponseEntity<>(makeMap("error", "no player connected"), HttpStatus.UNAUTHORIZED);
        }

        if (!gpShips.getShips().isEmpty()) {
            new ResponseEntity<>(makeMap("error", "all ships has been placed"), HttpStatus.FORBIDDEN);
        }

        for (Ship ship : ships) {
            gpShips.addShips(ship);
            ShipRepo.save(ship);

        }

        return new ResponseEntity<>(makeMap("success","OK"), HttpStatus.CREATED);


    }

    @RequestMapping(path = "/games/{nn}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long nn, Authentication authentication){

        if(authentication == null){
            return new ResponseEntity<>(makeMap("error", "No current user"),HttpStatus.UNAUTHORIZED);
        }

        Player player = PlayerRepo.findByUserName(authentication.getName());

        Game game = GameRepo.findOne(nn);



        if(game == null){
            return new ResponseEntity<>(makeMap("error","no such game"),HttpStatus.FORBIDDEN);
        }
        if(game.getGamePlayers().size() == 2){
            return new ResponseEntity<>(makeMap("error","games is full"),HttpStatus.FORBIDDEN);
        }

        GamePlayer gamePlayer = new GamePlayer(player, game);
        GamePlRepo.save(gamePlayer);

        return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
    }


    /**
     * Se crea una funcion saque un lista dentro del Leaderboard donde regrese resusltados de score dependiendo
     * de los wins, lose or draws
     * @param player
     * @return
     */




    private Map<String,Object> leaderBoardDto(Player player){
        Map<String, Object> listDto = new LinkedHashMap<>();
        listDto.put("id", player.getId());
        listDto.put("name", player.getUserName());
        listDto.put("score", player.getAllScores().stream().mapToDouble(score -> score.getScore()).sum());
        listDto.put("wins", player.getAllScores().stream().map(score -> score.getScore()).filter(scores -> scores==1).count());
        listDto.put("lose", player.getAllScores().stream().map(score -> score.getScore()).filter(scores -> scores==0).count());
        listDto.put("draw", player.getAllScores().stream().map(score -> score.getScore()).filter(scores -> scores==0.5).count());


        return listDto;
    }

    @RequestMapping("/game_view/{nn}")
    private Object findGame(@PathVariable Long nn, Authentication authentication) {

        Player loggedUser = PlayerRepo.findByUserName(authentication.getName());

        GamePlayer gamePlayer = GamePlRepo.findOne(nn);




            Game game = gamePlayer.getGame();

            Map<String, Object> gameViewPDto = new LinkedHashMap<>();
            gameViewPDto.put("id", game.getId());
            gameViewPDto.put("date", game.getCreationDate());
            gameViewPDto.put("gamePlayers", game.getGamePlayers()
                    .stream()
                    .map(gamePlayer1 -> makeGamePlayerListDto(gamePlayer1))
                    .collect(Collectors.toList()));

            gameViewPDto.put("ships", gamePlayer.getShips()
                    .stream()
                    .map(ship -> ShipsList(ship))
                    .collect(Collectors.toList()));


       /* gameViewPDto.put("salvoes", gamePlayer.getSalvoes()
                .stream()
                .map(salvo -> SalvoList(salvo))
                .collect(Collectors.toList()));*/


            gameViewPDto.put("salvoes", game.getGamePlayers()
                    .stream()
                    .map(gamep -> SalvoGP(gamep.getSalvoes()))
                    .collect(Collectors.toList()));

        if(loggedUser.getId() == gamePlayer.getPlayer().getId()) {
            return new ResponseEntity<>(gameViewPDto, HttpStatus.OK);
        }

        else {
            return new ResponseEntity<>("No authorized", HttpStatus.UNAUTHORIZED);
        }
    }


    private Map<String, Object> ShipsList(Ship ship) {
        Map<String, Object> ShipsDto = new LinkedHashMap<>();
        ShipsDto.put("type", ship.getShipName());
        ShipsDto.put("locations", ship.getLocations());

        return ShipsDto;
    }

    private Map<String, Object> SalvoList(Salvo salvo) {
        Map<String, Object> SalvoDto = new LinkedHashMap<>();
        SalvoDto.put("turn", salvo.getTurnN());
        SalvoDto.put("player", salvo.getGamePlayer().getPlayer().getId());
        SalvoDto.put("Shots_Locations", salvo.getShotLocations());

        return SalvoDto;
    }

    //TODO nombre de metodos son camelCase empezando con minuscula
   private List<Map<String, Object>> SalvoGP (Set<Salvo> salvos) {

       List<Map<String, Object>> dto2 = new ArrayList<>();

       for (Salvo salvo : salvos) {
           Map<String, Object> dto = new LinkedHashMap<>();
           dto.put("turn", salvo.getTurnN());
           dto.put("players", salvo.getGamePlayer().getPlayer().getId());
           dto.put("locations", salvo.getShotLocations());
           dto2.add(dto);


       }

       return dto2;

        /* Map<String, Object> SalvoGPDto = new LinkedHashMap<>();
        SalvoGPDto.put("players", gamePlayer.getSalvoes()
                .stream()
                .map(salvogp -> SalvoList(salvogp))
                .collect(Collectors.toList()));*/
    }


    private Map<String, Object> scoreList(Score score){

        return null;
    }


    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}


