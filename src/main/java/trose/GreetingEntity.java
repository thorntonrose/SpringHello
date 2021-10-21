package trose;

import java.text.*;
import javax.persistence.*;

@Entity
@Table(name="greetings")
public class GreetingEntity {
   @Id
   @Column(name="country")
   public String country;

   @Column(name="phrase")
   public String phrase;

   public GreetingEntity() {
   }

   public GreetingEntity(String country, String phrase) {
      this.country = country;
      this.phrase = phrase;
   }

   public String toString() {
      return MessageFormat.format("country={0}, phrase={1}", country, phrase);
   }
}
