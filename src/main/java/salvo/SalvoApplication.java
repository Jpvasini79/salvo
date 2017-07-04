package salvo;

import com.sun.crypto.provider.HmacMD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.AuthenticationManagerConfiguration;
import org.springframework.boot.autoconfigure.security.BootGlobalAuthenticationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);

    }

    @Bean
    public CommandLineRunner initData(PlayerRepository repository, GameRepository gamerepo, GamePlayerRepository gpRepository,
                                      ShipRepository shipsRepo, SalvoRepository salvoRepo, ScoreRepository scoreRepo) {
        return (String... args) -> {

            // save a couple of customers

            Player p1 = new Player("j.bauer@ctu.gov");
            p1.setPassword("24");
            Player p2 = new Player("c.obrian@ctu.gov");
            p2.setPassword("42");
            Player p3 = new Player("kim_bauer@gmail.com");
            p3.setPassword("kb");
            Player p4 = new Player("t.almeida@ctu.gov");
            p4.setPassword("mole");
            Player p5 = new Player("yeferson");
            p5.setPassword("lol");
            Player p6 = new Player("lalala");
            p6.setPassword("lmao");
            Player p7 = new Player("lonelyguy");
            p7.setPassword("brb");



            repository.save(p1);
            repository.save(p2);
            repository.save(p3);
            repository.save(p4);
            repository.save(p5);
            repository.save(p6);
            repository.save(p7);


            Game g1 = new Game();
            Game g2 = new Game();
            Game g3 = new Game();
            Game g4 = new Game();
            gamerepo.save(g1);
            gamerepo.save(g2);
            gamerepo.save(g3);
            gamerepo.save(g4);
            System.out.println(g1.getCreationDate());

            GamePlayer gp1 = new GamePlayer(p1, g1);
            GamePlayer gp2 = new GamePlayer(p2, g1);
            GamePlayer gp3 = new GamePlayer(p3, g2);
            GamePlayer gp4 = new GamePlayer(p4, g2);
            GamePlayer gp5 = new GamePlayer(p5, g3);
            GamePlayer gp6 = new GamePlayer(p6, g3);
            GamePlayer gp7 = new GamePlayer(p7, g4);
            gpRepository.save(gp1);
            gpRepository.save(gp2);
            gpRepository.save(gp3);
            gpRepository.save(gp4);
            gpRepository.save(gp5);
            gpRepository.save(gp6);
            gpRepository.save(gp7);


            ArrayList<String> loc1 = new ArrayList<>(Arrays.asList("A1", "A2", "A3", "A4", "A5"));
            ArrayList<String> loc2 = new ArrayList<>(Arrays.asList("B1", "B2", "B3", "B4"));
            ArrayList<String> loc3 = new ArrayList<>(Arrays.asList("C1", "C2", "C3"));
            ArrayList<String> loc4 = new ArrayList<>(Arrays.asList("D1", "D2", "D3"));
            ArrayList<String> loc5 = new ArrayList<>(Arrays.asList("E1", "E2"));


            Ship sp1 = new Ship("Carrier", loc1);
            Ship sp2 = new Ship("Battleship", loc2);
            Ship sp3 = new Ship("Submarine", loc3);
            Ship sp4 = new Ship("Destroyer", loc4);
            Ship sp5 = new Ship("Patrol Boat", loc5);

            gp1.addShips(sp1);
            gp1.addShips(sp2);
            gp2.addShips(sp3);
            gp2.addShips(sp4);


            shipsRepo.save(sp1);
            shipsRepo.save(sp2);
            shipsRepo.save(sp3);
            shipsRepo.save(sp4);
            shipsRepo.save(sp5);

            ArrayList<String> locShot1 = new ArrayList<>(Arrays.asList("A3","B5","C9"));
            ArrayList<String> locShot2 = new ArrayList<>(Arrays.asList("A7","D5","F9"));
            ArrayList<String> locShot3 = new ArrayList<>(Arrays.asList("G3","B6","C2"));
            ArrayList<String> locShot4 = new ArrayList<>(Arrays.asList("E3","B4","A9"));

            Salvo salvoTurn1 =  new Salvo(1, locShot1);
            Salvo salvoTurn2 =  new Salvo(2, locShot2);
            Salvo salvoTurn3 =  new Salvo(3, locShot3);
            Salvo salvoTurn4 =  new Salvo(4, locShot4);

            gp1.addSalvoes(salvoTurn1);
            gp2.addSalvoes(salvoTurn2);
            gp1.addSalvoes(salvoTurn3);
            gp2.addSalvoes(salvoTurn4);



            salvoRepo.save(salvoTurn1);
            salvoRepo.save(salvoTurn2);
            salvoRepo.save(salvoTurn3);
            salvoRepo.save(salvoTurn4);

            Date finishdate = new Date();
            Score s1 = new Score(p1, g1, 1f, finishdate);
            Score s2 = new Score(p2, g1, 0f, finishdate);
            Score s3 = new Score(p1, g2, 1f, finishdate);
            Score s4 = new Score(p2, g2, 0f, finishdate);

            scoreRepo.save(s1);
            scoreRepo.save(s2);
            scoreRepo.save(s3);
            scoreRepo.save(s4);

        };
    }
}

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter{

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService());

    }
    @Bean
    UserDetailsService userDetailsService(){
        return  new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Player player = playerRepository.findByUserName(username);
                if(player != null){

                    return new User(player.getUserName(), player.getPassword(),
                            AuthorityUtils.createAuthorityList("USER"));

                } else{
                    throw new UsernameNotFoundException("Unknow user: " + username);
                }

            }
        };
    }
}
@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/games").permitAll()
                .antMatchers("/api/player").permitAll()
                .antMatchers("/api/leaderboard").permitAll()
                .antMatchers("/scripts/**").permitAll()
                .antMatchers("/styles/**").permitAll()
                .antMatchers("/games.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin();

        http.formLogin()
                .usernameParameter("name")
                .passwordParameter("pwd")
                .loginPage("/api/login");


        http.logout()
                .logoutUrl("/api/logout");


        // turn off checking for CSRF tokens
        http.csrf().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}