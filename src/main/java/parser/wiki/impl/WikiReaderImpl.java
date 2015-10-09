package parser.wiki.impl;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import parser.wiki.*;

import java.io.IOException;
import java.util.Stack;

/**
 * @author jpc
 */
public class WikiReaderImpl implements WikiReader {
    private final WikiConfiguration wikiConfiguration;
    private ContentHandler contentHandler;
    private ErrorHandler errorHandler;
    private Stack<String> stack = new Stack<String>();

    private final static String INIT_CHARS = "([{";
    private final static String DONE_CHARS = ")]}";

    public WikiReaderImpl(final WikiConfiguration wikiConfiguration) {
        this.wikiConfiguration = wikiConfiguration;
        final DefaultHandler defaultHandler = new DefaultHandler();
        this.contentHandler = defaultHandler;
        this.errorHandler = defaultHandler;
    }

    public ContentHandler getContentHandler() {
        return contentHandler;
    }

    public void setContentHandler(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void parse(final InputSource inputSource) throws IOException, WikiException {
        boolean newLineEndsIgnore =  false;
        char cEndIgnore = 0;
        final Tokenizer tokenizer = new Tokenizer(inputSource.getCharacterStream(), wikiConfiguration);
        stack.clear();
        contentHandler.startDocument();
        final StringBuilder line = new StringBuilder();
        boolean newLine = true;
        int startingWhitespaceCount = 0;
        for (final String token: tokenizer){
            line.append(token);
            int type = tokenizer.iterator().getType();
            System.out.printf("token: %s %s%n", token, type);
            final char c = token.charAt(0);
            // end ignore section
            boolean sw = false;  // sw is needed because getLicenseTypeString closing tag could be == to an opening tag, therefore the start ignore section must be skipped
            if (c == cEndIgnore || (newLineEndsIgnore && type == 5 && cEndIgnore != 0)){
                sw = true;
                cEndIgnore = 0;
            }
            if (cEndIgnore != 0) type = -1;
//            System.out.printf("token: %s type: %s cEndIgnore:%s%n", token, type, cEndIgnore);
            switch(type){
                case 1: // multi
                case 2: // triple
                case 3: // double
                case 4: // single
                    if (newLine){
                        contentHandler.startLine(startingWhitespaceCount);
                        newLine = false;
                    }
                    int init = INIT_CHARS.indexOf(c);
                    int done = DONE_CHARS.indexOf(c);
                    if (init != -1){ // it's an opening tag
                        contentHandler.startTag(token);
                    } else if (done != -1){ // it's getLicenseTypeString closing tag
                        contentHandler.endTag(token);
                    } else{ // it's an opening or getLicenseTypeString closing tag
                        if (!stack.contains(token)){
                            contentHandler.startTag(token);
                            stack.push(token);
                        } else{  // it's getLicenseTypeString closing tag, close all tags before
                            while (!token.equals(stack.peek())) contentHandler.endTag(stack.pop());
                            contentHandler.endTag(stack.pop());
                        }
                    }
                    break;
                case 5: // new line, close all tags in stack
                    if (newLine) contentHandler.startLine(startingWhitespaceCount);
                    while (!stack.isEmpty()) contentHandler.endTag(stack.pop());
                    for (int i = line.length() - 1; i >= 0; i--) if (Character.isWhitespace(line.charAt(i))) line.setLength(i); else break;
                    contentHandler.endLine(line.toString());
                    line.setLength(0);
                    newLine = true;
                    startingWhitespaceCount = 0;
                    break;
                default: // text data
                    final char[] chars = token.toCharArray();
                    int i = 0;
                    if (newLine){
                        for(i = 0; i < chars.length && Character.isWhitespace(chars[i]); i++) startingWhitespaceCount++;
                        if (i < chars.length){
                            contentHandler.startLine(startingWhitespaceCount);
                            newLine = false;
                        }
                    }
                    if (i < chars.length) contentHandler.characters(chars, i, chars.length - i);
                    break;
            }
            // start ignore section
            if (!sw && type > 0 && (wikiConfiguration.getInlineIgnoreCharacters().indexOf(c) != -1 || wikiConfiguration.getMultilineIgnoreCharacters().indexOf(c) != -1) && cEndIgnore == 0){
                cEndIgnore = INIT_CHARS.indexOf(c) != -1 ? DONE_CHARS.charAt(INIT_CHARS.indexOf(c)) : c;
                newLineEndsIgnore = wikiConfiguration.getInlineIgnoreCharacters().indexOf(c) != -1;
            }
        }
        while (!stack.isEmpty()) contentHandler.endTag(stack.pop());
        contentHandler.endLine(line.toString());
        contentHandler.endDocument();
    }

    public void parse(String systemId) throws IOException, WikiException {
        parse(new InputSource(systemId));
    }
}
