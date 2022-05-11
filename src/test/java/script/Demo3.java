package script;

import org.testng.Reporter;
import org.testng.annotations.Test;

import generic.BaseTest;

public class Demo3 extends BaseTest {

			
	@Test(dataProvider = "getData", priority = 3,groups= {"smoke"},enabled=false)
	public void test3(String... data) {
		
		for(String v:data) {
			Reporter.log(v,true);
		}
		
		
		
	}
}
