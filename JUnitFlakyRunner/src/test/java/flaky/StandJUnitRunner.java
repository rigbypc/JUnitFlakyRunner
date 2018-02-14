package flaky;

import static org.junit.Assert.*;
import org.junit.Test;

public class StandJUnitRunner {

	@Test
	public void testPerfect() {
		//originally 1
		Threader fc = new Threader(1);
		assertTrue(fc.threadProcessed());
	}
	
	@Test
	public void testGetWorse() {
		//originally .9
		Threader fc = new Threader(.90);
		assertTrue(fc.threadProcessed());
	}
	
	@Test
	public void testGetBetter() {
		//originally .75
		Threader fc = new Threader(.75);
		assertTrue(fc.threadProcessed());
	}
	
	@Test
	public void testTrueRandom() {
		//originally .5
		Threader fc = new Threader(.5);
		assertTrue(fc.threadProcessed());
	}
	
	@Test
	public void testMostlyFail() {
		//originally .1
		Threader fc = new Threader(.1);
		assertTrue(fc.threadProcessed());
	}
	
	@Test
	public void testAllFails() {
		assertTrue(false);
	}
	
}
