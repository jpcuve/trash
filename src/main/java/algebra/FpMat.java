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
        for (int col = 0; col < dim.getWidth(); col++) for (int row = 0; row < dim.getHeight(); row++) dd[row * dim.getWidth() + col] += m.get(col, row);
        return new FpMat(dim.getWidth(), dd);
    }

    @Override
    public FpMat scalar(Double aDouble) {
        final double[] dd = new double[dim.getArea()];
        System.arraycopy(data, 0, dd, 0, dd.length);
        for (int i = 0; i < dd.length; i++) dd[i] *= aDouble;
        return new FpMat(dim.getWidth(), dd);
    }

    @Override
    public FpMat multiply(Mat<Double> m) {
        if (dim.getWidth() != m.getDim().getHeight()) throw new IllegalArgumentException();
        final Dim d = new Dim(m.getDim().getWidth(), dim.getHeight());
        final double[] dd = new double[d.getArea()];
        for (int col = 0; col < d.getWidth(); col++) for (int row = 0; row < d.getHeight(); row++){
            int index = col * d.getWidth() + row;
            for(int i = 0; i < dim.getWidth(); i++){
                dd[index] += data[row * dim.getWidth() + i] * m.get(col, i);
            }
        }
        return new FpMat(d.getWidth(), dd);
    }

    @Override
    public Double get(int col, int row) {
        return data[row * getDim().getWidth() + col];
    }

    @Override
    public FpVec toVec() {
        return new FpVec(data);
    }

    @Override
    public Dim getDim() {
        return dim;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        boolean sp1 = false;
        sb.append(dim.getHeight()).append('x').append(dim.getWidth()).append('[');
        for (int row = 0; row < dim.getHeight(); row++) {
            if (sp1) sb.append(" | ");
            boolean sp2 = false;
            for (int col = 0; col < dim.getWidth(); col++) {
                if (sp2) sb.append(' ');
                sb.append(get(col, row));
                sp2 = true;
            }
            sp1 = true;
        }
        sb.append(']');
        return sb.toString();
    }

    public static void main(String[] args) {
        final FpMat m = new FpMat(3, 1, 2, 3, 4, 5, 6);
        System.out.println(m);
        final FpVec v = new FpVec(7, 8, 9);
        System.out.println(v);
        System.out.println(m.multiply(v));
    }
}
