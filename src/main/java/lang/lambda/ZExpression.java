package lang.lambda;

public abstract class ZExpression implements Cloneable {
	
	public abstract boolean isBoolean();
	public abstract boolean isSymbol();
	public abstract boolean isChar();
	public abstract boolean isVector();
	public abstract boolean isProcedure();
	public abstract boolean isPair();
	public abstract boolean isNumber();
	public abstract boolean isString();
	public abstract boolean isPort();
	public abstract boolean isLink();
	
	public abstract boolean isList();
	
	public boolean isEq(ZExpression z){	return this == z; }
	public boolean isEqv(ZExpression z){ return this.isEq(z); }
	public boolean isEqual(ZExpression z){ return this.isEqv(z); }
													
	public abstract String write();
	
	public ZPair cons(ZExpression z){
		return new ZPair(this, z);
	}
	
	public ZExpression append(ZExpression z) throws ZException {
		if(z == ZEmpty.EMPTY) return this;
		throw new ZException(ZError.APPEND_TO_ATOM, this);
	}
	
	public ZExpression first() throws ZException {
		throw new ZException(ZError.PAIR_EXPECTED, this);
	}
	
	public ZExpression rest() throws ZException {
		throw new ZException(ZError.PAIR_EXPECTED, this);
	}
	
	public ZExpression second() throws ZException{
		return this.rest().first();
	}
	
	public ZExpression third() throws ZException{
		return this.rest().rest().first();
	}
	
	public ZBoolean toBoolean() throws ZException{
		if(!this.isBoolean()) throw new ZException(ZError.BOOLEAN_EXPECTED, this);
		return (ZBoolean)this;
	}

	public ZSymbol toSymbol() throws ZException{
		if(!this.isSymbol()) throw new ZException(ZError.SYMBOL_EXPECTED, this);
		return (ZSymbol)this;
	}
	
	public ZChar toChar() throws ZException{
		if(!this.isChar()) throw new ZException(ZError.CHAR_EXPECTED, this);
		return (ZChar)this;
	}
	
	public ZVector toVector() throws ZException{
		if(!this.isVector()) throw new ZException(ZError.VECTOR_EXPECTED, this);
		return (ZVector)this;
	}
	
	public ZProcedure toProcedure() throws ZException{
		if(!this.isProcedure()) throw new ZException(ZError.PROCEDURE_EXPECTED, this);
		return (ZProcedure)this;
	}
	
	public ZPair toPair() throws ZException{
		if(!this.isPair()) throw new ZException(ZError.PAIR_EXPECTED, this);
		return (ZPair)this;
	}
	
	public ZPair toList() throws ZException{
		if(!this.isList()) throw new ZException(ZError.LIST_EXPECTED, this);
		return (ZPair)this;
	}
	
	public ZNumber toNumber() throws ZException{
		if(!this.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, this);
		return (ZNumber)this;
	}
	
	public ZString toString_() throws ZException{
		if(!this.isString()) throw new ZException(ZError.STRING_EXPECTED, this);
		return (ZString)this;
	}
	
	public ZPort toPort() throws ZException{
		if(!this.isPort()) throw new ZException(ZError.PORT_EXPECTED, this);
		return (ZPort)this;
	}
	
	// JAVA support
	
	public Object clone(){
		ZExpression z = null;
		try{
			z = (ZExpression)super.clone();
		}catch(CloneNotSupportedException ex){
			ex.printStackTrace();
		}
		return z;
	}

}
