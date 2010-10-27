package rajsahae.geneticalgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import rajsahae.configuration.Parameters;

public class GeneticAlgorithm<T extends Genome.Unit> {
	
	private ArrayList<Genome<T>> population;
	private int 	populationSize;
	private double	mutationRate;
	private double	crossoverRate;
	private int		genomeSize;
	
	private double 	totalFitnessOfPopulation;
	private double 	bestFitness;
	private double 	averageFitness;
	private double 	worstFitness;
	private int 	indexOfBestGenome;
	
	private int		generationCounter;
	
	/**
	 * 
	 * @param size		Size of Genome Population
	 * @param mutRate	Mutation Rate of Population
	 * @param crossRate	Crossover Rate of Population
	 * @param weights	List of weights for initial population
	 */
	public GeneticAlgorithm(int popSize, int genomeSize, double mutRate, double crossRate, List<T> weights){
		this.populationSize 	= popSize;
		this.mutationRate 		= mutRate;
		this.crossoverRate	 	= crossRate;
		this.genomeSize 		= genomeSize;
		this.population 		= new ArrayList<Genome<T>>(this.populationSize);
		
		assert(weights.size() == (popSize * genomeSize) );
		
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
	
	// Runs the GA for one generation
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
			newPopulation.add(this.crossover(genome1, genome2));
		}
		
		// go through newPop and do mutations
		for( Genome<T> genome : newPopulation) {
			genome.mutate(this.mutationRate);
		}
		
		// replace the oldPopulation with the 
		assert(newPopulation.size() == this.populationSize);
		this.population.clear();
		this.population.addAll(newPopulation);
		
		this.generationCounter++;
		
		return this.population;
	}
	
	public List<Genome<T>> getGenomes(){
		return population;
	}
	
	private void calculateAverageFitness(){
		this.averageFitness = (this.totalFitnessOfPopulation/this.populationSize);
	}
	
	public int getLengthOfGenome() {
		return this.genomeSize;
	}
	public double getAverageFitness() {
		return this.averageFitness;
	}
	
	public double getBestFitness(){
		return this.bestFitness;
	}
	
	public double getWorstFitness() {
		return this.worstFitness;
	}
	
	public Genome<T> getBestGenome(){
		return this.population.get(this.indexOfBestGenome);
	}
	
	public int getNumberOfGenerations(){
		return this.generationCounter;
	}
	
	public double getTotalFitnessOfPopulation() {
		return this.totalFitnessOfPopulation;
	}
	
	public void organizePopulation(){
		this.calculateTotalFitnessOfPopulation();
		this.indexOfBestGenome = this.population.indexOf(Collections.max(this.population));
		this.worstFitness = Collections.min(this.population).getFitness();
		this.bestFitness = Collections.max(this.population).getFitness();
		this.calculateAverageFitness();
	}
	
	private void calculateTotalFitnessOfPopulation() {
		double sum = 0.0;
		for (Genome<T> genome : this.population){
			sum += genome.getFitness();
		}
		this.totalFitnessOfPopulation = sum;
	}

	private Genome<T> crossover(Genome<T> mom, Genome<T> dad) {
		return Genome.crossover(mom, dad, this.crossoverRate);
	}
	/*
	private void mutate(Genome<T> genome){
		genome.mutate(this.mutationRate);
	}
	*/
	private Genome<T> getGenomeByRoulette(){
		/*
		 * Algorithm uses the Roulette Wheel selection method
		 */
		
		Genome<T> result = null;
		
		/*
		 * Set a target number randomly which is greater than 0
		 * and less than the sum of the probabilities.
		 * 
		 * Starting at the beginning of the population,
		 * sum fitness until sum > target
		 * 
		 */
		double target = new Random().nextDouble()*this.totalFitnessOfPopulation;
		double trackedSum = 0.0;
		for (Genome<T> genome : this.population){
			trackedSum += genome.getFitness();
			if(trackedSum > target){
				result = genome;
				break;
			}
		}
		return result;
	}
	
	public String toString() {
		return this.getStats();
	}
	public String getStats(){
		return  (   "Number of Generations is: " + this.getNumberOfGenerations()
				+ "\nBest  fitness: " + this.getBestFitness()
				+ "\nAvrg  fitness: " + this.getAverageFitness()
				+ "\nWorst fitness: " + this.getWorstFitness()
				+ "\nTotal fitness: " + this.totalFitnessOfPopulation );
	}

	/*
	private void grabBestN(int num, int numCopies, List<Genome<T>> population){
		finish;
	}
	
	private void reset(){
		finish;
	}
	*/
	public static class GeneticAlgorithmParameters extends Parameters {
		public static int 		defaultPopulationSize 	= 100;
		public static int 		defaultGenomeLength 	= 130;
		public static double 	defaultMutationRate 	= 0.15;
		public static double	defaultCrossoverRate	= 0.7;
		
	}
	
}
