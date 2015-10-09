package lang.fn;

import java.math.BigDecimal;

public class Real extends Nb {
	private BigDecimal value;
	
	public boolean isReal(){ return true; }
	public boolean isInteger(){ return false; }
	
	public Real(BigDecimal d){
		value = d;
	}
	
	public Real(double d) {
		value = new BigDecimal(d);
	}
	
	public int compareTo(Nb z) throws FnException {	return value.compareTo(z.toReal().value);	}
	
	public Nb monOp(char op) throws FnException {
		switch(op){
			case 'A': return(new Real(value.abs()));
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
		BigDecimal arg = z.toReal().value;
		switch(op){
			case '+': return(new Real(value.add(arg)));
			case '-': return(new Real(value.subtract(arg)));
			case '*': return(new Real(value.multiply(arg)));
			case '/': return(new Real(value.divide(arg, BigDecimal.ROUND_UP)));
			case 'X': return(new Real(value.max(arg)));
			case 'M': return(new Real(value.min(arg)));
		}
		throw new FnException(Error.UNIMPLEMENTED_FEATURE, this);
	}
	
	public String write(){
		return "" + value;
	}
	
	public String toString(){
		return "[Re=" + value + "]";
	}
	
	public double doubleValue() {
		return(value.doubleValue());
	}
	
}