package com.galli.project;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Before executing this Test Case from Eclipse:
 * 
 * 1) Run the following command from the folder where docker-compose.yml is in
 * and wait a few seconds:
 * 
 * docker compose up -d postgres_db
 * 
 * 2) Run the Project as a Spring Boot App from Eclipse.
 * 
 * 3) Now you can execute the Test Case.
 * 
 * 4) Before executing again the Test Case, to avoid errors, you must first stop
 * the container and the Spring Boot App and then repeat the steps from scratch.
 * 
 * After execution stop the Spring Boot App and run this command from the same
 * above mentioned folder to remove the container:
 * 
 * docker compose down
 * 
 */
class CarRacingOrganizationE2E { // NOSONAR java:S3577

	private static int port = Integer
			.parseInt(System.getProperty("server.port", "8080"));

	private static String baseUrl = "http://localhost:" + port;

	private WebDriver driver;

	@BeforeAll
	static void setupClass() {
		WebDriverManager.firefoxdriver().setup();
	}

	@BeforeEach
	void setup() {
		baseUrl = "http://localhost:" + port;
		driver = new FirefoxDriver();
	}

	@AfterEach
	void teardown() {
		driver.quit();
	}

	@Test
	@DisplayName("create pilots, circuits, and race")
	void test1() {
		// add two pilots to database
		driver.get(baseUrl + "/homepage");
		driver.findElement(By.linkText("Pilots List")).click();
		driver.findElement(By.linkText("New Pilot")).click();
		driver.findElement(By.name("name")).sendKeys("e2e first pilot");
		driver.findElement(By.name("btn_submit")).click();
		driver.findElement(By.linkText("New Pilot")).click();
		driver.findElement(By.name("name")).sendKeys("e2e second pilot");
		driver.findElement(By.name("btn_submit")).click();
		// verify pilots are added
		assertThat(driver.findElement(By.id("pilots_table")).getText())
				.contains("e2e first pilot", "e2e second pilot");

		// add two circuits to database
		driver.get(baseUrl + "/homepage");
		driver.findElement(By.linkText("Circuits List")).click();
		driver.findElement(By.linkText("New Circuit")).click();
		driver.findElement(By.name("name")).sendKeys("e2e first circuit");
		driver.findElement(By.name("length")).sendKeys("1000");
		driver.findElement(By.name("btn_submit")).click();
		driver.findElement(By.linkText("New Circuit")).click();
		driver.findElement(By.name("name")).sendKeys("e2e second circuit");
		driver.findElement(By.name("length")).sendKeys("2000");
		driver.findElement(By.name("btn_submit")).click();
		// verify circuits are added
		assertThat(driver.findElement(By.id("circuits_table")).getText())
				.contains("e2e first circuit", "1000", "e2e second circuit",
						"2000");

		// create new race
		driver.get(baseUrl + "/homepage");
		driver.findElement(By.linkText("Races List")).click();
		driver.findElement(By.linkText("New Race")).click();
		driver.findElement(By.name("name")).sendKeys("e2e first race");
		new Select(driver.findElement(By.name("circuit.id")))
				.selectByVisibleText("e2e first circuit");
		driver.findElement(By.name("btn_edit_name")).click();

		// add 2 pilots to the race
		new Select(driver.findElement(By.name("pilotId")))
				.selectByVisibleText("e2e first pilot");
		driver.findElement(By.name("btn_add_pilot")).click();
		new Select(driver.findElement(By.name("pilotId")))
				.selectByVisibleText("e2e second pilot");
		driver.findElement(By.name("btn_add_pilot")).click();
		assertThat(driver.findElement(By.id("pilots_table")).getText())
				.contains("e2e first pilot", "e2e second pilot");

		// remove one pilot from the race
		String xpath = "//tr[td[normalize-space()='e2e first pilot']]//*[@name='btn_remove_pilot']";
		driver.findElement(By.xpath(xpath)).click();
		assertThat(driver.findElement(By.id("pilots_table")).getText())
				.contains("e2e second pilot").doesNotContain("e2e first pilot");

		// add second race
		driver.get(baseUrl + "/races");
		driver.findElement(By.linkText("New Race")).click();
		driver.findElement(By.name("name")).sendKeys("e2e second race");
		new Select(driver.findElement(By.name("circuit.id")))
				.selectByVisibleText("e2e second circuit");
		driver.findElement(By.name("btn_edit_circuit")).click();

		// verify races are added
		driver.get(baseUrl + "/races");
		assertThat(driver.findElement(By.id("races_table")).getText())
				.contains("e2e first race", "e2e second race");

		// delete race and verify it is removed
		xpath = "//tr[td[normalize-space()='e2e first race']]//a[normalize-space()='View']";
		driver.findElement(By.xpath(xpath)).click();
		driver.findElement(By.name("btn_delete_race")).click();
		assertThat(driver.findElement(By.id("races_table")).getText())
				.contains("e2e second race")
				.doesNotContain("e2e first race");
	}

	// edit pilot, edit circuit, edit race
	@Test
	@DisplayName("edit pilot, circuit, race")
	void test2() {
		// add two pilots to database
		driver.get(baseUrl + "/homepage");
		driver.findElement(By.linkText("Pilots List")).click();
		driver.findElement(By.linkText("New Pilot")).click();
		driver.findElement(By.name("name")).sendKeys("e2e third pilot");
		driver.findElement(By.name("btn_submit")).click();
		driver.findElement(By.linkText("New Pilot")).click();
		driver.findElement(By.name("name")).sendKeys("e2e fourth pilot");
		driver.findElement(By.name("btn_submit")).click();

		// add two circuits to database
		driver.get(baseUrl + "/homepage");
		driver.findElement(By.linkText("Circuits List")).click();
		driver.findElement(By.linkText("New Circuit")).click();
		driver.findElement(By.name("name")).sendKeys("e2e third circuit");
		driver.findElement(By.name("length")).sendKeys("3000");
		driver.findElement(By.name("btn_submit")).click();
		driver.findElement(By.linkText("New Circuit")).click();
		driver.findElement(By.name("name")).sendKeys("e2e fourth circuit");
		driver.findElement(By.name("length")).sendKeys("4000");
		driver.findElement(By.name("btn_submit")).click();

		// create new race
		driver.get(baseUrl + "/homepage");
		driver.findElement(By.linkText("Races List")).click();
		driver.findElement(By.linkText("New Race")).click();
		driver.findElement(By.name("name")).sendKeys("e2e third race");
		new Select(driver.findElement(By.name("circuit.id")))
				.selectByVisibleText("e2e third circuit");
		driver.findElement(By.name("btn_edit_name")).click();

		// add 2 pilots to the race
		new Select(driver.findElement(By.name("pilotId")))
				.selectByVisibleText("e2e third pilot");
		driver.findElement(By.name("btn_add_pilot")).click();
		new Select(driver.findElement(By.name("pilotId")))
				.selectByVisibleText("e2e fourth pilot");
		driver.findElement(By.name("btn_add_pilot")).click();

		// edit race
		driver.get(baseUrl + "/races");
		String xpath = "//tr[td[normalize-space()='e2e third race']]//a[normalize-space()='View']";
		driver.findElement(By.xpath(xpath)).click();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("e2e modified race");
		driver.findElement(By.name("btn_edit_name")).click();
		driver.get(baseUrl + "/races");
		assertThat(driver.findElement(By.id("races_table")).getText())
				.doesNotContain("e2e third race").contains("e2e modified race");

		// edit pilot
		driver.get(baseUrl + "/pilots");
		xpath = "//tr[td[normalize-space()='e2e third pilot']]//a[normalize-space()='Edit']";
		driver.findElement(By.xpath(xpath)).click();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("e2e modified pilot");
		driver.findElement(By.name("btn_submit")).click();
		assertThat(driver.findElement(By.id("pilots_table")).getText())
				.doesNotContain("e2e third pilot")
				.contains("e2e modified pilot");

		// edit circuit
		driver.get(baseUrl + "/circuits");
		xpath = "//tr[td[normalize-space()='e2e third circuit']]//a[normalize-space()='Edit']";
		driver.findElement(By.xpath(xpath)).click();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("e2e modified circuit");
		driver.findElement(By.name("length")).clear();
		driver.findElement(By.name("length")).sendKeys("9000");
		driver.findElement(By.name("btn_submit")).click();
		assertThat(driver.findElement(By.id("circuits_table")).getText())
				.doesNotContain("e2e third circuit", "3000")
				.contains("e2e modified circuit", "9000");
	}
}