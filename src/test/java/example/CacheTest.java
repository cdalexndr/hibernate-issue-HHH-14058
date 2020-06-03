package example;

import java.util.UUID;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SpringBootTest
@AutoConfigureCache(cacheProvider = CacheType.JCACHE)
@TestPropertySource(properties = {
    "spring.jpa.properties.javax.persistence.sharedCache.mode=DISABLE_SELECTIVE",
    "spring.jpa.properties.hibernate.cache.use_second_level_cache=true"
})
public class CacheTest extends AbstractTransactionalTestNGSpringContextTests {

  @Autowired
  PersonRepository personRepository;
  @Autowired
  EntityManager entityManager;

  private int personId;
  private int nicknameId;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @BeforeClass
  public void init() {
    Person person = new Person(UUID.randomUUID().toString());
    person = personRepository.save(person);
    entityManager.flush();
    this.personId = person.getId();

    //update to store in second level cache
    person.setNickname(new Nickname(UUID.randomUUID().toString()));
    entityManager.flush();
    assert entityManager.getEntityManagerFactory().getCache().contains(Person.class, personId);

    assert person.getNickname().getId() > 0;
    this.nicknameId = person.getNickname().getId();
  }

  @Test
  public void test() {
    assert entityManager.getEntityManagerFactory().getCache().contains(Person.class, personId);

    int modified = entityManager.createQuery(
        "delete from nickname"
            + " where id = :id")
        .setParameter("id", nicknameId)
        .executeUpdate();
    assert modified == 1;
    entityManager.getEntityManagerFactory().getCache().evict(Nickname.class);

    assert entityManager.getEntityManagerFactory().getCache().contains(Person.class, personId);

    //uncomment following line to succeed
//    entityManager.getEntityManagerFactory().getCache().evictAll();

    Person person = entityManager.find(Person.class, personId);
    assert person != null;
    assert person.getNickname() == null;
  }
}
