package com.galli.project.jpa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.galli.project.model.Pilot;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PilotJpaTest {

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testJpaMapping() {
		Pilot saved = entityManager.persistFlushFind(new Pilot(null, "test"));
//		assertThat(saved.getName()).isEqualTo("test");
//		assertThat(saved.getSalary()).isEqualTo(1000);
//		assertThat(saved.getId()).isNotNull();
//		assertThat(saved.getId()).isPositive();
//		LoggerFactory.getLogger(EmployeeJpaTest.class)
//				.info("Saved: " + saved.toString());
	}

}
