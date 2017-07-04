package salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
/**
 * Created by Jeix4 on 28/04/2017.
 */

@RepositoryRestResource
public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {


}
