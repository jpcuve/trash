package api.transaction;

/**
 * @author jpc
 */
public class Test {
    public static void main(String[] args) throws Exception {
        DecisionSession.getInstance().insertDecision("some decision");
        final DecisionEntity decision = DecisionSession.getInstance().findDecision("some decision");

    }
}
