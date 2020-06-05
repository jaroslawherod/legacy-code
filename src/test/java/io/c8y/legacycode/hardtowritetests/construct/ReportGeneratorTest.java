package io.c8y.legacycode.hardtowritetests.construct;

import java.io.IOException;

import io.c8y.legacycode.hardtowritetests.statical.User;
import org.junit.jupiter.api.Test;

/**
 * TODO:
 * Run the test - See that it throws exception event when there is no assertion
 * Check the implementation of  new ReportGenerator()
 * find problematic instructions
 * Refactor it, to not throw exception
 * Refactor it, in the way to be able to write assertion
 * Finish the test to have full coverage
 *
 */
public class ReportGeneratorTest {

	@Test
	public void shouldGenerateReport() throws IOException {
		new ReportGenerator().generateReportFor(aUser("mike"));
		
		//how to assert that something was written to a file?
		//Assertions.assertThat(report).isEqualTo("normal,mike");
	}

	private User aUser(String name) {
		return new User(name + "@email.com",name);
	}
	
}
