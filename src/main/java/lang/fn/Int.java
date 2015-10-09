package lang.fn;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Int extends Nb {
	private BigInteger value;
	
	public boolean isReal(){ return false; }
	public boolean isInteger(){ return true; }
	
	public Int(BigInteger i){
		value = i;
	}
	
	public Int(int i) {
		value = new BigInteger(Integer.toString(i));
	}
		
	public Int(long l) {
		value = new BigInteger(Long.toString(l));
	}
		
	public int compareTo(Nb z) throws FnException {
		return value.compareTo(z.toInteger().value);
	}
	
	public Nb monOp(char op) throws FnException {
		switch(op){
			case 'A': return(new Int(value.abs()));
			case 'R': return(new Real(Math.sqrt(value.doubleValue())));
			case 'F': return(new Real(Math.floor(value.doubleValue())));
			case 'G': return(new Real(Math.ceil(value.doubleValue())));
			case 'U': return(new Real(Math.rint(value.doubleValue())));
			case 'O': return(new Real(Math.round(value.doubleValue())));
			case 'E': return(new Real(Math.exp(value.doubleValue())));
			case 'L': return(new Real(Math.log(value.doubleValue())));
			case 'S': return(new Real(Math.sin(value.doubleValue())));
			case 'C': return(new Real(Math.cos(value.doubleValue())));
			case 'T': return(new Real(Math.tan(value.doubleValue())));
			case 's': return(new Real(Math.asin(value.doubleValue())));
			case 'c': return(new Real(Math.acos(value.doubleValue())));
			case 't': return(new Real(Math.atan(value.doubleValue())));	
		}
		throw new FnException(Error.UNIMPLEMENTED_FEATURE, this);
	}

	public Nb binOp(char op, Nb z) throws FnException {
		if(!z.isInteger()) return toReal().binOp(op, z);
		BigInteger arg = z.toInteger().value;
		switch(op){
			case '+': return(new Int(value.add(arg)));
			case '-': return(new Int(value.subtract(arg)));
			case '*': return(new Int(value.multiply(arg)));
			case '/': if(value.mod(arg).compareTo(BigInteger.ZERO) == 0) return(new Int(value.divide(arg)));
		    	      else return toReal().binOp('/', z.toReal());
			case 'X': return(new Int(value.max(arg)));
			case 'M': return(new Int(value.min(arg)));
			case 'Q': return(new Int(value.divide(arg)));
			case 'R': return(new Int(value.mod(arg)));
			case 'O': return(new Int(value.mod(arg)));
		}
		throw new FnException(Error.UNIMPLEMENTED_FEATURE, this);
	}

	public String write(){
		return(value.toString());
	}

	public Real toReal(){
		return new Real(new BigDecimal(value));
	}

	public long toNatural() throws FnException {
		if(value.compareTo(BigInteger.ZERO) < 0) throw new FnException(Error.NUMBER_INTEGER_NATURAL_EXPECTED, this);
		return value.longValue();
	}

	public String toString(){
		return "[In=" + value + "]";
	}
	
	public int intValue() {
		return(value.intValue());
	}
	
	public long longValue() {
		return(value.longValue());
	}
	
}