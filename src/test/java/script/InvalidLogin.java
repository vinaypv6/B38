package script;

import org.testng.Assert;
import org.testng.annotations.Test;

import generic.BaseTest;
import page.LoginPage;

public class InvalidLogin extends BaseTest{

	@Test(priority = 2,groups= {"smoke"})
	public void testInvalidLogin() {
//		•	Enter invalid user name (abcd)
		test.info("Enter invalid user name (abcd)");
		LoginPage loginPage=new LoginPage(driver);
		loginPage.setUserName("abcd");
		
//		•	Enter invalid password (xyz)
		test.info("Enter invalid password (xyz)");
		loginPage.setPassword("xyz");
		
//		•	Click on login button
		test.info("Click on login button");
		loginPage.clickLoginButton();
		
//		•	Verify that err msg is displayed
		test.info("Verify that err msg is displayed");
		boolean result = loginPage.verifyErrMsgDisplayed(wait);
		Assert.assertTrue(result,"Err Msg is not displayed");
		test.pass("Err Msg is displayed");
	}
}
