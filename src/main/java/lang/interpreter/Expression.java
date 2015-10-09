package lang.interpreter;

public abstract class Expression {

	public boolean isEq(Expression z){	return this == z; }
	public boolean isEqv(Expression z){ return this.isEq(z); }
	public boolean isEqual(Expression z){ return this.isEqv(z); }

	public abstract String write();

	public Expression cons(Expression z){
		return new Pair(this, z);
	}

	public Expression append(Expression z) throws InterpreterException {
		if(z == Nil.NIL) return this;
		throw new InterpreterException(Error.APPEND_TO_ATOM, this);
	}

	public Expression first() throws InterpreterException {
		throw new InterpreterException(Error.PAIR_EXPECTED, this);
	}

	public Expression rest() throws InterpreterException {
		throw new InterpreterException(Error.PAIR_EXPECTED, this);
	}

	public Expression second() throws InterpreterException{
		return this.rest().first();
	}

	public Expression third() throws InterpreterException{
		return this.rest().rest().first();
	}

}