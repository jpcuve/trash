package lang.lambda;

public class ZInteger extends ZNumber {
	private long m_value;
	
	public boolean isComplex(){ return false; }
	public boolean isReal(){ return false; }
	public boolean isRational(){ return false; }
	public boolean isInteger(){ return true; }
	
	public ZInteger(long l){
		super(true);
		m_value = l;
	}
	
	public ZInteger(String s){
		this(Long.valueOf(s).longValue());
	}

    public long toLong() {
        return m_value;
    }

    public int compareTo(ZNumber z) throws ZException {
		return (int)(m_value - z.toInteger().m_value);
	}
	
	public ZNumber monOp(char op) throws ZException {
		long res = m_value;
		switch(op){
		case 'A': res = Math.abs(res);
		}
		return new ZInteger(res);
	}
	
	public ZNumber binOp(char op, ZNumber z) throws ZException {
		if(!z.isInteger()) return this.toReal().binOp(op, z);
		long res = m_value ;
		long arg = z.toInteger().m_value;
		switch(op){
		case '+': res += arg; break;
		case '-': res -= arg; break;
		case '*': res *= arg; break;
		case '/': if(res % arg != 0) return new ZReal((double)res / (double)arg);
				  else res /= arg;
				  break;
		case 'X': res = Math.max(res, arg); break;
		case 'M': res = Math.min(res, arg); break;
		case 'Q': res /= arg;
		case 'R': res -= (res / arg) * arg;
		case 'O': res %= arg;
		}
		return new ZInteger(res);
	}
	
	public String write(){
		return ""+ m_value;
	}
	
	public ZReal toReal(){
		return new ZReal((double)m_value);
	}
	
	public long toNatural() throws ZException {
		if(m_value <= 0) throw new ZException(ZError.NUMBER_INTEGER_NATURAL_EXPECTED, this);
		return m_value;
	}

	public String toString(){
		return "[ZIn=" + m_value + "]";
	}
}
