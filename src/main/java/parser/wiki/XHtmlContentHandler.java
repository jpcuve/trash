package parser.wiki;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author jpc
 */
public class XHtmlContentHandler implements DomContentHandler {
    private final DocumentBuilder documentBuilder;
    private final Document document;
    private final WikiConfiguration configuration;
    private Stack<Node> docNodes = new Stack<Node>();
    private Stack<Node> lineNodes = new Stack<Node>();
    private StringBuilder text = new StringBuilder();
    private DocumentFragment dummy;
    private DocumentFragment accumulate;
    private LineType lineType = LineType.NEUTRAL;
    private int startingWhitespaceCount = 0;
    private Stack<Integer> listLevels = new Stack<Integer>();
    private boolean paragraph;
    private boolean list;
    private boolean table;

    private enum LineType{
        NEUTRAL,
        PARAGRAPH,
        LIST_ITEM,
        TABLE_ROW
    }

    public XHtmlContentHandler(final DocumentBuilder documentBuilder, final Document document) {
        this.documentBuilder = documentBuilder;
        this.document = document;
        this.configuration = new WikiConfiguration();
        this.dummy = document.createDocumentFragment();
        this.accumulate = document.createDocumentFragment();
    }

    public WikiConfiguration getConfiguration() {
        return configuration;
    }

    public Document getDocument() {
        return document;
    }

    public Node processAccumulated(final char tag, final Document document, final DocumentFragment df) {
        final StringBuilder sb = new StringBuilder();
        final List<Node> removes = new ArrayList<Node>();
        for (int i = 0; i < df.getChildNodes().getLength(); i++){
            final Node node = df.getChildNodes().item(i);
            if (node instanceof Text) sb.append(((Text) node).getWholeText());
            removes.add(node);
        }
        for (final Node node: removes) df.removeChild(node);
        switch(tag){
            case '/':
                return document.createComment(sb.toString());
            case ')':
                // assume sb is XML, so parse it
                try {
                    final Document doc = documentBuilder.parse(new ByteArrayInputStream(sb.toString().getBytes()));
                    return document.importNode(doc.getDocumentElement(), true);
                } catch (Exception e) {
                    return document.createTextNode("Error: " + e.getMessage());
                }
            case ']':
                int space = sb.indexOf(" ");
                final String href = space != -1 ? sb.substring(0, space) : sb.toString();
                final String cont = space != -1 ? sb.substring(space + 1) : sb.toString();
                final Element a = document.createElement("a");
                a.appendChild(document.createTextNode(cont));
                a.setAttribute("href", href);
                return a;
        }
        return document.createTextNode("!!! Accumulation: " + tag);
    }

    private List<Node> getChildren(final Node node){
        final List<Node> children = new ArrayList<Node>();
        for (int i = 0; i < node.getChildNodes().getLength(); i++) children.add(node.getChildNodes().item(i));
        return children;
    }

    private void addText(){
        final String s = text.toString();
        if (s.length() > 0) lineNodes.peek().appendChild(document.createTextNode(s));
        text.setLength(0);
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
        exitStructuresExcept(LineType.LIST_ITEM);
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

    private void addLine(final String tag){
        final Node parent = document.createElement(tag);
        parent.appendChild(dummy);
        docNodes.peek().appendChild(parent);
    }

    public void startDocument() throws WikiException {
        final Element eHtml = document.createElement("html");
        document.appendChild(eHtml);
        docNodes.push(eHtml);
        eHtml.appendChild(document.createElement("head"));
        pushDocNode(document.createElement("body"));
    }

    public void endDocument() throws WikiException {
        docNodes.pop();
    }

    public void startLine(int startingWhitespaceCount) throws WikiException {
        this.startingWhitespaceCount = startingWhitespaceCount;
//        lineType = startingWhitespaceCount == 0 ? LineType.NEUTRAL : LineType.PARAGRAPH;
        if (lineType == LineType.PARAGRAPH){
            if (paragraph) docNodes.pop();
            pushDocNode(document.createElement("p"));
            paragraph = true;
            lineType = LineType.NEUTRAL;
        }
        lineNodes.push(dummy);
        addText();
    }

    public void exitStructuresExcept(LineType exceptLineType){
        // stuff to do before writing out the line, expirationDate.licenseType. exit list and table structures
        if (list && lineType != LineType.LIST_ITEM && exceptLineType != LineType.LIST_ITEM){
            unwindList(0);
            list = false;
        }
        if (table && lineType != LineType.TABLE_ROW && exceptLineType != LineType.TABLE_ROW){
            docNodes.pop();
            table = false;
        }
    }

    public void endLine(String l) throws WikiException {
        addText();
        exitStructuresExcept(LineType.LIST_ITEM);
        exitStructuresExcept(LineType.TABLE_ROW);
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
//                JNodeTree.openFrame(dummy);
                Element lastTd  = null;
                for (final Node child: getChildren(dummy)){
                    if (child instanceof Element && "td".equals(((Element) child).getTagName())){
                        lastTd = (Element) child;
                    } else{
                        if (lastTd != null) lastTd.appendChild(child);
                    }
                }
                for (final Node child: getChildren(dummy)) if (child.getChildNodes().getLength() == 0) dummy.removeChild(child);
                addLine("tr");
                break;
            default:
                addLine("p");
                break;
        }
        lineNodes.clear();
        lineType = (l.length() == 0) ? LineType.PARAGRAPH : LineType.NEUTRAL;
    }

    public void startTag(String localName) throws WikiException {
        addText();
        char c = localName.charAt(0);
        Node node = null;
        switch(c){
            case '[':
            case '(':
            case '/':
                lineNodes.push(accumulate);
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
                node = document.createElement("h" + Math.min(localName.length(), 6));
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
        addText();
        char c = localName.charAt(0);
        switch(c){
            case ']':
            case ')':
            case '/':
                if (lineNodes.size() > 0 && lineNodes.peek() == accumulate){
                    lineNodes.pop();
                    lineNodes.peek().appendChild(processAccumulated(c, document, accumulate));
                }
                break;
            case '|':
                lineNodes.peek().appendChild(document.createElement("td"));
                break;
            case '*':
                if (lineType != LineType.LIST_ITEM) lineNodes.pop();
                break;
            case '#':
                break;
            case '}':
            case '=':
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
