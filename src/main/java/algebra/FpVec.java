package algebra;

public class FpVec extends FpMat implements Vec<Double> {
    public FpVec(int rowCount) {
        super(1, rowCount);
    }

    public FpVec(double... data) {
        super(1, data);
    }

    @Override
    public FpVec cross(Vec<Double> v) {
        return null;
    }

    @Override
    public FpVec add(Vec<Double> v) {
        final FpMat m = super.add(v);
        return new FpVec(m.data);
    }
}
