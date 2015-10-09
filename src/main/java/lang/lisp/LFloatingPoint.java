package lang.lisp;

import java.math.BigDecimal;

public class LFloatingPoint extends LNumber{
	public BigDecimal value;
	
	public static int MANTISSA_SIZE = 64;
	
	public LFloatingPoint(String s){
		this.value = new BigDecimal(s);
	}
	
	public LFloatingPoint(BigDecimal f){
		this.value = f;
	}
	
	public boolean integerp(){ return false; }
	public boolean floatp(){ return true; }

	public boolean eql(LExpression l){
		if(this.eq(l)) return true;
		if(!l.numberp()) return false;
		if(!((LNumber)l).floatp()) return false;
		return (this.compareTo((LNumber)l) == 0);
	}
	

	// math operations
	
	public int compareTo(LNumber n){
		if(n.floatp()){
			LFloatingPoint f = (LFloatingPoint)n;
			return this.value.compareTo(f.value);
		}
		if(n.integerp()){
			LInteger i = (LInteger)n;
			return this.value.compareTo(new BigDecimal(i.value));
		}
		return 0;
	}
	
	public int signum(){
		return this.value.signum();
	}
	
	public LNumber random(){
		LFloatingPoint f = new LFloatingPoint(new BigDecimal(Math.random()));
		return f.mul(this);
	}
	
	public LNumber max(LNumber n){
		if(n.floatp()){
			LFloatingPoint f = (LFloatingPoint)n;
			return (this.value.max(f.value) == this.value) ? (LNumber)this : (LNumber)f;
		}
		if(n.integerp()){
			LInteger i = (LInteger)n;
			return (this.value.max(new BigDecimal(i.value)) == this.value) ? (LNumber)this : (LNumber)i;
		}
		return null;
	}
	
	public LNumber min(LNumber n){
		if(n.floatp()){
			LFloatingPoint f = (LFloatingPoint)n;
			return (this.value.min(f.value) == this.value) ? (LNumber)this : (LNumber)f;
		}
		if(n.integerp()){
			LInteger i = (LInteger)n;
			return (this.value.min(new BigDecimal(i.value)) == this.value) ? (LNumber)this : (LNumber)i;
		}
		return null;
	}
	
	public LNumber floaT(){
		return new LFloatingPoint(this.value);
	}
	
	public LNumber round(){
		return new LInteger(this.value.toBigInteger());
	}
	
	public LNumber add(LNumber n){
		if(n.integerp()) return new LFloatingPoint(this.value.add(new BigDecimal(((LInteger)n).value)));
		if(n.floatp()) return new LFloatingPoint(this.value.add(((LFloatingPoint)n).value));
		return null;
	}
	
	public LNumber sub(LNumber n){
		if(n.integerp()) return new LFloatingPoint(this.value.subtract(new BigDecimal(((LInteger)n).value)));
		if(n.floatp()) return new LFloatingPoint(this.value.subtract(((LFloatingPoint)n).value));
		return null;
	}
	
	public LNumber mul(LNumber n){
		if(n.integerp()) return new LFloatingPoint(this.value.multiply(new BigDecimal(((LInteger)n).value)));
		if(n.floatp()) return new LFloatingPoint(this.value.multiply(((LFloatingPoint)n).value));
		return null;
	}
	
	public LNumber div(LNumber n){
		if(n.integerp()){
			LInteger i = (LInteger)n;
			return new LFloatingPoint(this.value.divide(new BigDecimal(i.value), MANTISSA_SIZE, BigDecimal.ROUND_HALF_UP ));
		}
		if(n.floatp()){
			LFloatingPoint f = (LFloatingPoint)n;
			return new LFloatingPoint(this.value.divide(f.value, MANTISSA_SIZE, BigDecimal.ROUND_HALF_UP ));
		}
		return null;
	}
	
	// JAVA support
	
	public String toString(){
		return this.value.toString();
	}

}
