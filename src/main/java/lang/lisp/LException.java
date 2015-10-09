package lang.lisp;

public class LException extends Exception{
	/*
	public static String SYNTAX = "Syntax error";
	public static String CAR_CDR_NON_LIST = "CAR (FIRST) or CDR (REST) of non-list object";
	public static String UNBOUND_VARIABLE = "Unbound variable";
	public static String BAD_SETF = "Bad SETF form";
	public static String WRONG_NB_ARGS = "Wrong number of arguments";
	public static String LIST_ARG_EXPECTED = "List type argument expected";
	public static String UNDEFINED_FUNCTION = "Undefined function";
	public static String SYMB_ARG_EXPECTED = "Symbol type argument expected";
	public static String BUILTIN_SYMBOL = "Built-in symbol";
	public static String APPEND_TO_ATOM = "Cannot append to atom";
	public static String NUMBER_ARG_EXPECTED = "Number type argument expected";
	public static String INTEGER_ARG_EXPECTED = "Integer type argument expected";
	public static String FUNCTION_ARG_EXPECTED = "Function type argument expected";
	public static String PACKAGED_FUNCTION_NOT_FOUND = "Packaged function not found";
	public static String UNEXPECTED_RESERVED = "Unexpected reserved symbol";
	*/
	/*
	public LException(String s){
		super(s);
	}
	*/
	
	private LExpression retVal;
	
	public LException(LError e, LExpression l){
		super(e.getMessage() + ": " + l);
		this.retVal = l;
	}
	
	
	public LException(LError e, String l){
		super(e.getMessage() + ": " + l);
	}
	
	public LExpression getValue(){
		return this.retVal;
	}
	
	
}
