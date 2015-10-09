package lang.interpreter;

public class Number extends Expression {
    private long value;
	public static Number ZERO = new Number(0);
	public static Number ONE = new Number(1);
	
	public Number(int i) {
		this((long)i);
	}

	public Number(long l) {
		value = l;
	}

    public Number(double d) {
        this((long)d);
    }

    public boolean isEqv(Expression z){
		if(!(z instanceof Number)) return false;
        return value == ((Number)z).value;
	}

	public int compareTo(Number z) throws InterpreterException {
        long arg = z.value;
        if (value < arg) return -1;
        if (value > arg) return 1;
		return 0;
	}

	public Number monOp(char op) throws InterpreterException {
		switch(op){
            case '-': return(new Number(-value));
			case 'A': return(new Number(Math.abs(value)));
			case 'R': return(new Number(Math.sqrt(value)));
		}
		throw new InterpreterException(Error.UNIMPLEMENTED_FEATURE, this);
	}

	public Number binOp(char op, Number z) throws InterpreterException {
		long arg = z.value;
		switch(op){
			case '+': return(new Number(value + arg));
			case '-': return(new Number(value - arg));
			case '*': return(new Number(value * arg));
			case 'X': return(new Number(Math.max(value, arg)));
			case 'N': return(new Number(Math.min(value, arg)));
			case 'Q': return(new Number(value / arg));
			case 'R': return(new Number(value % arg));
			case 'O': return(new Number(value % arg));
		}
		throw new InterpreterException(Error.UNIMPLEMENTED_FEATURE, this);
	}

	public String write(){
		return(Long.toString(value));
	}

	public String toString(){
		return "[In=" + value + "]";
	}

	public long longValue() {
		return(value);
	}


}