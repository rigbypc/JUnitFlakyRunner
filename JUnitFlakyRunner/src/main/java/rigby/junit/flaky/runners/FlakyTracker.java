package rigby.junit.flaky.runners;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class FlakyTracker {

	private HashMap<String, Double> historicalPassRatio;
	private String fileName;
	private double confidence = .05;
	private String testClassName;

	@SuppressWarnings("unchecked")
	public FlakyTracker(String testClassName) {
		this.testClassName = testClassName;
		fileName = System.getProperty("user.dir") + "/" + testClassName + "FlakyTest.ser";
		System.out.println("FlakyResults go here: " + fileName);

		try {
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			historicalPassRatio = (HashMap<String, Double>)in.readObject();
			in.close();
			fileIn.close();
		} 
		catch(FileNotFoundException | ClassCastException e) {
			historicalPassRatio = new HashMap<>();
		}
		catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("Historical Flaky Class not found");
			c.printStackTrace();
			return;
		}
	}

	public void storeFlakyResults() {

		try {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(historicalPassRatio);
			out.close();
			fileOut.close();
		} 
		catch (IOException i) {
			i.printStackTrace();
			return;
		} 
	}
	
	public boolean contains(String testName) {
		return historicalPassRatio.containsKey(testName);
	}
	
	public double getPassRatio(String testName) {
		return historicalPassRatio.get(testName);
	}

	public double getConfidence() {
		return confidence ;
	}
	
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public void putPassRatio(String testName, double passRatio) {
		historicalPassRatio.put(testName, passRatio);
		
	}

	public void recalibrate(String testName) {
		String testFullName = testClassName + "." + testName;
		if(historicalPassRatio.remove(testFullName) == null) {
			System.out.println("Does not exist: testName: " + testFullName);
		}
		
	}
	
	public void recalibrateAll() {
		historicalPassRatio.clear();
	}
	
	public void showFlakyTests() {
		System.out.println("For class \"" + testClassName + "\"the following are flaky:");
		for (String name : historicalPassRatio.keySet()) {
			if(historicalPassRatio.get(name) < 1) {
				System.out.printf("\t %s passes: %.0f%%\n", name, historicalPassRatio.get(name)*100);
			}
		}
	}
	
}
