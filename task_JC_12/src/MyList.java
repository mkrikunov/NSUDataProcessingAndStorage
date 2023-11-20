public class MyList {
  private static class Node {
    String value;
    Node next;

    Node(String value, Node next) {
      this.value = value;
      this.next = next;
    }
  }

  private volatile Node head = null;

  public void add(String value) {
    head = new Node(value, head);
  }

  public void print() {
    Node currHead = head;
    while (currHead != null) {
      System.out.println(currHead.value);
      currHead = currHead.next;
    }
  }

  public void sort() {
    Node i = head;
    while (i != null) {
      Node j = i.next;
      while (j != null) {
        if (i.value.compareTo(j.value) > 0) {
          String s = i.value;
          i.value = j.value;
          j.value = s;
        }
        j = j.next;
      }
      i = i.next;
    }
  }
}
