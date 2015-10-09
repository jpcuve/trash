package lang.lisp;

import java.math.BigDecimal;
import java.math.BigInteger;

public class LInteger extends LNumber{
	public BigInteger value;
	
	public LInteger(String s){
		this.value = new BigInteger(s);
	}
	
	public LInteger(BigInteger i){
		this.value = i;
	}
		
	// lisp constructs
	
	public boolean integerp(){ return true;	}
	public boolean floatp(){ return false; }
	
	public boolean eql(LExpression l){
		if(this.eq(l)) return true;
		if(!l.numberp()) return false;
		if(!((LNumber)l).integerp()) return false;
		return (this.compareTo((LNumber)l) == 0);
	}
	
	// math operations
	
	public LInteger rem(LInteger i){
		BigInteger[] d = this.value.divideAndRemainder(i.value);
		return new LInteger(d[1]);
	}
	
	public int compareTo(LNumber n){
		if(n.integerp()){
			LInteger i = (LInteger)n;
			return this.value.compareTo(i.value);
		}
		if(n.floatp()){
			LFloatingPoint f = (LFloatingPoint)n;
			return (new BigDecimal(this.value)).compareTo(f.value);
		}
		return 0;
	}
	
	public int signum(){
		return this.value.signum();
	}
	
	public LNumber random(){
		return this.floaT().random().round();
	}
	
	public LNumber max(LNumber n){
		if(n.integerp()){
			LInteger i = (LInteger)n;
			return (this.value.max(i.value) == this.value) ? (LNumber)this : (LNumber)i;
		}
		if(n.floatp()){
			LFloatingPoint f = (LFloatingPoint)n;
			return (f.value.max(new BigDecimal(this.value)) == f.value) ? (LNumber)f : (LNumber)this;
		}
		return null;
	}
	
	public LNumber min(LNumber n){
		if(n.integerp()){
			LInteger i = (LInteger)n;
			return (this.value.min(i.value) == this.value) ? (LNumber)this : (LNumber)i;
		}
		if(n.floatp()){
			LFloatingPoint f = (LFloatingPoint)n;
			return (f.value.min(new BigDecimal(this.value)) == f.value) ? (LNumber)f : (LNumber)this;
		}
		return null;
	}
	
	
	public LNumber floaT(){
		return new LFloatingPoint(new BigDecimal(this.value));
	}
	
	public LNumber round(){
		return new LInteger(this.value);
	}
	
	public LNumber add(LNumber n){
		if(n.integerp()) return new LInteger(this.value.add(((LInteger)n).value));
		if(n.floatp()) return new LFloatingPoint(new BigDecimal(this.value).add(((LFloatingPoint)n).value));
		return null;
	}
	
	public LNumber sub(LNumber n){
		if(n.integerp()) return new LInteger(this.value.subtract(((LInteger)n).value));
		if(n.floatp()) return new LFloatingPoint(new BigDecimal(this.value).subtract(((LFloatingPoint)n).value));
		return null;
	}
	
	public LNumber mul(LNumber n){
		if(n.integerp()) return new LInteger(this.value.multiply(((LInteger)n).value));
		if(n.floatp()) return new LFloatingPoint(new BigDecimal(this.value).multiply(((LFloatingPoint)n).value));
		return null;
	}
	
	public LNumber div(LNumber n){
		if(n.integerp()){
			LInteger i = (LInteger)n;
			BigInteger[] d = this.value.divideAndRemainder(i.value);
			return (d[1].signum() == 0) ? (LNumber)new LInteger(d[0]) : (LNumber)new LFloatingPoint(new BigDecimal(this.value).divide(new BigDecimal(i.value), 64, BigDecimal.ROUND_HALF_UP));
		}
		if(n.floatp()){
			LFloatingPoint f = (LFloatingPoint)n;
			return new LFloatingPoint(new BigDecimal(this.value).divide(f.value, LFloatingPoint.MANTISSA_SIZE, BigDecimal.ROUND_HALF_UP));
		}
		return null;
	}
	
	
	// JAVA support
	
	public String toString(){
		return this.value.toString();
	}

}
