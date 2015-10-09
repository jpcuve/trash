package lang.fn;

public class Str extends Expression {
	private boolean mutable;
	
	public boolean isBoolean() { return false; }
	public boolean isSymbol() { return false; }
	public boolean isChar() { return false; }
	public boolean isVector() { return false; }
	public boolean isProcedure() { return false; }
	public boolean isPair() { return false; }
	public boolean isNumber() { return false; }
	public boolean isString() { return true; }
	public boolean isPort() { return false; }
	public boolean isPromise() { return false; }
	public boolean isLink() { return false; }
	public boolean isEOF() { return false; }
	
	public boolean isList(){ return false; }

	public boolean isEqual(Expression z){
		if(!z.isString()) return false;
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

	public Str(Int n, Expression arg) throws FnException {
		this((int)n.toNatural(), '*');
		if(((Pair)arg).length() == 2) for(int i = 0; i < value.length(); i++)
		value.setCharAt(i, arg.second().toChar().charValue());
	}

	public Str(Expression arg) throws FnException{
		int size = (arg.isPair()) ? (int)((Pair)arg).length() : 0;
		value = new StringBuffer(size);
		for(Expression cur = arg; cur.isPair(); cur = cur.rest())
			value.append(cur.first().toChar().charValue());
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
		return new Int(value.length());
	}

	public Expression ref(Int z) throws FnException {
		int k = (int)z.toNatural();
		int size = value.length();
		if(k < 0 || k >= size) throw new FnException(Error.OUT_OF_RANGE, z);
		return new Char(value.charAt(k));
	}

	public Expression set(Int z, Char c) throws FnException {
		int k = (int)z.toNatural();
		int size = value.length();
		if(k < 0 || k >= size) throw new FnException(Error.OUT_OF_RANGE, z);
		if(!mutable) throw new FnException(Error.IMMUTABLE_STRING, this);
		set(k, c.charValue());
		return Void.VOID;
	}

	public Expression subString(Int z1, Int z2) throws FnException {
		int k1 = (int)z1.toNatural();
		int k2 = (int)z2.toNatural();
		int size = value.length();
		if(k1 > k2 || k1 >= size) throw new FnException(Error.OUT_OF_RANGE, z1);
		if(k2 >= size) throw new FnException(Error.OUT_OF_RANGE, z2);
		return new Str(new StringBuffer(value.toString().substring(k1, k2)));
	}

	public Expression append(Expression arg) throws FnException {
		Str retVal = new Str(value);
		for(Expression cur = arg; cur.isPair(); cur = cur.rest()){
			Str z = cur.first().toString_();
			retVal.value.append(z.value);
		}
		return retVal;
	}

	public int compareTo(Str z, boolean f){
		String s1 = value.toString();
		String s2 = z.value.toString();
		return (f) ? s1.compareToIgnoreCase(s2) : s1.compareTo(s2);
	}

	public Expression toList_(){
		Expression retVal = Empty.EMPTY;
		for(int i = value.length() - 1; i >= 0; i--)
			retVal = (new Char(value.charAt(i))).cons(retVal);
		return retVal;
	}

	public Expression fill(Char c) throws FnException{
		if(!mutable) throw new FnException(Error.IMMUTABLE_STRING, this);
		fill(c.toChar());
		return Void.VOID;
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
	

