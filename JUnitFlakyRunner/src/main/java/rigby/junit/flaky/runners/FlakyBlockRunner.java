package rigby.junit.flaky.runners;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import org.apache.commons.math3.stat.inference.AlternativeHypothesis;
import org.apache.commons.math3.stat.inference.BinomialTest;

public class FlakyBlockRunner extends BlockJUnit4ClassRunner {
    
	FlakyTracker flakyTracker;
	
	public FlakyBlockRunner(Class<?> klass) throws InitializationError {
        super(klass);
   
        //System.out.println("klass = " + klass.getName());
        flakyTracker = FlakyFactory.getInstance().getFlakyTracker(klass);
    }
 
	public int sampleSizeNormal(double passRatio) {
			
		//n=(Z/d)^2*p(1-p)
		int runs = (int)Math.ceil(Math.pow((2.33/.07), 2)*passRatio*(1-passRatio));
		//System.out.printf("PassPercentage = %.2f%%, Need %d runs\n", passRatio*100, runs);
		if (runs == 0) {
			runs = 1;
		}
		return runs;
		
	}
	
    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        
    	FlakyStatement flakyStatement = new FlakyStatement();
    	String testName = test.getClass().getName() + "." + method.getName(); 
    	//System.out.println("invoking: " + testName);
    	
    	if (!flakyTracker.contains(testName)) {
    		return firstRun(method, test, testName);
    	}
    	
    	//Test is deterministic
    	if (flakyTracker.getPassRatio(testName) == 1) {
    		//System.out.println("Deterministic: " + testName);
    		return super.methodInvoker(method, test);
    	}
    	
    	//Test is flaky so we need to run it multiple times
        int passCount = 0;
        int runs = sampleSizeNormal(flakyTracker.getPassRatio(testName));
        Throwable lastThrowable = null;
        
    	for (int run = 1; run <= runs; run++) {
        	//System.out.println("invoking: " + method.getName());
        	Statement statement = super.methodInvoker(method, test);
        	try {
        		statement.evaluate();
        		passCount ++;
        	}
        	catch (Throwable e) {
        		lastThrowable = e;
        	}
        		
    		if (run >= runs) { 
    			if (binomialFlakyTrueFail(flakyTracker.getPassRatio(testName), run, passCount, 
    						flakyTracker.getConfidence())) {
	    			System.out.println("\t for test: " + testName);
	    			
	    			flakyStatement.setAssert(lastThrowable);
	    			return flakyStatement;
				}        	
    		}
        }
    	
    	return flakyStatement;
    }
    
    
  	// checks if the change is statistically significant
  	public boolean binomialFlakyTrueFail(double historicalPassRatio, int trialRuns, 
  			int numSuccesses, double confidence) {
  		
  		BinomialTest binomTest = new BinomialTest();
  		double pval = binomTest.binomialTest(trialRuns, numSuccesses, 
  				historicalPassRatio, AlternativeHypothesis.TWO_SIDED);

  		//Statistically significant if pval <= confidence = we have a problem
  		// And we used to have more passes
  		if (pval <= confidence & historicalPassRatio > numSuccesses/(double)trialRuns) {
  			System.out.printf("Failue: Expected %.2f%% passes vs Actual %.2f%% on %d runs\n", 
  	  				historicalPassRatio*100, numSuccesses/(double)trialRuns*100, trialRuns);
  			
  			System.out.printf("\t pval: %.2f <= %.2f\n", pval, confidence);
  			return true;
  		}
  		else if (pval <= confidence) {
  			System.out.printf("Improvement: Expected %.2f%% passes vs Actual %.2f%% on %d runs\n", 
  	  				historicalPassRatio*100, numSuccesses/(double)trialRuns*100, trialRuns);
  			return true;
  		}
  		
  		//not a statistically significant difference
  		return false;
  				
  		
  	}
  	
  	private FlakyStatement firstRun(FrameworkMethod method, Object test, String testName) {
    	int passCount = 0;
    	int runs = sampleSizeNormal(.5);
    	Throwable lastThrowable = null;
    	for (int run = 1; run <= runs; run++) {
        	//System.out.println("invoking: " + method.getName());
        	Statement statement = super.methodInvoker(method, test);
        	try {
        		statement.evaluate();
				passCount ++;
        	}
        	catch (Throwable e) {
        		lastThrowable = e;        		
        	}
    	}
    	double passRatio = passCount/(double)runs; 
    	
    	System.out.printf("Calcluate ratio for %s passed = %.2f%% in %d runs\n", testName, passRatio*100, runs);
    	flakyTracker.putPassRatio(testName, passRatio);
    	flakyTracker.storeFlakyResults();
		
		FlakyStatement flakyStatement = new FlakyStatement();
		flakyStatement.setAssert(lastThrowable);
		return flakyStatement;
    }
    
}