package lang.lambda;

public class ZException extends Throwable {
	private ZError m_error;
	private Object m_value;
	
	public ZException(ZError e, Object o){
		m_error = e;
		m_value = o;
	}
	
	public String toString(){
		return m_error.toString() + " : " + ((m_value instanceof ZExpression) ? ((ZExpression)m_value).write() : m_value.toString());
	}
	
}
