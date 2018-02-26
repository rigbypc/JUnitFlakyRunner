package flaky;

import java.util.Random;

public class RandomNormal {
	
	public RandomNormal() {
	}
	
	public boolean getRandom(double passProbability) {
		Random rand = new Random();
		int num = rand.nextInt(100) + 1;
		
		if (num <= 100*passProbability) {
			return true;
		}
		
		return false;
	}
}
