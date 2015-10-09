package lang.fn;

public class Char extends Expression {
	public static Char SPACE = new Char(' ');
	public static Char NEWLINE = new Char('\n');
	
	public boolean isBoolean() { return false; }
	public boolean isSymbol() { return false; }
	public boolean isChar() { return true; }
	public boolean isVector() { return false; }
	public boolean isProcedure() { return false; }
	public boolean isPair() { return false; }
	public boolean isNumber() { return false; }
	public boolean isString() { return false; }
	public boolean isPort() { return false; }
	public boolean isPromise() { return false; }
	public boolean isLink() { return false; }
	public boolean isEOF() { return false; }
	
	public boolean isList(){ return false; }
	
	public boolean isEqv(Expression z){
		if(!z.isChar()) return false;
		return (value == ((Char)z).value);
	}
	
	private char value;
	
	public Char(char c){
		value = c;
	}

    public Char(int i){
       	value = (char)i;
    }
	
	public char charValue() {
		return(value);
	}

    public static Char toUp(Char z){
    	return new Char(Character.toUpperCase(z.value));
    }

    public static Char toDown(Char z){
    	return new Char(Character.toLowerCase(z.value));
    }

    public int compareTo(Char z, boolean f){
    	Character c1 = new Character((f) ? Character.toUpperCase(value) : value);
        Character c2 = new Character((f) ? Character.toUpperCase(z.value) : z.value);
    	return c1.compareTo(c2);
    }

    public boolean isAlphabetic(){
    	return Character.isLetter(value);
    }

    public boolean isNumeric(){
    	return Character.isDigit(value);
    }

    public boolean isWhitespace(){
    	return Character.isSpaceChar(value);
    }

    public boolean isUpperCase(){
    	return Character.isUpperCase(value);
    }

    public boolean isLowerCase(){
    	return Character.isLowerCase(value);
    }
	
	public String toString(){
		return "[Ch=" + value + "]";
	}
	
	public String write(){
		if(this == Char.SPACE) return "#\\space";
		if(this == Char.NEWLINE) return "#\\newline";
		return "#\\" + value;
	}			   
	
}