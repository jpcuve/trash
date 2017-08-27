package algebra;

public class FpMat implements Mat<Double> {
    private final Dim dim;
    protected final double[] data;

    public FpMat(int colCount, int rowCount) {
        this(colCount, new double[colCount * rowCount]);
    }

    public FpMat(int colCount, double... data){
        this.dim = new Dim(colCount, data.length / colCount);
        this.data = data;
    }

    @Override
    public FpMat add(Mat<Double> m) {
        if (!dim.equals(m.getDim())) throw new IllegalArgumentException();
        final double[] dd = new double[dim.getArea()];
        System.arraycopy(data, 0, dd, 0, dd.length);
        for (int i = 0; i < dim.getWidth(); i++){
            for (int j = 0; j < dim.getHeight(); j++){
                dd[j * dim.getWidth() + i] += m.get(i, j);
            }
        }
        return new FpMat(dim.getWidth(), dd);
    }

    @Override
    public Double get(int col, int row) {
        return data[row * getDim().getWidth() + col];
    }

    @Override
    public Dim getDim() {
        return dim;
    }
}
