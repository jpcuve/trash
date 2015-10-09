package api.transaction;

import java.lang.reflect.*;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * @author jpc
 */
public class PersistenceInvocationHandler<E> implements InvocationHandler {
    private static final Log LOG = new Log();
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("darts");
    private Class<? extends E> clazz;
    private E original;
    private static ThreadLocal<Stack<EntityManager>> ems = new ThreadLocal<Stack<EntityManager>>();

    @SuppressWarnings("unchecked")
    public static <E> E getProxyInstance(final Class<? extends E> clazz){
        return (E) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new PersistenceInvocationHandler(clazz));
    }

    private PersistenceInvocationHandler(final Class<? extends E> clazz){
        this.clazz = clazz;
        try{
            this.original = clazz.newInstance();
        } catch(Exception x){
            throw new RuntimeException("cannot instantiate object of class: " + clazz);
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        TransactionAttributeType transactionAttributeType = TransactionAttributeType.REQUIRED;
        boolean commit = false;
        EntityManager em = null;
        Stack<EntityManager> stack = ems.get();
        if (stack == null){
            stack = new Stack<EntityManager>();
            ems.set(stack);
        }
        try {
            final Method originalMethod = clazz.getMethod(method.getName(), method.getParameterTypes());
            final TransactionAttribute transactionAttribute = originalMethod.getAnnotation(TransactionAttribute.class);
            if (transactionAttribute != null) transactionAttributeType = transactionAttribute.value();
            LOG.debug(String.format("method: %s, transaction attribute type: %s%n", method.getName(), transactionAttributeType));
            commit = false;
            switch(transactionAttributeType){
                case SUPPORTS:
                    try{
                        em = stack.peek();
                    } catch(EmptyStackException x){
                        em = null;
                    }
                    break;
                case REQUIRED:
                    try{
                        em = stack.peek();
                    } catch(EmptyStackException x){
                        em = entityManagerFactory.createEntityManager();
                        final EntityTransaction tr = em.getTransaction();
                        LOG.debug(String.format("beginning transaction: %s", tr));
                        tr.begin();
                        stack.push(em);
                        commit = true;
                    }
                    break;
                case REQUIRES_NEW:
                    em = entityManagerFactory.createEntityManager();
                    final EntityTransaction tr = em.getTransaction();
                    LOG.debug(String.format("beginning transaction: %s", tr));
                    tr.begin();
                    stack.push(em);
                    break;
            }
            for (final Field field: clazz.getDeclaredFields()){
                if (field.isAnnotationPresent(PersistenceContext.class)){
                    field.setAccessible(true);
                    field.set(original, em);
                }
            }
            result = method.invoke(original, args);
            if (commit){
                em = stack.peek();
                final EntityTransaction tr = em.getTransaction();
                LOG.debug(String.format("committing transaction: %s", tr));
                tr.commit();
            }
        } catch(PersistenceException x){
            if (commit){
                em = stack.peek();
                final EntityTransaction tr = em.getTransaction();
                if (tr.isActive()){
                    LOG.debug(String.format("rolling back transaction: %s", tr));
                    tr.rollback();
                }
            }
            throw x;
        } catch (InvocationTargetException x) {
            throw x.getTargetException();
        } catch (Exception x) {
            throw new RuntimeException("unexpected invocation exception: " + x.getMessage());
        } finally {
            if (commit){
                em = stack.pop();
                em.close();
            }
        }
        return result;
    }
}
