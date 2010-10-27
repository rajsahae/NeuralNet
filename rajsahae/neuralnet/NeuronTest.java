/**
 * 
 */
package rajsahae.neuralnet;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import rajsahae.neuralnet.NeuralNet.Neuron;


/**
 * @author Raj Sahae
 *
 */
public class NeuronTest {
	private NeuralNet.Neuron neuronA;
	private double delta = 0.0001;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		neuronA = new NeuralNet.Neuron(3, 1);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testNumInputs() {
		assertTrue(neuronA.getNumInputs() == 3);
		assertFalse(neuronA.getNumInputs() == 5);
	}
	
	@Test
	public void testActivationThreshold() {
		assertTrue(neuronA.getActivationThreshold() == 1);
		assertFalse(neuronA.getActivationThreshold() == 3);
		neuronA.setActivationThreshold(3);
		assertTrue(neuronA.getActivationThreshold() == 3);
		assertFalse(neuronA.getActivationThreshold() == 1);
	}
	
	@Test
	public void testWeights() {
		List<Double> list = new ArrayList<Double>(5);
		
		assertTrue(neuronA.getInputWeights().size() == 3);
		assertTrue(neuronA.getNumInputs() == 3);
		assertFalse(neuronA.getInputWeights().size() == 1);
		assertFalse(neuronA.getNumInputs() == 1);
		
		list.add(Double.valueOf(0.5));
		list.add(Double.valueOf(1.55));
		list.add(Double.valueOf(-0.26));
		list.add(Double.valueOf(0.0));
		list.add(Double.valueOf(42.367));
		
		neuronA.setWeights(list);
		
		assertTrue(neuronA.getInputWeights().size() == 5);
		assertTrue(neuronA.getNumInputs() == 5);
		assertFalse(neuronA.getInputWeights().size() == 3);
		assertFalse(neuronA.getNumInputs() == 3);
		assertEquals(list, neuronA.getInputWeights());
	}
	
	@Test
	public void testRandomizeNeuronWeights() {
		List<Double> list1 = new ArrayList<Double>(3);
		List<Double> list2 = new ArrayList<Double>(3);
		Random randGen = new Random(1);
		for(int i=0; i<3; i++) {
			list1.add(randGen.nextDouble());
		}
		randGen = new Random(2);
		for(int i=0; i<3; i++) {
			list2.add(randGen.nextDouble());
		}
		Neuron.randomizeNeuronWeights(neuronA, 3, 1);
		assertTrue(list1.equals(neuronA.getInputWeights()));
		assertFalse(list2.equals(neuronA.getInputWeights()));
	}
	
	@Test
	public void testToString() {
		String message = "Neuron Parameters:\n"
			+ "Number of Inputs - " + neuronA.getNumInputs() + "\n"
			+ "Activation Threshold - " + neuronA.getActivationThreshold() + "\n"
			+ "Shape Factor - " + neuronA.getShapeFactor() + "\n"
			+ "Input Weights -\n" + neuronA.getInputWeights().toString();
		
		assertTrue(neuronA.toString().equals(message));
	}
	
	@Test
	public void testSigmoid() {
		assertEquals(NeuralNet.sigmoid(1.0, neuronA), 0.5, delta);
		assertEquals(NeuralNet.sigmoid(-1.0, neuronA), 0.1192, delta);
		assertEquals(NeuralNet.sigmoid(0.0, neuronA), 0.26894, delta);
	}
	
	@Test
	public void testErrorFunction() {
		assertEquals(NeuralNet.errorFunction(1.0, neuronA), 0.0, delta);
		assertEquals(NeuralNet.errorFunction(-1.0, neuronA), -0.761594, delta);
		assertEquals(NeuralNet.errorFunction(0.0, neuronA), -0.462117, delta);
	}
	
	@Test
	public void testMyTanH() {
		assertEquals(NeuralNet.myTanh(1.0, neuronA), 0, delta);
		assertEquals(NeuralNet.myTanh(-1.0, neuronA), -0.761594, delta);
		assertEquals(NeuralNet.myTanh(0.0, neuronA), -0.462117, delta);
	}
}
