package api.transaction;

import java.util.List;

/**
 * @author jpc
 */
public class DecisionSession implements DecisionLocal {
    private static DecisionLocal decision = PersistenceInvocationHandler.getProxyInstance(DecisionSession.class);

    public static DecisionLocal getInstance(){
        return decision;
    }

    @PersistenceContext
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public DecisionEntity findDecision(long decisionId) {
        return em.find(DecisionEntity.class, decisionId);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public DecisionEntity findDecision(String reference) {
        try{
            return (DecisionEntity) em.createNamedQuery("query by ref").setParameter("reference", reference).getSingleResult();
        } catch(NoResultException x){
            return null;
        }
    }

    public void insertDecision(String reference) {
        final DecisionEntity decision = new DecisionEntity();
        decision.setReference(reference);
        em.persist(decision);
    }

    public boolean deleteDecision(long decisionId) {
        final DecisionEntity decision = findDecision(decisionId);
        if (null == decision) return false;
        em.remove(decision);
        return true;
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Long> selectDecisions() {
        return (List<Long>) em.createNamedQuery("Decision.all").getResultList();
    }
}
