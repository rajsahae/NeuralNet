/**
 * 
 */
package rajsahae.geneticalgorithm;

import static org.junit.Assert.*;
import java.util.Random;
import org.junit.*;

import rajsahae.geneticalgorithm.Genome.GenomeDouble;

/**
 * @author rsahae
 *
 */
public class GenomeDoubleTest {

	GenomeDouble gd0 = new GenomeDouble(0.0);
	GenomeDouble gd1 = new GenomeDouble(1.0);
	GenomeDouble gd5 = new GenomeDouble(5.0);
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testDoubleValue(){
		assertTrue(gd0.doubleValue() == 0.0);
		assertTrue(gd1.doubleValue() == 1.0);
		assertTrue(gd5.doubleValue() == 5.0);
	}
	
	@Test
	public void testFloatValue(){
		assertTrue(gd0.floatValue() == 0.0);
		assertTrue(gd1.floatValue() == 1.0);
		assertTrue(gd5.floatValue() == 5.0);
	}
	
	@Test
	public void testIntValue(){
		assertTrue(gd0.intValue() == 0);
		assertTrue(gd1.intValue() == 1);
		assertTrue(gd5.intValue() == 5);
	}
	
	@Test
	public void testLongValue(){
		assertTrue(gd0.longValue() == 0);
		assertTrue(gd1.longValue() == 1);
		assertTrue(gd5.longValue() == 5);
	}
	
	@Test
	public void testMutate(){
		gd0.mutate();
		gd1.mutate();
		gd5.mutate();
		assertTrue(gd0.doubleValue() == new Random(1).nextDouble());
		assertTrue(gd1.doubleValue() == 1.0);
		assertTrue(gd5.doubleValue() == 0.2);
	}
	
	@Test
	public void testEquals(){
		assertTrue(gd0.equals(new GenomeDouble(0.0)));
		assertTrue(gd1.equals(new GenomeDouble(1.0)));
		assertTrue(gd5.equals(new GenomeDouble(5.0)));
	}

}
