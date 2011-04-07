package rajsahae.generatesequence;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rajsahae.geneticalgorithm.GeneticAlgorithm;
import rajsahae.geneticalgorithm.Genome;
import rajsahae.geneticalgorithm.GeneticAlgorithm.*;

public class GenerateSequence {

	private static double targetNum = 38;
	private static int populationSize = 
		GeneticAlgorithmParameters.defaultPopulationSize;
	private static int genomeLength = 40;
	private static double mutationRate = 0.15;
	private static double crossoverRate = 
		GeneticAlgorithmParameters.defaultCrossoverRate;

	public static final Byte4 NUM0 		= new Byte4("0000");
	public static final Byte4 NUM1 		= new Byte4("0001");
	public static final Byte4 NUM2 		= new Byte4("0010");
	public static final Byte4 NUM3 		= new Byte4("0011");
	public static final Byte4 NUM4 		= new Byte4("0100");
	public static final Byte4 NUM5 		= new Byte4("0101");
	public static final Byte4 NUM6 		= new Byte4("0110");
	public static final Byte4 NUM7 		= new Byte4("0111");
	public static final Byte4 NUM8 		= new Byte4("1000");
	public static final Byte4 NUM9 		= new Byte4("1001");
	public static final Byte4 PLUS 		= new Byte4("1010");
	public static final Byte4 MINUS 	= new Byte4("1011");
	public static final Byte4 TIMES 	= new Byte4("1100");
	public static final Byte4 DIVIDE 	= new Byte4("1101");
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// create GeneticAlgorithm
		ArrayList<Byte4> newWeights = new ArrayList<Byte4>(genomeLength
				* populationSize);
		newWeights.addAll(Byte4.randomUnitList(genomeLength*populationSize));
		GeneticAlgorithm<Byte4> myGA = new GeneticAlgorithm<Byte4>(populationSize,
				genomeLength, mutationRate, crossoverRate, newWeights);
		long startTime = System.currentTimeMillis();
		if (args.length != 0){
			targetNum = Double.parseDouble(args[0]);
		}
		System.out.println("Generating solution for: " + targetNum);
		doGeneticAlgorithm(myGA, targetNum);
		long endTime = System.currentTimeMillis();
		double totalTime = (endTime-startTime)/1000.0;
		System.out.println("Runtime in seconds: " + totalTime);
	}
	
	/**
	 * 
	 * @param target
	 */
	public static void doGeneticAlgorithm(GeneticAlgorithm<Byte4> ga, double target) {
		
		while(true){
			// test each genome for fitness
			for(Genome<Byte4> genome : ga.getGenomes()) {
				genome.setFitness(GenerateSequence.getFitnessScore(genome, target));
			}
			ga.organizePopulation();
			
			if(ga.getBestFitness() == Double.MAX_VALUE) {
				Genome<Byte4> bestG = ga.getBestGenome();
				String trimmed = trimChromosome(getAnswerString((bestG)));
				System.out.println("A solution was found for " + targetNum + "!");
				System.out.print("The solution is: ");
				System.out.println(trimmed);
				System.out.println(ga.toString());
				System.out.print("Answer is ");
				System.out.println(interpretStringOperations(trimmed));
				System.out.println();
				return;
			}
			
			if(Math.IEEEremainder(ga.getNumberOfGenerations(), 1000) == 0){
				Genome<Byte4> bestG = ga.getBestGenome();
				String trimmed = trimChromosome(getAnswerString((bestG)));
				System.out.println(ga.toString());
				System.out.print("Best String is ");
				System.out.println(trimmed);
				System.out.print("Answer is ");
				System.out.println(interpretStringOperations(trimmed));
				System.out.println();
			}
			
			// step one generation forward
			ga.evolve();
		}
	}
	

	/**
	 * 
	 * @param genome
	 * @return
	 */
	public static String getAnswerString(Genome<Byte4> genome) {
		String answerString = "";
		for(Byte4 b : genome.getUnits()){
			answerString += getByteString(b);
		}
		return answerString;
	}

	/**
	 * 
	 * @param b
	 * @return
	 */
	public static String getByteString(Byte4 b){
		String temp = "";
		
		if(		 b.equals(NUM0)){	temp = "0"; }
		else if (b.equals(NUM1)){ 	temp = "1"; }
		else if (b.equals(NUM2)){ 	temp = "2"; }
		else if (b.equals(NUM3)){ 	temp = "3"; }
		else if (b.equals(NUM4)){ 	temp = "4"; }
		else if (b.equals(NUM5)){ 	temp = "5"; }
		else if (b.equals(NUM6)){ 	temp = "6"; }
		else if (b.equals(NUM7)){ 	temp = "7"; }
		else if (b.equals(NUM8)){ 	temp = "8"; }
		else if (b.equals(NUM9)){ 	temp = "9"; }
		else if (b.equals(PLUS)){ 	temp = "+"; }
		else if (b.equals(MINUS)){ 	temp = "-"; }
		else if (b.equals(TIMES)){ 	temp = "*"; }
		else if (b.equals(DIVIDE)){ temp = "/"; }
		else { 						temp = "0"; }
		
		return temp;
	}

	/**
	 * 
	 * @param genome
	 * @param target
	 * @return
	 */
	public static double getFitnessScore(Genome<Byte4> genome, double target) {
		double answer;
		double score;
		// feed genome to get answer, trim it of nonsense patterns,
		// and interpret the operations
		answer = interpretStringOperations(trimChromosome(
				getAnswerString((Genome<Byte4>) genome)));
		if (answer == target){
			score =  Double.MAX_VALUE;
		} else {
			score = Math.abs( 1 /( target - answer ));
		}
		return score;
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static double interpretStringOperations(String s){
		double runningTotal = 0.0;
		
		for (int i=0; i<s.length(); i++){
			if(i==0){
				try{
					runningTotal = Double.parseDouble(s.substring(i, i+1));
				}
				catch(Exception e){
					// The parsing probably failed.  
					// Set runningTotal to the second character
					runningTotal = Double.parseDouble(s.substring(i+1, i+2));
				}
			} else {
				switch(s.charAt(i)){
				case '+': 
					runningTotal += (Double.parseDouble(s.substring(i+1, i+2)));
					break; 
				case '-': 
					runningTotal -= (Double.parseDouble(s.substring(i+1, i+2))); 
					break;
				case '*': 
					runningTotal *= (Double.parseDouble(s.substring(i+1, i+2))); 
					break;
				case '/': 
					runningTotal /= Double.parseDouble(s.substring(i+1, i+2));
					if(Double.isInfinite(runningTotal) || Double.isNaN(runningTotal)){
						runningTotal = 0.0;
					} 
				default: break;
				}
			}
		}
		/*
		 if(Double.isNaN(runningTotal)){
		 	System.err.println("Breakpoint here");
			//the problem is here;
			runningTotal = 0.0;
		}
		*/
		return runningTotal;
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String trimChromosome(String str){
		//remove any nonsense bits and return the trimmed string
		Pattern frontOps = Pattern.compile("^([\\+\\-\\*\\/])+");
		Pattern backOps = Pattern.compile("([\\+\\-\\*\\/])+$");
		Pattern frontPattern = Pattern.compile("\\d[\\+\\-\\*\\/]");
		Pattern backPattern = Pattern.compile("[\\+\\-\\*\\/]\\d");
		Pattern digit = Pattern.compile("\\d");
		Pattern operand = Pattern.compile("[\\+\\-\\*\\/]");
		
		String s = "", trimmed = "";
		int start = 0, end = str.length();
		
		//Trim leading and trailing operands
		Matcher mPtr1, mPtr2, mPtr3, mPtr4, mPtr5, mPtr6;
		mPtr1 = frontOps.matcher(str);
		mPtr2 = backOps.matcher(str);
		if(mPtr1.find()){
			//System.out.println("Trimming leading operands");
			start = mPtr1.end() > 0 ? mPtr1.end() : 0;
		}
		if(mPtr2.find()){
			//System.out.println("Trimming trailing operands");
			end = mPtr2.start() > 0 ? mPtr2.start() : str.length();
		}
		
		s = str.substring(start, end);
		
		//System.out.println("After trim, string is: " + s);

		// Need to handle boundary cases of no operands, and no digits
		mPtr3 = digit.matcher(s);
		mPtr4 = operand.matcher(s);
		mPtr5 = frontPattern.matcher(s);
		mPtr6 = backPattern.matcher(s);
		
		
		if (!mPtr3.find(0)){
			//No digits in the string
			trimmed = "0";
			//System.out.println("No digits found in string: " + s);
		} else if (!mPtr4.find(0)){
			//No operands in the string
			trimmed = s.substring(0, 1);
			//System.out.println("No operands found in string: " + s);
		} else { // run normal algorithm
			//Match intermediate groups
			while(mPtr5.find()){
				trimmed += mPtr5.group();
			}
			
			//Add the last digit
			while(mPtr6.find()){
				start = mPtr6.start();
				end = mPtr6.end();
			}
			trimmed += s.substring(start+1, end);
		}
		
		return trimmed;
	}

}
