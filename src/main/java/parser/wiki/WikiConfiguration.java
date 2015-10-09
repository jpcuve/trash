package parser.wiki;

/**
 * @author jpc
 */
public class WikiConfiguration {
    private String multilineIgnoreCharacters = "{";
    private String inlineIgnoreCharacters = "[(/`";
    private String multiTagCharacters = "=-";
    private String tripleTagCharacters = "{}";
    private String doubleTagCharacters = ",~|/()";
    private String singleTagCharacters = "_*`^#[]";

    public WikiConfiguration() {
    }

    public WikiConfiguration(String multilineIgnoreCharacters, String inlineIgnoreCharacters, String multiTagCharacters, String tripleTagCharacters, String doubleTagCharacters, String singleTagCharacters) {
        this.multilineIgnoreCharacters = multilineIgnoreCharacters;
        this.inlineIgnoreCharacters = inlineIgnoreCharacters;
        this.multiTagCharacters = multiTagCharacters;
        this.tripleTagCharacters = tripleTagCharacters;
        this.doubleTagCharacters = doubleTagCharacters;
        this.singleTagCharacters = singleTagCharacters;
    }

    public String getMultilineIgnoreCharacters() {
        return multilineIgnoreCharacters;
    }

    public void setMultilineIgnoreCharacters(String multilineIgnoreCharacters) {
        this.multilineIgnoreCharacters = multilineIgnoreCharacters;
    }

    public String getInlineIgnoreCharacters() {
        return inlineIgnoreCharacters;
    }

    public void setInlineIgnoreCharacters(String inlineIgnoreCharacters) {
        this.inlineIgnoreCharacters = inlineIgnoreCharacters;
    }

    public String getMultiTagCharacters() {
        return multiTagCharacters;
    }

    public void setMultiTagCharacters(String multiTagCharacters) {
        this.multiTagCharacters = multiTagCharacters;
    }

    public String getTripleTagCharacters() {
        return tripleTagCharacters;
    }

    public void setTripleTagCharacters(String tripleTagCharacters) {
        this.tripleTagCharacters = tripleTagCharacters;
    }

    public String getDoubleTagCharacters() {
        return doubleTagCharacters;
    }

    public void setDoubleTagCharacters(String doubleTagCharacters) {
        this.doubleTagCharacters = doubleTagCharacters;
    }

    public String getSingleTagCharacters() {
        return singleTagCharacters;
    }

    public void setSingleTagCharacters(String singleTagCharacters) {
        this.singleTagCharacters = singleTagCharacters;
    }
}
