package lang.lisp;

public class LError {
	private String id;
	
	public static LError SYNTAX = new LError( "Syntax error");
	public static LError CAR_CDR_NON_LIST = new LError( "CAR (FIRST) or CDR (REST) of non-list object");
	public static LError UNBOUND_VARIABLE = new LError( "Unbound variable");
	public static LError BAD_SETF = new LError( "Bad SETF form");
	public static LError WRONG_NB_ARGS = new LError( "Wrong number of arguments");
	public static LError LIST_ARG_EXPECTED = new LError( "List type argument expected");
	public static LError UNDEFINED_FUNCTION = new LError( "Undefined function");
	public static LError SYMB_ARG_EXPECTED = new LError( "Symbol type argument expected");
	public static LError BUILTIN_SYMBOL = new LError( "Built-in symbol");
	public static LError APPEND_TO_ATOM = new LError( "Cannot append to atom");
	public static LError NUMBER_ARG_EXPECTED = new LError( "Number type argument expected");
	public static LError INTEGER_ARG_EXPECTED = new LError( "Integer type argument expected");
	public static LError FUNCTION_ARG_EXPECTED = new LError( "Function type argument expected");
	public static LError PACKAGED_FUNCTION_NOT_FOUND = new LError( "Packaged function not found");
	public static LError UNEXPECTED_RESERVED = new LError( "Unexpected reserved symbol");

	public LError(String id){
		this.id = id;
	}
	
	public String getMessage(){
		return this.id;
	}
	
}
