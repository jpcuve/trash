package lang.lisp;

public class LString extends LAtom {
	public String value;
	/*
	public static String NIL = "nil";
	public static String T = "t";
	public static String QUOTE = "quote";
	public static String IF = "if";
	public static String COND = "cond";
	public static String GET = "get";
	public static String APPLY = "apply";
	public static String SETQ = "setq";
	public static String SETF = "setf";
	public static String DEFUN = "defun";
	public static String LAMBDA = "lambda";
	public static String LET = "let";
	public static String LETSTAR = "let*";
	public static String FUNCTION = "function";
	public static String WHEN = "when";
	public static String UNLESS = "unless";
	public static String CASE = "case";
	public static String OTHERWISE = "otherwise";
	public static String MAPCAR = "mapcar";
	*/
	public LString(String s){
		this.value = s;
	}
	
	public boolean stringp(){ return true; }

}
