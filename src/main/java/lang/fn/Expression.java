package lang.fn;

public abstract class Expression {
	public abstract boolean isBoolean();
	public abstract boolean isSymbol();
	public abstract boolean isChar();
	public abstract boolean isVector();
	public abstract boolean isProcedure();
	public abstract boolean isPair();
	public abstract boolean isNumber();
	public abstract boolean isString();
	public abstract boolean isPort();
	public abstract boolean isPromise();
	public abstract boolean isLink();
	public abstract boolean isEOF();

	public abstract boolean isList();

	public boolean isEq(Expression z){	return this == z; }
	public boolean isEqv(Expression z){ return this.isEq(z); }
	public boolean isEqual(Expression z){ return this.isEqv(z); }

	public abstract String write();

	public Expression cons(Expression z){
		return new Pair(this, z);
	}

	public Expression append(Expression z) throws FnException {
		if(z == Empty.EMPTY) return this;
		throw new FnException(Error.APPEND_TO_ATOM, this);
	}

	public Expression first() throws FnException {
		throw new FnException(Error.PAIR_EXPECTED, this);
	}

	public Expression rest() throws FnException {
		throw new FnException(Error.PAIR_EXPECTED, this);
	}

	public Expression second() throws FnException{
		return this.rest().first();
	}

	public Expression third() throws FnException{
		return this.rest().rest().first();
	}

	public Bool toBoolean() throws FnException{
		if(!this.isBoolean()) throw new FnException(Error.BOOLEAN_EXPECTED, this);
		return (Bool)this;
	}

	public Symbol toSymbol() throws FnException{
		if(!this.isSymbol()) throw new FnException(Error.SYMBOL_EXPECTED, this);
		return (Symbol)this;
	}

	public Char toChar() throws FnException{
		if(!this.isChar()) throw new FnException(Error.CHAR_EXPECTED, this);
		return (Char)this;
	}

	public Vec toVector() throws FnException{
		if(!this.isVector()) throw new FnException(Error.VECTOR_EXPECTED, this);
		return (Vec)this;
	}

	public Procedure toProcedure() throws FnException{
		if(!this.isProcedure()) throw new FnException(Error.PROCEDURE_EXPECTED, this);
		return (Procedure)this;
	}

	public Pair toPair() throws FnException{
		if(!this.isPair()) throw new FnException(Error.PAIR_EXPECTED, this);
		return (Pair)this;
	}

	public List toList() throws FnException{
		if(!this.isList()) throw new FnException(Error.LIST_EXPECTED, this);
		return (List)this;
	}

	public Nb toNumber() throws FnException{
		if(!this.isNumber()) throw new FnException(Error.NUMBER_EXPECTED, this);
		return (Nb)this;
	}

	public Str toString_() throws FnException{
		if(!this.isString()) throw new FnException(Error.STRING_EXPECTED, this);
		return (Str)this;
	}

	public Port toPort() throws FnException{
		if(!this.isPort()) throw new FnException(Error.PORT_EXPECTED, this);
		return (Port)this;
	}

	public Promise toPromise() throws FnException{
		if(!this.isPromise()) throw new FnException(Error.PROMISE_EXPECTED, this);
		return (Promise)this;
	}

/*
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
*/	
}