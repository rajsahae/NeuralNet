package rajsahae.generatesequence;

public class Bit implements Comparable<Bit> {
	
	public int value;
	
	public Bit(int num){
		value = num;
	}
	
	public Bit(String num){
		value = Integer.parseInt(num);
	}
	
	public int compareTo(Bit otherBit){
		if (this.value < otherBit.value) {
			return -1;
		} else if (this.value == otherBit.value){
			return 0;
		} else {
			return 1;
		}
	}
	
	public boolean equals(Bit otherBit){
		return (this.value == otherBit.value);
	}
	
	public Bit swap(){
		value = (value == 0 ? 1 : 0);
		return this;
	}
	
	public String toString(){
		return Integer.toString(value);
	}
}
