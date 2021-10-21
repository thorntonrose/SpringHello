package trose;

import java.text.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.*;

@RestController
public class Controller {
   @Autowired
   private GreetingDao greetingDao;

   @GetMapping("/")
   public String index() {
      return "hello";
   }

   @GetMapping("/{country}")
   public String getGreeting(@PathVariable String country) {
      GreetingEntity greeting = greetingDao.findById(country).orElse(null);
		if (greeting != null) { return greeting.phrase; }
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, MessageFormat.format("{0} not found", country));
   }

   @PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE)
   public String postGreeting(@RequestBody GreetingEntity greeting) {
      if (greetingDao.countByCountry(greeting.country) > 0) {
         throw new ResponseStatusException(HttpStatus.CONFLICT, MessageFormat.format("{0} found", greeting.country));
      }

      greetingDao.save(greeting);
      return greeting.phrase;
   }
}
