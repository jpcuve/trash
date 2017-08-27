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
        return new FpVec(super.add(v).data);
    }

    public FpVec scalar(double aDouble){
        return new FpVec(super.scalar(aDouble).data);
    }
}
