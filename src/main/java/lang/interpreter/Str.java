package lang.interpreter;

public class Str extends Expression {
	private boolean mutable;

	public boolean isEqual(Expression z){
		if(!(z instanceof Str)) return false;
		return (value.equals(((Str)z).value));
	}

	private StringBuffer value;

    public Str() {
    	setMutable();
    }

    public Str(StringBuffer sb) {
    	this();
        value = new StringBuffer(sb.toString());
    }

	public Str(String s){
        this();
		value = new StringBuffer(s);
        clearMutable();
	}

	public Str(Str z){
		this();
		value = new StringBuffer(z.value.toString());
	}

	public Str(int n, char c){
    	this();
		value = new StringBuffer(n);
		for(int i = 0; i < n; i++) value.append(c);
	}

	public Str(Symbol z){
		this(z.stringValue());
		for(int i = 0; i < value.length(); i++)
			value.setCharAt(i, Character.toLowerCase(value.charAt(i)));
			clearMutable();
	}

	public void setMutable(){
		mutable = true;
	}

	public void clearMutable(){
		mutable = false;
	}

	public boolean isMutable(){
		return mutable;
	}

	public void fill(char c){
		for(int i = 0; i < value.length(); i++) value.setCharAt(i, c);
	}

	public void set(int at, char c){
		value.setCharAt(at, c);
	}

	public Expression length(){
		return new Number(value.length());
	}

	public Expression subString(Number z1, Number z2) throws InterpreterException {
		int k1 = (int)z1.longValue();
		int k2 = (int)z2.longValue();
		int size = value.length();
		if(k1 > k2 || k1 >= size) throw new InterpreterException(Error.OUT_OF_RANGE, z1);
		if(k2 >= size) throw new InterpreterException(Error.OUT_OF_RANGE, z2);
		return new Str(new StringBuffer(value.toString().substring(k1, k2)));
	}

	public Expression append(Expression arg) throws InterpreterException {
		Str retVal = new Str(value);
		for(Expression cur = arg; cur instanceof Pair; cur = cur.rest()){
			Str z = (Str)cur.first();
			retVal.value.append(z.value);
		}
		return retVal;
	}

	public int compareTo(Str z, boolean f){
		String s1 = value.toString();
		String s2 = z.value.toString();
		return (f) ? s1.compareToIgnoreCase(s2) : s1.compareTo(s2);
	}

	public String toString(){
		return "[St=" + value + "]";
	}

	public String write(){
		return('"' + value.toString() + '"');
	}
	
	public StringBuffer stringBufferValue(){
		return(value);
	}
	
	public String stringValue() {
		return(value.toString());
	}
}
	

