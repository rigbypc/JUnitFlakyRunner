package rigby.junit.flaky.runners;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.lang.annotation.Annotation;


public class FlakyThreeRunner extends BlockJUnit4ClassRunner {
   
	private int runs = 3;
	
    public FlakyThreeRunner(Class<?> klass) throws InitializationError {
		super(klass);
		// TODO Auto-generated constructor stub
	}

	@Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        
    	FlakyStatement flakyStatement = new FlakyStatement();
    	String testName = test.getClass().getName() + "." + method.getName(); 
    	//System.out.println("invoking: " + testName);
    	
    	boolean flaky = false;
    	Annotation[] annotations = method.getAnnotations();
    	for (Annotation annotation : annotations) {
			//System.out.println("Annotation = " + annotation.toString());
    		if (annotation.toString().equals("@rigby.junit.flaky.runners.Flaky()")) {
				//System.out.println("Flaky: " + testName);
				flaky = true;
			}
		}
    	
    	if (!flaky) {
    		return super.methodInvoker(method, test);
    	}
    	
    	Throwable throwable = null;
    	int numPass = 0;
    	for (int run = 1; run <= runs; run++) {
        	//System.out.println("invoking: " + method.getName());
        	Statement statement = super.methodInvoker(method, test);
        	try {
        		statement.evaluate();
        		numPass ++;
        	}
        	catch (Throwable e) {
        		throwable = e;
        	}
        }
    	
    	int fails = runs - numPass;
    	if(fails > 0) {
    		System.out.println(testName + " failed " + fails + " times in a row");
    	}
    	
    	if(numPass == 0) {
    		flakyStatement.setAssert(throwable);
    	}
    	
    	return flakyStatement;
    }
       
}