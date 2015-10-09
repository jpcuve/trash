package samples;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import java.util.Hashtable;

public class Lookup {
	
	public static void main(String[] args)
	throws Exception {
		Hashtable h = new Hashtable();
		h.put("java.naming.factory.initial", "com.evermind.server.rmi.RMIInitialContextFactory");
		h.put("java.naming.provider.url", "ormi://10.1.1.23/cms");
		h.put("java.naming.security.principal", "admin");
		h.put("java.naming.security.credentials", "admin");
		Context ctx = new InitialContext(h);
		for (NamingEnumeration e = ctx.listBindings(""); e.hasMore(); ) {
			Binding b = (Binding)e.next();
		}
	}
}