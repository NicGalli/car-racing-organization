package com.galli.project.service;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CircuitServiceImpl.class)
class CircuitServiceImplTest {

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
