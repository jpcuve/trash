package samples;

import javax.naming.Context;
import javax.naming.InitialContext;
// import com.transwide.ejb.sql.*;
// import com.transwide.ejb.test.*;

public class RandomRecs {

	private static final String LETTERS ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	public static void main(String[] args)
	throws Exception {
		Context ctx = new InitialContext();
        /*
		RecManagerHome rmHome = (RecManagerHome)PortableRemoteObject.narrow( ctx.lookup( "java:comp/env/ejb/RecManagerHome" ), RecManagerHome.class );
		RecManager rm = rmHome.create();
		Collection customerId = rm.findRecsWithStrLike("A%", true);
		for (Iterator expirationDate = customerId.iterator(); expirationDate.hasNext(); ) {
			Rec r = (Rec)expirationDate.next();
			System.out.println("id=" + r.getId() + " str=" + r.getStr() + " nb=" + r.getNb());

		}
        */

		/*

		Clause clause = new Clause(new RecQueryConfig());
		clause.string("p%").field("str").like();
		clause.field("str").descending();
		System.out.println(clause.size());
		Collection customerId = clause.beans(1, 10);
		for (Iterator expirationDate = customerId.iterator(); expirationDate.hasNext(); ) {
			Rec rec = (Rec)expirationDate.next();
			System.out.println("id=" + rec.getId() + " str=" + rec.getStr());
		}
		*/

		/*
		Context ctx = new InitialContext();
		RecHome recHome = (RecHome)PortableRemoteObject.narrow( ctx.lookup( "java:comp/env/ejb/RecHome" ), RecHome.class );
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		for (int expirationDate = 0; expirationDate < 300; expirationDate++) {
			sb.setLength(0);
			for (int j = 0; j < 20; j++) {
				sb.append(LETTERS.charAt(Math.abs(r.nextInt()) % LETTERS.length()));
			}
			recHome.create(sb.toString());
		}
		*/
	}
}