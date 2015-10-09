package lang.lambda;

public class ZToken {
	
	private String m_value;
	private int m_type;
	
	public static int UNDEFINED = 0;
	public static int IDENTIFIER = 1;
	public static int BOOLEAN = 2;
	public static int NUMBER = 3;
	public static int CHARACTER = 4;
	public static int STRING = 5;
	
	public static String SPACE = "space";
	public static String NEWLINE = "newline";
	
	public ZToken(String s, int type){
		m_value = s;
		m_type = type;
	}
	
	// accessors
	
	public String getValue(){
		return m_value;
	}
	
	// types
	public boolean isUndefined(){
		return m_type == ZToken.UNDEFINED;
	}
	
	public boolean isIdentifier(){
		return m_type == ZToken.IDENTIFIER;
	}
	
	public boolean isBoolean(){
		return m_type == ZToken.BOOLEAN;
	}
	
	public boolean isNumber(){
		return m_type == ZToken.NUMBER;
	}
	
	public boolean isCharacter(){
		return m_type == ZToken.CHARACTER;
	}
	
	public boolean isString(){
		return m_type == ZToken.STRING;
	}
	
	// boolean
	
	public boolean isTrue(){
		return m_value.equals("#t");
	}
	
	// character
	
	public boolean isSpace(){
		return m_value.equals("#\\" + ZToken.SPACE );
	}
	
	public boolean isNewLine(){
		return m_value.equals("#\\" + ZToken.NEWLINE );
	}
	
	
	
	public String toString(){
		return m_value + "," + m_type;
	}
}
