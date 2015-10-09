package parser.wiki;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.util.*;

/**
 * @author jpc
 */
public class DixitContentHandler implements DomContentHandler {
    private final WikiConfiguration wikiConfiguration;
    private final DocumentBuilder documentBuilder;
    private final Document document;
    private Stack<Node> docNodes = new Stack<Node>();
    private Stack<Node> lineNodes = new Stack<Node>();
    private StringBuilder text = new StringBuilder();
    private DocumentFragment dummy;
    private LineType lineType = LineType.NEUTRAL;
    private int startingWhitespaceCount = 0;
    private Stack<Integer> listLevels = new Stack<Integer>();
    private boolean list;
    private boolean table;
    private Stack<Integer> sectionLevels = new Stack<Integer>();

    private enum LineType{
        NEUTRAL,
        PARAGRAPH,
        LIST_ITEM,
        TABLE_ROW
    }

    public DixitContentHandler(final DocumentBuilder documentBuilder, final Document document) {
        this.documentBuilder = documentBuilder;
        this.document = document;
        this.wikiConfiguration = new WikiConfiguration();
        this.dummy = document.createDocumentFragment();
    }

    public WikiConfiguration getConfiguration() {
        return wikiConfiguration;
    }

    public Document getDocument() {
        return document;
    }

    private Node accumulation(char tag, String content, List<Node> nodes){
        switch(tag){
            case ']': // link
                int colon = content.indexOf(':');
                if (colon != -1){
                    final String data = content.substring(colon + 1).trim();
                    final String[] parameters = content.substring(0, colon).split(",");
                    if (parameters.length > 0){
                        final String tagName = parameters[0];
                        if ("ctmr".equalsIgnoreCase(tagName)){
                            final Element eCtmr = document.createElement("ctmr");
                            eCtmr.appendChild(document.createTextNode(data));
                            return eCtmr;
                        } else if ("directive".equalsIgnoreCase(tagName)){
                            final Element eDirective = document.createElement("directive");
                            eDirective.appendChild(document.createTextNode(data));
                            return eDirective;
                        } else if ("decision".equalsIgnoreCase(tagName)){
                            final Element eDecision = document.createElement("decision");
                            if (parameters.length > 1) eDecision.setAttribute("ref", parameters[1]);
                            if (parameters.length > 2) eDecision.setAttribute("stage", parameters[2]);
                            eDecision.appendChild(document.createTextNode(data));
                            return eDecision;
                        }
                    }
                }
                break;
            case ')': // xml content
                try {
                    final Document doc = documentBuilder.parse(new ByteArrayInputStream(content.getBytes()));
                    return document.importNode(doc.getDocumentElement(), true);
                } catch (Exception e) {
                    return document.createTextNode("Error: " + e.getMessage());
                }
            case '/': // comment
                return document.createComment(content);
        }
        return document.createTextNode("tag:" + tag + " " + content);
    }

    private void section(int level, final String content, final Node eTitle){
        unwindSections(level);
        sectionLevels.push(level);
        String title = content;
        // search for filter
        String filter = null;
        int lPar = content.indexOf('(');
        int rPar = content.indexOf(')');
        if (lPar != -1 && rPar == lPar + 3){
            filter = content.substring(lPar + 1, rPar);
            title = title.substring(0, lPar) + title.substring(rPar + 1, title.length());
        }
        // search for titles
        final Map<String, String> titles = new HashMap<String, String>();
        int colon = title.indexOf(':');
        if (colon == -1){
            if (title.trim().length() > 0) titles.put("", title.trim());
        } else{
            while (colon != -1){
                int space = title.lastIndexOf(' ', colon);
                if (space < 0) space = 0;
                if (space == colon - 3){
                    int next = title.indexOf(':', colon + 1);
                    next = next == -1 ? title.length() : title.lastIndexOf(' ', next);
                    if (next != -1){
                        titles.put(title.substring(space + 1, colon), title.substring(colon + 1, next).trim());
                    }
                }
                colon = title.indexOf(':', colon + 1);
            }
        }

        final Element eSection = document.createElement((filter != null ? filter + ":" : "") + "section");
        for (final Map.Entry<String, String> entry: titles.entrySet()) eSection.setAttribute((entry.getKey().length() != 0 ? entry.getKey() + ":" : "") + "title", entry.getValue());
//        eSection.setAttribute("data", s);
        pushDocNode(eSection);
    }

    private List<Node> getChildren(final Node node){
        final List<Node> children = new ArrayList<Node>();
        for (int i = 0; i < node.getChildNodes().getLength(); i++) children.add(node.getChildNodes().item(i));
        return children;
    }

    private String addText(){
        final String s = text.toString();
        if (s.length() > 0) lineNodes.peek().appendChild(document.createTextNode(s));
        text.setLength(0);
        return s;
    }

    private void pushDocNode(final Node node){
        docNodes.peek().appendChild(node);
        docNodes.push(node);
    }

    private void pushLineNode(final Node node){
        lineNodes.peek().appendChild(node);
        lineNodes.push(node);
    }

    private void listItem(char type){
        lineType = LineType.LIST_ITEM;
        if (listLevels.isEmpty() || startingWhitespaceCount > listLevels.peek()){
            listLevels.push(startingWhitespaceCount);
            pushDocNode(document.createElement(type == '*' ? "ul" : "ol"));
        } else unwindList(startingWhitespaceCount);
        list = true;
    }

    private void unwindList(int limit){
        while (!listLevels.isEmpty() && listLevels.peek() > limit){
            listLevels.pop();
            docNodes.pop();
        }
    }

    private void unwindSections(int limit){
        while (!sectionLevels.isEmpty() && sectionLevels.peek() >= limit){
            sectionLevels.pop();
            docNodes.pop();
        }
    }

    private void addLine(final String tag){
        final Node parent = document.createElement(tag);
        parent.appendChild(dummy);
        docNodes.peek().appendChild(parent);
    }

    public void startDocument() throws WikiException {
        final Element eDixitContent = document.createElement("dixit-content");
        eDixitContent.setAttribute("xmlns:fr", "http://www.darts-ip.com/fr");
        eDixitContent.setAttribute("xmlns:en", "http://www.darts-ip.com/en");
        document.appendChild(eDixitContent);
        docNodes.push(eDixitContent);
        final Element eArticle = document.createElement("article");
        eArticle.setAttribute("restricted", "false");
        pushDocNode(eArticle);
    }

    public void endDocument() throws WikiException {
        unwindSections(0);
        docNodes.pop();
    }

    public void startLine(int startingWhitespaceCount) throws WikiException {
        this.startingWhitespaceCount = startingWhitespaceCount;
        lineType = startingWhitespaceCount == 0 ? LineType.NEUTRAL : LineType.PARAGRAPH;
        lineNodes.push(dummy);
        addText();
    }

    public void endLine(String l) throws WikiException {
        addText();
        // stuff to do before writing out the line, expirationDate.licenseType. exit list and table structures
        if (list && lineType != LineType.LIST_ITEM){
            unwindList(0);
            list = false;
        }
        if (table && lineType != LineType.TABLE_ROW){
            docNodes.pop();
            table = false;
        }
        // write out line
        switch(lineType){
            case NEUTRAL:
                addLine("span");
                break;
            case PARAGRAPH:
                addLine("p");
                break;
            case LIST_ITEM:
                addLine("li");
                break;
            case TABLE_ROW:
                Element lastTd  = null;
                for (final Node child: getChildren(dummy)){
                    if (child instanceof Element && "td".equals(((Element) child).getTagName())){
                        lastTd = (Element) child;
                    } else{
                        if (lastTd != null) lastTd.appendChild(child);
                    }
                }
                addLine("tr");
                break;
            default:
                addLine("p");
                break;
        }
        lineNodes.clear();
    }

    public void startTag(String localName) throws WikiException {
        addText();
        char c = localName.charAt(0);
        Node node = null;
        switch(c){
            case '[':
            case '(':
            case '/':
                lineNodes.push(document.createElement("accumulate"));
                break;
            case '|':
                lineType = LineType.TABLE_ROW;
                if (!table){
                    pushDocNode(document.createElement("table"));
                    table = true;
                }
                lineNodes.peek().appendChild(document.createElement("td"));
                break;
            case '*':
                if (startingWhitespaceCount > 0) listItem('*');
                else node = document.createElement("b");
                break;
            case '#':
                if (startingWhitespaceCount > 0) listItem('#');
                break;
            case '{':
                node = document.createElement("code");
                break;
            case '=':
                lineNodes.push(document.createElement("title"));
                break;
            case '`':
                node = document.createElement("tt");
                break;
            case '_':
                node = document.createElement("i");
                break;
            case '^':
                node = document.createElement("sup");
                break;
            case ',':
                node = document.createElement("sub");
                break;
            case '~':
                node = document.createElement("strike");
                break;
        }
        if (node != null) pushLineNode(node);
    }

    public void endTag(String localName) throws WikiException {
        final String s = addText();
        char c = localName.charAt(0);
        switch(c){
            case ']':
            case ')':
            case '/':
                final Node eAccumulate = lineNodes.pop();
                lineNodes.peek().appendChild(accumulation(c, s, getChildren(eAccumulate)));
                break;
            case '|':
                lineNodes.peek().appendChild(document.createElement("td"));
                break;
            case '*':
                if (lineType != LineType.LIST_ITEM) lineNodes.pop();
                break;
            case '#':
            case '=':
                final Node eTitle = lineNodes.pop();
                section(localName.length(), s, eTitle);
                break;
            case '}':
            case '`':
            case '_':
            case '^':
            case ',':
            case '~':
                lineNodes.pop();
                break;
        }
    }

    public void characters(char[] ch, int start, int length) throws WikiException {
        final String s = new String(ch, start, length);
        text.append(s);
    }
}
