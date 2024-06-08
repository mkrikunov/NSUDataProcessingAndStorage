import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamConstants;
import java.io.InputStream;
import java.util.*;

public class PeopleParser {

  public ArrayList<PersonInstance> parse(InputStream stream) throws XMLStreamException {
    ArrayList<PersonInstance> persons = new ArrayList<>();

    XMLInputFactory streamFactory = XMLInputFactory.newInstance();
    XMLStreamReader reader = streamFactory.createXMLStreamReader(stream);

    int peopleCount = 0;
    PersonInstance currPerson = null;
    while (reader.hasNext()) {
      reader.next();
      String[] tmp;
      int eventType = reader.getEventType();
      switch (eventType) {
        case XMLStreamConstants.START_ELEMENT -> {
          switch (reader.getLocalName()) {

            case "people":
              for (int i = 0; i < reader.getAttributeCount(); i++) {
                if (reader.getAttributeLocalName(i).equals("count")) {
                  peopleCount = Integer.parseInt(reader.getAttributeValue(i));
                }
              }
              break;

            case "person":
              currPerson = new PersonInstance();
              for (int i = 0; i < reader.getAttributeCount(); i++) {
                switch (reader.getAttributeLocalName(i)) {
                  case "name" -> {
                    String[] full = reader.getAttributeValue(i).trim().split("\\s+");
                    currPerson.firstName = full[0];
                    currPerson.lastName = full[1];
                  }
                  case "id" -> currPerson.ID = reader.getAttributeValue(i).trim();
                }
              }
              break;

            case "firstname":
              if (reader.getAttributeCount() > 0) {
                for (int i = 0; i < reader.getAttributeCount(); i++) {
                  if (reader.getAttributeLocalName(i).equals("value")) {
                    assert currPerson != null;
                    currPerson.firstName = reader.getAttributeValue(i).trim();
                  }
                }
              } else {
                reader.next();
                assert currPerson != null;
                currPerson.firstName = reader.getText().trim();
              }
              break;

            case "surname":
              for (int i = 0; i < reader.getAttributeCount(); i++) {
                if (reader.getAttributeLocalName(i).equals("value")) {
                  assert currPerson != null;
                  currPerson.lastName = reader.getAttributeValue(i).trim();
                }
              }
              break;

            case "fullname":
              break;

            case "first":
              reader.next();
              assert currPerson != null;
              currPerson.firstName = reader.getText().trim();
              break;

            case "family":
            case "family-name":
              reader.next();
              assert currPerson != null;
              currPerson.lastName = reader.getText().trim();
              break;

            case "id":
              for (int i = 0; i < reader.getAttributeCount(); i++) {
                if (reader.getAttributeLocalName(i).equals("value")) {
                  assert currPerson != null;
                  currPerson.ID = reader.getAttributeValue(i).trim();
                }
              }
              break;

            case "gender":
              if (reader.getAttributeCount() > 0) {
                for (int i = 0; i < reader.getAttributeCount(); i++) {
                  if (reader.getAttributeLocalName(i).equals("value")) {
                    assert currPerson != null;
                    currPerson.gender = reader.getAttributeValue(i).trim().toUpperCase().substring(0, 1);
                  }
                }
              } else {
                reader.next();
                assert currPerson != null;
                currPerson.gender = reader.getText().trim().toUpperCase().substring(0, 1);
              }
              break;

            case "spouce":
              for (int i = 0; i < reader.getAttributeCount(); i++) {
                if (reader.getAttributeLocalName(i).equals("value")) {
                  if (!reader.getAttributeValue(i).trim().equals("NONE")) {
                    assert currPerson != null;
                    currPerson.spouceName = reader.getAttributeValue(i);
                  }
                }
              }
              if (reader.hasText()) {
                if (!reader.getText().trim().equals("NONE")) {
                  assert currPerson != null;
                  currPerson.spouceName = reader.getText();
                }
              }
              break;

            case "husband":
              for (int i = 0; i < reader.getAttributeCount(); i++) {
                if (reader.getAttributeLocalName(i).equals("value")) {
                  assert currPerson != null;
                  currPerson.husbandID = reader.getAttributeValue(i).trim();
                  currPerson.spouceID = reader.getAttributeValue(i).trim();
                }
              }
              break;

            case "wife":
              for (int i = 0; i < reader.getAttributeCount(); i++) {
                if (reader.getAttributeLocalName(i).equals("value")) {
                  assert currPerson != null;
                  currPerson.wifeID = reader.getAttributeValue(i).trim();
                  currPerson.spouceID = reader.getAttributeValue(i).trim();
                }
              }
              break;

            case "siblings":
              for (int i = 0; i < reader.getAttributeCount(); i++) {
                if (reader.getAttributeLocalName(i).equals("val")) {
                  List<String> siblings = Arrays.asList(reader.getAttributeValue(i).trim().split("\\s+"));
                  assert currPerson != null;
                  currPerson.siblingsID.addAll(siblings);
                }
              }
              break;

            case "brother":
              reader.next();
              tmp = reader.getText().trim().split("\\s+");
              assert currPerson != null;
              currPerson.brothersName.add(tmp[0] + " " + tmp[1]);
              break;

            case "sister":
              reader.next();
              tmp = reader.getText().trim().split("\\s+");
              assert currPerson != null;
              currPerson.sistersName.add(tmp[0] + " " + tmp[1]);
              break;

            case "siblings-number":
              for (int i = 0; i < reader.getAttributeCount(); i++) {
                if (reader.getAttributeLocalName(i).equals("value")) {
                  assert currPerson != null;
                  currPerson.siblingsCount = Integer.parseInt(reader.getAttributeValue(i).trim());
                }
              }
              break;

            case "children":
              break;

            case "child":
              reader.next();
              tmp = reader.getText().trim().split("\\s+");
              assert currPerson != null;
              currPerson.childrenName.add(tmp[0] + " " + tmp[1]);
              break;

            case "children-number":
              for (int i = 0; i < reader.getAttributeCount(); i++) {
                if (reader.getAttributeLocalName(i).equals("value")) {
                  assert currPerson != null;
                  if (reader.getAttributeValue(i) != null) {
                    currPerson.childrenCount = Integer.parseInt(reader.getAttributeValue(i).trim());
                  }
                }
              }
              break;

            case "son":
              for (int i = 0; i < reader.getAttributeCount(); i++) {
                if (reader.getAttributeLocalName(i).equals("id")) {
                  assert currPerson != null;
                  currPerson.sonsID.add(reader.getAttributeValue(i).trim());
                }
              }
              break;

            case "daughter":
              for (int i = 0; i < reader.getAttributeCount(); i++) {
                if (reader.getAttributeLocalName(i).equals("id")) {
                  assert currPerson != null;
                  currPerson.daughtersID.add(reader.getAttributeValue(i).trim());
                }
              }
              break;

            case "parent":
              for (int i = 0; i < reader.getAttributeCount(); i++) {
                if (reader.getAttributeLocalName(i).equals("value")) {
                  if (!reader.getAttributeValue(i).trim().equals("UNKNOWN")) {
                    assert currPerson != null;
                    currPerson.parentsID.add(reader.getAttributeValue(i).trim());
                  }
                }
              }
              break;

            case "father":
              reader.next();
              tmp = reader.getText().trim().split("\\s+");
              assert currPerson != null;
              currPerson.fatherName = tmp[0] + " " + tmp[1];
              break;

            case "mother":
              reader.next();
              tmp = reader.getText().trim().split("\\s+");
              assert currPerson != null;
              currPerson.motherName = tmp[0] + " " + tmp[1];
              break;
          }
        }
        case XMLStreamConstants.END_ELEMENT -> {
          if (reader.getLocalName().equals("person")) {
            persons.add(currPerson);
            currPerson = new PersonInstance();
          }
        }
      }

    }

    reader.close();
    return PersonsUnification.unify(persons, peopleCount);
  }
}
