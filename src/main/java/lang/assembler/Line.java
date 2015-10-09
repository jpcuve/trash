package lang.assembler;

/**
 * Created by jpc on 13/09/15.
 */
public class Line {
    final String label;
    final String operation;
    final Mode mode;
    final int argument;
    final String comment;

    public Line(String label, String operation, Mode mode, int argument, String comment) {
        this.label = label;
        this.operation = operation;
        this.mode = mode;
        this.argument = argument;
        this.comment = comment;
    }
}
