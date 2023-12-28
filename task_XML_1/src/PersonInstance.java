import java.util.HashSet;
import java.util.Set;

public class PersonInstance {
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

    parentsID.addAll(person.parentsID);
    if (motherID == null)
      motherID = person.motherID;
    if (fatherID == null)
      fatherID = person.fatherID;
    parentsName.addAll(person.parentsName);
    if (motherName == null)
      motherName = person.motherName;
    if (fatherName == null)
      fatherName = person.fatherName;

    if (childrenCount == null)
      childrenCount = person.childrenCount;
    childrenID.addAll(person.childrenID);
    childrenName.addAll(person.childrenName);
    sonsID.addAll(person.sonsID);
    sonsName.addAll(person.sonsName);
    daughtersID.addAll(person.daughtersID);
    daughtersName.addAll(person.daughtersName);

    if (siblingsCount == null)
      siblingsCount = person.siblingsCount;
    siblingsID.addAll(person.siblingsID);
    brothersID.addAll(person.brothersID);
    sistersID.addAll(person.sistersID);
    siblingsName.addAll(person.siblingsName);
    brothersName.addAll(person.brothersName);
    sistersName.addAll(person.sistersName);
  }

  @Override
  public String toString() {
    return "ID: " + ID + " " +
        " | name: " + firstName + " " + lastName + " " +
        " | gender: " + gender + " " +
        " | spouceID: " + spouceID + " " +
        " | siblings: " + siblingsID.toString() + ", count: " + siblingsCount + " " +
        " | children count: " + childrenCount;
  }
}
