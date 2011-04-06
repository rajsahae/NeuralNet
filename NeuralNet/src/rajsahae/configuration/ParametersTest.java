package rajsahae.configuration;

import org.junit.*;

public class ParametersTest {
	
	public static class ParmClass1 extends Parameters {
		public static int p1 = 2;
		public static long p2 = 2;
		public static String hostAddress = "192.168.0.1";
		public static boolean p3 = true;
		public static double p4 = 4.0;
		public static String ipAddress = "192.168.0.100";
	}
	/*
	public void testImportParametersFromFile() {
		Parameters.importParametersFromFile(ParmClass1.class, "import_test1.ini");
		Parameters.exportParametersToFile(ParmClass1.class, "import_test2.ini");
	}
	*/
	@Test
	public void testExportParametersToFile() {
		Parameters.exportParametersToFile(ParmClass1.class, "export_test.ini");
	}
	
}
