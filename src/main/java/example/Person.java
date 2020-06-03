package example;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "nickname_id", foreignKey = @ForeignKey(name = "fk_nickname",
      foreignKeyDefinition = "foreign key(nickname_id) references nickname(id) on delete set null"))
  private Nickname nickname;

  public Person() {
  }

  public Person(String name) {
    this.name = name;
  }

  public void setNickname(Nickname nickname) {
    this.nickname = nickname;
  }

  public Nickname getNickname() {
    return nickname;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Person)) {
      return false;
    }
    Person person = (Person) o;
    return getName().equals(person.getName());
  }

  @Override
  public int hashCode() {
    return getName().hashCode();
  }
}
