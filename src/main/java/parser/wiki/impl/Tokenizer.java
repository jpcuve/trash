package parser.wiki.impl;

import parser.wiki.WikiConfiguration;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

/**
 * @author jpc
 */
public class Tokenizer implements Iterable<String> {
    private final CircularBuffer<Integer> circularBuffer;
    private final WikiConfiguration wikiConfiguration;
    private final TokenizerIterator iterator;
/*
    private static final String MULTI_CHARS = "=-";
    private static final String TRIPLE_CHARS = "{}";
    private static final String DOUBLE_CHARS = ",~|/()";
    private static final String SINGLE_CHARS = "_*`^#[]";
*/

    public Tokenizer(final Reader reader, final WikiConfiguration wikiConfiguration){
        this.circularBuffer = new CircularBuffer<Integer>(reader, 16384);
        this.wikiConfiguration = wikiConfiguration;
        this.iterator = new TokenizerIterator();
    }

    public class TokenizerIterator implements Iterator<String> {
        private State state;
        private int type;

        private TokenizerIterator() {
            readNext();
        }

        public int getType() {
            return type;
        }

        private void readNext(){
            try{
                for (state = State.INIT; state != State.NEXT && state != State.DONE; state = state.next(circularBuffer, wikiConfiguration)){}
            } catch(IOException x){
                state = State.DONE;
            }
        }

        public boolean hasNext() {
            return state != State.DONE;
        }

        public String next() {
            final String s = circularBuffer.getValue();
            type = circularBuffer.getType();
            readNext();
            return s;
        }

        public void remove() {
        }
    }

    public TokenizerIterator iterator() {
        return iterator;
    }

    private enum State{
        INIT{
            public State next(CircularBuffer<Integer> buffer, WikiConfiguration t) throws IOException {
                buffer.mark();
                buffer.reset(0);
                char c = buffer.readChar();
                while (Character.isWhitespace(c) && c != '\n') c = buffer.readChar();
                buffer.back();
                return Character.isLetterOrDigit(c) ? WORD0 : MULTI;
            }},
        WORD0{
            public State next(CircularBuffer<Integer> buffer, WikiConfiguration t) throws IOException {
                char c = buffer.readChar();
                if (Character.isLetterOrDigit(c)) return WORD0;
                buffer.back();
                return NEXT;
            }},
        MULTI{
            public State next(CircularBuffer<Integer> buffer, WikiConfiguration t) throws IOException {
                buffer.reset(1);
                char c = buffer.readChar();
                if (t.getMultiTagCharacters().indexOf(c) == -1) return TRIPLE;
                char cc;
                do{
                    cc = buffer.readChar();
                } while (c == cc);
                buffer.back();
                return NEXT;
            }},
        TRIPLE {
            public State next(CircularBuffer<Integer> buffer, WikiConfiguration t) throws IOException {
                buffer.reset(2);
                char c = buffer.readChar();
                if (t.getTripleTagCharacters().indexOf(c) == -1) return DOUBLE;
                for (int i = 0; i < 2; i++){
                    char cc = buffer.readChar();
                    if (cc != c) return DOUBLE;
                }
                return NEXT;
            }},
        DOUBLE{
            public State next(CircularBuffer<Integer> buffer, WikiConfiguration t) throws IOException {
                buffer.reset(3);
                char c = buffer.readChar();
                if (t.getDoubleTagCharacters().indexOf(c) == -1) return SINGLE;
                char cc = buffer.readChar();
                if (cc != c) return SINGLE;
                return NEXT;
            }},
        SINGLE{
            public State next(CircularBuffer<Integer> buffer, WikiConfiguration t) throws IOException {
                buffer.reset(4);
                char c = buffer.readChar();
                return (t.getSingleTagCharacters().indexOf(c) != -1) ? NEXT : NEWLINE;
            }},
        NEWLINE{
            public State next(CircularBuffer<Integer> buffer, WikiConfiguration t) throws IOException {
                buffer.reset(5);
                char c = buffer.readChar();
                if (c == '\r'){
                    c = buffer.readChar();
                    if (c != '\n'){
                        buffer.back();
                        c = '\r';
                    }
                }
                return (c == '\n' || c == '\r') ? NEXT : INVALID;
            }},
        INVALID{
            public State next(CircularBuffer<Integer> buffer, WikiConfiguration t) throws IOException {
                buffer.reset(0);
                char c = buffer.readChar();
                return c == CircularBuffer.EOT ? DONE : NEXT;
            }},
        NEXT {
            public State next(CircularBuffer<Integer> buffer, WikiConfiguration t) throws IOException {
                return null;
            }},
        DONE{
            public State next(CircularBuffer<Integer> buffer, WikiConfiguration t) throws IOException {
                return null;
            }
        };

        public abstract State next(CircularBuffer<Integer> buffer, WikiConfiguration t) throws IOException;
    }
}
