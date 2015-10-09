package api.transaction;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 27, 2008
 * Time: 5:30:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DecisionLocal {
    DecisionEntity findDecision(long decisionId);
    DecisionEntity findDecision(String reference);
    void insertDecision(String reference);
    boolean deleteDecision(long transactionId);
    List<Long> selectDecisions();
}
