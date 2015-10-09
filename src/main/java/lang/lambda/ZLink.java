package lang.lambda;

import java.util.HashMap;

public class ZLink extends ZPair {
    private int m_id;
    private boolean m_visited;

    public boolean isPair() { return false; }
    public boolean isLink() { return true; }

    public boolean isList(){ return false; }

    public ZLink(ZPair z, int i){
        this.rplca(z.first());
        this.rplcd(z.rest());
        m_id = i;
        m_visited = false;
    }

    public String write(){
        return "#" + m_id + "#";
    }

    public String write(HashMap p){
        StringBuffer sb = new StringBuffer(1024);
        sb.append("#" + m_id);
        if(m_visited){
            sb.append("#");
        }else{
            m_visited = true;
            sb.append("=" + super.write(p));
        }
        return sb.toString();
    }

    public String toString(){
        return "[ZLi=" + m_id + "]";
    }

}
