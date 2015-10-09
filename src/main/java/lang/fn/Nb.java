package lang.fn;

import java.math.BigInteger;

public abstract class Nb extends Expression {
	public static Nb ZERO = new Int(BigInteger.ZERO);
	public static Nb ONE = new Int(BigInteger.ONE);
	
	public boolean isBoolean() { return false; }
	public boolean isSymbol() { return false; }
	public boolean isChar() { return false; }
	public boolean isVector() { return false; }
	public boolean isProcedure() { return false; }
	public boolean isPair() { return false; }
	public boolean isNumber() { return true; }
	public boolean isString() { return false; }
	public boolean isPort() { return false; }
	public boolean isPromise() { return false; }
	public boolean isLink() { return false; }
	public boolean isEOF() { return false; }
	
	public boolean isList(){ return false; }

	public boolean isEqv(Expression z){
		if(!z.isNumber()) return false;
		try{
			Nb n = (Nb)z;
			return(this.compareTo(n) == 0);
		}catch(FnException ex){
		}
		return false;
	}

	public abstract boolean isReal();
	public abstract boolean isInteger();
	
	public Real toReal() throws FnException {
		if(!this.isReal()) throw new FnException(Error.NUMBER_REAL_EXPECTED, this);
		return (Real)this;
	}

	public Int toInteger() throws FnException {
		if(!this.isInteger()) throw new FnException(Error.NUMBER_INTEGER_EXPECTED, this);
		return (Int)this;
	}
	
	public abstract int compareTo(Nb z) throws FnException ;
	public abstract Nb monOp(char op) throws FnException ;
	public abstract Nb binOp(char op, Nb z) throws FnException ;
		
	public String toString(){
		return "[Nb=]";
	}
	
	
}