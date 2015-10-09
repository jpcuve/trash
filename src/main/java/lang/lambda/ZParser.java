package lang.lambda;

import java.util.HashMap;

public class ZParser {


    private ZTokenizer m_tokens;
    private ZToken m_tok;

    private HashMap m_symbols;

    public ZParser(){
        m_symbols = new HashMap();
        ZSymbol.register(this);
    }

    public void recSymbol(ZSymbol sym){
        m_symbols.put(sym.getName(), sym);
    }

    public ZSymbol recSymbol(String s){
        ZSymbol sym = (ZSymbol)m_symbols.get(s );
        if(sym == null){
            sym = new ZSymbol(s );
            m_symbols.put(s , sym);
        }
        return sym;
    }

    private void nextToken() throws ZException {
        if(m_tokens.hasNext()){
            m_tok = (ZToken)m_tokens.next();
            if(m_tok == null) throw new ZException(ZError.SYNTAX, m_tokens.getSyntaxError());
        }else{
            m_tok = null;
        }
    }

    public ZExpression read(String s) throws ZException {
        m_tokens = new ZTokenizer(s);
        return read();
    }

    private ZExpression read() throws ZException {
        this.nextToken();
        if(m_tok == null) return null;
        ZExpression z;
        if((z = this.readSimple()) != null) return z;
        if((z = this.readCompound()) != null) return z;
        return null;
    }

    private ZExpression readSimple(){
        ZExpression z;
        if((z = this.readBoolean()) != null) return z;
        if((z = this.readNumber()) != null) return z;
        if((z = this.readCharacter()) != null) return z;
        if((z = this.readString()) != null) return z;
        if((z = this.readSymbol()) != null) return z;
        return null;
    }

    private ZExpression readBoolean(){
        if(!m_tok.isBoolean()) return null;
        return (m_tok.isTrue()) ? ZBoolean.TRUE : ZBoolean.FALSE;
    }

    private ZExpression readNumber(){
        if(!m_tok.isNumber()) return null;
        return ZNumber.parse(m_tok.getValue());
    }

    private ZExpression readCharacter(){
        if(!m_tok.isCharacter()) return null;
        if(m_tok.isSpace()) return ZChar.SPACE ;
        if(m_tok.isNewLine()) return ZChar.NEWLINE ;
        return new ZChar(m_tok.getValue().charAt(2));
    }

    private ZExpression readString(){
        if(!m_tok.isString()) return null;
        String s = m_tok.getValue();
        int l = s.length();
        return new ZString(s.substring(1, l-1));
    }

    private ZExpression readSymbol(){
        if(!m_tok.isIdentifier()) return null;
        return this.recSymbol(m_tok.getValue());
    }

    private ZExpression readCompound() throws ZException {
        ZExpression z;
        if((z = this.readList()) != null) return z;
        if((z = this.readVector()) != null) return z;
        return null;
    }

    private ZExpression readList() throws ZException {
        if(!m_tok.isUndefined()) return null;
        String s = m_tok.getValue();
        if(s.equals("(")){
            ZPair l = new ZPair();
            ZPair cur = l;
            ZExpression e = this.read();
            while(e != null){
                cur.rplca(e);
                e = this.read();
                if(e != null){
                    ZPair z = new ZPair();
                    cur.rplcd(z);
                    cur = z;
                }
            }
            if(m_tok == null) throw new ZException(ZError.SYNTAX, m_tokens.getString());
            if(m_tok.getValue().equals(".")){
                e = this.read();
                cur.rplcd(e);
                this.nextToken();
            }
            if(!m_tok.getValue().equals(")")) return null;
            return (l.first() != null || l.rest() != ZEmpty.EMPTY ) ? (ZExpression)l : ZEmpty.EMPTY;
        }
        if(s.equals("'")) return new ZPair(ZSymbol.QUOTE, new ZPair(this.read()));
        if(s.equals("`")) return new ZPair(ZSymbol.QUASIQUOTE, new ZPair(this.read()));
        if(s.equals(",")) return new ZPair(ZSymbol.UNQUOTE, new ZPair(this.read()));
        if(s.equals(",@")) return new ZPair(ZSymbol.UNQUOTE_SPLICING, new ZPair(this.read()));
        return null;
    }

    private ZExpression readVector() throws ZException {
        if(!m_tok.isUndefined()) return null;
        if(!m_tok.getValue().equals("#(")) return null;
        ZVector v= new ZVector();
        ZExpression e = this.read();
        while(e != null){
            v.append(e);
            e = this.read();
        }
        if(m_tok == null) throw new ZException(ZError.SYNTAX, m_tokens.getString());
        if(!m_tok.getValue().equals(")")) return null;
        return v;
    }

}
