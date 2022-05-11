package script;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import generic.BaseTest;
import generic.Utility;

public class Demo1 extends BaseTest {

	@Test(priority = 1,groups= {"smoke"},enabled=true)
	public void test1() {
		
		String title = driver.getTitle();
		Reporter.log(title,true);
		test.info(title);
		int rc=Utility.getXlRowCount(DATA_PATH, "test1");
		for(int i=1;i<=rc;i++) {//1 2 3
				String un=Utility.getXLData(DATA_PATH, "test1",i, 0);
				Reporter.log(un,true);
				String pw=Utility.getXLData(DATA_PATH, "test1",i, 1);
				Reporter.log(pw,true);
		}
		
		Assert.assertEquals("abcd","xyz");
	}
}
