package rajsahae.geneticalgorithm;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;


import rajsahae.configuration.Parameters;

public class Genome<T extends Genome.Unit> implements Comparable<Genome<T>> {
	private ArrayList<T> units;
	private Double fitness;
	
	public Genome(List<T> units, double fitness) {
		this.units = new ArrayList<T>(units.size());
		this.units.addAll(units);
		this.fitness = fitness;
	}
	
	public int compareTo(Genome<T> g) {
		//System.err.println("Comparing " + this.fitness + " to " + g.fitness);
		return this.fitness.compareTo(g.fitness);
	}
	
	public static class GenomeParameters extends Parameters {
		public static double defaultFitness = 0.0;
	}
	
	public static abstract class Unit extends Number implements GeneticUnit {
		private static final long serialVersionUID = 7786152013569807331L;
		
		public boolean equals(Unit u) {
			return this.doubleValue() == u.doubleValue();
		}
		
	}
	
	public interface GeneticUnit {
		public void mutate();
	}
	
	public void mutate(double mutationRate) {
		assert(mutationRate <= 1.0);
		
		Random randGen = new Random();
		for(T unit : this.units){
			if(randGen.nextDouble() <= mutationRate) {
				unit.mutate();
			}
		}
	}
	
	public List<T> getUnits(){
		return this.units;
	}
	
	public double getFitness() {
		return this.fitness;
	}
	
	public void setFitness(double value){
		this.fitness = Double.valueOf(value);
	}
	
	public String toString() {
		String string = "";
		string += "Fitness = " + this.fitness + "\n";
		for(T unit : this.units) {
			string += unit.toString() + ", ";
		}
		return string;
	}
	
	public static <U extends Genome.Unit> Genome<U> crossover(Genome<U> parent1, 
			Genome<U> parent2, double crossoverRate) {
		
		assert(parent1 != null && parent2 != null);
		assert(parent1.units.size() == parent2.units.size());
		
		int listSize = parent1.units.size();
		int pivot = (int)Math.round(new Random().nextDouble()
				* crossoverRate * listSize);
		
		ArrayList<U> newWeights = new ArrayList<U>(listSize);
		newWeights.addAll(parent1.units.subList(0, pivot));
		newWeights.addAll(parent2.units.subList(pivot, listSize));
		
		assert(newWeights.size() == listSize);
		
		return new Genome<U>(newWeights, GenomeParameters.defaultFitness);
	}
	
	public static class GenomeDouble extends Genome.Unit {

		private static final long serialVersionUID = 4483662798023642128L;
		private Double value;
		
		public GenomeDouble(double newValue){
			this.value = newValue;
		}
		
		public void mutate() {
			if (this.value == 0.0) {
				this.value = new Random(1).nextDouble();
			} else {
				this.value = (1/this.value);
			}
		}

		public double doubleValue() {
			return this.value;
		}

		public float floatValue() {
			return Float.parseFloat(Double.toString(this.value));
		}

		public int intValue() {
			return (int)(Math.round(this.value));
		}

		public long longValue() {
			return Math.round(this.value);
		}
	}
}
