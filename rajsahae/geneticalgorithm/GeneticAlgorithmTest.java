/**
 * 
 */
package rajsahae.geneticalgorithm;


import static org.junit.Assert.*;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rajsahae.geneticalgorithm.Genome.GenomeDouble;


/**
 * @author Raj Sahae
 *
 */
public class GeneticAlgorithmTest {

	private int popSize = 5;
	private int geneSize = 10;
	private int totalSize = popSize * geneSize;
	private int seed = 1;
	private double mutRate = 0.0;
	private double crossRate = 0.5;
	
	private GeneticAlgorithm<GenomeDouble> ga;
	private Random randGen;
	private ArrayList<Double> randNums;
	private ArrayList<Double> randFitnesses;
	private List<Genome<GenomeDouble>> genomeControl;
	private List<GenomeDouble> gdList;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		// Create the Genetic Algorithm with seed
		gdList = GenomeDouble.randomUnitList(totalSize, seed);
		ga = new GeneticAlgorithm<GenomeDouble>(popSize, geneSize, 
				mutRate, crossRate, gdList);
		
		// Now create our control list to compare it to
		randGen = new Random(seed);
		randNums = new ArrayList<Double>(totalSize);
		randFitnesses = new ArrayList<Double>(popSize);
		
		while(randNums.size() < totalSize) {
			randNums.add(randGen.nextDouble());
		}
		while(randFitnesses.size() < popSize) {
			randFitnesses.add(randGen.nextDouble());
		}
		
		genomeControl = new ArrayList<Genome<GenomeDouble>>(popSize);
		
		for (int i=0; i<popSize; i++) {
			ArrayList<GenomeDouble> temp = new ArrayList<GenomeDouble>(geneSize);
			for (int j=0; j<geneSize; j++) {
				temp.add(new GenomeDouble(randNums.get(i*geneSize + j)));
			}
			//System.out.println("Setting fitness of " + i + " to " + randFitnesses.get(i));
			double tempFitness = randFitnesses.get(i).doubleValue();
			ga.getGenomes().get(i).setFitness(tempFitness);
			genomeControl.add(new Genome<GenomeDouble>(temp, tempFitness));
		}	
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	private boolean assertGenomeListEquals(List<Genome<GenomeDouble>> g1, List<Genome<GenomeDouble>> g2) {
		boolean value = true;
		if(g1.size() != g2.size()) {
			value = false;
		} else {
			for(int i=0; i<g1.size(); i++) {
				assertTrue(g1.get(i).equals(g2.get(i)));
			}
		}
		return value;
	}
	
	@Test
	public void testEvolve() {
		fail();
	}
	
	@Test
	public void testGetGenomes() {
		assertGenomeListEquals(genomeControl, ga.getGenomes());
	}
	
	@Test
	public void testGetLengthOfGenomes() {
		assertEquals(geneSize, ga.getLengthOfGenome());
	}
	
	@Test
	public void testGetAverageFitness() {
		double sum = 0.0;
		for(double fitness : randFitnesses) {
			sum += fitness;
		}
		sum /= randFitnesses.size();
		
		assertEquals(sum, ga.getAverageFitness(), 0.0001);
	}
	
	@Test
	public void testGetBestFitness() {
		assertEquals(Collections.max(randFitnesses), ga.getBestFitness(), 0.0001);
	}
	
	@Test
	public void testGetWorstFitness() {
		assertEquals(Collections.min(randFitnesses), ga.getWorstFitness(), 0.0001);
	}
	
	@Test
	public void testGetBestGenome() {
		assertTrue(Collections.max(genomeControl).equals(ga.getBestGenome()));
	}
	
	@Test
	public void testGetNumberOfGenerations() {
		assertSame(0, ga.getNumberOfGenerations());
		for(int j=0; j<5; j++) {
			ga.evolve();
			for (int i=0; i<popSize; i++) {
				double tempFitness = randFitnesses.get(i).doubleValue();
				ga.getGenomes().get(i).setFitness(tempFitness);
			}	
		}
		assertSame(5, ga.getNumberOfGenerations());
	}
	
	@Test
	public void testGetTotalFitnessOfPopulation() {
		double sum = 0.0;
		for(double fitness : randFitnesses) {
			sum += fitness;
		}
		assertEquals(sum, ga.getTotalFitnessOfPopulation(), 0.0001);
	}
	
	@Test
	public void testOrganizePopulation() {
		assertGenomeListEquals(genomeControl, ga.getGenomes());
		ga.organizePopulation();
		Collections.sort(genomeControl);
		assertGenomeListEquals(genomeControl, ga.getGenomes());
	}
	
	@Test
	public void testCrossover() {
		fail();
	}
	
	@Test
	public void testGetGenomeByRoulette() {
		fail();
	}
	
	@Test
	public void testToString() {
		fail();
	}
	
	@Test
	public void testGetStats() {
		fail();
	}
}