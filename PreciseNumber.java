

// This class represents numbers with very high precision--down to 2^(-Integer.MAXVALUE)
// This is limited to numbers within normal integer limits with the fractional part always
// positive. The only supported operations are taking averages (of 2 numbers) and comparisons
public class PreciseNumber implements Comparable<PreciseNumber>{
	// This represents the integer part of the number
	private final int intPart;
	// This represents the fractional part of the number as a boolean array
	// where fracPart[i] represents if the i+1th bit from after the decimal is 1
	private final boolean[] fracPart;
	// approximate value of this number
	private final double x;
	
	// constructor for a given integer
	public PreciseNumber(int i) {
		intPart = i;
		fracPart = null;
		x = (double)i;
	}
	/*
	// constructor for the given double number to highest precision
	public PreciseNumber(double d) {
		intPart = (int)Math.floor(d);
		x = d;
		d -= intPart;
		ArrayList<Boolean> temp = new ArrayList<Boolean>();
		while(d > 0) {
			if(d > .5) {
				temp.add(true);
			} else {
				temp.add(false);
			}
			d *= 2;
		}
		boolean[] temp2 = new boolean[temp.size()];
		for(int i = 0; i < temp2.length; i++) {
			temp2[i] = temp.get(i);
		}
		fracPart = temp2;
	}*/
	
	// constructor for a given integer with the given part
	public PreciseNumber(int i, boolean[] f) {
		intPart = i;
		fracPart = f;
		double temp = (double)intPart;
		for(int j = 0; j < (fracPart == null ? 0 :fracPart.length); j++) {
			if(fracPart[j]) {
				temp += Math.pow(.5, j+1);
			}
		}
		x = temp;
	}
	
	// gets the length of the decimal expansion
	public int getLength() {
		return (fracPart == null ? 0 :fracPart.length);
	}
	
	// gets the fractional digit at the given position after the decimal point
	public boolean getFracDigit(int n) {
		return (fracPart != null && n < fracPart.length ? fracPart[n] : false);
	}
	
	// gets the integer part of the number
	public int getInt() {
		return intPart;
	}
	
	// returns the PreciseNumber average of the given PreciseNumbers
	public static PreciseNumber average(PreciseNumber i1, PreciseNumber i2) {
		// Compute the fractional part
		boolean[] f;
		int resLength = Math.max(i1.getLength(), i2.getLength());
		// if there is no fractional part
		if(resLength == 0) {
			// see if there is a new fractional part & add it if needed
			if((i1.getInt() + i2.getInt())%2 == 1) {
				f = new boolean[1];
				f[0] = true;
			} else {
				f = null;
			}
		} else {
			// compute if the number of digits needed
			if(i1.getLength() != i2.getLength()) {
				resLength++;
			}
			// Do ripple adder addition (with a right shift)
			f = new boolean[resLength];
			boolean carry = false;
			for(int i = resLength -2; i >= 0; i--) {
				f[i+1] = i1.getFracDigit(i) ^ i2.getFracDigit(i) ^ carry;
				carry = (i1.getFracDigit(i) && i2.getFracDigit(i)) || (carry && (i1.getFracDigit(i) ^ i2.getFracDigit(i)));
			}
			f[0] = ((i1.getInt() + i2.getInt())%2 == 1) ^ carry;
		}
		// Compute the integer part and return the result
		return new PreciseNumber((i1.getInt() + i2.getInt())/2, f);
	}
	
	// Compare against the given number as specified in the Comparable interface
	// only returns -1, 0, or 1
	public int compareTo(PreciseNumber i) {
		if(intPart < i.getInt()) {
			return -1;
		}
		if(intPart > i.getInt()) {
			return 1;
		}
		int j = 0;
		while(j < (fracPart == null ? 0 :fracPart.length)) {
			if(!fracPart[j] && i.getFracDigit(j)) {
				return -1;
			}
			if(fracPart[j] && !i.getFracDigit(j)) {
				return 1;
			}
			j++;
		}
		return (i.getLength() > j ? -1 : 0);
	}
	
	// simple override of equals
	public boolean equals(PreciseNumber i) {
		return compareTo(i) == 0;
	}
	
	// return a double approximation of the number
	public double approxValue() {
		return x;
	}
	
	// return a string for the approximate value of the number (to double precision)
	// also adds a '~' to show the potential inaccuracy
	public String toString(){
		return "~" + x;
	}
	
	// Subtract 2 precise numbers
	public static PreciseNumber subtract(PreciseNumber i1, PreciseNumber i2){
		int resLength = Math.max(i1.getLength(), i2.getLength());
		if(resLength == 0) {
			return new PreciseNumber(i1.intPart-i2.intPart);
		}
		boolean[] f = null;
		boolean carry = false;
		for(int i = resLength-1; i >= 0; i--) {
			boolean res = i1.getFracDigit(i) ^ i2.getFracDigit(i) ^ carry;
			if(f==null && res){
				f = new boolean[i+1];
			}
			if(f != null) {
				f[i] = res;
			}

			carry = (i2.getFracDigit(i) && carry) || (!i1.getFracDigit(i) && (i2.getFracDigit(i) ^ carry));
		}
		if(carry) {
			return new PreciseNumber(i1.intPart-i2.intPart-1, f);
		} else {
			return new PreciseNumber(i1.intPart-i2.intPart, f);
		}
	}
}
