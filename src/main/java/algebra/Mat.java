package algebra;

public interface Mat<N extends Number> {
    Mat<N> add(Mat<N> n);
    Dim getDim();
    N get(int col, int row);
}
