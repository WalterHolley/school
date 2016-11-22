
public class NameTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("***BEGIN NAME CLASS TESTS***");
		
		System.out.println("***END NAME CLASS TESTS***");

	}
	
	private static boolean runReverseNameTest(String expectedResult, Name name){
		boolean testPassed = false;
		String testResult = name.getReverseOrder();
		testPassed = (testResult == expectedResult)? true: false;
		System.out.println("***REVERSE NAME TEST***");
		System.out.printf("EXPECTED: %s \nRESULT: %s \n", expectedResult, testResult);
		
		if(testPassed){
			System.out.println("TEST SUCCESSFUL");
		}
		else{
			System.out.println("TEST FAILED");
		}
		
		System.out.println("***END OF TEST***");
		return testPassed;
	}
	
	private static boolean runNameStringTest(String expectedResult, Name name){
		boolean testPassed = false;
		String testResult = name.toString();
		testPassed = (testResult == expectedResult)? true: false;
		System.out.println("***NAME STRING TEST***");
		System.out.printf("EXPECTED: %s \nRESULT: %s \n", expectedResult, testResult);
		
		if(testPassed){
			System.out.println("TEST SUCCESSFUL");
		}
		else{
			System.out.println("TEST FAILED");
		}
		
		System.out.println("***END OF TEST***");
		return testPassed;
	}

}
