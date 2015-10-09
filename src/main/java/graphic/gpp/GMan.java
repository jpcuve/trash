package graphic.gpp;

import java.awt.*;

public class GMan extends GPicture {
    private GCircle head;
    private GLine body;
    private GLine leftLeg;
    private GLine rightLeg;
    private GLine arms;

    public GMan(int x, int y){
        super(x, y);
        head = new GCircle(5, 0, 5);
        this.add(head);
        Point neck = new Point(10, 10);
        Point leftHand = new Point(0, 15);
        Point rightHand = new Point(20, 15);
        Point leftFoot = new Point(0, 30);
        Point rightFoot = new Point(20, 30);
        Point sex = new Point(10, 20);
        body = new GLine(neck, sex);
        this.add(body);
        leftLeg = new GLine(sex, leftFoot);
        this.add(leftLeg);
        rightLeg = new GLine(sex, rightFoot);
        this.add(rightLeg);
        arms = new GLine(leftHand, rightHand);
        this.add(arms);
    }

}
