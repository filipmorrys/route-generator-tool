package es.indra.tools.route.generator.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TopologyDaoTest {
  
  @Autowired
  private TopologyDao dao;

  @Test
  void test() {
    String expected = "20190130_133122";
    
    String groupId = dao.getCurrengGroupId();
    assertEquals(expected, groupId);
  }

}
