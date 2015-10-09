package api.transaction;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 27, 2008
 * Time: 5:30:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocumentLocal {
    DocumentEntity findDocument(long documentId);
    DocumentEntity findDocument(long decisionId, String language);
    void insertDocument(long decisionId, String language);
    void updateDocumentLocation(long documentId, String location);
}
