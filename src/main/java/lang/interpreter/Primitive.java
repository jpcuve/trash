package lang.interpreter;

public class Primitive extends Procedure {
	private Pack pack;
	private int index;
	private String name;
	private int minArgNb;
	private int maxArgNb;
	
	public Primitive(Pack p, int index, String name, int minArgNb, int maxArgNb){
		pack = p;
		this.index = index;
		this.name = name;
		this.minArgNb = minArgNb;
		this.maxArgNb = maxArgNb;
	}
	
	public String getName(){
		return name;
	}

	public Expression apply(Interpreter i, Expression arg, Environment env) throws InterpreterException {
		long n = (arg instanceof Pair) ? ((Pair)arg).length() : 0;
		if(n < minArgNb || n > maxArgNb) throw new InterpreterException(Error.WRONG_NB_ARGS, name);
		return pack.f(index, i, arg, env);
	}
	
}