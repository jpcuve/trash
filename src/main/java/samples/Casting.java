package samples;

public class Casting {

	public int plus(int a) {
		return(a);
	}
	
	public static void main(String[] args) {
		Integer a = new Integer(10);
		Object b = a;
		String s = (String)b;
	}
}