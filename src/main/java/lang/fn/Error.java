package lang.fn;

public class Error {
	private String value;

	public static Error SYNTAX = new Error("*syntax error*");
	public static Error UNBALANCED_PARENTHESIS = new Error("*unbalanced parenthesis*");
	public static Error UNIMPLEMENTED_FEATURE = new Error("*unimplemented feature*");
	public static Error UNSUPPORTED_NUMBER_FORMAT = new Error("*unsupported number format*");
	public static Error PACKAGED_FUNCTION_NOT_FOUND = new Error("*packaged function not found*");
	public static Error SYMBOL_REBIND = new Error("*symbol rebind*");
	public static Error UNBOUND_SYMBOL = new Error("*unbound symbol*");
	public static Error WRONG_NB_ARGS = new Error("*wrong number of arguments*");
	public static Error PROPER_LIST_EXPECTED = new Error("*proper list expected*");
	public static Error OUT_OF_RANGE = new Error("*out of range*");
	public static Error PROCEDURE_EXPECTED = new Error("*procedure expected*");
	public static Error SYMBOL_EXPECTED = new Error("*symbol expected*");
	public static Error LIST_EXPECTED = new Error("*list expected*");
	public static Error NUMBER_EXPECTED = new Error("*number expected*");
	public static Error PAIR_EXPECTED = new Error("*pair expected*");
	public static Error APPEND_TO_ATOM = new Error("*append to atom*");
	public static Error BOOLEAN_EXPECTED = new Error("*boolean expected*");
	public static Error CHAR_EXPECTED = new Error("*char expected*");
	public static Error VECTOR_EXPECTED = new Error("*vector expected*");
	public static Error STRING_EXPECTED = new Error("*string expected*");
	public static Error PORT_EXPECTED = new Error("*port expected*");
	public static Error PROMISE_EXPECTED = new Error("*promise expected*");
	public static Error NUMBER_REAL_EXPECTED = new Error("*REAL number expected*");
	public static Error NUMBER_INTEGER_EXPECTED = new Error("*INTEGER number expected*");
	public static Error NUMBER_INTEGER_NATURAL_EXPECTED = new Error("*NATURAL number expected*");
    public static Error IMMUTABLE_STRING = new Error("*immutable string*");
	public static Error INPUT_PORT_EXPECTED = new Error("*input port expected*");
	public static Error OUTPUT_PORT_EXPECTED = new Error("*output port expected*");
	public static Error FILE_NOT_FOUND = new Error("*file not found (FileNotFoundException)*");
	public static Error IO = new Error("*input/output error (IOException)*");
	public static Error ILLEGAL_OPERAND = new Error("*illegal operand(s)*");

	public Error(String s){
		value = s;
	}


	public String toString(){
		return value;
	}

}