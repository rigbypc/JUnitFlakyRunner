package rigby.junit.flaky.runners;

import org.junit.runners.model.Statement;
import java.lang.Throwable;

public class FlakyStatement extends Statement {

	private Throwable throwable = null;

	public FlakyStatement() {
		// TODO Auto-generated constructor stub
	}
	
	public void setAssert(Throwable throwable) {
		this.throwable = throwable;
	}

	@Override
	public void evaluate() throws Throwable {
		if (throwable != null) {
			throw throwable;
		}
		

	}

}
