package parser.pdf;

public class CrossReference implements Comparable<CrossReference> {
    private final int position;
    private final int generation;
    private boolean inUse;

    public CrossReference(final String desc){
        position = Integer.parseInt(desc.substring(0, 10));
        generation = Integer.parseInt(desc.substring(11, 16));
        inUse = desc.charAt(17) == 'n';
    }

    public CrossReference(int position, int generation, boolean inUse) {
        this.position = position;
        this.generation = generation;
        this.inUse = inUse;
    }

    public int getPosition() {
        return position;
    }

    public int getGeneration() {
        return generation;
    }

    public boolean isInUse() {
        return inUse;
    }

    public int compareTo(CrossReference o) {
        if (o == this) return 0;
        return position - o.position;
    }

    public String toString() {
        return String.format("[%s %s %s]", position, generation, inUse ? "n" : "f");
    }
}
