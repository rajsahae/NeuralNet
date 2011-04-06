/**
 * 
 */
package rajsahae.neuralnet;

import static org.junit.Assert.*;
import rajsahae.neuralnet.NeuralNet.NeuronLayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Raj
 *
 */
public class NeuronLayerTest {
	public NeuronLayer layer551;
	public NeuronLayer layer342;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		 layer551 = new NeuronLayer(5, 5, 1);
		 layer342 = new NeuronLayer(3, 4, 2);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testNumNeurons() {
		assertTrue(layer551.getNumNeurons() == 5);
		assertFalse(layer551.getNumNeurons() == 3);
		assertTrue(layer342.getNumNeurons() == 3);
		assertFalse(layer342.getNumNeurons() == 5);
	}
	
	@Test
	public void testNeurons() {
		assertTrue(layer551.getNeurons().size() == 5);
		assertTrue(layer342.getNeurons().size() == 3);
		assertTrue(layer551.getNeurons().get(0).getNumInputs() == 5);
		assertTrue(layer551.getNeurons().get(1).getNumInputs() == 5);
		assertTrue(layer551.getNeurons().get(2).getNumInputs() == 5);
		assertTrue(layer551.getNeurons().get(3).getNumInputs() == 5);
		assertTrue(layer551.getNeurons().get(4).getNumInputs() == 5);
		assertFalse(layer551.getNeurons().get(0).getNumInputs() == 4);
		assertFalse(layer551.getNeurons().get(1).getNumInputs() == 4);
		assertFalse(layer551.getNeurons().get(2).getNumInputs() == 4);
		assertTrue(layer342.getNeurons().get(0).getNumInputs() == 4);
		assertTrue(layer342.getNeurons().get(1).getNumInputs() == 4);
		assertTrue(layer342.getNeurons().get(2).getNumInputs() == 4);
		assertTrue(layer551.getNeurons().get(1).getActivationThreshold() == 1);
		assertFalse(layer551.getNeurons().get(1).getActivationThreshold() == 2);
		assertTrue(layer342.getNeurons().get(1).getActivationThreshold() == 2);
		assertFalse(layer342.getNeurons().get(1).getActivationThreshold() == 1);
	}

}
