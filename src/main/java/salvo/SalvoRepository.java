package salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by Jeix4 on 17/05/2017.
 */

@RepositoryRestResource

public interface SalvoRepository extends JpaRepository<Salvo, Long> {
}
