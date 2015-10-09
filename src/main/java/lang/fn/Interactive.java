package lang.fn;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Interactive {
	public static void main(String[] args) throws FnException, IOException {
		Interpreter ip = new Interpreter();
		Environment env = new Environment();
		Parser p = new Parser(ip);
		Core c = new Core();
		c.bind(p, env);
		Reader r = new InputStreamReader(System.in);
		// Reader r = new FileReader("test.fn");
		Tokenizer t = new Tokenizer(r);
		Expression e = null;
		do {
			try {
				e = p.read(t);
				if (e == null) throw new FnException(Error.SYNTAX, e);
				System.out.print(e.write() + " => ");
				Expression v = ip.eval(e, env);
				System.out.println(v.write());
			} catch (FnException ex) {
			}
		} while(e != null);
	}
}