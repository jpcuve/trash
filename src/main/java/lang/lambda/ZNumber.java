package lang.lambda;

public abstract class ZNumber extends ZExpression {
	
	public static ZNumber ZERO = new ZInteger(0);
	public static ZNumber ONE = new ZInteger(1);
	
	private boolean m_exact;

	public boolean isBoolean() { return false; }
	public boolean isSymbol() { return false; }
	public boolean isChar() { return false; }
	public boolean isVector() { return false; }
	public boolean isProcedure() { return false; }
	public boolean isPair() { return false; }
	public boolean isNumber() { return true; }
	public boolean isString() { return false; }
	public boolean isPort() { return false; }
	public boolean isLink() { return false; }
	
	public boolean isList(){ return false; }

	public boolean isEqv(ZExpression z){
		if(!z.isNumber()) return false;
		try{
			ZNumber n = (ZNumber)z;
			return (this.compareTo(n) == 0 && m_exact == n.isExact());
		}catch(ZException ex){
		}
		return false;
	}

	public abstract boolean isComplex();
	public abstract boolean isReal();
	public abstract boolean isRational();
	public abstract boolean isInteger();

    public abstract long toLong();

    public static ZNumber parse(String s){
		if(s.indexOf(".") == -1) return new ZInteger(s);
		else return new ZReal(s);
	}
	
	public ZReal toReal() throws ZException {
		if(!this.isReal()) throw new ZException(ZError.NUMBER_REAL_EXPECTED, this);
		return (ZReal)this;
	}
	
	public ZInteger toInteger() throws ZException {
		if(!this.isInteger()) throw new ZException(ZError.NUMBER_INTEGER_EXPECTED, this);
		return (ZInteger)this;
	}
	
	public abstract int compareTo(ZNumber z) throws ZException ;
	public abstract ZNumber monOp(char op) throws ZException ;
	public abstract ZNumber binOp(char op, ZNumber z) throws ZException ;

    public ZNumber add(ZNumber z) throws ZException {
        return binOp('+', z);
    }

    public ZNumber sub(ZNumber z) throws ZException {
        return binOp('-', z);
    }

    public ZNumber mul(ZNumber z) throws ZException {
        return binOp('*', z);
    }

    public ZNumber div(ZNumber z) throws ZException {
        return binOp('/', z);
    }

    public ZNumber(boolean exact){
		if(exact) this.setExact();
		else this.clearExact();
	}
	
	public void setExact(){
		m_exact = true;
	}
	
	public void clearExact(){
		m_exact = false;
	}
	
	public boolean isExact(){
		return m_exact;
	}

	public String toString(){
		return "[ZNu=]";
	}
	

}
