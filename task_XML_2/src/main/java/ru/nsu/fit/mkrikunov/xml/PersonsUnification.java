package ru.nsu.fit.mkrikunov.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PersonsUnification {

  private static void childrenAssertion(HashMap<String, PersonInstance> storageWithID) {
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

  private static void siblingsAssertion(HashMap<String, PersonInstance> storageWithID) {
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

  private static void genderAssertion(HashMap<String, PersonInstance> storageWithID) {
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

  private static List<PersonInstance> findInStorage(
      Predicate<PersonInstance> pred, Collection<PersonInstance> coll) {
    return coll.parallelStream().filter(pred).collect(Collectors.toList());
  }

  public static ArrayList<PersonInstance> unify(ArrayList<PersonInstance> persons, Integer peopleCount) {
    HashMap<String, PersonInstance> storageWithID = new HashMap<>();
    ArrayList<PersonInstance> personsTemp = new ArrayList<>(); //сюда попадут все персоны без ID

    // Заполняем хранилища
    for (PersonInstance i : persons) {
      if (i.ID != null) {
        if (storageWithID.containsKey(i.ID)) {
          storageWithID.get(i.ID).merge(i); //если уже есть персона с таким же ID, то мерджим
        } else {
          storageWithID.put(i.ID, i); //иначе кладем в storageWithID
        }
      } else {
        personsTemp.add(i); //кладем сюда если у персоны нет ID
      }
    }
    persons = personsTemp;
    personsTemp = new ArrayList<>();

    // Слияние дупликатов
    for (PersonInstance person : persons) {
      List<PersonInstance> found =  findInStorage(
          x -> x.firstName.equals(person.firstName) && x.lastName.equals(person.lastName), storageWithID.values());
      if (found.size() == 1) {
        PersonInstance foundPerson = found.getFirst();
        foundPerson.merge(person);
        storageWithID.replace(foundPerson.ID, foundPerson);
      } else if (found.size() > 1) {
        PersonInstance foundPerson = found.getFirst();
        personsTemp.addAll(found);
        if (foundPerson.gender == null && person.gender != null) {
          foundPerson.merge(person);
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

}
