package lang.lambda;

public class ZClosure extends ZProcedure {
	private ZExpression m_params;
	private ZExpression m_body;
	private ZEnvironment m_env;
	
	public ZClosure(ZExpression params, ZExpression body, ZEnvironment env) throws ZException {
		m_params = params;
		m_env = env;
		m_body = (body.rest() == ZEmpty.EMPTY) ? body.first() : ZSymbol.BEGIN.cons(body);
		System.out.println("Closure created: params=" + m_params.write() + ", body=" + m_body.write());
	}

	public boolean isPrimitive(){ return false; }
	public boolean isClosure(){ return true; }
	public boolean isSpecial(){ return false; }
	public boolean isTail(){ return false; }
	
	public ZExpression apply(ZInterpreter ip, ZExpression arg) throws ZException{
		return ip.eval(m_body, new ZEnvironment(m_env, m_params, arg));
	}
	
	public ZExpression getBody(){
		return m_body;
	}
	
	public ZExpression getParameters(){
		return m_params;
	}
	
	public ZEnvironment getEnvironment(){
		return m_env;
	}
	
}
