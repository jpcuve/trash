package lang.interpreter;

public class InterpreterException extends Exception {
	private Error error;
	private Object value;

	public InterpreterException(Error e, Object o){
		super(e.toString());
		error = e;
		value = o;
	}

	public String toString(){
		return error.toString() + " : " + ((value instanceof Expression) ? ((Expression)value).write() : value.toString());
	}

}