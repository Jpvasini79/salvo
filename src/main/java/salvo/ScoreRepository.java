package salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by Jeix4 on 22/05/2017.
 */
@RepositoryRestResource
public interface ScoreRepository extends JpaRepository <Score, Long> {

}
