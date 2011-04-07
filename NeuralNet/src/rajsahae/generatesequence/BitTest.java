package rajsahae.generatesequence;
import org.junit.*;
import org.junit.Before;
import static org.junit.Assert.*;

public class BitTest {
	private Bit b0, b1, b2;
	
	@Before
	public void setUp(){
		b0 = new Bit(0);
		b1 = new Bit(1);
		b2 = new Bit("0");
	}
	
	@Test
	public void testCompareTo(){
		assertTrue(b0.compareTo(b1) == -1);
		assertTrue(b1.compareTo(b2) == 1);
		assertTrue(b2.compareTo(b0) == 0);
	}
	
	@Test
	public void testEquals(){
		assertTrue(!b0.equals(b1));
		assertTrue(b0.equals(b2));
	}
	
	@Test
	public void testSwap(){
		assertTrue(b0.swap().toString().equals("1"));
		assertTrue(b1.swap().toString().equals("0"));
	}
	
	@Test
	public void testToString(){
		assertTrue(b0.toString().equals("0"));
		assertTrue(b1.toString().equals("1"));
		assertTrue(b2.toString().equals("0"));
	}
}
