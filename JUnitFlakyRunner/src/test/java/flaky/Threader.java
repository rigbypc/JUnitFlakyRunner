package flaky;

import java.util.Random;

public class Threader {

	double threshold = 1;
	
	public Threader(double threshold) {
		this.threshold = threshold;
	}
	
	public boolean threadProcessed() {
		Random rand = new Random();
		int num = rand.nextInt(1000) + 1;
		
		if (num <= 1000*threshold) {
			return true;
		}
		
		return false;
	}
}
