package api.transaction;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 27, 2008
 * Time: 5:31:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentSession implements DocumentLocal {
    private static DocumentLocal document = PersistenceInvocationHandler.getProxyInstance(DocumentSession.class);

    public static DocumentLocal getInstance(){
        return document;
    }

    @PersistenceContext
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public DocumentEntity findDocument(long documentId) {
        return em.find(DocumentEntity.class, documentId);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public DocumentEntity findDocument(long decisionId, String language) {
        try{
            return (DocumentEntity) em.createNamedQuery("Document.byDecisionByLanguage").setParameter("decision", decisionId).setParameter("language", language).getSingleResult();
        } catch(NoResultException x){
            return null;
        }
    }

    public void insertDocument(long decisionId, String language) {
        final DecisionEntity decision = DecisionSession.getInstance().findDecision(decisionId);
        if (decision != null){
            final DocumentEntity document = new DocumentEntity();
            document.setDecision(decision);
            document.setLocale(language);
            document.setLocation(""); // mmh
            em.persist(document);
        }
    }

    public void updateDocumentLocation(long documentId, String location) {
        final DocumentEntity document = findDocument(documentId);
        if (document != null){
            document.setLocation(location);
            em.merge(document);
        }
    }
}
