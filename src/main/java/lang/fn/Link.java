package lang.fn;

import java.util.HashMap;

public class Link extends Pair {
	private int id;
	private boolean visited;

	public boolean isPair() { return false; }
	public boolean isLink() { return true; }

	public boolean isList(){ return false; }

	public Link(Pair z, int i){
		this.rplca(z.first());
		this.rplcd(z.rest());
		id = i;
		visited = false;
	}

	public String write(){
		return "#" + id + "#";
	}

	public String write(HashMap p){
		StringBuffer sb = new StringBuffer(1024);
		sb.append("#" + id);
		if(visited){
			sb.append("#");
		}else{
			visited = true;
			sb.append("=" + super.write(p));
		}
		return sb.toString();
	}

	public String toString(){
		return "[Li=" + id + "]";
	}

}