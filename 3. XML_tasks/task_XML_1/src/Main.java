import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Main {

  public static void main(String[] args) throws IOException, XMLStreamException {
    InputStream stream = new FileInputStream("src/people.xml");
    ArrayList<PersonInstance> parsedData = new PeopleParser().parse(stream);
    parsedData.subList(1, 100).forEach(System.out::println);
  }
}