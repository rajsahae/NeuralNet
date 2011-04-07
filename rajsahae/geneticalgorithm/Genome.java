package rajsahae.geneticalgorithm;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;


import rajsahae.configuration.Parameters;

public class Genome<T extends Genome.Unit> implements Comparable<Genome<T>> {
	private static Random randNum = new Random();
	private ArrayList<T> units;
	private Double fitness;
	
	public Genome(List<T> units, double fitness) {
		this.units = new ArrayList<T>(units.size());
		this.units.addAll(units);
		this.fitness = fitness;
	}
	
	public String toString() {
		return "Fitness is: " + this.fitness + "\n"
		+ "Units are:\n" + this.units.toString();
	}
	
	public boolean equals(Genome<T> otherGenome) {
		boolean value = true;
		if(this.units.size() != otherGenome.units.size()) {
			value = false;
		} else {
			for(int i=0; i<this.units.size(); i++) {
				if(!this.units.get(i).equals(otherGenome.units.get(i))) {
					value = false;
				}
			}
		}
		return value;
	}
	
	public int compareTo(Genome<T> g) {
		return this.fitness.compareTo(g.fitness);
	}
	
	public static class GenomeParameters extends Parameters {
		public static double defaultFitness = 0.0;
	}
	
	public static abstract class Unit extends Number implements GeneticUnit, Comparable<Double> {
		private static final long serialVersionUID = 7786152013569807331L;

		public boolean equals(Unit otherUnit) {
			return this.doubleValue() == otherUnit.doubleValue();
		}
		
	}
	
	public interface GeneticUnit {
		public void mutate();
	}
	
	public void mutate(double mutationRate, long seed) {
		Genome.randNum.setSeed(seed);
		this.mutate(mutationRate);
	}
	
	public void mutate(double mutationRate) {
		assert(mutationRate <= 1.0);

		for(T unit : this.units){
			if(Genome.randNum.nextDouble() <= mutationRate) {
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

	public static <U extends Unit> List<Genome<U>> crossover(Genome<U> parent1,
			Genome<U> parent2, double crossoverRate, long seed) {
		Genome.randNum.setSeed(seed);
		return Genome.crossover(parent1, parent2, crossoverRate);
	}
	
	public static <U extends Genome.Unit> List<Genome<U>> crossover(Genome<U> parent1, 
			Genome<U> parent2, double crossoverRate) {

		int listSize = parent1.units.size();
		int pivot = (int)Math.round(Genome.randNum.nextDouble()
				* crossoverRate * listSize);
		ArrayList<U> newWeights1 = new ArrayList<U>(listSize);
		ArrayList<U> newWeights2 = new ArrayList<U>(listSize);
		ArrayList<Genome<U>> children = new ArrayList<Genome<U>>(2);
		Genome<U> child1;
		Genome<U> child2;
		
		newWeights1.addAll(parent1.units.subList(0, pivot));
		newWeights1.addAll(parent2.units.subList(pivot, listSize));
		newWeights2.addAll(parent2.units.subList(0, pivot));
		newWeights2.addAll(parent1.units.subList(pivot, listSize));
		
		assert(newWeights1.size() == listSize);
		assert(newWeights2.size() == listSize);
		
		 child1 = new Genome<U>(newWeights1, GenomeParameters.defaultFitness);
		 child2 = new Genome<U>(newWeights2, GenomeParameters.defaultFitness);
		 
		 children.add(child1);
		 children.add(child2);
		 
		 return children;
	}
	
	public static class GenomeDouble extends Genome.Unit {

		private static final long serialVersionUID = 4483662798023642128L;
		private Double value;
		
		public GenomeDouble(double newValue){
			this.value = Double.valueOf(newValue);
		}
		
		public void mutate(long seed) {
			Genome.randNum.setSeed(seed);
			this.mutate();
		}
		
		public void mutate() {
			if (this.value == 0.0) {
				this.value = randNum.nextDouble();
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
		
		public static GenomeDouble randomUnit(long seed){
			Genome.randNum.setSeed(seed);
			return GenomeDouble.randomUnit();
		}
		
		public static GenomeDouble randomUnit() {
			GenomeDouble newGD = new GenomeDouble(Genome.randNum.nextDouble());
			return newGD;
		}
		
		public static List<GenomeDouble> randomUnitList(int size, long seed) {
			Genome.randNum.setSeed(seed);
			return GenomeDouble.randomUnitList(size);
		}
		
		public static List<GenomeDouble> randomUnitList(int size) {
			ArrayList<GenomeDouble> newList = new ArrayList<GenomeDouble>(size);
			while(newList.size() < size) {
				newList.add(new GenomeDouble(Genome.randNum.nextDouble()));
			}
			return newList;
		}
		
		public String toString() {
			return Double.toString(this.value);
		}

		public int compareTo(Double o) {
			return this.value.compareTo(o.doubleValue());
		}
		
		public int compareTo(GenomeDouble gd) {
			return this.value.compareTo(gd.value);
		}
		
		public boolean equals(GenomeDouble gd) {
			return this.compareTo(gd) == 0;
		}
		
	}
}
