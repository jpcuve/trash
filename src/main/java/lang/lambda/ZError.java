package lang.lambda;

public class ZError {
	
	private String m_value;
	
	public static ZError SYNTAX = new ZError("*syntax error*");
	public static ZError UNIMPLEMENTED_FEATURE = new ZError("*unimplemented feature*");
	public static ZError UNSUPPORTED_NUMBER_FORMAT = new ZError("*unsupported number format*");
	public static ZError PACKAGED_FUNCTION_NOT_FOUND = new ZError("*packaged function not found*");
	public static ZError SYMBOL_REBIND = new ZError("*symbol rebind*");
	public static ZError UNBOUND_SYMBOL = new ZError("*unbound symbol*");
	public static ZError WRONG_NB_ARGS = new ZError("*wrong number of arguments*");
	public static ZError PROPER_LIST_EXPECTED = new ZError("*proper list expected*");
	public static ZError OUT_OF_RANGE = new ZError("*out of range*");
	public static ZError PROCEDURE_EXPECTED = new ZError("*procedure expected*");
	public static ZError SYMBOL_EXPECTED = new ZError("*symbol expected*");
	public static ZError LIST_EXPECTED = new ZError("*list expected*");
	public static ZError NUMBER_EXPECTED = new ZError("*number expected*");
	public static ZError PAIR_EXPECTED = new ZError("*pair expected*");
	public static ZError APPEND_TO_ATOM = new ZError("*append to atom*");
	public static ZError BOOLEAN_EXPECTED = new ZError("*boolean expected*");
	public static ZError CHAR_EXPECTED = new ZError("*char expected*");
	public static ZError VECTOR_EXPECTED = new ZError("*vector expected*");
	public static ZError STRING_EXPECTED = new ZError("*string expected*");
	public static ZError PORT_EXPECTED = new ZError("*port expected*");
	public static ZError NUMBER_REAL_EXPECTED = new ZError("*REAL number expected*");
	public static ZError NUMBER_INTEGER_EXPECTED = new ZError("*INTEGER number expected*");
	public static ZError NUMBER_INTEGER_NATURAL_EXPECTED = new ZError("*NATURAL number expected*");

	
	public ZError(String s){
		m_value = s;
	}
	
	
	public String toString(){
		return m_value;
	}
}
