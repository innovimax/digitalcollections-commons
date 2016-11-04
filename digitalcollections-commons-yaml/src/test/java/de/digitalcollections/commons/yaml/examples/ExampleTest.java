package de.digitalcollections.commons.yaml.examples;

import de.digitalcollections.commons.yaml.examples.Book;
import de.digitalcollections.commons.yaml.examples.Person;
import static org.assertj.core.api.Assertions.assertThat;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

public class ExampleTest {

  @Test
  public void toStringShouldSerializeBook() {
    Person arkadiStrugatzki = new Person("Arkadi", "Strugatzki", LocalDateTime.parse("1925-08-28"));
    LocalDateTime published = LocalDateTime.parse("1971");
    Book roadsidePicknic = new Book(arkadiStrugatzki, "Roadside Picknic", published);

    System.out.println("roadsidePicknic.toString()");
    System.out.println(roadsidePicknic.toString());

    assertThat(roadsidePicknic.toString())
        .isEqualTo("!!de.digitalcollections.commons.yaml.examples.Book {author: {bornAt: !!timestamp '1925-08-27T23:00:00Z', firstName: Arkadi, lastName: Strugatzki}, published: !!timestamp '1970-12-31T23:00:00Z', title: Roadside Picknic}");
  }

}
