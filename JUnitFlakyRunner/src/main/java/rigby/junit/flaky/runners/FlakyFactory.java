package rigby.junit.flaky.runners;

import java.util.HashMap;

public class FlakyFactory {

	private static FlakyFactory instance = null;
	private static HashMap<String, FlakyTracker> trackerMap; 
	
	private FlakyFactory() {
		
	}

	public static FlakyFactory getInstance() {
		
		if (instance == null) {
			instance = new FlakyFactory();
			trackerMap = new HashMap<>();
		}
		return instance;
	}
	
	public FlakyTracker getFlakyTracker(Class<?> testClass) {
				
		String testClassName = testClass.getName();
		//System.out.println("testClassName = " + testClassName);
		if (!trackerMap.containsKey(testClassName)) {
			trackerMap.put(testClassName, new FlakyTracker(testClassName));
		}
		
		return trackerMap.get(testClassName);
	}
}
