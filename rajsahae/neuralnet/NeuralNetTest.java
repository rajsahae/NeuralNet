/**
 * 
 */
package rajsahae.neuralnet;


import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rajsahae.neuralnet.NeuralNet.Neuron;
import static rajsahae.neuralnet.NeuralNet.sigmoid;


/**
 * @author rsahae
 *
 */
public class NeuralNetTest {

	private int[] numHiddens = {2, 3, 2};
	private NeuralNet net2332;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		net2332 = new NeuralNet(1, 1, 3, numHiddens);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testUpdate() {
		int numOut 		= net2332.getNumberOfOutputs();
		int numIn 		= net2332.getNumberOfInputs();
		int numWeights 	= net2332.getNumberOfWeights();
		double newWeight 	= 1;
		double myInput	= 1;
		double expectedValue;
		Neuron testN;
		
		ArrayList<Double> weights = new ArrayList<Double>(numWeights);
		ArrayList<Double> inputs = new ArrayList<Double>(numIn);
		ArrayList<Double> outputs = new ArrayList<Double>(numOut);
		ArrayList<Double> control = new ArrayList<Double>(numOut);
		
		for(int i=0; i<numWeights; i++) {
			weights.add(newWeight);
		}
		net2332.setWeights(weights);
		
		for(int i=0; i<numOut; i++){
			testN = new Neuron(numHiddens[i], 1);
			testN.setWeights(weights.subList(0, numHiddens[i]));
			expectedValue = sigmoid(numHiddens[i]*newWeight*
					sigmoid(numHiddens[i]*newWeight
					*sigmoid(numIn*newWeight*myInput, testN), testN), testN);
			control.add(expectedValue);
		}
		for(int i=0; i<numIn; i++) {
			inputs.add(myInput);
		}
		outputs.addAll(net2332.update(inputs));
		
		//System.out.println(control.toString());
		//System.out.println(outputs.toString());
		assertArrayEquals(control.toArray(), outputs.toArray());
	}
	
	
	
}
