package example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "nickname")
public class Nickname {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String value;

  protected Nickname() {
  }

  public Nickname(String value) {
    this.value = value;
  }

  public int getId() {
    return id;
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Nickname address1 = (Nickname) o;
    return getValue().equals(address1.getValue());
  }

  @Override
  public int hashCode() {
    return getValue().hashCode();
  }
}
