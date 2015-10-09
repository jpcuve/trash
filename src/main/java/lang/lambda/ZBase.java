package lang.lambda;

public class ZBase extends ZPackage {
	/*
	public static int // numbers
		EQV_QUESTION = 1, EQ_QUESTION = 2, EQUAL_QUESTION= 3, NUMBER_QUESTION = 4, COMPLEX_QUESTION = 5, REAL_QUESTION = 6,
		RATIONAL_QUESTION = 7, INTEGER_QUESTION = 8, EXACT_QUESTION = 9, INEXACT_QUESTION = 10, EQUAL_TO = 11, LESS_THAN = 12,
		MORE_THAN = 13, LESS_OR_EQUAL_TO = 14, MORE_OR_EQUAL_TO = 15, ZERO_QUESTION = 16, POSITIVE_QUESTION = 17, NEGATIVE_QUESTION = 18,
		ODD_QUESTION = 19, EVEN_QUESTION = 20, MAX = 21, MIN = 22, ADD = 23, SUB = 24, MUL = 25, DIV = 26, ABS = 27, 
		QUOTIENT = 28, REMAINDER = 29, MODULO = 30, GCD = 31, LCM = 32, NUMERATOR = 33, DENOMINATOR = 34, FLOOR = 35,
		CEILING = 36, TRUNCATE = 37, ROUND = 38, RATIONALIZE = 39, EXP = 40, LOG = 41, SIN = 42, COS = 43, TAN = 44, ASIN = 45,
		ACOS = 46, ATAN = 47, SQRT = 48, EXPT = 49, MAKE_RECTANGULAR = 50, MAKE_POLAR = 51, REAL_PART = 52,	IMAG_PART = 53,
		MAGNITUDE = 54,	ANGLE = 55,	EXACT_TO_INEXACT = 56, INEXACT_TO_EXACT = 57, NUMBER_TO_STRING = 58, STRING_TO_NUMBER = 59;
	
	public static int // booleans
		NOT = 60, BOOLEAN_QUESTION = 61;
	
	public static int // pairs & lists
		PAIR_QUESTION = 62, CONS = 63, CAR = 64, CDR = 65, SET_CAR_EXCLAMATION = 66, SET_CDR_EXCLAMATION = 67, NULL_QUESTION = 68,
		LIST_QUESTION = 69, LIST = 70, LENGTH = 71, APPEND = 72, REVERSE = 73, LIST_TAIL = 74, LIST_REF = 75, MEMQ = 76,
		MEMV = 77, MEMBER = 78, ASSQ = 79, ASSV = 80, ASSOC = 82;
		
	public static int // symbols
		SYMBOL_QUESTION = 82, SYMBOL_TO_STRING = 83, STRING_TO_SYMBOL = 84;

	
	public ZBase(){
		this.addPrimitive(ABS, "abs", 1, 1); 
		this.addPrimitive(ACOS, "acos", 1, 1); 
		this.addPrimitive(ADD, "+", 1, 1); 
		this.addPrimitive(ANGLE, "angle", 1, 1); 
		this.addPrimitive(APPEND, "append", 1, 1); 
		this.addPrimitive(ASIN, "asin", 1, 1); 
		this.addPrimitive(ASSOC, "assoc", 1, 1); 
		this.addPrimitive(ASSQ, "assq", 1, 1); 
		this.addPrimitive(ASSV, "assv", 1, 1); 
		this.addPrimitive(ATAN, "atan", 1, 1); 
		this.addPrimitive(BOOLEAN_QUESTION, "boolean?", 1, 1); 
		this.addPrimitive(CAR, "car", 1, 1); 
		this.addPrimitive(CDR, "cdr", 1, 1); 
		this.addPrimitive(CEILING, "ceiling", 1, 1); 
		this.addPrimitive(COMPLEX_QUESTION, "complex?", 1, 1); 
		this.addPrimitive(CONS, "cons", 1, 1); 
		this.addPrimitive(COS, "cos", 1, 1); 
		this.addPrimitive(DENOMINATOR, "denominator", 1, 1); 
		this.addPrimitive(DIV, "/", 1, 1); 
		this.addPrimitive(EQ_QUESTION, "eq?", 1, 1); 
		this.addPrimitive(EQUAL_QUESTION, "equal?", 1, 1); 
		this.addPrimitive(EQUAL_TO, "=", 1, 1); 
		this.addPrimitive(EQV_QUESTION, "eqv?", 1, 1); 
		this.addPrimitive(EVEN_QUESTION, "even?", 1, 1); 
		this.addPrimitive(EXACT_QUESTION, "exact?", 1, 1); 
		this.addPrimitive(EXACT_TO_INEXACT, "exact->inexact", 1, 1); 
		this.addPrimitive(EXP, "exp", 1, 1); 
		this.addPrimitive(EXPT, "expt", 1, 1); 
		this.addPrimitive(FLOOR, "floor", 1, 1); 
		this.addPrimitive(GCD, "gcd", 1, 1); 
		this.addPrimitive(IMAG_PART, "imag-part", 1, 1); 
		this.addPrimitive(INEXACT_QUESTION, "inexact?", 1, 1); 
		this.addPrimitive(INEXACT_TO_EXACT, "inexact->exact", 1, 1); 
		this.addPrimitive(INTEGER_QUESTION, "integer?", 1, 1); 
		this.addPrimitive(LCM, "lcm", 1, 1); 
		this.addPrimitive(LENGTH, "length", 1, 1); 
		this.addPrimitive(LESS_OR_EQUAL_TO, "<=", 1, 1); 
		this.addPrimitive(LESS_THAN, "<", 1, 1); 
		this.addPrimitive(LIST, "list", 1, 1); 
		this.addPrimitive(LIST_QUESTION, "list?", 1, 1); 
		this.addPrimitive(LIST_REF, "list-ref", 1, 1); 
		this.addPrimitive(LIST_TAIL, "list-tail", 1, 1); 
		this.addPrimitive(LOG, "log", 1, 1); 
		this.addPrimitive(MAGNITUDE, "magnitude", 1, 1); 
		this.addPrimitive(MAKE_RECTANGULAR, "make-rectangular", 1, 1); 
		this.addPrimitive(MAKE_POLAR, "make-polar", 1, 1); 
		this.addPrimitive(MAX, "max", 1, 1); 
		this.addPrimitive(MEMBER ="member", 1, 1); 
		this.addPrimitive(MEMQ, "memq", 1, 1); 
		this.addPrimitive(MEMV, "memv", 1, 1); 
		this.addPrimitive(MIN, "min", 1, 1); 
		this.addPrimitive(MODULO, "modulo", 1, 1); 
		this.addPrimitive(MORE_OR_EQUAL_TO, ">=", 1, 1); 
		this.addPrimitive(MORE_THAN, ">", 1, 1); 
		this.addPrimitive(MUL, "*", 1, 1); 
		this.addPrimitive(NEGATIVE_QUESTION, "negative?", 1, 1); 
		this.addPrimitive(NOT, "not", 1, 1); 
		this.addPrimitive(NULL_QUESTION, "null?", 1, 1); 
		this.addPrimitive(NUMBER_QUESTION, "number?", 1, 1); 
		this.addPrimitive(NUMBER_TO_STRING, "number->string", 1, 1); 
		this.addPrimitive(NUMERATOR, "numerator", 1, 1); 
		this.addPrimitive(ODD_QUESTION, "odd?", 1, 1); 
		this.addPrimitive(PAIR_QUESTION, "pair?", 1, 1); 
		this.addPrimitive(POSITIVE_QUESTION, "positive?", 1, 1); 
		this.addPrimitive(QUOTIENT, "quotient", 1, 1); 
		this.addPrimitive(RATIONAL_QUESTION, "rational?", 1, 1); 
		this.addPrimitive(RATIONALIZE, "rationalize", 1, 1); 
		this.addPrimitive(REAL_QUESTION, "real?", 1, 1); 
		this.addPrimitive(REAL_PART, "real-part", 1, 1); 
		this.addPrimitive(REMAINDER, "remainder", 1, 1); 
		this.addPrimitive(REVERSE, "reverse", 1, 1); 
		this.addPrimitive(ROUND, "round", 1, 1); 
		this.addPrimitive(SET_CAR_EXCLAMATION, "set-car!", 1, 1); 
		this.addPrimitive(SET_CDR_EXCLAMATION, "set-cdr!", 1, 1); 
		this.addPrimitive(SIN, "sin", 1, 1); 
		this.addPrimitive(SQRT, "sqrt", 1, 1); 
		this.addPrimitive(STRING_TO_NUMBER, "string->number", 1, 1); 
		this.addPrimitive(STRING_TO_SYMBOL, "string->symbol", 1, 1); 
		this.addPrimitive(SUB, "-", 1, 1); 
		this.addPrimitive(SYMBOL_QUESTION, "symbol?", 1, 1); 
		this.addPrimitive(SYMBOL_TO_STRING, "symbol->string", 1, 1); 
		this.addPrimitive(TAN, "tan", 1, 1); 
		this.addPrimitive(TRUNCATE, "truncate", 1, 1); 
		this.addPrimitive(ZERO_QUESTION, "zero?", 1, 1);
	}
	

	*/
	
	private static String ABS = "abs";
	private static String ACOS = "acos";
	private static String ADD = "+";
	private static String ANGLE = "angle";
	private static String APPEND = "append";
	private static String ASIN = "asin";
	private static String ASSOC = "assoc";
	private static String ASSQ = "assq";
	private static String ASSV = "assv";
	private static String ATAN = "atan";
	private static String BOOLEAN_QUESTION = "boolean?";
	private static String CAR = "car";
	private static String CDR = "cdr";
	private static String CEILING = "ceiling";
	private static String COMPLEX_QUESTION = "complex?";
	private static String CONS = "cons";
	private static String COS = "cos";
	private static String DENOMINATOR = "denominator";
	private static String DIV = "/";
	private static String EQ_QUESTION = "eq?";
	private static String EQUAL_QUESTION = "equal?";
	private static String EQUAL_TO = "=";
	private static String EQV_QUESTION = "eqv?";
	private static String EVEN_QUESTION = "even?";
	private static String EXACT_QUESTION = "exact?";
	private static String EXACT_TO_INEXACT = "exact->inexact";
	private static String EXP = "exp";
	private static String EXPT = "expt";
	private static String FLOOR = "floor";
	private static String GCD = "gcd";
	private static String IMAG_PART = "imag-part";
	private static String INEXACT_QUESTION = "inexact?";
	private static String INEXACT_TO_EXACT = "inexact->exact";
	private static String INTEGER_QUESTION = "integer?";
	private static String LCM = "lcm";
	private static String LENGTH = "length";
	private static String LESS_OR_EQUAL_TO = "<=";
	private static String LESS_THAN = "<";
	private static String LIST = "list";
	private static String LIST_QUESTION = "list?";
	private static String LIST_REF = "list-ref";
	private static String LIST_TAIL = "list-tail";
	private static String LOG = "log";
	private static String MAGNITUDE = "magnitude";
	private static String MAKE_RECTANGULAR = "make-rectangular";
	private static String MAKE_POLAR = "make-polar";
	private static String MAX = "max";
	private static String MEMBER ="member";
	private static String MEMQ = "memq";
	private static String MEMV = "memv";
	private static String MIN = "min";
	private static String MODULO = "modulo";
	private static String MORE_OR_EQUAL_TO = ">=";
	private static String MORE_THAN = ">";
	private static String MUL = "*";
	private static String NEGATIVE_QUESTION = "negative?";
	private static String NOT = "not";
	private static String NULL_QUESTION = "null?";
	private static String NUMBER_QUESTION = "number?";
	private static String NUMBER_TO_STRING = "number->string";
	private static String NUMERATOR = "numerator";
	private static String ODD_QUESTION = "odd?";
	private static String PAIR_QUESTION = "pair?";
	private static String POSITIVE_QUESTION = "positive?";
	private static String QUOTIENT = "quotient";
	private static String RATIONAL_QUESTION = "rational?";
	private static String RATIONALIZE = "rationalize";
	private static String REAL_QUESTION = "real?";
	private static String REAL_PART = "real-part";
	private static String REMAINDER = "remainder";
	private static String REVERSE = "reverse";
	private static String ROUND = "round";
	private static String SET_CAR_EXCLAMATION = "set-car!";
	private static String SET_CDR_EXCLAMATION = "set-cdr!";
	private static String SIN = "sin";
	private static String SQRT = "sqrt";
	private static String STRING_TO_NUMBER = "string->number";
	private static String STRING_TO_SYMBOL = "string->symbol";
	private static String SUB = "-";
	private static String SYMBOL_QUESTION = "symbol?";
	private static String SYMBOL_TO_STRING = "symbol->string";
	private static String TAN = "tan";
	private static String TRUNCATE = "truncate";
	private static String ZERO_QUESTION = "zero?";

    public String getInfo(){
		switch(m_id){
		case  1:  return EQV_QUESTION;
		case  2:  return EQ_QUESTION;
		case  3:  return EQUAL_QUESTION;
		case  4:  return NUMBER_QUESTION;
		case  5:  return COMPLEX_QUESTION;
		case  6:  return REAL_QUESTION;
		case  7:  return RATIONAL_QUESTION;
		case  8:  return INTEGER_QUESTION;
		case  9:  return EXACT_QUESTION;
		case 10:  return INEXACT_QUESTION;
		case 11:  return EQUAL_TO;
		case 12:  return LESS_THAN;
		case 13:  return MORE_THAN;
		case 14:  return LESS_OR_EQUAL_TO;
		case 15:  return MORE_OR_EQUAL_TO;
		case 16:  return ZERO_QUESTION;
		case 17:  return POSITIVE_QUESTION;
		case 18:  return NEGATIVE_QUESTION;
		case 19:  return ODD_QUESTION;
		case 20:  return EVEN_QUESTION;
		case 21:  return MAX;
		case 22:  return MIN;
		case 23:  return ADD;
		case 24:  return SUB;
		case 25:  return MUL;
		case 26:  return DIV;
		case 27:  return ABS;
		case 28:  return QUOTIENT;
		case 29:  return REMAINDER;
		case 30:  return MODULO;
		case 31:  return GCD;
		case 32:  return LCM;
		case 33:  return NUMERATOR;
		case 34:  return DENOMINATOR;
		case 35:  return FLOOR;
		case 36:  return CEILING;
		case 37:  return TRUNCATE;
		case 38:  return ROUND;
		case 39:  return RATIONALIZE;
		case 40:  return EXP;
		case 41:  return LOG;
		case 42:  return SIN;
		case 43:  return COS;
		case 44:  return TAN;
		case 45:  return ASIN;
		case 46:  return ACOS;
		case 47:  return ATAN;
		case 48:  return SQRT;
		case 49:  return EXPT;
		case 50:  return MAKE_RECTANGULAR;
		case 51:  return MAKE_POLAR;
		case 52:  return REAL_PART;
		case 53:  return IMAG_PART;
		case 54:  return MAGNITUDE;
		case 55:  return ANGLE;
		case 56:  return EXACT_TO_INEXACT;
		case 57:  return INEXACT_TO_EXACT;
		case 58:  return NUMBER_TO_STRING;
		case 59:  return STRING_TO_NUMBER;
		case 60:  return NOT;
		case 61:  return BOOLEAN_QUESTION;
		case 62:  return PAIR_QUESTION;
		case 63:  return CONS;
		case 64:  return CAR;
		case 65:  return CDR;
		case 66:  return SET_CAR_EXCLAMATION;
		case 67:  return SET_CDR_EXCLAMATION;
		case 68:  return NULL_QUESTION;
		case 69:  return LIST_QUESTION;
		case 70:  return LIST;
		case 71:  return LENGTH;
		case 72:  return APPEND;
		case 73:  return REVERSE;
		case 74:  return LIST_TAIL;
		case 75:  return LIST_REF;
		case 76:  return MEMQ;
		case 77:  return MEMV;
		case 78:  return MEMBER;
		case 79:  return ASSQ;
		case 80:  return ASSV;
		case 81:  return ASSOC;
		case 82:  return SYMBOL_QUESTION;
		case 83:  return SYMBOL_TO_STRING;
		case 84:  return STRING_TO_SYMBOL;
		}
		return "Lambda 1.0";
	}

    public ZExpression f(int index, ZExpression arg) throws ZException {
        switch(index){
                  // equivalence
        case  1:  return eqv_question_(arg);
        case  2:  return eq_question_(arg);
        case  3:  return equal_question_(arg);
                  // numbers
        case  4:  return number_question_(arg);
        case  5:  return complex_question_(arg);
        case  6:  return real_question_(arg);
        case  7:  return rational_question_(arg);
        case  8:  return integer_question_(arg);
        case  9:  return exact_question_(arg);
        case 10:  return inexact_question_(arg);
        case 11:  return equal_to_(arg);
        case 12:  return less_than_(arg);
        case 13:  return more_than_(arg);
        case 14:  return less_or_equal_to_(arg);
        case 15:  return more_or_equal_to_(arg);
        case 16:  return zero_question_(arg);
        case 17:  return positive_question_(arg);
        case 18:  return negative_question_(arg);
        case 19:  return odd_question_(arg);
        case 20:  return even_question_(arg);
        case 21:  return max_(arg);
        case 22:  return min_(arg);
        case 23:  return add_(arg);
        case 24:  return sub_(arg);
        case 25:  return mul_(arg);
        case 26:  return div_(arg);
        case 27:  return abs_(arg);
        case 28:  return quotient_(arg);
        case 29:  return remainder_(arg);
        case 30:  return modulo_(arg);
        case 31:  return gcd_(arg);
        case 32:  return lcm_(arg);
        case 33:  return numerator_(arg);
        case 34:  return denominator_(arg);
        case 35:  return floor_(arg);
        case 36:  return ceiling_(arg);
        case 37:  return truncate_(arg);
        case 38:  return round_(arg);
        case 39:  return rationalize_(arg);
        case 40:  return exp_(arg);
        case 41:  return log_(arg);
        case 42:  return sin_(arg);
        case 43:  return cos_(arg);
        case 44:  return tan_(arg);
        case 45:  return asin_(arg);
        case 46:  return acos_(arg);
        case 47:  return atan_(arg);
        case 48:  return sqrt_(arg);
        case 49:  return expt_(arg);
        case 50:  return make_rectangular_(arg);
        case 51:  return make_polar_(arg);
        case 52:  return real_part_(arg);
        case 53:  return imag_part_(arg);
        case 54:  return magnitude_(arg);
        case 55:  return angle_(arg);
        case 56:  return exact_to_inexact_(arg);
        case 57:  return inexact_to_exact_(arg);
        case 58:  return number_to_string_(arg);
        case 59:  return string_to_number_(arg);
                  // booleans
        case 60:  return not_(arg);
        case 61:  return boolean_question_(arg);
                   // pairs and lists
        case 62:  return pair_question_(arg);
        case 63:  return cons_(arg);
        case 64:  return car_(arg);
        case 65:  return cdr_(arg);
        case 66:  return set_car_exclamation_(arg);
        case 67:  return set_cdr_exclamation_(arg);
        case 68:  return null_question_(arg);
        case 69:  return list_question_(arg);
        case 70:  return list_(arg);
        case 71:  return length_(arg);
        case 72:  return append_(arg);
        case 73:  return reverse_(arg);
        case 74:  return list_tail_(arg);
        case 75:  return list_ref_(arg);
        case 76:  return memq_(arg);
        case 77:  return memv_(arg);
        case 78:  return member_(arg);
        case 79:  return assq_(arg);
        case 80:  return assv_(arg);
        case 81:  return assoc_(arg);
                  // symbols
        case 82:  return symbol_question_(arg);
        case 83:  return symbol_to_string_(arg);
        case 84:  return string_to_symbol_(arg);
                  // characters
        case 85:  return char_question_(arg);
        case 86:  return char_equal_to_question_(arg);
        case 87:  return char_less_than_question_(arg);
        case 88:  return char_more_than_question_(arg);
        case 89:  return char_less_or_equal_to_(arg);
        case 90:  return char_more_or_equal_to_(arg);
        case 91:  return char_ci_equal_to_question_(arg);
        case 92:  return char_ci_less_than_question_(arg);
        case 93:  return char_ci_more_than_question_(arg);
        case 94:  return char_ci_less_or_equal_to_(arg);
        case 95:  return char_ci_more_or_equal_to_(arg);
        case 96:  return char_alphabetic_question_(arg);
        case 97:  return char_numeric_question_(arg);
        case 98:  return char_whitespace_question_(arg);
        case 99:  return char_upper_case_question_(arg);
        case 100: return char_lower_case_question_(arg);
        case 101: return char_to_integer_(arg);
        case 102: return integer_to_char_(arg);
        case 103: return char_upcase_(arg);
        case 104: return char_downcase_(arg);
                  // strings

        }
        throw new ZException(ZError.PACKAGED_FUNCTION_NOT_FOUND, this);
    }



	// language definition
	
	private ZExpression abs_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, ABS);
	}
	
	private ZExpression add_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , ADD);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		if(!arg1.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg1);
		ZExpression arg2 = argl.second();
		if(!arg2.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg2);
		return ((ZNumber)arg1).add((ZNumber)arg2);
	}
	
	private ZExpression acos_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, ACOS);
	}
	
	private ZExpression angle_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, ANGLE);
	}
	
	private ZExpression append_(ZExpression arg) throws ZException {
		if(!arg.isPair()) return ZEmpty.EMPTY ;
		ZPair argl = (ZPair)arg;
		return argl.first().append(this.append_(argl.rest()));
	}
	
	/*
	private ZExpression append_(ZExpression arg) throws ZException {
		if(!arg.isPair()) return ZEmpty.EMPTY;
		ZPair argl = (ZPair)arg;
		return this.append_1(argl.first(), argl.rest());
	}
	
	private ZExpression append_1(ZExpression arg1, ZExpression arg2) throws ZException {
		if(!arg2.isPair()) return arg1;
		if(!arg1.isList()) throw new ZException(ZError.LIST_EXPECTED, arg1);
		ZPair arg2l = (ZPair)arg2;
		if(arg1 == ZEmpty.EMPTY) return this.append_1(arg2l.first(), arg2l.rest());
		ZPair arg1l = (ZPair)arg1;
		return arg1l.first().cons(this.append_1(arg1l.rest(), arg2));
	}	
	*/
	
	
	private ZExpression asin_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, ASIN);
	}
	
	private ZExpression assoc_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , ASSOC);
		ZPair argl = (ZPair)arg;
		ZExpression key = argl.first();
		ZExpression pairs = argl.second();
		while(pairs.isPair()){
			ZPair pairsl = (ZPair)pairs;
			ZExpression arg1 = pairsl.first();
			if(arg1.isPair()){
				ZPair arg1l = (ZPair)arg1;
				if(arg1l.first().isEqual(key)) return arg1l;
			}
			pairs = pairsl.rest();
		}
		return ZBoolean.FALSE;
	}

	private ZExpression assq_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , ASSQ);
		ZPair argl = (ZPair)arg;
		ZExpression key = argl.first();
		ZExpression pairs = argl.second();
		while(pairs.isPair()){
			ZPair pairsl = (ZPair)pairs;
			ZExpression arg1 = pairsl.first();
			if(arg1.isPair()){
				ZPair arg1l = (ZPair)arg1;
				if(arg1l.first().isEq(key)) return arg1l;
			}
			pairs = pairsl.rest();
		}
		return ZBoolean.FALSE;
	}
	
	private ZExpression assv_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , ASSV);
		ZPair argl = (ZPair)arg;
		ZExpression key = argl.first();
		ZExpression pairs = argl.second();
		while(pairs.isPair()){
			ZPair pairsl = (ZPair)pairs;
			ZExpression arg1 = pairsl.first();
			if(arg1.isPair()){
				ZPair arg1l = (ZPair)arg1;
				if(arg1l.first().isEqv(key)) return arg1l;
			}
			pairs = pairsl.rest();
		}
		return ZBoolean.FALSE;
	}
	
	private ZExpression atan_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, ATAN);
	}
	
	private ZExpression boolean_question_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 1)) throw new ZException(ZError.WRONG_NB_ARGS , BOOLEAN_QUESTION);
		ZPair argl = (ZPair)arg;
		return (argl.first().isBoolean()) ? ZBoolean.TRUE : ZBoolean.FALSE;
	}
	
	private ZExpression car_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 1)) throw new ZException(ZError.WRONG_NB_ARGS , CAR);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		if(!arg1.isPair()) throw new ZException(ZError.PAIR_EXPECTED, arg1);
		return ((ZPair)arg1).first();
	}
	
	private ZExpression cdr_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 1)) throw new ZException(ZError.WRONG_NB_ARGS , CDR);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		if(!arg1.isPair()) throw new ZException(ZError.PAIR_EXPECTED, arg1);
		return ((ZPair)arg1).rest();
	}
	
	private ZExpression ceiling_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, CEILING);
	}
	
	private ZExpression char_question_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression char_equal_to_question_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression char_less_than_question_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression char_more_than_question_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression char_less_or_equal_to_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression char_more_or_equal_to_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression char_ci_equal_to_question_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression char_ci_less_than_question_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression char_ci_more_than_question_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression char_ci_less_or_equal_to_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression char_ci_more_or_equal_to_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression char_alphabetic_question_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression char_numeric_question_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression char_whitespace_question_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression char_upper_case_question_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression char_lower_case_question_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression char_to_integer_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression integer_to_char_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression char_upcase_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression char_downcase_(ZExpression arg) throws ZException{
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, "wait");
	}
	
	private ZExpression complex_question_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, COMPLEX_QUESTION);
	}
	
	private ZExpression cons_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , CONS);
		ZPair argl = (ZPair)arg;
		return new ZPair(argl.first(), argl.second());
	}
	
	private ZExpression cos_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, COS);
	}
	
	private ZExpression denominator_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, DENOMINATOR);
	}
	
	private ZExpression div_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , DIV);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		if(!arg1.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg1);
		ZExpression arg2 = argl.second();
		if(!arg2.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg2);
		return ((ZNumber)arg1).div((ZNumber)arg2);
	}
	
	private ZExpression eq_question_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , EQ_QUESTION);
		ZPair argl = (ZPair)arg;
		return (argl.first().isEq(argl.second())) ? ZBoolean.TRUE : ZBoolean.FALSE;
	}
	
	private ZExpression equal_question_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , EQUAL_QUESTION);
		ZPair argl = (ZPair)arg;
		return (argl.first().isEqual(argl.second())) ? ZBoolean.TRUE : ZBoolean.FALSE;
	}
	
	private ZExpression equal_to_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , EQUAL_TO);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		if(!arg1.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg1);
		ZExpression arg2 = argl.second();
		if(!arg2.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg2);
		return (((ZNumber)arg1).compareTo((ZNumber)arg2) == 0) ? ZBoolean.TRUE : ZBoolean.FALSE ;
	}
	
	private ZExpression eqv_question_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , EQV_QUESTION);
		ZPair argl = (ZPair)arg;
		return (argl.first().isEqv(argl.second())) ? ZBoolean.TRUE : ZBoolean.FALSE;
	}
	
	private ZExpression even_question_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, EVEN_QUESTION);
	}
	
	private ZExpression exact_question_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, EXACT_QUESTION);
	}
	
	private ZExpression exact_to_inexact_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, EXACT_TO_INEXACT);
	}
	
	private ZExpression exp_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, EXP);
	}
	
	private ZExpression expt_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, EXPT);
	}
	
	private ZExpression floor_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, FLOOR);
	}
	
	private ZExpression gcd_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, GCD);
	}
	
	private ZExpression imag_part_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, IMAG_PART);
	}
	
	private ZExpression inexact_question_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, INEXACT_QUESTION);
	}
	
	private ZExpression inexact_to_exact_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, INEXACT_TO_EXACT);
	}
	
	private ZExpression integer_question_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, INTEGER_QUESTION);
	}
	
	private ZExpression lcm_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, LCM);
	}
	
	private ZExpression length_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 1)) throw new ZException(ZError.WRONG_NB_ARGS , LENGTH);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		if(arg1 == ZEmpty.EMPTY) return ZNumber.ZERO;
		if(!arg1.isPair()) throw new ZException(ZError.PAIR_EXPECTED , arg1);
		ZPair arg1l = (ZPair)arg1;
		return new ZInteger(arg1l.length());
	}
	
	private ZExpression less_or_equal_to_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , LESS_OR_EQUAL_TO);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		if(!arg1.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg1);
		ZExpression arg2 = argl.second();
		if(!arg2.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg2);
		return (((ZNumber)arg1).compareTo((ZNumber)arg2) <= 0) ? ZBoolean.TRUE : ZBoolean.FALSE ;
	}
	
	private ZExpression less_than_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , LESS_THAN);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		if(!arg1.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg1);
		ZExpression arg2 = argl.second();
		if(!arg2.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg2);
		return (((ZNumber)arg1).compareTo((ZNumber)arg2) < 0) ? ZBoolean.TRUE : ZBoolean.FALSE ;
	}
	
	private ZExpression list_(ZExpression arg) throws ZException {
		if(!arg.isPair()) return ZEmpty.EMPTY;
		ZPair argl = (ZPair)arg;
		return argl;
	}
	
	private ZExpression list_question_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 1)) throw new ZException(ZError.WRONG_NB_ARGS , LIST_QUESTION);
		ZPair argl = (ZPair)arg;
		return (argl.first().isList()) ? ZBoolean.TRUE : ZBoolean.FALSE;
	}
	
	private ZExpression list_ref_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , LIST_REF);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		if(!arg1.isList()) throw new ZException(ZError.LIST_EXPECTED, arg1);
		ZExpression arg2 = argl.second();
		if(arg1 == ZEmpty.EMPTY) throw new ZException(ZError.OUT_OF_RANGE, arg2);
		if(!arg2.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg2);
		long l = ((ZNumber)arg2).toLong();
		ZPair arg1l = (ZPair)arg1;
		ZExpression retVal = arg1l.nth(l);
		if(retVal == null)  throw new ZException(ZError.OUT_OF_RANGE, arg2);
		return retVal;
	}
	
	private ZExpression list_tail_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , LIST_TAIL);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		if(!arg1.isList()) throw new ZException(ZError.LIST_EXPECTED, arg1);
		ZExpression arg2 = argl.second();
		if(!arg2.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg2);
		long l = ((ZNumber)arg2).toLong();
		if(arg1 == ZEmpty.EMPTY){
			if(l != 0) throw new ZException(ZError.OUT_OF_RANGE, arg2);
			return ZEmpty.EMPTY;
		}
		ZPair arg1l = (ZPair)arg1;
		ZExpression retVal = arg1l.nthcdr(l);
		if(retVal == null)  throw new ZException(ZError.OUT_OF_RANGE, arg2);
		return retVal;
	}
	
	private ZExpression log_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, LOG);
	}
	
	private ZExpression magnitude_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, MAGNITUDE);
	}
	
	private ZExpression make_rectangular_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, MAKE_RECTANGULAR);
	}
	
	private ZExpression make_polar_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, MAKE_POLAR);
	}
	
	private ZExpression max_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, MAX);
	}
	
	private ZExpression member_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , MEMBER);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		ZExpression arg2 = argl.second();
		while(arg2.isPair()){
			ZPair arg2l = (ZPair)arg2;
			if(arg2l.first().isEqual(arg1)) return arg2l;
			arg2 = arg2l.rest();
		}
		return ZBoolean.FALSE;
	}
	
	private ZExpression memq_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , MEMQ);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		ZExpression arg2 = argl.second();
		while(arg2.isPair()){
			ZPair arg2l = (ZPair)arg2;
			if(arg2l.first().isEq(arg1)) return arg2l;
			arg2 = arg2l.rest();
		}
		return ZBoolean.FALSE;
	}
	
	private ZExpression memv_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , MEMV);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		ZExpression arg2 = argl.second();
		while(arg2.isPair()){
			ZPair arg2l = (ZPair)arg2;
			if(arg2l.first().isEqv(arg1)) return arg2l;
			arg2 = arg2l.rest();
		}
		return ZBoolean.FALSE;
	}
	
	private ZExpression min_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, MIN);
	}
	
	private ZExpression modulo_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, MODULO);
	}
	
	private ZExpression more_or_equal_to_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , MORE_OR_EQUAL_TO);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		if(!arg1.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg1);
		ZExpression arg2 = argl.second();
		if(!arg2.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg2);
		return (((ZNumber)arg1).compareTo((ZNumber)arg2) >= 0) ? ZBoolean.TRUE : ZBoolean.FALSE ;
	}
	
	private ZExpression more_than_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , MORE_THAN);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		if(!arg1.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg1);
		ZExpression arg2 = argl.second();
		if(!arg2.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg2);
		return (((ZNumber)arg1).compareTo((ZNumber)arg2) > 0) ? ZBoolean.TRUE : ZBoolean.FALSE ;
	}
	
	private ZExpression mul_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , MUL);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		if(!arg1.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg1);
		ZExpression arg2 = argl.second();
		if(!arg2.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg2);
		return ((ZNumber)arg1).mul((ZNumber)arg2);
	}
	
	private ZExpression negative_question_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, NEGATIVE_QUESTION);
	}
	
	private ZExpression not_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 1)) throw new ZException(ZError.WRONG_NB_ARGS , NOT);
		ZPair argl = (ZPair)arg;
		return (argl.first() == ZBoolean.FALSE) ? ZBoolean.TRUE : ZBoolean.FALSE;
	}
	
	private ZExpression null_question_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 1)) throw new ZException(ZError.WRONG_NB_ARGS , NULL_QUESTION);
		ZPair argl = (ZPair)arg;
		return (argl.first() == ZEmpty.EMPTY) ? ZBoolean.TRUE : ZBoolean.FALSE;
	}
	
	private ZExpression number_question_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 1)) throw new ZException(ZError.WRONG_NB_ARGS , NUMBER_QUESTION);
		ZPair argl = (ZPair)arg;
		return (argl.first().isNumber()) ? ZBoolean.TRUE : ZBoolean.FALSE;
	}
	
	private ZExpression number_to_string_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, NUMBER_TO_STRING);
	}
	
	private ZExpression numerator_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, NUMERATOR);
	}
	
	private ZExpression odd_question_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, ODD_QUESTION);
	}
	
	private ZExpression pair_question_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 1)) throw new ZException(ZError.WRONG_NB_ARGS , PAIR_QUESTION);
		ZPair argl = (ZPair)arg;
		return (argl.first().isPair()) ? ZBoolean.TRUE : ZBoolean.FALSE;
	}
	
	private ZExpression positive_question_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, POSITIVE_QUESTION);
	}
	
	private ZExpression quotient_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, QUOTIENT);
	}
	
	private ZExpression rational_question_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, RATIONAL_QUESTION);
	}
	
	private ZExpression rationalize_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, RATIONALIZE);
	}
	
	private ZExpression real_question_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, REAL_QUESTION);
	}
	
	private ZExpression real_part_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, REAL_PART);
	}
	
	private ZExpression remainder_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, REMAINDER);
	}
	
	private ZExpression reverse_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 1)) throw new ZException(ZError.WRONG_NB_ARGS , REVERSE);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		if(!arg1.isList()) throw new ZException(ZError.LIST_EXPECTED, arg1);
		ZExpression retVal = ZEmpty.EMPTY;
		while(arg1.isPair()){
			ZPair arg1l = (ZPair)arg1;
			retVal = arg1l.first().cons(retVal);
			arg1 = arg1l.rest();
		}
		return retVal;
	}
		
	private ZExpression round_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, ROUND);
	}
	
	private ZExpression set_car_exclamation_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , SET_CAR_EXCLAMATION);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		if(!arg1.isPair()) throw new ZException(ZError.PAIR_EXPECTED, arg1);
		((ZPair)arg1).rplca(argl.second());
		return ZEmpty.VOID;
	}
	
	private ZExpression set_cdr_exclamation_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , SET_CDR_EXCLAMATION);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		if(!arg1.isPair()) throw new ZException(ZError.PAIR_EXPECTED, arg1);
		((ZPair)arg1).rplcd(argl.second());
		return ZEmpty.VOID;
	}
	
	private ZExpression sin_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, SIN);
	}
	
	private ZExpression sqrt_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, SQRT);
	}
	
	private ZExpression string_to_number_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, STRING_TO_NUMBER);
	}
	
	private ZExpression string_to_symbol_(ZExpression arg) throws ZException {
		return ZEmpty.EMPTY;
	}
	
	private ZExpression sub_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 2)) throw new ZException(ZError.WRONG_NB_ARGS , SUB);
		ZPair argl = (ZPair)arg;
		ZExpression arg1 = argl.first();
		if(!arg1.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg1);
		ZExpression arg2 = argl.second();
		if(!arg2.isNumber()) throw new ZException(ZError.NUMBER_EXPECTED, arg2);
		return ((ZNumber)arg1).sub((ZNumber)arg2);
	}
	
	private ZExpression symbol_question_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 1)) throw new ZException(ZError.WRONG_NB_ARGS , SYMBOL_QUESTION);
		return ZBoolean.tf(arg.first().isSymbol());
	}
	
	private ZExpression symbol_to_string_(ZExpression arg) throws ZException {
		if(!this.isArgNbEqual(arg, 1)) throw new ZException(ZError.WRONG_NB_ARGS , SYMBOL_QUESTION);
		return ZEmpty.EMPTY;
	}
	
	private ZExpression tan_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, TAN);
	}
	
	private ZExpression truncate_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, TRUNCATE);
	}
	
	private ZExpression zero_question_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, ZERO_QUESTION);
	}

}
