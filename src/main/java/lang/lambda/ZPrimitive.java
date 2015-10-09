package lang.lambda;

public class ZPrimitive extends ZProcedure {
	private ZPackage m_pack;
	private int m_index;
	private String m_name;
	private int m_minArgNb;
	private int m_maxArgNb;
	
	public ZPrimitive(ZPackage p, int index, String name, int minArgNb, int maxArgNb){
		m_pack = p;
		m_index = index;
		m_name = name;
		m_minArgNb = minArgNb;
		m_maxArgNb = maxArgNb;
	}
	
	public boolean isPrimitive(){ return true; }
	public boolean isClosure(){ return false; }
	
	public String getName(){
		return m_name;
	}

	public ZExpression apply(ZInterpreter i, ZExpression arg) throws ZException {
		long n = (arg.isPair()) ? ((ZPair)arg).length() : 0;
		if(n < m_minArgNb || n > m_maxArgNb) throw new ZException(ZError.WRONG_NB_ARGS, m_name);
		return m_pack.f(m_index, arg);
	}
}
