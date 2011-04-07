package rajsahae.neuralnet;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rajsahae.configuration.Parameters;
import rajsahae.geneticalgorithm.GeneticAlgorithm;
import rajsahae.geneticalgorithm.Genome;
import rajsahae.geneticalgorithm.Genome.GenomeDouble;
import static rajsahae.neuralnet.TrainNeuralNet.TNNParms.*;

public class TrainNeuralNet extends Parameters {
	
	private static GeneticAlgorithm<GenomeDouble> myGA;
	private static NeuralNet myNN;
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		double bestFitness = 0.0;
		double bestOfGen = 0.0;
		int numWeights = 0;
		
		for (int i=0; i<=hiddens.length; i++) {
			if(i == 0) {
				numWeights += hiddens[i]*numInputs;
			} else if(i == hiddens.length){
				numWeights += hiddens[i-1]*numOutputs;
			} else {
				numWeights += hiddens[i]*hiddens[i-1];
			}
		}
		
		genomeLength = numWeights;
		exportParametersToFile(TNNParms.class, parmFileName);
		initializeExpectations();
		
		// create genetic algorithm
		ArrayList<GenomeDouble> newWeights;
		newWeights = new ArrayList<GenomeDouble>(popSize*genomeLength);
		newWeights.addAll(GenomeDouble.randomUnitList(popSize*genomeLength));
		myGA = new GeneticAlgorithm<GenomeDouble>(popSize, genomeLength,
				mutationRate, crossoverRate, newWeights);
		
		// create neural net
		myNN = new NeuralNet(numInputs, numOutputs, numHiddenLayers, hiddens);
		
		while(true) {
			// for each genome, run neural net and set fitnesses
			//int counter = 0;
			if(myGA.getNumberOfGenerations() % 1000 == 0) {
				System.out.print("Running generation ");
				System.out.println(myGA.getNumberOfGenerations());
				System.out.print("Best fitness in the last 1000 generations is: ");
				System.out.println(bestOfGen);
				bestOfGen = 0.0;
			}
			for(Genome<GenomeDouble> genome : myGA.getGenomes()) {
				//System.out.println("On Genome" + counter++);
				ArrayList<Double> netWeights = 
					new ArrayList<Double>(genome.getUnits().size());
				for(GenomeDouble unit : genome.getUnits()) {
					netWeights.add(unit.doubleValue());
				}
				myNN.setWeights(netWeights);
				double newFitness = determineFitnessForT3();
				genome.setFitness(newFitness);
				
				if (newFitness > bestOfGen) {
				 	 bestOfGen = newFitness;
					 //System.out.println("Best fitness in this generation so far is: " + bestFitness);
				}
				if (newFitness > bestFitness) {
					bestFitness = newFitness;
					System.out.print("New best fitness found!: ");
					System.out.println(bestFitness);
					exportGenomeToFile(genome, "Fitness-" + bestFitness);
				}
				if(newFitness == MAX_FITNESS) {
					winnerFound(genome);
					return;
				}
			}
			//System.out.println("Best fitness in this generation is: " + bestFitness);
			// run evolution for genetic algorithm
			myGA.evolve();
		}
	}
	
	private static void winnerFound(Genome<GenomeDouble> genome) throws IOException {
		System.out.println("We have a winner!");
		System.out.print("Number of generations evolved: ");
		System.out.println(myGA.getNumberOfGenerations());
		System.out.println("Outputting weights to file: "
				+ weightsFileName);
		exportGenomeToFile(genome, weightsFileName);
	}
	
	private static void exportGenomeToFile(Genome<GenomeDouble> genome, 
			String filename) throws IOException {
		PrintWriter output = new PrintWriter(new FileWriter(filename));
		for(GenomeDouble unit : genome.getUnits()) {
			output.println(unit.doubleValue());
		}
		safeClose(output);
	}

	private static double determineFitnessForT3() {
		
		/*
		 *  Run neural net for each input
		 *  Sum up the correct responses
		 *  Return fitness as sum of correct responses
		 */
		double sum = 0.0;
		List<Double> output = new ArrayList<Double>(myNN.getNumberOfOutputs());
		for(int i=0; i<netInputs.size(); i++) {
			output = myNN.update(netInputs.get(i));
			
			if( (Math.round(output.get(0)) == expectedOutputs.get(i).get(0))
					&& (Math.round(output.get(1)) == expectedOutputs.get(i).get(1)) ) {
				sum++;
			}
		}
		
		//System.out.println("Fitness is: " + sum);
		
		return sum;
	}
	
	private static void initializeExpectations() {
		netInputs = new ArrayList<List<Double>>(MAX_FITNESS);
		netInputs.add(Arrays.asList(input1));
		netInputs.add(Arrays.asList(input2));
		netInputs.add(Arrays.asList(input3));
		netInputs.add(Arrays.asList(input4));
		netInputs.add(Arrays.asList(input5));
		netInputs.add(Arrays.asList(input6));
		netInputs.add(Arrays.asList(input7));
		netInputs.add(Arrays.asList(input8));
		netInputs.add(Arrays.asList(input9));
		netInputs.add(Arrays.asList(input10));
		netInputs.add(Arrays.asList(input11));
		
		expectedOutputs = new ArrayList<List<Integer>>(MAX_FITNESS);
		expectedOutputs.add(Arrays.asList(output1));
		expectedOutputs.add(Arrays.asList(output2));
		expectedOutputs.add(Arrays.asList(output3));
		expectedOutputs.add(Arrays.asList(output4));
		expectedOutputs.add(Arrays.asList(output5));
		expectedOutputs.add(Arrays.asList(output6));
		expectedOutputs.add(Arrays.asList(output7));
		expectedOutputs.add(Arrays.asList(output8));
		expectedOutputs.add(Arrays.asList(output9));
		expectedOutputs.add(Arrays.asList(output10));
		expectedOutputs.add(Arrays.asList(output11));
	}
	
	private static void safeClose(Closeable stream) {
		try {
			if (stream != null) {
				stream.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
	}
	
	public static class TNNParms extends Parameters {

		// Neural Net Parameters
		public static int numInputs = 10;
		public static int numOutputs = 2;
		public static int numHiddenLayers = 1;
		public static int[] hiddens = {5};
		
		// Genetic Algorithm Parameters
		public static int popSize = 100;
		//genomeLength = (numInputs + numOutputs)*hiddens
		public static int genomeLength = 0;
		public static double mutationRate = 0.15;
		public static double crossoverRate = 0.7;
		
		// Application Specific Parameters
		public static String parmFileName = "TrainParms.ini";
		public static String weightsFileName = "NeuralNetWeights.ini";
		public static ArrayList<List<Double>> netInputs;
		public static ArrayList<List<Integer>> expectedOutputs;
		public static final int MAX_FITNESS = 11;
		
		// T3 Parameters
		public static Double[] input1 = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0};
		public static Double[] input2 = {1.0,0.0,-1.0,-1.0,-1.0,0.0,1.0,0.0,1.0,1.0};
		public static Double[] input3 = {1.0,0.0,-1.0,0.0,-1.0,0.0,1.0,-1.0,1.0,1.0};
		public static Double[] input4 = {1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,-1.0};
		public static Double[] input5 = {1.0,0.0,0.0,0.0,-1.0,0.0,0.0,0.0,0.0,1.0};
		public static Double[] input6 = {1.0,0.0,0.0,0.0,-1.0,0.0,0.0,0.0,1.0,-1.0};
		public static Double[] input7 = {1.0,0.0,0.0,0.0,-1.0,0.0,0.0,-1.0,1.0,1.0};
		public static Double[] input8 = {1.0,1.0,0.0,0.0,-1.0,0.0,0.0,-1.0,1.0,-1.0};
		public static Double[] input9 = {1.0,1.0,-1.0,0.0,-1.0,0.0,0.0,-1.0,1.0,1.0};
		public static Double[] input10 = {1.0,1.0,-1.0,0.0,-1.0,0.0,1.0,-1.0,1.0,-1.0};
		public static Double[] input11 = {1.0,1.0,-1.0,-1.0,-1.0,0.0,1.0,-1.0,1.0,1.0};
		/*
		public static Double[] input12 = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0};
		public static Double[] input13 = {1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,-1.0};
		public static Double[] input14 = 
		public static Double[] input15 = 
		public static Double[] input16 = 
		public static Double[] input17 = 
		public static Double[] input18 = 
		public static Double[] input19 = 
		*/
		
		public static Integer[] output1 = {1, 1};
		public static Integer[] output2 = {2, 1};
		public static Integer[] output3 = {1, 0};
		public static Integer[] output4 = {1, 1};
		public static Integer[] output5 = {2, 2};
		public static Integer[] output6 = {2, 1};
		public static Integer[] output7 = {0, 1};
		public static Integer[] output8 = {0, 2};
		public static Integer[] output9 = {2, 0};
		public static Integer[] output10 = {1, 0};
		public static Integer[] output11 = {1, 2};
		/*
		public static Integer[] output12 = {0, 0};
		public static Integer[] output13 = 
		public static Integer[] output14 = 
		public static Integer[] output15 = 
		public static Integer[] output16 = 
		public static Integer[] output17 = 
		public static Integer[] output18 = 
		public static Integer[] output19 = 
		*/
	}

}
