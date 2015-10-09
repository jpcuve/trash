package lang.interpreter;

public abstract class Procedure extends Expression {

	public abstract Expression apply(Interpreter ip, Expression arg, Environment env) throws InterpreterException;
	
	public String toString(){
		return "[Pr=" + super.toString() + "]";
	}

	public String write(){
		return "#<procedure>";
	}
}