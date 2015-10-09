package parser.pdf.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.pdf.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static parser.pdf.impl.Token.*;


public class Parser {
    private static final Charset CHARSET_ASCII = Charset.forName("US-ASCII");
    private static final Logger LOG = LoggerFactory.getLogger(Parser.class);
    private static final Map<String, Class> TYPES = new HashMap<String, Class>();
    static{
        TYPES.put("Catalog", Catalog.class);
        TYPES.put("Pages", Pages.class);
        TYPES.put("Page", Page.class);
        TYPES.put("Font", Font.class);
        TYPES.put("XRef", XRef.class);
        TYPES.put("Metadata", Metadata.class);
    }

    private final File file;
    private final long position;
    private final Tokenizer tokenizer;
    private final Tokenizer.TokenizerIterator i;
    private Token lookahead;

    public Parser(final File file, final long position) throws IOException {
        this.file = file;
        this.position = position;
        this.tokenizer = new Tokenizer(new RandomAccessFileInputStream(file, position));
        this.i = tokenizer.iterator();
    }

    public Object parse() throws IOException {
        long current = i.getPosition();
        lookahead = i.next();
        return literal(current);
    }

    private void accept(Token token) throws IOException {
        if (lookahead != token) throw new IOException("syntax error: '" + token + "' expected, found: " + new String(i.getValue(), CHARSET_ASCII));
        lookahead = i.next();
    }

    private static PdfDictionary newDictionaryInstance(final Map<String, Object> map, final boolean stream){
        if (map == null) return null;
        PdfDictionary dict = null;
        final Object type = map.get("Type");
        if (type != null){
            final Class clazz = TYPES.get(type.toString());
            if (clazz != null) try {
                dict = (PdfDictionary) clazz.newInstance();
            } catch (InstantiationException e) {
                // ignore
            } catch (IllegalAccessException e) {
                // ignore
            }
        }
        if (dict == null) dict = stream ? new PdfStream() : new PdfDictionary();
        dict.putAll(map);
        return dict;
    }

    public PdfDictionary parseDictionary(){
        try{
            final Object o = parse();
            return (o instanceof PdfDictionary) ? (PdfDictionary) o : null;
        } catch(IOException x){
            LOG.error("cannot parse", x);
            return null;
        }
    }

    public PdfObject parseObject(){
        try{
            final Object o = parse();
            return o instanceof PdfObject ? (PdfObject) o : null;
        } catch(IOException x){
            LOG.error("cannot parse", x);
            return null;
        }
    }

    public Object literal(long current) throws IOException {
        final byte[] bytes = i.getValue();
        if (bytes.length <= 0) throw new IOException("not a token");
        final String s = new String(bytes, CHARSET_ASCII);
        switch(lookahead){
            case NULL:
                accept(NULL);
                return null;
            case BOOLEAN:
                accept(BOOLEAN);
                return bytes[0] == 't';
            case NUMBER:
                accept(NUMBER);
                return s.indexOf('.') != -1 ? new BigDecimal(s) :new BigInteger(s);
            case NAME:
                accept(NAME);
                return s.substring(1);
            case STRING:
                accept(STRING);
                return new PdfString(bytes);
            case REFERENCE:
                accept(REFERENCE);
                return new PdfReference(s);
            case LEFT_BRACKET:
                final PdfArray array = new PdfArray();
                array.setPosition(position + current);
                accept(LEFT_BRACKET);
                while (lookahead != RIGHT_BRACKET) array.add(literal(i.getPosition()));
                accept(RIGHT_BRACKET);
                return array;
            case DICTIONARY_INIT:
                final Map<String, Object> map = new HashMap<String, Object>();
                accept(DICTIONARY_INIT);
                while (lookahead != DICTIONARY_DONE){
                    if (i.getValue().length <= 0) throw new IOException("not a token");
                    final String name = new String(i.getValue(), 1, i.getValue().length - 1, CHARSET_ASCII);
                    accept(NAME);
                    map.put(name, literal(i.getPosition()));
                }
                accept(DICTIONARY_DONE);
                boolean isStream = false;
                if (lookahead == STREAM_INIT){
                    accept(STREAM_INIT);
                    isStream = true;
                }
                final PdfDictionary dictionary = newDictionaryInstance(map, isStream);
                dictionary.setPosition(position + current);
                if (dictionary instanceof PdfStream){
                    final PdfStream stream = (PdfStream) dictionary;
                    stream.setStart(position + i.getPosition());
                }
                return dictionary;
            case OBJ_INIT:
                final PdfObject object = new PdfObject(s);
                accept(OBJ_INIT);
                final Object content = literal(i.getPosition());
                object.setContent(content);
                if (!(content instanceof PdfStream)) accept(OBJ_DONE);
                return object;
        }
        throw new IOException("unidentified literal: " + s);
    }

}
