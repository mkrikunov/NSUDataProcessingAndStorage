package ru.nsu.fit.mkrikunov.xml;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.util.ArrayList;
import org.xml.sax.SAXException;
import types.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.validation.SchemaFactory;

public class Main {
  public static void main(String[] args) {
    ArrayList<PersonInstance> data = new ArrayList<>();
    try (FileInputStream stream = new FileInputStream("src/main/resources/people.xml")) {
      data = new PeopleParser().parse(stream);
    } catch (IOException | XMLStreamException e) {
      System.err.println("Exception: " + e.getMessage());
    }

    People people = new People();
    Map<String, PersonType> table = new HashMap<>();

    for (PersonInstance p : data) {
      PersonType pp = new PersonType();

      //устанавливаем основную инфу
      pp.setId(p.ID);
      pp.setName(p.firstName + " " + p.lastName);
      pp.setGender(GenderType.fromValue(p.gender));
      table.put(p.ID, pp);
    }

    int j = 0;
    for (PersonType p : table.values()) {
      PersonInstance person = new PersonInstance();
      if (data.size() != j + 1) {
        person = data.get(j);
      } else {
        break;
      }

      //устанавливаем супруга
      if (person.spouceID != null) {
        IdType id = new IdType();
        id.setId(table.get(person.spouceID));
        p.setSpouce(id);
      }
      if (person.wifeID != null) {
        IdType id = new IdType();
        id.setId(table.get(person.wifeID));
        p.setSpouce(id);
      }
      if (person.husbandID != null) {
        IdType id = new IdType();
        id.setId(table.get(person.husbandID));
        p.setSpouce(id);
      }

      //устанавливаем детей (в угол (да, такой юмор))
      ChildrenType childrenType = new ChildrenType();
      childrenType.setCount(BigInteger.valueOf(
          person.childrenCount != null ? person.childrenCount : -1
      ));
      for (String i : person.childrenID) {
        IdType id = new IdType();
        id.setId(table.get(i));
        childrenType.getChildId().add(id);
      }
      p.setChildren(childrenType);

      //устанавливаем сиблингов
      SiblingsType siblingsType = new SiblingsType();
      siblingsType.setCount(BigInteger.valueOf(
          person.siblingsCount != null ? person.siblingsCount : -1
      ));
      for (String i : person.siblingsID) {
        IdType id = new IdType();
        id.setId(table.get(i));
        siblingsType.getSiblingId().add(id);
      }
      p.setSiblings(siblingsType);

      //устанавливаем родителей (родителей нельзя устанавливать, они у нас одни, их надо уважать)
      ParentsType parentsType = new ParentsType();
      for (String i : person.parentsID) {
        IdType id = new IdType();
        id.setId(table.get(i));
        parentsType.getParentId().add(id);
      }
      p.setParents(parentsType);
      j++;
    }

    people.getPerson().addAll(table.values());

    try {
      JAXBContext jc = JAXBContext.newInstance("types", People.class.getClassLoader());
      Marshaller writer = jc.createMarshaller();
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      File schemaFile = new File("src/main/resources/schema.xsd");
      writer.setSchema(schemaFactory.newSchema(schemaFile));
      writer.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      writer.marshal(people, new File("src/main/resources/output.xml"));
    } catch (JAXBException | SAXException e) {
      System.err.println("Exception: " + e.getMessage());
    }
  }
}
