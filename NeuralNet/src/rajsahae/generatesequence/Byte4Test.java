package rajsahae.generatesequence;

import org.junit.*;
import static org.junit.Assert.*;

public class Byte4Test {
	private Byte4 b1, b2, b3, b4, b5, b6;
	
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
	public static final Byte4 NUM10 	= new Byte4("1010");
	public static final Byte4 NUM11 	= new Byte4("1011");
	public static final Byte4 NUM12 	= new Byte4("1100");
	public static final Byte4 NUM13 	= new Byte4("1101");
	public static final Byte4 NUM14 	= new Byte4("1110");
	public static final Byte4 NUM15 	= new Byte4("1111");
	
	@Before
	public void setUp(){
		Bit a = new Bit(0);
		Bit b = new Bit(0);
		Bit c = new Bit(1);
		Bit d = new Bit(1);
		
		b1 = new Byte4(a, b, c, d);
		b2 = new Byte4("0", "1", "1", "0");
		b3 = new Byte4("1110");
		b4 = new Byte4("0011");
		b5 = new Byte4("0110");
		b6 = new Byte4("1110");
	}

	@After
	public void tearDown(){
		//Nothing really
	}
	
	@Test public void testCompareTo(){
		assertTrue(b1.compareTo(b2) == -1);
		assertTrue(b2.compareTo(b3) == -1);
		assertTrue(b3.compareTo(b4) == 1);
		assertTrue(b2.compareTo(b1) == 1);
		assertTrue(b1.compareTo(b4) == 0);
		assertTrue(b2.compareTo(b5) == 0);
		assertTrue(b3.compareTo(b6) == 0);
	}
	
	@Test
	public void testEquals(){
		assertTrue(b1.equals(b4));
		assertTrue(b2.equals(b5));
		assertTrue(b3.equals(b6));
		assertTrue(!b1.equals(b2));
		assertTrue(!b2.equals(b3));
		assertTrue(!b3.equals(b4));
	}
	
	@Test
	public void testSwap(){
		assertTrue(b1.swap().toString().equals("1100"));
		assertTrue(b2.swap().toString().equals("1001"));
		assertTrue(b3.swap().toString().equals("0001"));
	}
	
	@Test
	public void testToString(){
		assertTrue(b1.toString().equals("0011"));
		assertTrue(b2.toString().equals("0110"));
		assertTrue(b3.toString().equals("1110"));
	}
	
	@Test
	public void testGetDoubleValue() {
		assertTrue(3.0 == NUM3.doubleValue());
		assertTrue(6.0 == NUM6.doubleValue());
		assertTrue(14.0 == NUM14.doubleValue());
	}
	
	@Test
	public void testGetFloatValue() {
		assertTrue(3.0 == NUM3.floatValue());
		assertTrue(6.0 == NUM6.floatValue());
		assertTrue(14.0 == NUM14.floatValue());
	}
	
	@Test
	public void testGetIntValue() {
		assertTrue(3 == NUM3.intValue());
		assertTrue(6 == NUM6.intValue());
		assertTrue(14 == NUM14.intValue());
	}
	
	@Test
	public void testGetLongValue() {
		assertTrue(3 == NUM3.longValue());
		assertTrue(6 == NUM6.longValue());
		assertTrue(14 == NUM14.longValue());
	}
	
	@Test
	public void testRandomUnit() {
		
	}
	
	@Test
	public void testRandomUnitList() {
		
	}

}
