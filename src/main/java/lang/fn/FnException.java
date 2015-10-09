package lang.fn;

public class FnException extends Exception {
	private Error error;
	private Object value;

	public FnException(Error e, Object o){
		super(e.toString());
		error = e;
		value = o;
	}

	public String toString(){
		return error.toString() + " : " + ((value instanceof Expression) ? ((Expression)value).write() : value.toString());
	}

}