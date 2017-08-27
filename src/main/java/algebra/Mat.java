package algebra;

public interface Mat<N extends Number> {
    Mat<N> add(Mat<N> m);
    Mat<N> scalar(N n);
    Mat<N> multiply(Mat<N> m);
    Dim getDim();
    N get(int col, int row);
    Vec<N> toVec();
}
