import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SortSplittedPath {

	StringBuffer PATH_after = new StringBuffer();
	int SIZE_QUEUE=0;

	public String ROOT="";
	public String DESTINATION=""; 
	public StringBuffer PATH = new StringBuffer();

	//Nodes that was already read
	public int [] READ_NODE = new int[1000];

	public SortSplittedPath(){		

		super();

	}//fim construtor

	public StringBuffer remove(String v1, String v2, StringBuffer path){

		String v3="";
		String v4="";
		StringBuffer aux = new StringBuffer();
		StringTokenizer t1 = new StringTokenizer(path.toString(),"|");
		StringTokenizer t1_sub;

		//System.out.println("Before Remove ["+v1+"]["+v2+"]. path: " + path);
		while(t1.hasMoreElements()){
			//6:4			
			t1_sub=new StringTokenizer(t1.nextToken(),":");
			//6
			v3 = t1_sub.nextToken();
			//4
			v4 = t1_sub.nextToken();
			if(!v1.equals(v3) || !v2.equals(v4))
				//Not found node
				aux.append(v3+":"+v4+"|");
			//if found, not append it

		}//end while

		//'path' without node that was found
		path = aux;

		//System.out.println("After Remove ["+v1+"]["+v2+"]. path: " + path);

		return path;

	}//end remove


	public void findRoot(){

		//Return: path, node
		//StringBuffer [] list = new StringBuffer[2];

		String v1="";
		String v2="";
		String node="";

		StringTokenizer t1 = new StringTokenizer(PATH.toString(),"|");
		StringTokenizer t1_sub;

		StringBuffer aux = new StringBuffer();

		//1) Search for the root
		boolean foundRoot=false;
		while(t1.hasMoreElements() && !foundRoot){
			//6:4			
			t1_sub=new StringTokenizer(t1.nextToken(),":");
			//6
			v1 = t1_sub.nextToken();
			//4
			v2 = t1_sub.nextToken();
			if(v1.equals(ROOT)){
				foundRoot=true;
				PATH_after.append(v1+":"+v2+"|");
				//path=remove(v1,v2,path);
				node=v2;
			} else {
				//Not found root
				aux.append(v1+":"+v2+"|");
			}//end else

		}//end while		
		//'path' begins without root (that is in PATH_after)
		PATH = aux;
		aux = new StringBuffer();

		setPath(PATH);
		setRoot(node);

		System.out.println("Search node: [" + node+"]");
		System.out.println("path: [" + PATH +"]");
		System.out.println("aux: [" + aux+"]");
		System.out.println("PATH_after(root): [" + PATH_after+"]");
		//System.exit(0);

	}//end findRoot

	public void setSizeQueue(StringBuffer path){

		StringTokenizer t1 = new StringTokenizer(path.toString(),"|");

		SIZE_QUEUE=2*t1.countTokens();

	}//end setSizeQueue

	public void inicializar(String [] queue){

		int i=0;

		while (i<queue.length){
			queue[i]="";
			i++;
		}//end while

	}//fim inicializar

	public void exibir(String [] queue){

		int i=0;
		while (i<queue.length){
			System.out.print("["+queue[i]+"] ");
			i++;
		}//end while

	}//fim exibir

	public void setPath(StringBuffer path){		
		PATH=path;
	}
	public void setRoot(String root){
		//root
		ROOT=root;
	}
	public void setDestination(String destination){
		//destination
		DESTINATION=destination;
	}

	public StringBuffer getPath(){
		return PATH;		
	}
	public String getRoot(){
		return ROOT;
	}

	public StringBuffer getPath_after(){
		//System.out.println("\n\nFinal PATH_after: "+PATH_after);
		return PATH_after;
	}//end getPath_after()	

	public void readPath(StringBuffer path, String root, String destination){

		setPath(path);
		setRoot(root);
		setDestination(destination);

		findRoot();

		read(path,root);

	}//end readPath

	public void read(StringBuffer path, String node){

		String v1="";
		String v2="";
		String v3="";
		String v4="";
		String nextNode="";

		StringTokenizer t1;
		StringTokenizer t1_sub;
		StringBuffer aux = new StringBuffer("");
		String [] queue = new String[1000];
		inicializar(queue);
		int header=0;

		//while(path.length()>0 && !isDest(v2)){			
		while(path.length()>0){
			t1 = new StringTokenizer(path.toString(),"|");
			System.out.println("\n---\nSearch: " + node);
			while(t1.hasMoreTokens()){				
				t1_sub = new StringTokenizer(t1.nextToken(),":");
				v1 = t1_sub.nextToken();				
				if (v1.equals(node)){
					v2 = t1_sub.nextToken();
					path=remove(v1,v2,path);
					aux.append(v1+":"+v2+"|");
					queue[header]=v2;
					header++;
				}//end if
			}//end while
			System.out.println("aux: ["+aux+"]");
			System.out.println("path: ["+path+"]");
			//exibir(queue);
			System.out.println("\nheader: "+header);
			if(header>0)
				header--;
			node=queue[header];
			queue[header]="";			
		}//end while		

		System.out.println("aux: "+aux);
		PATH_after=new StringBuffer(aux);		

	}//end read	

	public StringBuffer hasSplit(StringBuffer path, String node){

		StringBuffer listSplit = new StringBuffer("");

		String v1="";
		String v2="";		

		StringTokenizer t1;
		StringTokenizer t1_sub;

		t1 = new StringTokenizer(path.toString(),"|");
		System.out.println("\n---\nSearch: " + node);
		while(t1.hasMoreTokens()){				
			t1_sub = new StringTokenizer(t1.nextToken(),":");
			v1 = t1_sub.nextToken();
			if (v1.equals(node)){
				v2 = t1_sub.nextToken();
				listSplit.append(v2+":");					
			}//end if
		}//end while					

		System.out.println("hasSplit: "+listSplit.toString());

		return listSplit;

	}//end hasSplit
	
	public StringBuffer hasJoin(StringBuffer path, String node){

		StringBuffer listJoin = new StringBuffer("");

		String v1="";
		String v2="";		

		StringTokenizer t1;
		StringTokenizer t1_sub;

		t1 = new StringTokenizer(path.toString(),"|");
		System.out.println("\n---\nSearch: " + node);
		while(t1.hasMoreTokens()){				
			t1_sub = new StringTokenizer(t1.nextToken(),":");
			v1 = t1_sub.nextToken();
			v2 = t1_sub.nextToken();
			if (v2.equals(node)){				
				listJoin.append(v1+":");					
			}//end if
		}//end while					

		System.out.println("hasJoin: "+listJoin.toString());

		return listJoin;

	}//end hasJoin

	public boolean isDest(String node){

		boolean found=false;
		if(node.equals(DESTINATION))
			found=true;		
		return found;

	}//end isDest

	public void initialize(StringBuffer path){

		String v1="";
		String v2="";

		StringTokenizer t1 = new StringTokenizer(path.toString(),"|");
		StringTokenizer t1_sub;

		int i=0;
		while(i<READ_NODE.length){
			READ_NODE[i]=0;
			i++;
		}//end while

		while(t1.hasMoreTokens()){
			t1_sub = new StringTokenizer(t1.nextToken(),":");
			v1 = t1_sub.nextToken();
			v2 = t1_sub.nextToken();

			READ_NODE[Integer.parseInt(v1)]=1;
			READ_NODE[Integer.parseInt(v2)]=1;

			System.out.println("v1: "+v1 + " v2: "+v2);

		}//end while

		i=0;
		while(i<READ_NODE.length){
			if(READ_NODE[i]!=0)
				System.out.println("Readed node: "+i);
			i++;
		}//end while

	}//end initialize

	public static void main(String args[]){


		//With split
		/*
		SortSplittedPath s = new SortSplittedPath();
		StringBuffer path = new StringBuffer("2:5|3:5|4:2|4:3|5:8|6:4|");
		String root = "6";
		String destination="8";		
		 */

		//With split		
		/*SortSplittedPath s = new SortSplittedPath();
		StringBuffer path = new StringBuffer("1:5|2:1|4:11|5:11|6:2|7:4|10:17|11:10|11:17|12:6|12:7|17:29|18:12|");
		String root = "18";
		String destination = "29";
		*/


		//without split		
		/*
		SortSplittedPath s = new SortSplittedPath();
		StringBuffer path = new StringBuffer("2:4|4:7|5:2|9:5|");
		String root = "9";		
		String destination="7";
		 */

		//Initialize the list of READ_NODE
		/*s.initialize(path);
		s.readPath(path, root, destination);
		System.out.println("Final PATH_after: " + s.getPath_after());
		
		s.hasSplit(path, "12");
		 */		

		

	}//end main

}//fim classe
