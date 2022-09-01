package friends;

import java.lang.reflect.Array;
import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		ArrayList<String> a = new ArrayList<String>();
		if(g == null || p1 == null || p2 == null || p1.length() == 0 || p2.length() == 0){
			return null;
		}
		p1 = p1.toLowerCase();
		p2 = p2.toLowerCase();
		if(g.map.get(p1) == null || g.map.get(p2) == null){
			return null;
		}
		if(p1.equals(p2)){
			a.add(g.members[g.map.get(p1)].name);
			return a;
		}
		Queue<Integer> q = new Queue<Integer>();
		int[] b = new int[g.members.length];
		int[] c = new int[g.members.length];
		boolean[] d = new boolean[g.members.length];
		for(int i = 0; i < b.length; i++){
			d[i] = false;
			b[i] = Integer.MAX_VALUE;
			c[i] = -1;
		}
		int s = g.map.get(p1);
		d[s] = true;
		b[s] = 0;
		q.enqueue(s);
		while(!q.isEmpty()){
			int e = q.dequeue();
			Person p = g.members[e];
			for(Friend f = p.first; f != null; f = f.next){
				int h = f.fnum;
				if(d[h] == false){
					b[h] = b[e] + 1;
					c[h] = e;
					d[h] = true;
					q.enqueue(h);
				}
			}
		}
		Stack<String> st = new Stack<String>();
		int n = g.map.get(p2);
		if(d[n] == false){
			return null;
		}
		while(n != -1){
			st.push(g.members[n].name);
			n = c[n];
		}
		while(!st.isEmpty()){
			a.add(st.pop());
		}

		return a;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		ArrayList<ArrayList<String>> a = new ArrayList<>();
		if(g == null || school == null || school.length() == 0){
			return null;
		}
		school = school.toLowerCase();
		boolean[] b = new boolean[g.members.length];
		for(int i = 0; i < b.length; i++){
			b[i] = false;
		}
		for(Person m: g.members){
			if(b[g.map.get(m.name)] == false && m.school != null && m.school.equals(school)){
				Queue<Integer> q  = new Queue<>();
				ArrayList<String> c = new ArrayList<>();
				int s = g.map.get(m.name);
				b[s] = true;
				q.enqueue(s);
				c.add(m.name);
				while(!q.isEmpty()){
					int j = q.dequeue();
					Person p = g.members[j];
					for(Friend f = p.first; f != null; f = f.next){
						int n = f.fnum;
						Person pt = g.members[n];
						if(b[n] == false && pt.school != null && pt.school.equals(school)){
							b[n] = true;
							q.enqueue(n);
							c.add(g.members[n].name);
						}
					}
				}
				a.add(c);
			}
		}
		return a;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		boolean[] b = new boolean[g.members.length];
		int[] a = new int[g.members.length];
		int[] c = new int[g.members.length];
		ArrayList<String> l = new ArrayList<String>();
		for(Person m: g.members){
			if(b[g.map.get(m.name)] == false){
				a = new int[g.members.length];
				dfs(g, g.map.get(m.name), g.map.get(m.name), b, a, c, l);
			}
		}
		for(int i = 0; i < l.size(); i++){
			Friend f = g.members[g.map.get(l.get(i))].first;
			int ct = 0;
			while(f != null){
				f = f.next;
				ct ++;
			}
			if(ct == 0 || ct == 1){
				l.remove(i);
			}
		}
		for(Person m: g.members){
			if((m.first.next == null && !l.contains(g.members[m.first.fnum].name))){
				l.add(g.members[m.first.fnum].name);
			}
		}
		return l;
	}

	private static int AS(int[] a) {
        
        int n = 0;
        for ( int i = 0; i < a.length; i ++) {
            
            if ( a[i] != 0) {
                n ++;
            }
        }
        
        return n;
    }

	private static void dfs(Graph g, int a, int j, boolean[] b, int[] c, int[] d, ArrayList<String> s){

		Person p = g.members[a];
		b[g.map.get(p.name)] = true;
		int n = AS(c) + 1;
		if(c[a] == 0 && d[a] == 0){
			c[a] = n;
			d[a] = c[a];
		}
		for(Friend f = p.first; f != null; f =f.next){
			if(b[f.fnum] == false){
				dfs(g, f.fnum, j, b, c, d, s);
				if(c[a] > d[f.fnum]){
					d[a] = Math.min(d[a], d[f.fnum]);
				} else {
					if(Math.abs(c[a] - d[f.fnum]) < 1 && Math.abs(c[a] - c[f.fnum]) <= 1 && d[f.fnum] == 1 && a == j){
						continue;
					}
					if(c[a] <= d[f.fnum] && (a != j || d[f.fnum] == 1)){
						if(!s.contains(g.members[a].name)){
							s.add(g.members[a].name);
						}
					}
				}
			} else {
				d[a] = Math.min(d[a], c[f.fnum]);	
			}
		}
	}
}