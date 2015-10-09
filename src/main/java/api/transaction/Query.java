package api.transaction;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 27, 2008
 * Time: 5:37:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class Query {
    public Query setParameter(final String name, final Object o){
        return this;
    }

    public Object getSingleResult() throws NoResultException{
        return null;
    }

    public List getResultList(){
        return null;
    }
}
