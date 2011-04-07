package rajsahae.neuralnet;
/**
 * The Neural Net class contains all functionality for creating and using
 * neural nets.
 * 
 * @author		Raj Sahae
 * @version 	1.0
 * @date		Oct 2010
 */

import java.util.*;
import java.lang.Math;
import java.util.Random;

import rajsahae.configuration.Parameters;

/**
 * The Neural Net class is the control class for creating and running Neural Nets.
 * It will create a network of Neurons, in layers, with one input layer, one output
 * layer, and a specified number of hidden layers.  Feed it inputs of type double
 *  and it will output values of type double
 * 
 * @author 	Raj Sahae
 * @date 	Oct 2010
 * @version 1.0
 *
 * 
 */
public class NeuralNet {
	
	private static Random randNum = new Random();
	
	private int numInputs;				// number of Neural Net inputs
	private int numOutputs;				// number of Neural Net outputs
	private int numHiddenLayers;		// number of hidden neuron layers
	private int[] neuronsPerHiddenLayer;	// number of neurons per hidden layer
	private ArrayList<NeuronLayer> neuronLayers;	// neuron layer storage
	
	/**
	 * Constructs a Neural Net with the specified number of inputs, outputs,
	 * hidden layers, and neurons per hidden layer.  The constructor
	 * automatically calls createNet() to create the Neurons and Layers.
	 * 
	 * @param numInputs				- integer number of inputs
	 * @param numOutputs			- integer number of outputs
	 * @param numHiddenLayers		- integer number of hidden layers
	 * @param neuronsPerHiddenLayer	- Integer array with arguments representing
	 * the number of neurons contained in each hidden layer
	 * 
	 * @author 	Raj Sahae
	 * @date 	Oct 2010
	 * @version 1.0
	 */
	public NeuralNet(int numInputs, int numOutputs, 
			int numHiddenLayers, int[] neuronsPerHiddenLayer) {
		this.numInputs = 				numInputs;
		this.numOutputs =				numOutputs;
		this.numHiddenLayers = 			numHiddenLayers;
		this.neuronsPerHiddenLayer = 	neuronsPerHiddenLayer;
		this.neuronLayers = new ArrayList<NeuronLayer>(numHiddenLayers+1);
		
		assert(this.neuronsPerHiddenLayer.length == this.numHiddenLayers);
		
		this.createNet();
	}
	
	public String toString() {
		return this.neuronLayers.toString();
	}
	/**
	 * This function creates the Neural Net.  The actual construction of the net
	 * ignores the input layer, so the actual net has the hidden layers,
	 * then the output layer.  
	 */
	protected void createNet() {
		/**
		 * Set the first layers inputs per neuron to the number of inputs.
		 * Then create each hidden layer, setting it's numInputs to the number
		 * of neurons in the previous layer.
		 * The final layer should be the output neuron layer.
		 */
		int inputsPerNeuron = this.numInputs;
		int outputLayer = 1;
		this.neuronLayers.clear();
		this.neuronLayers.ensureCapacity(this.numHiddenLayers+outputLayer);
		
		for(int i=0; i<this.numHiddenLayers; i++) {
			
			if(i>0) {
				// inputsPerNeuron equals numOutputs of the previous layer
				inputsPerNeuron = this.neuronLayers.get(i-1).getNumNeurons();
			}
			// add each hidden layer 
			this.neuronLayers.add(new NeuronLayer(this.neuronsPerHiddenLayer[i],
					inputsPerNeuron, NeuralNetParameters.neuronActivationThreshold));
		}
		//add the output layer
		inputsPerNeuron = this.neuronLayers.get(this.neuronLayers.size()-1).numNeurons;
		this.neuronLayers.add(new NeuronLayer(this.numOutputs,
				inputsPerNeuron, NeuralNetParameters.neuronActivationThreshold));
	}
	
	public int getNumberOfInputs() {
		return this.numInputs;
	}
	
	public int getNumberOfOutputs() {
		return this.numOutputs;
	}
	
	public List<Double> getWeights() {
		ArrayList<Double> weights = new ArrayList<Double>();
		for(NeuronLayer layer : this.neuronLayers) {
			for(Neuron neuron : layer.neurons) {
				weights.ensureCapacity(weights.size() + neuron.numInputs);
				weights.addAll(neuron.getInputWeights());
			}
		}
		return weights;
	}
	
	public int getNumberOfWeights() {
		int sum = 0;
		for(NeuronLayer layer : this.neuronLayers) {
			for(Neuron neuron : layer.neurons) {
				sum += neuron.inputWeights.size();
			}
		}
		return sum;
	}
	
	public void setWeights(List<Double> weights) {
		assert(weights.size() == this.getNumberOfWeights());
		int fromIndex = 0;
		int toIndex = 0;
		
		for(NeuronLayer layer : this.neuronLayers) {
			for(Neuron neuron : layer.neurons) {
				fromIndex = toIndex;
				toIndex += neuron.getNumInputs();
				neuron.setWeights(weights.subList(fromIndex, toIndex));
			}
		}
	}
	
	public List<Double> update(List<Double> in) {
		ArrayList<Double> outputs = new ArrayList<Double>();
		double output = 0.0;
		
		// check for correct number of inputs
		assert(in.size() == this.numInputs);
		
		// For each NeuronLayer, sum the inputs of each neuron
		// multiplied by corresponding weights
		// Enter the sum into the activation function (in this case, myTanh
		// Set the output equal to the input of the next layer and repeat.
		ArrayList<Double> inputs = new ArrayList<Double>(in.size());
		inputs.addAll(in);
		
		for (int i=0; i<this.neuronLayers.size(); i++) {
			if(i > 0) {
				inputs.clear();
				inputs.ensureCapacity(outputs.size());
				inputs.addAll(outputs);
			}
			outputs.clear();
			outputs.ensureCapacity(this.neuronLayers.get(i).getNumNeurons());
			
			for (int j=0; j<this.neuronLayers.get(i).getNumNeurons(); j++) {
				double netInput = 0.0;
				
				int numInputs = 
					this.neuronLayers.get(i).getNeurons().get(j).getNumInputs();
				
				
				//for each weight
				for (int k=0; k<numInputs; k++) {
					assert(this.neuronLayers.get(i).neurons.get(j).inputWeights.size()
							== (inputs.size()));
					
					// sum the weights * inputs
					netInput += 
						this.neuronLayers.get(i).neurons.get(j).inputWeights.get(k)
						* inputs.get(k);
				}
								
				//output = netInput; // For junit testing purposes

				// Enter the net input into the sigmoid function and store the output
				output = sigmoid(netInput, this.neuronLayers.get(i).neurons.get(j));
				outputs.add(output);
			}
		}
		
		// Check for correct output size
		assert(outputs.size() == this.numOutputs);
		
		return outputs;
	}
	
	/**
	 * 	Sigmoid curve is a smooth step function with inflection occurring around 0.
	 *  returns value from 0 to 1 along the sigmoid curve
	 *  
	 * @param x				- Double from negative infinity to infinity
	 * @param n 			- Neuron to calculate the output of.  Must have
	 * activationThreshold and shapeFactor defined. 
	 * @return 				Double with lower limit 0.0 and upper limit 1.0
	 * @throws 				IllegalArgumentException when shapeFactor is less than 0.
	 * 
	 */
	protected static double sigmoid(double x, Neuron n)
			throws IllegalArgumentException {
		double shapeFactor = n.getShapeFactor();
		double activationThreshold = n.getActivationThreshold();
		if(Double.isNaN(shapeFactor)
				|| shapeFactor < 0 || Double.isNaN(activationThreshold)) {
			String message = "Neuron has illegal parameters\n" + n.toString();
			throw new IllegalArgumentException(message);
		}
		double numerator = 1.0;
		double exponent = -1.0*(x-n.getActivationThreshold());
		double denominator = 1.0 + Math.exp(exponent/shapeFactor); 
		return (numerator/denominator);
				
	}
	
	/**
	 * The error function is a form of the sigmoid curve that has output lower limit
	 * of -1, and output upper limit of +1.
	 * This errorFunction is equal to Math.tanh(x/(2*shapeFactor))
	 * 
	 * @param x				- Double from negative infinity to infinity
	 * @param shapeFactor	- Double with lower limit 0, upper limit infinity 
	 * @return				Double with lower limit -1.0 and upper limit 1.0
	 * @throws				IllegalArgumentException when shapeFactor is less than 0.
	 */
	
	protected static double errorFunction(double x, Neuron n)
			throws IllegalArgumentException {
		return (2 * sigmoid(x, n) - 1);
	}
	
	/**
	 * The tanh is a form of the sigmoid curve.  This tanh has been configured
	 * to have the same output as the Error Function.
	 * 
	 * @param x				- Double from negative infinity to infinity
	 * @param shapeFactor	- Double with lower limit 0, upper limit infinity 
	 * @return				Double with lower limit -1.0 and upper limit 1.0
	 * @throws				IllegalArgumentException when shapeFactor is less than 0.
	 */
	protected static double myTanh(double x, Neuron n) 
			throws IllegalArgumentException {
		double shapeFactor = n.getShapeFactor();
		double activationThreshold = n.getActivationThreshold();
		if(Double.isNaN(shapeFactor)
				|| shapeFactor < 0 || Double.isNaN(activationThreshold)) {
			String message = "Neuron has illegal parameters\n" + n.toString();
			throw new IllegalArgumentException(message);
		}
		return Math.tanh((x-activationThreshold)/(2*shapeFactor));
	}

	/**
	 * Class for saving Neural Net Parameters.
	 * Class extends rajsahae.configuration.Parameters.
	 * 
	 * @author 	Raj Sahae
	 * @version 1.0
	 * @date	Oct 2010
	 *
	 */
	public static class NeuralNetParameters extends Parameters {
		
		public static int numInputs = 1;
		public static int numOutputs = 1;
		public static int numHiddenLayers = 1;
		public static int numNeuronsPerHiddenLayer = 2;
		public static double neuronActivationThreshold = 1.0;
		public static double sigmoidShapeFactor = 1.0;
	}
	
	/**
	 * Fundamental inner class for the Neural Net class.
	 * The Neuron is the basic unit for the Neural net.  A neuron has any 
	 * number of inputs, and one output.  The output for a neuron fires 
	 * based on its activation energy.  The neuron applies weights to 
	 * its inputs, sums them, and if the sum exceeds its activation energy,
	 * it will output based on the sigmoid function it is using.
	 * 
	 * @author 	Raj Sahae
	 * @date 	Oct 2010
	 * @version 1.0
	 * 
	 */
	protected static class Neuron {

		private int 				numInputs;
		private ArrayList<Double> 	inputWeights;
		private double 				activationThreshhold;
		private double				shapeFactor;
		
		/**
		 * Constructor for the Neuron, the basic node of the Neural Net.
		 * @param numInputs Integer representing the number of inputs for 
		 * this Neuron
		 * @param activationThreshold Integer representing the 
		 * ActivationThreshold
		 */
		protected Neuron(int numInputs, double activationThreshold){
			this.numInputs 				= numInputs;
			this.inputWeights 			= new ArrayList<Double>(numInputs);
			this.activationThreshhold 	= activationThreshold;
			this.shapeFactor			= NeuralNetParameters.sigmoidShapeFactor;
			
			Neuron.randomizeNeuronWeights(this, numInputs);
		}
		
		public String toString() {
			String message = "Neuron Parameters:\n"
				+ "Number of Inputs - " + this.numInputs + "\n"
				+ "Activation Threshold - " + this.activationThreshhold + "\n"
				+ "Shape Factor - " + this.shapeFactor + "\n"
				+ "Input Weights -\n" + this.inputWeights.toString();
			
			return message;
		}
		/**
		 * Setter for the neurons Activation Threshold.
		 * 
		 * @param num double number for activation threshold
		 */
		protected void setActivationThreshold(double num) {
			this.activationThreshhold = num;
		}
		
		/**
		 * Setter for the weights of the neurons inputs.  This method will
		 * also reset the NumInputs field so be sure to check that the weights
		 * you are setting are the correct number.
		 * 
		 * @param weights List of doubles which represent the weights of the 
		 * inputs for this neuron.
		 */
		protected void setWeights(List<Double> weights) {
			this.inputWeights.clear();
			this.inputWeights.ensureCapacity(weights.size());
			this.inputWeights.addAll(weights);
			this.numInputs = this.inputWeights.size();
		}
		
		/**
		 * Sets the shape factor for this neuron, which is used to calculate
		 * it's output via the sigmoid function
		 * 
		 * @param newShapeFactor
		 */
		protected void setShapeFactor(double newShapeFactor) {
			this.shapeFactor = newShapeFactor;
		}
		
		/*
		 * getters
		 */
		
		/**
		 * Getter for the number of inputs for this neuron
		 * @return integer representing the number of inputs for this neuron.  
		 */
		protected int getNumInputs() {
			return this.numInputs;
		}
		
		/**
		 * Getter for the input weights of this neuron.
		 * @return List of Doubles
		 */
		protected List<Double> getInputWeights() {
			return this.inputWeights;
		}
		
		/**
		 * Getter for the activation threshold of the neuron.
		 * @return double representing the activation threshold
		 */
		protected double getActivationThreshold() {
			return this.activationThreshhold;
		}
		
		/**
		 * Getter for the shape factor of this neuron, which is
		 * used to calculate the output via the sigmoid function.
		 * @return the shape factor of the neuron as a double
		 */
		protected double getShapeFactor() {
			return this.shapeFactor;
		}
		/*
		 * Static Methods
		 */
		
		/**
		 * Static method in the Neuron class, that takes a neuron and the
		 * number of inputs and creates a random set of weights for that
		 * neuron.  This function will replace all the weights
		 * that the neuron currently has and reset numInputs
		 * 
		 * @param neuron Neuron to assign random weights to
		 * @param weights integer for number of input weights to create
		 * @param seed sets seed for random number generator. Set this equal
		 * to 0 to use a randomly generated seed.
		 */
		protected static void randomizeNeuronWeights(Neuron neuron, int weights,
				long seed) {
			NeuralNet.randNum.setSeed(seed);
			Neuron.randomizeNeuronWeights(neuron, weights);
		}
		
		protected static void randomizeNeuronWeights(Neuron neuron, int weights) {
			
			neuron.inputWeights.clear();
			neuron.inputWeights.ensureCapacity(weights);

			for (int i=0; i<weights; i++) {
				neuron.inputWeights.add(NeuralNet.randNum.nextDouble());
			}
			neuron.numInputs = neuron.inputWeights.size();
		}
	}
	
	/**
	 * The NeuronLayer is a container for Neurons in a NeuralNet.
	 * 
	 * @author 	Raj Sahae
	 * @date 	Oct 2010
	 * @version 1.0
	 *
	 */
	protected static class NeuronLayer {
		
		private int numNeurons;				//number of neurons in this layer
		private ArrayList<Neuron> neurons;	//collection of neurons in this layer
		
		/**
		 * Constructor for the NeuronLayer.
		 * 
		 * @param numNeurons integer number of neurons in this layer
		 * @param numInputsPerNeuron integer number of neurons in the 
		 * previous layer
		 * @param activationThreshold activation threshold energy for
		 * each neuron in this layer
		 */
		protected NeuronLayer(int numNeurons, int numInputsPerNeuron,
				double activationThreshold) {
			
			this.numNeurons = numNeurons;
			this.neurons 	= new ArrayList<Neuron>(numNeurons);
			
			for(int i=0; i<numNeurons; i++) {
				neurons.add(new Neuron(numInputsPerNeuron,
						activationThreshold));
			}
			assert(this.neurons.size() == this.numNeurons);
			
		}
		
		public String toString() {
			return this.neurons.toString();
		}
		
		protected int getNumNeurons() {
			return this.numNeurons;
		}
		
		protected List<Neuron> getNeurons() {
			return this.neurons;
		}
		
		protected void setNeurons(List<Neuron> neurons) {
			this.neurons.clear();
			this.neurons.ensureCapacity(neurons.size());
			this.numNeurons = neurons.size();
			this.neurons.addAll(neurons);
		}
		
	}
}
