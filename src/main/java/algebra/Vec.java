package algebra;

public interface Vec<N extends Number> extends Mat<N> {
    Vec<N> cross(Vec<N> v);
    Vec<N> add(Vec<N> v);
}
