package lang.lisp;

public class LScanner{
	
	private LExpression result;
	private StringBuffer expression;
	private int charPointer;
	private StringBuffer temp;
	private LEnvironment env;
	
	private static String DIGITS = "0123456789.-";
	private static String ALPHAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz*-";
	private static String BLANK = " \n\013";
	
	private static String QUOTE = "quote";
	private static String FUNCTION = "function";
	private static String NIL = "nil";
									
	
	public LScanner(LEnvironment env){
		this.env = env;
		this.temp = new StringBuffer(1024);
	}
	
	public LExpression scan(String s) throws LException {
		System.out.println("scanning=" + s);
		this.expression = new StringBuffer(s);
		this.result = null;
		this.charPointer = 0;
		if(!this.readSExpression()) throw new LException(LError.SYNTAX, s);
		return this.result;
	}
	
	// transforming string buffer into S expression
	
	private boolean readSExpression(){
		while(this.rBlank());
        if(this.readAtom())	return true;
        if(this.readQuote()) return true;
		if(this.readFunction()) return true;
        if(this.readList())	return true;
        return false;
	}
	
	private boolean readAtom(){
		if(this.readSymbol() || this.readNumber()) return true;
		return false;
	}
	
	private boolean readQuote(){
		// while(this.rBlank());
		if(!this.rCharacter('\''))
			return false;
        if(!this.readSExpression())
			return false;
		this.result = new LList(env.recSymbol(QUOTE), new LList(this.result));
        return true;
	}
	
	private boolean readFunction(){
		if(!this.rCharacter('#')) return false;
		if(!this.rCharacter('\'')) return false;
		if(!this.readSExpression()) return false;
		this.result = new LList(env.recSymbol(FUNCTION), new LList(this.result));
		return true;
	}
	
	private boolean readList(){
        if(!this.rCharacter('('))
			return false;
		LExpression l = LSymbol.NIL;
		LList cur = null;
		while(this.readSExpression()){
			LList tp = new LList(this.result);
			if(cur == null) l = tp; else cur.rplcd(tp);
			cur = tp;
		}
        if(!this.rCharacter(')'))
			return false;
        this.result = l;
        return true;
	}
	
	private boolean readSymbol(){
		this.tclear();
        if(this.readNumericalOperator()) return true;
        if(!this.rAlphabetical()) return false;
        while(this.rAlphabetical() || this.rNumerical() || this.rCharacter('-'));
        this.result = this.env.recSymbol(this.temp.toString());
        return true;
	}
	
	private boolean readNumber(){
		this.tclear();
		int s = 1;
		if(this.rCharacter('-'))
			s=-1;
		if(!this.rNumerical())
			return false;
		while(this.rNumerical());
        try{
			String n = this.temp.toString();
			if(n.indexOf('.') != -1){
				this.result = new LFloatingPoint(n);
			}else{
				this.result = new LInteger(n);
			}
		}catch(NumberFormatException ex){
			ex.printStackTrace();
		}
		return true;
	}
	
	private boolean readNumericalOperator(){
		this.tclear();
		if(this.rCharacter('<')){
			if(this.rCharacter('=') || this.rCharacter('>')){
				this.result = this.env.recSymbol(this.temp.toString());
				return true;
			}
			this.result = this.env.recSymbol(this.temp.toString());
			return true;
        }
		if(this.rCharacter('>')){
			if(this.rCharacter('=')){
				this.result = this.env.recSymbol(this.temp.toString());
				return true;
			}
			this.result = this.env.recSymbol(this.temp.toString());
			return true;
        }
        if(this.rCharacter('=') || this.rCharacter('+') || this.rCharacter('-') || this.rCharacter('*') || this.rCharacter('/')){
				this.result = this.env.recSymbol(this.temp.toString());
                return true;
		}
		else return false;
	}
	
	private void tclear(){
		this.temp.setLength(0);
	}
	
	private void tputc(char c){
		this.temp.append(c);
		this.charPointer++;
	}
	
	private boolean rAlphabetical(){
		if(this.charPointer == this.expression.length())
			return false;
		char c = this.expression.charAt(this.charPointer);
		if(this.ALPHAS.indexOf(c) != -1){
			this.tputc(c);
			return true;
		}
        return false;
	}
	
	private boolean rNumerical(){
		if(this.charPointer == this.expression.length())
			return false;
		char c = this.expression.charAt(this.charPointer);
		if(this.DIGITS.indexOf(c) != -1){
			this.tputc(c);
            return true;
        }
        return false;
	}
	
	private boolean rCharacter(char what){
		if(this.charPointer == this.expression.length())
			return false;
		char c = this.expression.charAt(this.charPointer);
		if(c == what){
			this.temp.append(c);
			this.charPointer++;
            return true;
        }
        return false;
	}
	
	private boolean rBlank(){
		if(this.charPointer == this.expression.length())
			return false;
		char c = this.expression.charAt(this.charPointer);
		if(this.BLANK.indexOf(c) != -1){
			this.temp.append(c);
			this.charPointer++;
            return true;
        }
        return false;
	}
	

}
