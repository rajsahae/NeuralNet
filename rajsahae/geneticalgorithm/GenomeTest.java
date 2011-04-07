/**
 * 
 */
package rajsahae.geneticalgorithm;


import static org.junit.Assert.*;
import static rajsahae.geneticalgorithm.Genome.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rajsahae.generatesequence.Byte4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author 	Raj Sahae
 * @date	October 2010
 *
 */
public class GenomeTest {
	
	private Genome<GenomeDouble> g1;
	private Genome<GenomeDouble> g1_same;
	private Genome<GenomeDouble> g5;
	private Genome<GenomeDouble> g10;
	private Genome<GenomeDouble> g10_2;
	
	private Genome<Byte4> gb1;
	private Genome<Byte4> gb2;
	private Genome<Byte4> gb3;
	private Genome<Byte4> gb4;
	
	private GenomeDouble gd1;
	private GenomeDouble gd2;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		g1 = new Genome<GenomeDouble>(GenomeDouble.randomUnitList(1, 1), 1);
		g1_same = new Genome<GenomeDouble>(GenomeDouble.randomUnitList(1, 1), 1);
		g5 = new Genome<GenomeDouble>(GenomeDouble.randomUnitList(5, 2), 3);
		g10 = new Genome<GenomeDouble>(GenomeDouble.randomUnitList(10, 3), 0.1);
		g10_2 = new Genome<GenomeDouble>(GenomeDouble.randomUnitList(10, 4), 1);
		
		gb1 = new Genome<Byte4>(Byte4.randomUnitList(3, 1), 1);
		gb2 = new Genome<Byte4>(Byte4.randomUnitList(3, 2), 1);
		gb3 = new Genome<Byte4>(Byte4.randomUnitList(3, 3), 0.1);
		gb4 = new Genome<Byte4>(Byte4.randomUnitList(3, 4), 5);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCompareTo() {
		assertTrue(g1.compareTo(g5) == -1);
		assertTrue(g5.compareTo(g10) == 1);
		assertTrue(g1.compareTo(g10) == 1);
		assertTrue(g1.compareTo(g1) == 0);
		assertTrue(g5.compareTo(g5) == 0);
		assertTrue(g10.compareTo(g10) == 0);
		assertTrue(g10.compareTo(g5) == -1);
		assertTrue(g5.compareTo(g1) == 1);
		assertTrue(g10.compareTo(g1) == -1);
	}
	
	@Test
	public void testCollectionsSort() {
		ArrayList<Genome<GenomeDouble>> gdList;
		ArrayList<Genome<GenomeDouble>> gdCompare;
		ArrayList<Genome<Byte4>> 		gbList;
		ArrayList<Genome<Byte4>> 		gbCompare;
		
		gdList 		= new ArrayList<Genome<GenomeDouble>>(4);
		gdCompare 	= new ArrayList<Genome<GenomeDouble>>(4);
		gbList 		= new ArrayList<Genome<Byte4>>(4);
		gbCompare 	= new ArrayList<Genome<Byte4>>(4);
		
		gdList.add(g1);
		gdList.add(g5);
		gdList.add(g10);
		gdList.add(g10_2);
		
		gbList.add(gb1);
		gbList.add(gb2);
		gbList.add(gb3);
		gbList.add(gb4);
		
		gdCompare.add(g10);
		gdCompare.add(g1);
		gdCompare.add(g10_2);
		gdCompare.add(g5);
		
		gbCompare.add(gb3);
		gbCompare.add(gb1);
		gbCompare.add(gb2);
		gbCompare.add(gb4);
		
		//assertEquals(gdList, gdCompare);
		Collections.sort(gdList);
		assertEquals(gdList, gdCompare);

		//assertEquals(gbList, gbCompare);
		Collections.sort(gbList);
		assertEquals(gbList, gbCompare);
	}
	
	@Test
	public void testUnitEquals() {
		gd1 = new GenomeDouble(3.1415);
		gd2 = new GenomeDouble(3.1415);
		assertTrue(gd1.equals(gd2));
	}
	
	@Test
	public void testGenomeEquals() {
		assertTrue(g1.equals(g1_same));
	}
	
	@Test
	public void testMutate() {
		ArrayList<GenomeDouble> temp = new ArrayList<GenomeDouble>(g1.getUnits().size());
		temp.add(new GenomeDouble(1.3682174));
		Genome<GenomeDouble> g1m = new Genome<GenomeDouble>(temp, 1);
		
		g1.mutate(1, 1);

		assertEquals(g1m.getUnits().get(0).doubleValue(),
				g1.getUnits().get(0).doubleValue(),	0.0001);
	}
	
	@Test
	public void testFitness() {
		assertEquals(g1.getFitness(), 1.0, 0.0001);
		assertEquals(g5.getFitness(), 3.0, 0.0001);
		assertEquals(g10.getFitness(), 0.1, 0.0001);
		
		g1.setFitness(3.1415);
		g5.setFitness(1.0/3.1415);
		
		g1.mutate(1.0, 1);
		
		assertEquals(g1.getFitness(), 3.1415, 0.0001);
		assertEquals(g5.getFitness(), 1.0/3.1415, 0.0001);
	}

	@Test
	public void testToString() {
		String g1String = "Fitness is: " + g1.getFitness() + "\n"
		+ "Units are:\n" + g1.getUnits().toString();
		String g5String = "Fitness is: " + g5.getFitness() + "\n"
		+ "Units are:\n" + g5.getUnits().toString();
		String g10String = "Fitness is: " + g10.getFitness() + "\n"
		+ "Units are:\n" + g10.getUnits().toString();
		
		assertEquals(g1.toString(), g1String);
		assertEquals(g5.toString(), g5String);
		assertEquals(g10.toString(), g10String);
	}
	
	@Test
	public void testCrossover() {
		// Random(10).nextDouble(0.73)*size(10)*crossover(0.7) = 5.3
		// Pivot should equal 5
		int pivot = 5;
		int size = g10.getUnits().size();
		ArrayList<GenomeDouble> expectedWeights1;
		ArrayList<GenomeDouble> expectedWeights2;
		
		expectedWeights1 = new ArrayList<GenomeDouble>(g10.getUnits().size());
		expectedWeights2 = new ArrayList<GenomeDouble>(g10_2.getUnits().size());
		
		expectedWeights1.addAll(g10.getUnits().subList(0, pivot));
		expectedWeights1.addAll(g10_2.getUnits().subList(pivot, size));
		expectedWeights2.addAll(g10_2.getUnits().subList(0, pivot));
		expectedWeights2.addAll(g10.getUnits().subList(pivot, size));
		
		List<Genome<GenomeDouble>> results = Genome.crossover(g10, g10_2, 0.7, 10);
		assertEquals(expectedWeights1, results.get(0).getUnits());
		assertEquals(expectedWeights2, results.get(1).getUnits());
	}
}
