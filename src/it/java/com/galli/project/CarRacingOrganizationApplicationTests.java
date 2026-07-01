package com.galli.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Before executing this Test Case from Eclipse run the following
 * command from the folder where docker-compose.yml is in
 * and wait a few seconds:
 * 
 * docker compose up -d postgres_db
 * 
 * After execution run this command from the same folder to remove the container:
 * 
 * docker compose down
 */
@SpringBootTest
class CarRacingOrganizationApplicationTests {

	@Test
	void contextLoads() {
	}

}
