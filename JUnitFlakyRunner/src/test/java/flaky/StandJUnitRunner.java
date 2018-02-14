package flaky;

import static org.junit.Assert.*;
import org.junit.Test;

public class StandJUnitRunner {

	@Test
	public void testPerfect() {
		//originally 1
		RandomNormal randomNormal = new RandomNormal(1);
		assertTrue(randomNormal.get());
	}
	
	@Test
	public void testGetWorse() {
		//originally .9
		RandomNormal randomNormal = new RandomNormal(.90);
		assertTrue(randomNormal.get());
	}
	
	@Test
	public void testGetBetter() {
		//originally .75
		RandomNormal randomNormal = new RandomNormal(.75);
		assertTrue(randomNormal.get());
	}
	
	@Test
	public void testTrueRandom() {
		//originally .5
		RandomNormal randomNormal = new RandomNormal(.5);
		assertTrue(randomNormal.get());
	}
	
	@Test
	public void testMostlyFail() {
		//originally .1
		RandomNormal randomNormal = new RandomNormal(.1);
		assertTrue(randomNormal.get());
	}
	
	@Test
	public void testAllFails() {
		assertTrue(false);
	}
	
}
