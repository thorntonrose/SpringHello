package trose;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GreetingDao extends CrudRepository<GreetingEntity, String> {
   public long countByCountry(String country);
}
