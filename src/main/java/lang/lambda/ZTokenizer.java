package lang.lambda;

import java.util.Iterator;

public class ZTokenizer implements Iterator {

    private static String CHAR_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String CHAR_SPECIAL_INITIALS = "!$%&*/:<=>?^~";
    private static String CHAR_DIGITS = "0123456789";
    private static String CHAR_BOOLEAN = "tf";
    private static String CHAR_PLUSMINUS = "+-";
    private static String CHAR_SPECIAL_SUBSEQUENTS = "+-.@";
    private static String[] STRING_PECULIAR_IDENTIFIERS = { "+", "-", "..." };
    private static String CHAR_WHITESPACES = " \n";
    private static String CHAR_DELIMITERS = "()\";";
    private static String CHAR_TOKENS = "(),.'`";
    private static String[] STRING_TOKENS = { "#(", ",@" };
    private static String CHAR_EXPONENTS = "esfdl";

    private String m_s;
    private int m_p;
    private StringBuffer m_sb;

    private ZToken m_lastToken;

    public ZTokenizer(String s){
        m_s = s;
        m_p = 0;
        m_sb = new StringBuffer(1024);
        this.rInterTokenSpace();
        m_lastToken = this.readToken();
    }

    public boolean hasNext(){
        return (m_lastToken != null);
    }

    public Object next(){
        ZToken cur = m_lastToken;
        this.rInterTokenSpace();
        m_lastToken = this.readToken();
        return cur;
    }

    public void remove(){
    }

    public String getSyntaxError(){
        return m_sb.toString();
    }

    public String getString(){
        return m_s;
    }

    private ZToken readToken(){
        ZToken z;
        if((z = this.readNumber()) != null) return z;
        if((z = this.readIdentifier()) != null) return z;
        if((z = this.readSharp()) != null) return z;
        if((z = this.readString()) != null) return z;
        if(!this.rToken()) return null;
        z = new ZToken(m_sb.toString(), ZToken.UNDEFINED);
        m_sb.setLength(0);
        return z;
    }

    private ZToken readSharp(){
        if(!this.rSharp()) return null;
        ZToken z;
        if((z = this.readBoolean()) != null) return z;
        if((z = this.readCharacter()) != null) return z;
        return null;
    }

    private ZToken readBoolean(){
        if(this.rCharacter(ZTokenizer.CHAR_BOOLEAN)){
            ZToken z = new ZToken(m_sb.toString(), ZToken.BOOLEAN);
            m_sb.setLength(0);
            return z;
        }
        return null;
    }

    private ZToken readNumber(){
        if(!this.rNumber10()) return null;
        ZToken z = new ZToken(m_sb.toString(), ZToken.NUMBER);
        m_sb.setLength(0);
        return z;
    }

    private boolean rNumber10(){
        if(!this.rPrefix10()) return false;
        if(!this.rReal10()) return false;
        return true;
    }

    private boolean rPrefix10(){
        this.rRadix10();
        this.rExactness();
        this.rRadix10();
        return true;
    }

    private boolean rRadix10(){
        if(this.rString("#d")) return true;
        return true;
    }

    private boolean rExactness(){
        if(this.rString("#i") || this.rString("#e")) return true;
        return true;
    }

    private boolean rReal10(){
        this.rSign();
        return this.rUReal10();
    }

    private boolean rUReal10(){
        if(this.rDigit()){
            while(this.rDigit());
            if(this.rCharacter('.')){
                while(this.rDigit());
                while(this.rSharp());
                return this.rSuffix();
            }
            if(this.rSharp()){
                while(this.rSharp());
                if(this.rCharacter('.')){
                    while(this.rSharp());
                    return this.rSuffix();
                }
            }
            if(this.rCharacter('/')){
                if(!this.rDigit()) return false;
                while(this.rDigit());
                while(this.rSharp());
                return true;
            }
            return this.rSuffix();
        }
        if(this.rDotDigit()){
            while(this.rDigit());
            while(this.rSharp());
            return this.rSuffix();
        }
        return false;
    }

    private boolean rSuffix(){
        if(this.rCharacter(ZTokenizer.CHAR_EXPONENTS)) {
            if(!this.rSign()) return false;
            if(!this.rDigit()) return false;
            while(this.rDigit());
        }
        return true;
    }

    private boolean rSign(){
        if(m_p == m_s.length()) return false;
        char c = m_s.charAt(m_p);
        if(ZTokenizer.CHAR_DIGITS.indexOf(c) != -1) return true;
        if(ZTokenizer.CHAR_PLUSMINUS.indexOf(c) == -1) return false;
        if(m_p + 1 == m_s.length()) return false;
        c = m_s.charAt(m_p + 1);
        if(ZTokenizer.CHAR_DIGITS.indexOf(c) == -1 && c != '.') return false;
        return this.rCharacter(ZTokenizer.CHAR_PLUSMINUS );
    }

    private ZToken readCharacter(){
        if(!this.rCharacter('\\')) return null;
        if(this.rString(ZToken.SPACE ) || this.rString(ZToken.NEWLINE)){
            ZToken z =new ZToken(m_sb.toString(), ZToken.CHARACTER);
            m_sb.setLength(0);
            return z;
        }
        this.rCharacter();
        ZToken z = new ZToken(m_sb.toString(), ZToken.CHARACTER);
        m_sb.setLength(0);
        return z;
    }

    private ZToken readString(){
        if(!this.rCharacter('"')) return null;
        while(this.rStringElement());
        if(!this.rCharacter('"')) return null;
        ZToken z = new ZToken(m_sb.toString(), ZToken.STRING);
        m_sb.setLength(0);
        return z;
    }

    private ZToken readIdentifier(){
        if(this.rInitial()){
            while(this.rSubsequent());
            ZToken z = new ZToken(m_sb.toString(), ZToken.IDENTIFIER);
            m_sb.setLength(0);
            return z;
        }
        if(this.rPeculiarIdentifier()){
            ZToken z = new ZToken(m_sb.toString(), ZToken.IDENTIFIER);
            m_sb.setLength(0);
            return z;
        }
        return null;
    }

    private boolean rInterTokenSpace(){
        while(this.rWhiteSpace() || this.rComment());
        m_sb.setLength(0);
        return true;
    }

    private boolean rAtmosphere(){
        if(this.rWhiteSpace()) return true;
        if(this.rComment()) return true;
        return false;
    }

    private boolean rComment(){
        int l = m_s.length();
        if(m_p == l) return false;
        char c = m_s.charAt(m_p);
        if(c != ';') return false;
        while(c != '\n' && m_p != l){
            c = m_s.charAt(m_p);
            m_p++;
        }
        return true;
    }

    private boolean rStringElement(){
        int l = m_s.length();
        if(m_p == l) return false;
        char c = m_s.charAt(m_p);
        if(c == '"') return false;
        if(c == '\\'){
            m_p++;
            if(m_p == l) return false;
            c = m_s.charAt(m_p);
            if(c != '"' && c != '\\') return false;
        }
        m_sb.append(c);
        // System.out.println(m_sb.toString());
        m_p++;
        return true;
    }

    private boolean rSubsequent(){
        return (this.rInitial() || this.rDigit() || this.rSpecialSubsequent());
    }

    private boolean rInitial(){
        return (this.rLetter() || this.rSpecialInitial());
    }

    private boolean rWhiteSpace(){
        return this.rCharacter(ZTokenizer.CHAR_WHITESPACES);
    }

    private boolean rLetter(){
        return this.rCharacter(ZTokenizer.CHAR_LETTERS);
    }

    private boolean rSpecialInitial(){
        return this.rCharacter(ZTokenizer.CHAR_SPECIAL_INITIALS);
    }

    private boolean rDigit(){
        return this.rCharacter(ZTokenizer.CHAR_DIGITS);
    }

    private boolean rDotDigit(){
        if(m_p + 2 > m_s.length()) return false;
        String s = m_s.substring(m_p, m_p + 2);
        if(s.charAt(0) != '.') return false;
        if(ZTokenizer.CHAR_DIGITS.indexOf(s.charAt(1)) == -1) return false;
        m_sb.append(s);
        m_p += 2;
        return true;
    }

    private boolean rSpecialSubsequent(){
        return this.rCharacter(ZTokenizer.CHAR_SPECIAL_SUBSEQUENTS);
    }

    private boolean rPeculiarIdentifier(){
        return (this.rString(ZTokenizer.STRING_PECULIAR_IDENTIFIERS));
    }

    private boolean rSharp(){
        return this.rCharacter('#');
    }

    private boolean rToken(){
        return (this.rString(ZTokenizer.STRING_TOKENS) || this.rCharacter(ZTokenizer.CHAR_TOKENS));
    }

    private boolean rCharacter(){
        if(m_p == m_s.length()) return false;
        char c = m_s.charAt(m_p);
        m_sb.append(c);
        m_p++;
        return true;
    }

    private boolean rCharacter(char what){
        if(m_p == m_s.length()) return false;
        char c = m_s.charAt(m_p);
        if(c == what){
            m_sb.append(c);
            m_p++;
            return true;
        }
        return false;
    }

    private boolean rCharacter(String s){
        if(m_p == m_s.length()) return false;
        char c = m_s.charAt(m_p);
        if(s.indexOf(c) == -1) return false;
        m_sb.append(c);
        m_p++;
        return true;
    }

    private boolean rString(String what){
        int l = what.length();
        if(m_p + l > m_s.length()) return false;
        String s = m_s.substring(m_p, m_p + l);
        if(s.equals(what)){
            m_sb.append(s);
            m_p += l;
            return true;
        }
        return false;
    }

    private boolean rString(String[] ss){
        for(int i = 0; i < ss.length; i++){
            int l = ss[i].length();
            if(m_p + l <= m_s.length()){
                String s = m_s.substring(m_p, m_p + l);
                if(s.equals(ss[i])){
                    m_sb.append(s);
                    m_p += l;
                    return true;
                }
            }
        }
        return false;
    }


}
