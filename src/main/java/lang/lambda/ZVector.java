package lang.lambda;

import java.util.Enumeration;
import java.util.Vector;

public class ZVector extends ZExpression {
	
	private Vector m_value;

	public ZVector(){
		m_value = new Vector();
	}
	
	public boolean isBoolean() { return false; }
	public boolean isSymbol() { return false; }
	public boolean isChar() { return false; }
	public boolean isVector() { return true; }
	public boolean isProcedure() { return false; }
	public boolean isPair() { return false; }
	public boolean isNumber() { return false; }
	public boolean isString() { return false; }
	public boolean isPort() { return false; }
	public boolean isLink() { return false; }
	
	public boolean isList(){ return false; }
	
	public boolean isEqual(ZExpression z){
		if(!z.isVector()) return false;
		Enumeration e1 = m_value.elements();
		Enumeration e2 = ((ZVector)z).elements();
		while(e1.hasMoreElements()){
			if(!e2.hasMoreElements()) return false;
			ZExpression z1 = (ZExpression)e1.nextElement();
			ZExpression z2 = (ZExpression)e2.nextElement();
			if(!z1.isEqual(z2)) return false;
		}
		if(e2.hasMoreElements()) return false;
		return true;
	}

	public void addElement(Object o){
		m_value.addElement(o);
	}
	
	public Enumeration elements(){
		return m_value.elements();
	}
	
	public boolean isElement(ZExpression z){
		for(Enumeration e1 = m_value.elements(); e1.hasMoreElements();) if(z == e1.nextElement()) return true;
		return false;
	}
	
	public String write(){
		StringBuffer sb = new StringBuffer(1024);
		sb.append("#(");
		for(Enumeration e1 = m_value.elements(); e1.hasMoreElements();){
			ZExpression cur = (ZExpression)e1.nextElement();
			sb.append(cur.write() + " ");
		}
		sb.append(")");
		return sb.toString();
	}
	
	public ZPair toList(){
		ZPair retVal = null;
		ZPair cur = null;
		for(Enumeration e1 = m_value.elements(); e1.hasMoreElements();){
			ZExpression z = (ZExpression)e1.nextElement();
			if(cur == null){
				cur = new ZPair(z);
				retVal = cur;
			}else{
				ZPair temp = new ZPair(z);
				cur.rplcd(temp);
				cur = temp;
			}
		}
		return retVal;
	}
	
	// JAVA support

	public String toString(){
		StringBuffer sb = new StringBuffer(1024);
		sb.append("[ZVe=");
		for(Enumeration e1 = m_value.elements(); e1.hasMoreElements();) sb.append(e1.nextElement());
		sb.append("]");
		return sb.toString();
	}

}
