/**
 * 
 */
package rajsahae.neuralnet;


import static org.junit.Assert.*;
import java.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author rsahae
 *
 */
public class NeuralNetTest {

	private int[] hiddens232 	= {2, 3, 2};
	private int[] hiddens212 	= {2, 1, 2};
	private int[] hiddens2		= {2};
	
	private NeuralNet net12321;
	private NeuralNet net32123;
	private NeuralNet net321;
	
	private ArrayList<Double>weights12321 	= new ArrayList<Double>();
	private ArrayList<Double>weights32123 	= new ArrayList<Double>();
	private ArrayList<Double>weights321		= new ArrayList<Double>();
	
	private int numInputs12321;
	private int numInputs32123;
	private int numInputs321;
	
	private int numOutputs12321;
	private int numOutputs32123;
	private int numOutputs321;
	
	private ArrayList<Double> inputs12321;
	private ArrayList<Double> inputs32123;
	private ArrayList<Double> inputs321;
	
	private ArrayList<Double> outputs12321;
	private ArrayList<Double> outputs32123;
	private ArrayList<Double> outputs321;
	
	private ArrayList<Double> expected12321;
	private ArrayList<Double> expected32123;
	private ArrayList<Double> expected321;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		net12321 	= new NeuralNet(1, 1, 3, hiddens232);
		net32123 	= new NeuralNet(3, 3, 3, hiddens212);
		net321		= new NeuralNet(3, 1, 1, hiddens2);
		
		Double[] weightsFor12321 = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0,
				10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0};
		Double[] weightsFor32123 = {11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 9.0, 10.0, 7.0, 8.0,
				1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
		Double[] weightsFor321 = {4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0};
		
		weights12321.ensureCapacity(weightsFor12321.length);
		weights32123.ensureCapacity(weightsFor32123.length);
		weights321.ensureCapacity(weightsFor321.length);
		
		weights12321.addAll(Arrays.asList(weightsFor12321));
		weights32123.addAll(Arrays.asList(weightsFor32123));
		weights321.addAll(Arrays.asList(weightsFor321));
		
		net12321.setWeights(weights12321);
		net32123.setWeights(weights32123);
		net321.setWeights(weights321);
		
		numInputs12321  = net12321.getNumberOfInputs();
		numInputs32123	= net32123.getNumberOfInputs();
		numInputs321	= net321.getNumberOfInputs();
		
		numOutputs12321 = net12321.getNumberOfOutputs();
		numOutputs32123	= net32123.getNumberOfOutputs();
		numOutputs321	= net321.getNumberOfOutputs();
		
		inputs12321 	= new ArrayList<Double>(numInputs12321);
		inputs32123 	= new ArrayList<Double>(numInputs32123);
		inputs321 		= new ArrayList<Double>(numInputs321);
		
		outputs12321 	= new ArrayList<Double>(numOutputs12321);
		outputs32123	= new ArrayList<Double>(numOutputs32123);
		outputs321		= new ArrayList<Double>(numOutputs321);
		
		expected12321 	= new ArrayList<Double>(numOutputs12321);
		expected32123 	= new ArrayList<Double>(numOutputs32123);
		expected321		= new ArrayList<Double>(numOutputs321);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testUpdate() {
		/* Before running this test, change the output assignment in the UPDATE
		 * method to output=netInput.  Once the test is done, change it back to
		 * the current (default is the sigmoid function);
		 */
		Double[] inputFor12321 	= {1.0};
		Double[] inputFor32123 	= {1.0, 2.0, 3.0};
		Double[] inputFor321	= {1.0, 2.0, 3.0};
		
		Double[] outputFor12321 = {18630.0};
		Double[] outputFor32123 = {36478.0, 84058.0, 131638.0};
		Double[] outputFor321	= {870.0};
		
		inputs12321.addAll(Arrays.asList(inputFor12321));
		inputs32123.addAll(Arrays.asList(inputFor32123));
		inputs321.addAll(Arrays.asList(inputFor321));
		
		expected12321.addAll(Arrays.asList(outputFor12321));
		expected32123.addAll(Arrays.asList(outputFor32123));
		expected321.addAll(Arrays.asList(outputFor321));
		
		outputs12321.addAll(net12321.update(inputs12321));
		outputs32123.addAll(net32123.update(inputs32123));
		outputs321.addAll(net321.update(inputs321));
		
		assertEquals(expected12321, outputs12321);
		assertEquals(expected32123, outputs32123);
		assertEquals(expected321, outputs321);
	}
	
	
	
}
