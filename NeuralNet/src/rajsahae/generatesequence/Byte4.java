package rajsahae.generatesequence;

import rajsahae.geneticalgorithm.Genome;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Byte4 extends Genome.Unit {

	private static final long serialVersionUID = 3501184081203133945L;
	private static Random randNum = new Random();
	
	public static int LENGTH = 4;
	public Bit[] bits = new Bit[4];
	
	public Byte4(Bit a, Bit b, Bit c, Bit d){
		bits[0] = a;
		bits[1] = b;
		bits[2] = c;
		bits[3] = d;
	}
	
	public Byte4(String numbers){
		bits[0] = new Bit(numbers.substring(0, 1));
		bits[1] = new Bit(numbers.substring(1, 2));
		bits[2] = new Bit(numbers.substring(2, 3));
		bits[3] = new Bit(numbers.substring(3, 4));
	}
	

	public Byte4(String a, String b, String c, String d){
		bits[0] = new Bit(a);
		bits[1] = new Bit(b);
		bits[2] = new Bit(c);
		bits[3] = new Bit(d);
	}
	
	public int compareTo(Byte4 otherByte){
		int value  = 0;
		
		if (this.equals(otherByte)){
			value = 0;
		} else {
			for (int i=0; i<this.bits.length; i++){
				if(this.bits[i].compareTo(otherByte.bits[i]) == 0){
					continue;
				} else if (this.bits[i].compareTo(otherByte.bits[i]) == 1){
					value = 1;
					break;
				} else {
					value =  -1;
					break;
				}
			}
		}
		return value;
	}

	public boolean equals(Byte4 otherByte){
		for (int i=0; i<bits.length; i++){
			if (!this.bits[i].equals(otherByte.bits[i])){
				return false;
			}
		}
		return true;
	}

	public Byte4 swap(){
		for (int i=0; i<bits.length; i++){
			bits[i].swap();
		}
		return this;
	}
	
	public String toString(){
		String temp = "";
		for (int i=0; i<bits.length; i++){
			temp += bits[i].toString();
		}
		return temp;
	}

	public void mutate() {
		this.swap();
	}

	private double getBaseTenValue() {
		double base = 2.0;
		double sum = 0.0;
		
		for(int i=0; i<this.bits.length; i++) {
			double temp = Math.pow(base, this.bits.length-i-1);
			sum += (this.bits[i].value * temp);
		}
		return (double)sum;
	}
	
	public double doubleValue() {
		return Double.valueOf(this.getBaseTenValue());
	}

	public float floatValue() {
		return (float)this.getBaseTenValue();
	}

	public int intValue() {
		return (int)this.getBaseTenValue();
	}

	public long longValue() {
		return (long)this.getBaseTenValue();
	}
	
	public static Byte4 randomUnit(long seed){
		Byte4.randNum.setSeed(seed);
		return randomUnit();
	}
	
	public static Byte4 randomUnit() {
		String randString = "";
		for (int i=0; i<Byte4.LENGTH; i++){
			randString += Byte4.randNum.nextInt(2);
		}
		return new Byte4(randString);
	}
	
	public static List<Byte4> randomUnitList(int size, long seed) {
		Byte4.randNum.setSeed(seed);
		return randomUnitList(size);
	}
	
	public static List<Byte4> randomUnitList(int size) {
		ArrayList<Byte4> newList = new ArrayList<Byte4>(size);
		while(newList.size() < size) {
			newList.add(Byte4.randomUnit());
		}
		return newList;
	}

	public int compareTo(Double o) {
		return Double.valueOf(this.doubleValue()).compareTo(o);
	}
}
