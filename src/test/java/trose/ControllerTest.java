package trose;

import java.util.concurrent.*;
import org.junit.*;
import org.junit.runner.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.context.junit4.*;
import org.springframework.web.server.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllerTest {
   private static final Logger log = LoggerFactory.getLogger(ControllerTest.class);

   @Autowired
   private Controller controller;

   @Autowired
   public GreetingDao greetingDao;

   @Before
   public void setUp() throws Exception {
      log.debug("setUp ...");
      greetingDao.deleteAll();
   }

   //--------------------------------------------------------------------------

   @Test
   public void testIndex() {
      log.debug("testIndex ...");
      assertEquals("index:", "hello", controller.index());
   }

   //--------------------------------------------------------------------------

   @Test
   public void testGetGreeting() {
      log.debug("testGetGreeting ...");
      GreetingEntity greeting = addGreeting("ja", "konichiwa");
      assertEquals("greeting:", greeting.phrase, controller.getGreeting(greeting.country));
   }

   @Test
   public void testGetGreeting_NotFound() throws Exception {
      log.debug("testGetGreeting_NotFound ...");
      assertResponseStatusException(HttpStatus.NOT_FOUND, () -> controller.getGreeting("/ja"));
   }

   //--------------------------------------------------------------------------

   @Test
   public void testPostGreeting() {
      log.debug("testPostGreeting ...");
      GreetingEntity greeting = new GreetingEntity("ja", "konichiwa");
      controller.postGreeting(greeting);
      assertEquals("count:", 1, greetingDao.countByCountry(greeting.country));
   }

   @Test
   public void testPostGreeting_Duplicate() throws Exception {
      log.debug("testPostGreeting_Duplicate ...");

      assertResponseStatusException(
         HttpStatus.CONFLICT,
         () -> controller.postGreeting(addGreeting("ja", "konichiwa"))
      );
   }

   //--------------------------------------------------------------------------

   public GreetingEntity addGreeting(String country, String phrase) {
      GreetingEntity greeting = new GreetingEntity("ja", "konichiwa");
      greetingDao.save(greeting);
      return greeting;
   }

   public void assertResponseStatusException(HttpStatus status, Callable callable) throws Exception {
      try {
         callable.call();
         fail("ResponseStatusException not thrown");
      } catch (ResponseStatusException e) {
         assertEquals("status:", status, e.getStatus());
      }
   }
}
