package lang.lambda;

public class ZReal extends ZNumber {
	private double m_value;
	
	public boolean isComplex(){ return false; }
	public boolean isReal(){ return true; }
	public boolean isRational(){ return false; }
	public boolean isInteger(){ return false; }
	
	public ZReal(double d){
		super(false);
		m_value = d;
	}
	
	public ZReal(String s){
		this(Double.valueOf(s).doubleValue());
	}
	
	protected ZReal(double d, boolean exact){
		super(exact);
		m_value = d;
	}

    public long toLong() {
        return (long)m_value;
    }

    public int compareTo(ZNumber z) throws ZException {	return (int)(m_value - z.toReal().m_value);	}
	
	public ZNumber monOp(char op) throws ZException {
		double res = m_value;
		switch(op){
		case 'A': res = Math.abs(res);
		}
		return new ZReal(res);
	}
	
	public ZNumber binOp(char op, ZNumber z) throws ZException {
		double res = m_value ;
		double arg = z.toReal().m_value;
		switch(op){
		case '+': res += arg; break;
		case '-': res -= arg; break;
		case '*': res *= arg; break;
		case '/': res /= arg; break;
		case 'X': res = Math.max(res, arg); break;
		case 'M': res = Math.min(res, arg); break;
		}
		return new ZReal(res);
	}
	
	public String write(){
		return "" + m_value;
	}
	
	public String toString(){
		return "[ZRe=" + m_value + "]";
	}
}
