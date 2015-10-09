package lang.pool;

public interface ItemFactory<E> {
    E create();
    void activate(E e);
    void passivate(E e);
    void destroy(E e);
    void pulse(E e);
}
