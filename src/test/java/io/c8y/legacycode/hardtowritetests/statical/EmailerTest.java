package io.c8y.legacycode.hardtowritetests.statical;


import org.junit.jupiter.api.Test;

/**
 * TODO:
 * Your goal is to write a tests for components sending emails
 * Run tests
 *    - See that calls of real code are throwing the exception without assertions
 *    - Do refactoring in the way to be able to test email send
 *    - Finish the assertions and implementation of Emailer component
 *    - Add full test coverage
 *
 */
public class EmailerTest {

	@Test
	public void shouldSendHelloEmail() throws Exception {
		
		new Emailer().sendGreetingEmailTo(aUser("email@gmail.com", "User"));
		
		//TODO: verify email was sent to email@gmail.com with subject "hello, User"
		//sth like
//		Assertions.assertThat(getMessageTo("email@gmail.com",)).isEqualTo("hello, User");
	}
	
	@Test
	public void shouldSendUnsubscribeEmailAndNotifyAdminAboutUserLeaving() throws Exception {
		
		new Emailer().sendByeByeEmailTo(aUser("email@gmail.com","User"));
		
		//TODO: verify email was sent to email@gmail.com with subject "bye, User"
		//TODO: verify email was sent to admin@gmail.com with subject "user User left the system"
	}

	private User aUser(String email, String name) {
		return new User(email,name);
	}
	
}
