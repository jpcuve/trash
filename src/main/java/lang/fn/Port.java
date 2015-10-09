package lang.fn;

public abstract class Port extends Expression {
	public boolean isBoolean() { return false; }
	public boolean isSymbol() { return false; }
	public boolean isChar() { return false; }
	public boolean isVector() { return false; }
	public boolean isProcedure() { return false; }
	public boolean isPair() { return false; }
	public boolean isNumber() { return false; }
	public boolean isString() { return false; }
	public boolean isPort() { return true; }
	public boolean isPromise() { return false; }
	public boolean isLink() { return false; }
	public boolean isEOF() { return false; }
	
	public boolean isList(){ return false; }
	
	public abstract boolean isInputPort();
	public abstract boolean isOutputPort();
	
	public InputPort toInputPort() throws FnException {
		if(!isInputPort()) throw new FnException(Error.INPUT_PORT_EXPECTED, this);
		return (InputPort)this;
	}

	public OutputPort toOutputPort() throws FnException	{
		if(!isOutputPort()) throw new FnException(Error.OUTPUT_PORT_EXPECTED, this);
		return (OutputPort)this;
	}
	
	public static OutputPort outputPort(Expression arg, int pos) throws FnException {
		OutputPort retVal = OutputPort.CURRENT;
		if(arg != Empty.EMPTY && ((Pair)arg).length() >= pos) retVal = ((Pair)arg).nth(pos - 1).toPort().toOutputPort();
		return retVal;
	}
	
	public static InputPort inputPort(Expression arg, int pos) throws FnException {
		InputPort retVal = InputPort.CURRENT;
		if(arg != Empty.EMPTY && ((Pair)arg).length() >= pos) retVal = ((Pair)arg).nth(pos - 1).toPort().toInputPort();
		return retVal;
	}

	public String toString(){
		return "[Po=]";
	}
	
	public String write(){
		return this.toString();
	}			   
	
}