package ru.nsu.fit.mkrikunov.xml;

import java.util.HashSet;
import java.util.Set;

public class  PersonInstance {
  public String ID;
  public String firstName;
  public String lastName;
  public String gender;

  public String spouceID;
  public String spouceName;
  public String husbandID;
  public String husbandName;
  public String wifeID;
  public String wifeName;

  public Integer childrenCount = null;
  public Set<String> childrenID = new HashSet<String>();
  public Set<String> childrenName = new HashSet<String>();
  public Set<String> sonsID = new HashSet<String>();
  public Set<String> sonsName = new HashSet<String>();
  public Set<String> daughtersID = new HashSet<String>();
  public Set<String> daughtersName = new HashSet<String>();

  public Set<String> parentsID = new HashSet<String>();
  public Set<String> parentsName = new HashSet<String>();
  public String motherID;
  public String motherName;
  public String fatherID;
  public String fatherName;

  public Integer siblingsCount = null;
  public Set<String> siblingsID = new HashSet<String>();
  public Set<String> siblingsName = new HashSet<String>();
  public Set<String> brothersID = new HashSet<String>();
  public Set<String> brothersName = new HashSet<String>();
  public Set<String> sistersID = new HashSet<String>();
  public Set<String> sistersName = new HashSet<String>();

  private void mergeSets(Set<String> set1, Set<String> set2) {
    for (String item : set2) {
      if (!set1.contains(item)) {
        set1.add(item);
      }
    }
  }

  public void merge(PersonInstance person) {
    if (person == null)
      return;

    if (firstName == null)
      firstName = person.firstName;
    if (lastName == null)
      lastName = person.lastName;
    if (ID == null)
      ID = person.ID;
    if (gender == null)
      gender = person.gender;

    if (spouceID == null)
      spouceID = person.spouceID;
    if (husbandID == null)
      husbandID = person.husbandID;
    if (wifeID == null)
      wifeID = person.wifeID;
    if (spouceName == null)
      spouceName = person.spouceName;
    if (husbandName == null)
      husbandName = person.husbandName;
    if (wifeName == null)
      wifeName = person.wifeName;

    mergeSets(parentsID, person.parentsID);
    if (motherID == null)
      motherID = person.motherID;
    if (fatherID == null)
      fatherID = person.fatherID;
    mergeSets(parentsName, person.parentsName);
    if (motherName == null)
      motherName = person.motherName;
    if (fatherName == null)
      fatherName = person.fatherName;

    if (childrenCount == null)
      childrenCount = person.childrenCount;
    mergeSets(childrenID, person.childrenID);
    mergeSets(childrenName, person.childrenName);
    mergeSets(sonsID, person.sonsID);
    mergeSets(sonsName, person.sonsName);
    mergeSets(daughtersID, person.daughtersID);
    mergeSets(daughtersName, person.daughtersName);

    if (siblingsCount == null)
      siblingsCount = person.siblingsCount;
    mergeSets(siblingsID, person.siblingsID);
    mergeSets(siblingsName, person.siblingsName);
    mergeSets(brothersID, person.brothersID);
    mergeSets(brothersName, person.brothersName);
    mergeSets(sistersID, person.sistersID);
    mergeSets(sistersName, person.sistersName);
  }

  @Override
  public String toString() {
    return "ID: " + ID + " " +
        " | name: " + firstName + " " + lastName + " " +
        " | gender: " + gender + " " +

        " | spouceID: " + spouceID + " " +
        " | spouceName: " + spouceName + " " +

        " | fatherID: " + fatherID + " " +
        " | fatherName: " + fatherName + " " +
        " | motherID: " + motherID + " " +
        " | motherName: " + motherName + " " +

        " | siblings: " + siblingsName.toString() + ", count: " + siblingsCount + " " +

        " | children: " + childrenName.toString() + ", count: " + childrenCount + " ";
  }
}
