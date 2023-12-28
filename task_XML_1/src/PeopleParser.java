import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamConstants;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PeopleParser {

  private void childrenAssertion(HashMap<String, PersonInstance> storageWithID) {
    for (String key : storageWithID.keySet()) {
      PersonInstance p = storageWithID.get(key);
      p.childrenID.addAll(p.sonsID);
      p.childrenID.addAll(p.daughtersID);
      for (String s : p.daughtersName) {
        List<PersonInstance> f = findInStorage(x -> s.equals(x.firstName + " " + x.lastName),
            storageWithID.values());
        if (!f.isEmpty()) {
          PersonInstance daughter = f.getFirst();
          if (daughter != null) {
            p.childrenID.add(daughter.ID);
          }
        }
      }
      for (String s : p.sonsName) {
        List<PersonInstance> f = findInStorage(x -> s.equals(x.firstName + " " + x.lastName),
            storageWithID.values());
        if (!f.isEmpty()) {
          PersonInstance son = f.getFirst();
          if (son != null) {
            p.childrenID.add(son.ID);
          }
        }
      }
      for (String s : p.childrenName) {
        List<PersonInstance> f = findInStorage(x -> s.equals(x.firstName + " " + x.lastName),
            storageWithID.values());
        if (!f.isEmpty()) {
          PersonInstance child = f.getFirst();
          if (child != null) {
            p.childrenID.add(child.ID);
          }
        }
      }
    }
  }

  private void siblingsAssertion(HashMap<String, PersonInstance> storageWithID) {
    for (String key : storageWithID.keySet()) {
      PersonInstance p = storageWithID.get(key);
      p.siblingsID.addAll(p.brothersID);
      p.siblingsID.addAll(p.sistersID);
      for (String s : p.sistersName) {
        List<PersonInstance> f = findInStorage(x -> s.equals(x.firstName + " " + x.lastName),
            storageWithID.values());
        if (!f.isEmpty()) {
          PersonInstance sister = f.getFirst();
          if (sister != null) {
            p.siblingsID.add(sister.ID);
          }
        }
      }
      for (String s : p.brothersName) {
        List<PersonInstance> f = findInStorage(x -> s.equals(x.firstName + " " + x.lastName),
            storageWithID.values());
        if (!f.isEmpty()) {
          PersonInstance brother = f.getFirst();
          if (brother != null) {
            p.siblingsID.add(brother.ID);
          }
        }
      }
      for (String s : p.siblingsName) {
        List<PersonInstance> f = findInStorage(x -> s.equals(x.firstName + " " + x.lastName),
            storageWithID.values());
        if (!f.isEmpty()) {
          PersonInstance sibling = f.getFirst();
          if (sibling != null) {
            p.siblingsID.add(sibling.ID);
          }
        }
      }
    }
  }

  private void genderAssertion(HashMap<String, PersonInstance> storageWithID) {
    for (var p : storageWithID.values()) {
      if (p.gender == null) {
        if (p.wifeID != null || p.wifeName != null) {
          p.gender = "M";
        }
        else if (p.husbandID != null || p.husbandName != null) {
          p.gender = "F";
        }
        else if (p.spouceID != null) {
          PersonInstance pp = storageWithID.get(p.spouceID);
          if (pp.gender != null) {
            if (pp.gender.equals("M")) {
              p.gender = "F";
            }
            if (pp.gender.equals("F"))  {
              p.gender = "M";
            }
          }
          else if (pp.husbandName != null || pp.husbandID != null) {
            p.gender = "M";
          }
          else if (pp.wifeName != null || pp.wifeID != null) {
            p.gender = "F";
          }
        }
        else {
          p.gender = "M";
        }
      }
    }


    for (var p : storageWithID.values()) {
      try {
        assert p.gender != null && (p.gender.equals("M") || p.gender.equals("F"));
      } catch (AssertionError e) {
        System.out.println("This person hasn't gender: " + p);
      }
    }
  }

  private List<PersonInstance> findInStorage(
      Predicate<PersonInstance> pred, Collection<PersonInstance> coll) {
    return coll.parallelStream().filter(pred).collect(Collectors.toList());
  }
  private ArrayList<PersonInstance> unify(ArrayList<PersonInstance> persons, Integer peopleCount) {
    HashMap<String, PersonInstance> storageWithID = new HashMap<>();
    ArrayList<PersonInstance> personsTemp = new ArrayList<>();

    // Заполняем хранилища
    for (PersonInstance i : persons) {
      if (i.ID != null) {
        if (storageWithID.containsKey(i.ID)) {
          storageWithID.get(i.ID).merge(i);
        } else {
          storageWithID.put(i.ID, i);
        }
      } else {
        personsTemp.add(i);
      }
    }
    persons = personsTemp;
    personsTemp = new ArrayList<>();

    // Слияние дупликатов
    for (PersonInstance p : persons) {
      List<PersonInstance> found =  findInStorage(x -> x.firstName.equals(p.firstName) && x.lastName.equals(p.lastName),
          storageWithID.values());
      if (found.size() == 1) {
        PersonInstance foundPerson = found.getFirst();
        foundPerson.merge(p);
        storageWithID.replace(foundPerson.ID, foundPerson);
      } else if (found.size() > 1) {
        PersonInstance foundPerson = found.getFirst();
        personsTemp.addAll(found);
        if (foundPerson.gender == null && p.gender != null) {
          foundPerson.merge(p);
        }
      }
    }
    persons = personsTemp;
    personsTemp = new ArrayList<>();

    //Слияние по братьям и сестрам
    for (PersonInstance person : persons) {
      if (person.siblingsID != null) {
        HashSet<String> siblings = new HashSet<>(person.siblingsID);
        List<PersonInstance> found = findInStorage(
            x -> {
              HashSet<String> xsib = new HashSet<>(x.siblingsID);
              xsib.retainAll(siblings);
              return !xsib.isEmpty();
            }, storageWithID.values()
        );
        if (found.size() == 1) {
          found.getFirst().merge(person);
        } else {
          personsTemp.add(person);
        }
      }
    }

    // Заполнение детей
    childrenAssertion(storageWithID);
    // Заполнение братьев и сестер
    siblingsAssertion(storageWithID);
    // Заполнение гендеров
    genderAssertion(storageWithID);

    return new ArrayList<>(storageWithID.values());
  }

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

            case "id":
              for (int i = 0; i < reader.getAttributeCount(); i++) {
                if (reader.getAttributeLocalName(i).equals("value")) {
                  assert currPerson != null;
                  currPerson.ID = reader.getAttributeValue(i).trim();
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
                if ("value".equals(reader.getAttributeLocalName(i))) {
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
    return unify(persons, peopleCount);
  }
}
