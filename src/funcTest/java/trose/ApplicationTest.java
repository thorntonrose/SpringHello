package trose;

import java.io.*;
import java.net.*;
import java.text.*;
import org.junit.*;
import org.slf4j.*;
import org.testcontainers.containers.*;
import org.testcontainers.containers.output.*;
import org.testcontainers.containers.wait.strategy.*;
import static org.junit.Assert.*;

public class ApplicationTest {
   private static final Logger log = LoggerFactory.getLogger(ApplicationTest.class);
   private static GenericContainer container;

   @BeforeClass
   public static void classSetUp() throws Exception {
      log.debug("classSetUp ...");

      container = new GenericContainer(System.getProperty("image"))
         .withExposedPorts(8080, 8080)
         .withLogConsumer(new Slf4jLogConsumer(log))
         .waitingFor(Wait.forLogMessage(".*ACCEPTING_TRAFFIC.*", 1));
      container.start();
   }

   @AfterClass
   public static void classTearDown() throws Exception {
      log.debug("classTearDown ...");
      if (container != null) { container.stop(); }
   }

   @Test
   public void testIndex() throws Exception {
      log.debug("testIndex ...");
      assertEquals("index:", "hello", getText("/"));
   }

   @Test
   public void testUpper() throws Exception {
      log.debug("testUpper ...");
      assertEquals("upper:", "HELLO", getText("/upper"));
   }

   //--------------------------------------------------------------------------

   private String getText(String uri) throws Exception {
      StringBuilder text = new StringBuilder();
      URL url = new URL(MessageFormat.format("http://localhost:{0}{1}", container.getFirstMappedPort(), uri));
      BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

      try {
         String line;
         while ((line = reader.readLine()) != null) { text.append(line); }
      } finally {
         reader.close();
      }

      return text.toString();
   }
}
