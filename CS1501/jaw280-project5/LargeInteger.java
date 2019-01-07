import java.io.Serializable;
import java.util.Random;
import java.math.BigInteger;

public class LargeInteger implements Serializable {
	
	private final byte[] ONE = {(byte) 1};
	private final byte[] ZERO = {(byte) 0};
	private final byte MAX = (byte) 0b11111111;


	private byte[] val;

	/**
	 * Construct the LargeInteger from a given byte array
	 * @param b the byte array that this LargeInteger should represent
	 */
	public LargeInteger(byte[] b) {
		val = b;
	}

	/**
	 * Construct the LargeInteger by generatin a random n-bit number that is
	 * probably prime (2^-100 chance of being composite).
	 * @param n the bitlength of the requested integer
	 * @param rnd instance of java.util.Random to use in prime generation
	 */
	public LargeInteger(int n, Random rnd) {
		val = BigInteger.probablePrime(n, rnd).toByteArray();
	}
	
	/**
	 * Return this LargeInteger's val
	 * @return val
	 */
	public byte[] getVal() {
		return val;
	}

	/**
	 * Return the number of bytes in val
	 * @return length of the val byte array
	 */
	public int length() {
		return val.length;
	}

	/** 
	 * Add a new byte as the most significant in this
	 * @param extension the byte to place as most significant
	 */
	public void extend(byte extension) {
		byte[] newv = new byte[val.length + 1];
		newv[0] = extension;
		for (int i = 0; i < val.length; i++) {
			newv[i + 1] = val[i];
		}
		val = newv;
	}

	/**
	 * If this is negative, most significant bit will be 1 meaning most 
	 * significant byte will be a negative signed number
	 * @return true if this is negative, false if positive
	 */
	public boolean isNegative() {
		return (val[0] < 0);
	}

	/**
	 * Computes the sum of this and other
	 * @param other the other LargeInteger to sum with this
	 */
	public LargeInteger add(LargeInteger other) {
		byte[] a, b;
		// If operands are of different sizes, put larger first ...
		if (val.length < other.length()) {
			a = other.getVal();
			b = val;
		}
		else {
			a = val;
			b = other.getVal();
		}

		// ... and normalize size for convenience
		if (b.length < a.length) {
			int diff = a.length - b.length;

			byte pad = (byte) 0;
			if (b[0] < 0) {
				pad = (byte) 0xFF;
			}

			byte[] newb = new byte[a.length];
			for (int i = 0; i < diff; i++) {
				newb[i] = pad;
			}

			for (int i = 0; i < b.length; i++) {
				newb[i + diff] = b[i];
			}

			b = newb;
		}

		// Actually compute the add
		int carry = 0;
		byte[] res = new byte[a.length];
		for (int i = a.length - 1; i >= 0; i--) {
			// Be sure to bitmask so that cast of negative bytes does not
			//  introduce spurious 1 bits into result of cast
			carry = ((int) a[i] & 0xFF) + ((int) b[i] & 0xFF) + carry;

			// Assign to next byte
			res[i] = (byte) (carry & 0xFF);

			// Carry remainder over to next byte (always want to shift in 0s)
			carry = carry >>> 8;
		}

		LargeInteger res_li = new LargeInteger(res);
	
		// If both operands are positive, magnitude could increase as a result
		//  of addition
		if (!this.isNegative() && !other.isNegative()) {
			// If we have either a leftover carry value or we used the last
			//  bit in the most significant byte, we need to extend the result
			if (res_li.isNegative()) {
				res_li.extend((byte) carry);
			}
		}
		// Magnitude could also increase if both operands are negative
		else if (this.isNegative() && other.isNegative()) {
			if (!res_li.isNegative()) {
				res_li.extend((byte) 0xFF);
			}
		}

		// Note that result will always be the same size as biggest input
		//  (e.g., -127 + 128 will use 2 bytes to store the result value 1)
		return res_li;
	}

	/**
	 * Negate val using two's complement representation
	 * @return negation of this
	 */
	public LargeInteger negate() {
		byte[] neg = new byte[val.length];
		int offset = 0;

		// Check to ensure we can represent negation in same length
		//  (e.g., -128 can be represented in 8 bits using two's 
		//  complement, +128 requires 9)
		if (val[0] == (byte) 0x80) { // 0x80 is 10000000
			boolean needs_ex = true;
			for (int i = 1; i < val.length; i++) {
				if (val[i] != (byte) 0) {
					needs_ex = false;
					break;
				}
			}
			// if first byte is 0x80 and all others are 0, must extend
			if (needs_ex) {
				neg = new byte[val.length + 1];
				neg[0] = (byte) 0;
				offset = 1;
			}
		}

		// flip all bits
		for (int i  = 0; i < val.length; i++) {
			neg[i + offset] = (byte) ~val[i];
		}

		LargeInteger neg_li = new LargeInteger(neg);
	
		// add 1 to complete two's complement negation
		return neg_li.add(new LargeInteger(ONE));
	}

	/**
	 * Implement subtraction as simply negation and addition
	 * @param other LargeInteger to subtract from this
	 * @return difference of this and other
	 */
	public LargeInteger subtract(LargeInteger other) {
		return this.add(other.negate());
	}

	/**
	 * Compute the product of this and other
	 * @param other LargeInteger to multiply by this
	 * @return product of this and other
	 */
	public LargeInteger multiply(LargeInteger other) {
		LargeInteger a1,b1;
//		byte[] a = signExtend(this).getVal();
//		byte[] b = signExtend(other).getVal();
		int productSign = this.isNegative() && other.isNegative() || !this.isNegative() && !other.isNegative() ? 0 : 1;

		if(this.isZero() || other.isZero()) return new LargeInteger(ZERO);

		if (val.length < other.length()) {
			a1 = other;
			b1 = this;
		}
		else {
			a1 = this;
			b1 = other;
		}

		boolean resultSign = (a1.isNegative() && !b1.isNegative()) || (!a1.isNegative() && b1.isNegative());

		if(a1.isNegative() && b1.isNegative()) {
			a1 = a1.negate();
			b1 = b1.negate();
		} else if(a1.isNegative() && !b1.isNegative()) {
			a1 = a1.negate();
		} else if(!a1.isNegative() && b1.isNegative()) {
			b1 = b1.negate();
		}

		byte[] a = a1.getVal();
		byte[] b = b1.getVal();
		LargeInteger partials = new LargeInteger(ZERO);
		LargeInteger shift = new LargeInteger(a);

		for(int i = b.length-1; i >= 0; i--){
			for(int j =0; j<8; j++){
				int currentBit = b[i] >> j & 1;
				if(currentBit == 1){
					partials = partials.add(shift);
				}

				shift = leftShift(shift.getVal(),1,0);
			}
		}

		if(resultSign) partials = partials.negate();

		int aLength = this.length()*8 - findPadding(this);
		int bLength = other.length()*8 - findPadding(other);
		int prodLength = (int)Math.ceil((aLength + bLength) / 8.0);
		byte[] product = new byte[prodLength];
		System.arraycopy(partials.getVal(),partials.length()-product.length,product,0,product.length);

		LargeInteger result = new LargeInteger(product);
		int msb = product[0] >> 7 & 1;
		if(productSign !=  msb){
			if(productSign == 0) result.extend((byte) 0);
			else result.extend(MAX);
		}

		return result;
	}

	public int compareTo(LargeInteger other){
		LargeInteger a = this.trim();
		LargeInteger b = other.trim();
		if(a.isNegative() && !b.isNegative()) return -1;
		if(!a.isNegative() && b.isNegative()) return 1;

		int negativeIndex = a.isNegative() ? -1 : 1;

		if(a.length() > b.length()) return negativeIndex;
		if(a.length() < b.length()) return -1 * negativeIndex;

		byte[] aval = a.isNegative() ? a.negate().getVal() : a.getVal();
		byte[] bval = a.isNegative() ? b.negate().getVal() : b.getVal();

		for(int i=0; i <a.length(); i++){
			if((val[i] & 0xFF) < (bval[i] & 0xFF)){
				return -1 * negativeIndex;
			} else if ((aval[i] & 0xFF) > (bval[i] & 0xFF)){
				return negativeIndex;
			}
		}

		return 0;
	}

	public LargeInteger trim(){
		int pad = findPadding(this)-1;
		int amount = pad/8;
		if(pad > 8){
			byte[] temp = new byte[val.length-amount];
			System.arraycopy(val,amount,temp,0,temp.length);
			return new LargeInteger(temp);
		}

		return this;
	}

	private int findPadding(LargeInteger num){
		int padding = 0;
		byte[] a = num.getVal();

		int sign = num.isNegative() ? 1 : 0;

		for(int i=0; i<num.length(); i++){
			for(int j=7; j>=0; j--){
				if((a[i] >> j & 1) != sign) return padding;
				padding++;
			}
		}

		return padding;
	}

	private LargeInteger signExtend(LargeInteger num){
		byte[] temp = new byte[num.length()*2];
		if(num.isNegative()){
			for(int i=0; i<num.length(); i++){
				temp[i] = MAX;
			}
		} else {
			for(int i=0; i<num.length(); i++){
				temp[i] = (byte) 0;
			}
		}
		System.arraycopy(num.getVal(),0,temp,num.length(),num.length());
		return new LargeInteger(temp);
	}

	private LargeInteger leftShift(byte[] arr, int count, int num){
		byte[] shifted = new byte[arr.length];


		int memory = num;
		for(int j=0; j<count; j++){
			boolean shouldExtend = findPadding(new LargeInteger(arr)) <= 1;
			boolean isZero = (arr[0] >> 7 & 1) == 0;

			for(int i = shifted.length-1; i >= 0; i--){
				int msb = (arr[i] & 0xFF) >> 7;
				shifted[i] = (byte)((arr[i] << 1) & 0b11111110 | memory);
				memory = msb;
			}

			if(shouldExtend){
				byte[] temp = new byte[shifted.length+1];
				System.arraycopy(shifted,0,temp,1, shifted.length);
				temp[0] = isZero ? (byte)0 : (byte)0xFF;
				shifted = temp;
			}

			arr = shifted;
			memory = num;
		}


		return new LargeInteger(shifted);
	}

	private LargeInteger rightShift(byte[] arr){
		byte[] shifted = new byte[arr.length];

		int memory = arr[0] & 1;
		shifted[0] = (byte)(arr[0] >> 1);
		for(int i=1; i<shifted.length;i++){
			int lsb = arr[i] & 1;
			byte mask = memory == 1 ? (byte) 0b10000000 : (byte) 0;
			shifted[i] = (byte)(arr[i] >> 1 & 0b01111111 | mask);
			memory = lsb;
		}

		return new LargeInteger(shifted);
	}
	
	/**
	 * Run the extended Euclidean algorithm on this and other
	 * @param other another LargeInteger
	 * @return an array structured as follows:
	 *   0:  the GCD of this and other
	 *   1:  a valid x value
	 *   2:  a valid y value
	 * such that this * x + other * y == GCD in index 0
	 */
	 public LargeInteger[] XGCD(LargeInteger other) {
	 	if(other.isZero()){
	 		return new LargeInteger[] {this, new LargeInteger(ONE), new LargeInteger(ZERO)};
		}

		LargeInteger[] vals = other.XGCD(this.divide(other)[1]);
	 	LargeInteger d = vals[0];
	 	LargeInteger a = vals[2];
	 	LargeInteger b = vals[1].subtract(this.divide(other)[0].multiply(vals[2]));

	 	return new LargeInteger[] {d,a,b};
	 }

	 public boolean isZero(){
	 	for(byte b : val){
	 		if (b!=0) return false;
		}
		return true;
	 }

	 public LargeInteger[] divide(LargeInteger other){
//	 	if(this.subtract(other).isNegative()) return new LargeInteger[] {new LargeInteger(ZERO),this};
//		LargeInteger remainder = this.isNegative() ? this.negate() : this;
//		LargeInteger b = other.isNegative() ? other.negate() : other;
		LargeInteger dividend = this;
		LargeInteger divisor = other;
		boolean quotientSign = (this.isNegative() && !divisor.isNegative()) || (!this.isNegative() && divisor.isNegative());
		boolean remainderSign = dividend.isNegative();
		if(divisor.isNegative()) divisor = divisor.negate();
		if(dividend.isNegative()) dividend = dividend.negate();

		if(dividend.compareTo(divisor) == 0) {
			LargeInteger temp = new LargeInteger(ONE);
			if(quotientSign) temp = temp.negate();
			return new LargeInteger[] { temp, new LargeInteger(ZERO)};
		}

		if(dividend.compareTo(divisor) < 0) {
			if(remainderSign) dividend = dividend.negate();
			return new LargeInteger[] { new LargeInteger(ZERO), dividend};
		}


	 	LargeInteger remainder = dividend;
		int shiftIndex = remainder.length()*8-1;
		divisor = leftShift(divisor.getVal(), shiftIndex,0);
		int i = 0;
		LargeInteger quotient = new LargeInteger(ZERO);

		while(i <= shiftIndex){
			LargeInteger diff = remainder.subtract(divisor);
			if(diff.isNegative()){
				quotient = quotient.leftShift(quotient.getVal(),1,0);
			} else {
				quotient = quotient.leftShift(quotient.getVal(),1,1);
				remainder = diff;
			}
			divisor = divisor.rightShift(divisor.getVal());
			i++;
		}


		quotient = quotient.trim();
		remainder = remainder.trim();
//		int remainderLength = (int)Math.ceil((remainder.length()*8 - findPadding(remainder)) / 8.0);
//		byte[] temp = new byte[remainderLength];
//		System.arraycopy(remainder.getVal(), remainder.length()-temp.length, temp, 0, temp.length);
//		remainder = new LargeInteger(temp);

		 if(remainderSign && !remainder.isZero()) remainder = remainder.negate();
		 if(quotientSign && !quotient.isZero()) quotient = quotient.negate();

		return new LargeInteger[] {quotient, remainder};
	 }

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(byte b : val) {
			String s1 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
			sb.append(s1+"");
		}
		return sb.toString();
	}

	 /**
	  * Compute the result of raising this to the power of y mod n
	  * @param y exponent to raise this to
	  * @param n modulus value to use
	  * @return this^y mod n
	  */
	 public LargeInteger modularExp(LargeInteger y, LargeInteger n) {
		byte[] exp = y.getVal();
		LargeInteger ans = new LargeInteger(ONE);
		for(byte b : exp){
			for(int i=7; i>=0; i--){
				ans = ans.multiply(ans).divide(n)[1];
				if((b >> i & 1) == 1){
					ans = ans.multiply(this).divide(n)[1];
				}
			}
		}

		return ans;
	 }
}
