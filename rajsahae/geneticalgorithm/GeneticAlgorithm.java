package rajsahae.geneticalgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import rajsahae.configuration.Parameters;

/**
 * The GeneticAlgorithm class encapsulates the method of training chromosomes
 * or genomes by using the Genetic Algorithm.  The class contains a population
 * of genomes that themselves contain a list of some sort of gene.  The gene
 * MUST extend Genome.Unit.  There is a default unit that you can use called
 * Genome.Unit.  When you call the evolve method, the algorithm will select
 * Genomes at random using the Roulette method, and cross them depending
 * on the crossover rate to create child Genomes, which it will then 
 * use to populate the new population with.  Children will also randomly
 * experience mutation, which is dependant on the Unit.  A mutation method
 * must be defined for the Genome Unit.
 * 
 * @author Raj Sahae
 *
 * @param <T> class of Unit for the Genome, which must extend Genome.Unit,
 * which itself extends the abstract class Number.
 */
public class GeneticAlgorithm<T extends Genome.Unit> {
	
	private static Random randNum = new Random();
	
	private ArrayList<Genome<T>> population = new ArrayList<Genome<T>>();
	
	private int 	populationSize;
	private double	mutationRate;
	private double	crossoverRate;
	private int		genomeSize;
	
	private double 	totalFitness;
	private double 	averageFitness;
	
	private int		generationCounter;
	
	/**
	 * The constructor for the GeneticAlgorithm constructs a Genetic Algorithm
	 * with Genome population size (size).
	 * 
	 * @param popSize		Integer Size of genome population
	 * @param genomeSize	Integer Length of Genome in units
	 * @param mutRate		Double Mutation rate for Genomes, 
	 * between 0.0 and 1.0
	 * @param crossRate		Double Crossover rate for Genomes, 
	 * between 0.0 and 1.0
	 * @param weights		List<T> of weights.  The constructor will assign
	 * the weights to all the Genomes in order, so g1.units will be assigned
	 * from the first subset of weights, and then g2.units will be assigned
	 * from the second subset of weights, etc, where the subset of weights
	 * is of length "genomeSize" and the total length of weights is
	 * popSize*genomeSize 
	 */
	public GeneticAlgorithm(int popSize, int genomeSize, double mutRate,
			double crossRate, List<T> weights){
		this.populationSize 	= popSize;
		this.mutationRate 		= mutRate;
		this.crossoverRate	 	= crossRate;
		this.genomeSize 		= genomeSize;
		this.population 		= new ArrayList<Genome<T>>(this.populationSize);
		
		assert(weights.size() == (popSize * genomeSize) );
		assert(popSize % 2 == 0);
		
		int fromIndex = 0;
		int toIndex = 0;
		
		while(this.population.size() < this.populationSize) {
			fromIndex = toIndex;
			toIndex += this.genomeSize;
			this.population.add(new Genome<T>(weights.subList(fromIndex, toIndex),
					Genome.GenomeParameters.defaultFitness));
		}
		assert(this.population.size() == this.populationSize);
		
	}

	/**
	 * Performs a single evolution of Genetic Algorithm on this set of
	 * Genomes.  This is done by selecting genomes from the population
	 * by the roulette method and then performing the crossover
	 * technique with those two Genomes.
	 * The entire set of Genomes is randomly mutated and then the old
	 * population is replaced by the new one.  The new population is also
	 * returned when the method ends
	 * 
	 * @return List of new Genomes of type <T>
	 */
	public List<Genome<T>> evolve(){
		assert(this.population.size() == this.populationSize);
		
		// create blank population
		ArrayList<Genome<T>> newPopulation = 
			new ArrayList<Genome<T>>(this.populationSize);
		
		// while new pop.size() < currentPop.size()
		while (newPopulation.size() < this.populationSize) {
			// select 2 genomes from current pop by roulette
			Genome<T> genome1 = this.getGenomeByRoulette();
			Genome<T> genome2 = this.getGenomeByRoulette();
			// crossover genomes to get a child
			// add child to new population
			newPopulation.addAll(this.crossover(genome1, genome2));
		}
		
		// go through newPop and do mutations
		for( Genome<T> genome : newPopulation) {
			genome.mutate(this.mutationRate);
		}
		
		// replace the oldPopulation with the newPopulation
		assert(newPopulation.size() == this.populationSize);
		
		this.population = newPopulation;
		this.generationCounter++;
		
		return this.population;
	}
	
	/**
	 * Get the List of Genomes in this Genetic Algorithm
	 * @return	List of Genomes of type <T>
	 */
	public List<Genome<T>> getGenomes(){
		return population;
	}
	
	private void calculateAverageFitness(){
		this.calculateTotalFitness();
		this.averageFitness = (this.totalFitness / this.populationSize);
	}
	
	/**
	 * Get the length of a Genome
	 * @return The integer length of a genome in this algorithm
	 */
	public int getLengthOfGenome() {
		return this.genomeSize;
	}
	
	/**
	 * Returns the average fitness of the population of Genomes
	 * @return
	 */
	public double getAverageFitness() {
		this.calculateAverageFitness();
		return this.averageFitness;
	}
	
	/**
	 * Returns the Fitness value of the 
	 * @return
	 */
	public double getBestFitness(){
		return Collections.max(this.population).getFitness();
	}
	
	/**
	 * 
	 * @return
	 */
	public double getWorstFitness() {
		return Collections.min(this.population).getFitness();
	}
	
	/**
	 * 
	 * @return
	 */
	public Genome<T> getBestGenome(){
		return Collections.max(this.population);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumberOfGenerations(){
		return this.generationCounter;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getTotalFitnessOfPopulation() {
		this.calculateTotalFitness();
		return this.totalFitness;
	}
	
	/**
	 * 
	 */
	public void organizePopulation(){
		Collections.sort(this.population);
	}
	
	/**
	 * 
	 */
	private void calculateTotalFitness() {
		double sum = 0.0;
		for (Genome<T> genome : this.population){
			sum += genome.getFitness();
		}
		this.totalFitness = sum;
	}

	/**
	 * 
	 * @param mom
	 * @param dad
	 * @return
	 */
	private List<Genome<T>> crossover(Genome<T> mom, Genome<T> dad) {
		return Genome.crossover(mom, dad, this.crossoverRate);
	}

	/**
	 * 
	 * @param seed
	 * @return
	 */
	protected Genome<T> getGenomeByRoulette(long seed) {
		GeneticAlgorithm.randNum.setSeed(seed);
		return getGenomeByRoulette();
	}
	
	/**
	 * 
	 * @return
	 */
	protected Genome<T> getGenomeByRoulette(){
		/*
		 * Algorithm uses the Roulette Wheel selection method
		 * 
		 * Set a target number randomly which is greater than 0
		 * and less than the sum of the probabilities.
		 * 
		 * Starting at the beginning of the population,
		 * sum fitness until sum > target
		 * 
		 */

		Genome<T> result = null;
		double target = GeneticAlgorithm.randNum.nextDouble()
			* this.totalFitness;
		double trackedSum = 0.0;
		for (Genome<T> genome : this.population){
			result = genome;
			trackedSum += genome.getFitness();
			if(trackedSum >= target){
				break;
			}
		}
		return result;
	}
	
	/**
	 * 
	 */
	public String toString() {
		return this.getStats();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getStats(){
		return  (   "Number of Generations is: " + this.getNumberOfGenerations()
				+ "\nBest  fitness: " + this.getBestFitness()
				+ "\nAvrg  fitness: " + this.getAverageFitness()
				+ "\nWorst fitness: " + this.getWorstFitness()
				+ "\nTotal fitness: " + this.totalFitness );
	}
	
	/**
	 * 
	 * @author Raj Sahae
	 *
	 */
	public static class GeneticAlgorithmParameters extends Parameters {
		public static int 		defaultPopulationSize 	= 100;
		public static int 		defaultGenomeLength 	= 130;
		public static double 	defaultMutationRate 	= 0.15;
		public static double	defaultCrossoverRate	= 0.7;
		
	}
	
}
