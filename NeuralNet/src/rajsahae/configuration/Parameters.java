package rajsahae.configuration;

import java.lang.reflect.*;
import java.io.*;

/**
 * Class for saving, importing, and exporting application parameters.
 * The target class should extend the Parameter class and all parameter fields should
 * be "public static" fields.
 * 
 * @author 	Raj Sahae
 * @date 	Oct 2010
 * @version 1.0
 */
public abstract class Parameters {
	//private static final String fieldDelimiter 		= "$";
	private static final String classDelimiter 		= ".";
	private static final String parameterDelimiter 	= "=";
	
	/*
	public static boolean importParametersFromFile(Class<? extends Parameters> target,
			String filename) {
		BufferedReader input 	= null;
		boolean status 			= true;
		String className 		= null;
		String parameterName 	= null;
		String parameterValue 	= null;
		String line 			= null;
		int classIndex 			= 0;
		int parameterIndex 		= 0;
		
		try {
			input = new BufferedReader(new FileReader(filename));
			
			while ((line = input.readLine()) != null) {
				classIndex = line.indexOf(classDelimiter);
				parameterIndex = line.indexOf(parameterDelimiter);
				
				className = line.substring(0, classIndex);
				parameterName = line.substring(++classIndex, parameterIndex);
				parameterValue = line.substring(++parameterIndex);
				
				finish;
			}
		} catch (Exception ex) {
			status = false;
			ex.printStackTrace(System.err);
		} finally {
			safeClose(input);
		}
		
		return status;
	}
	*/
	public static boolean exportParametersToFile(Class<? extends Parameters> target,
			String filename) {
		
		PrintWriter output 		= null;
		boolean status 			= true;
		String parameterClass 	= null;
		String parameterName 	= null;
		String parameterValue 	= null;
		
		try {
			output = new PrintWriter(new FileWriter(filename));

			Field[] classFields = target.getFields();
			
			for(Field aField : classFields) {
				parameterClass 	= aField.getDeclaringClass().getSimpleName();
				parameterName 	= aField.getName();
				parameterValue 	= aField.get(target).toString();
				
				output.print(parameterClass);
				output.print(classDelimiter);
				output.print(parameterName);
				output.print(parameterDelimiter);
				output.print(parameterValue);
				output.println();
			}
			
		} catch (Exception ex) {
			status = false;
			ex.printStackTrace(System.err);
			System.err.println("Had trouble exporting parameter " + parameterName);
		} finally {
			safeClose(output);
		}
		
		return status;
	}
	
	private static void safeClose(Closeable stream) {
		try {
			if (stream != null) {
				stream.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
	}
}
