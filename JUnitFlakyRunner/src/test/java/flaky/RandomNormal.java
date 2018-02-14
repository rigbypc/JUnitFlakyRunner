package flaky;

import java.util.Random;

public class RandomNormal {

	double passProbability = 1;
	
	public RandomNormal(double passProbability) {
		this.passProbability = passProbability;
	}
	
	public boolean get() {
		Random rand = new Random();
		int num = rand.nextInt(1000) + 1;
		
		if (num <= 1000*passProbability) {
			return true;
		}
		
		return false;
	}
}
