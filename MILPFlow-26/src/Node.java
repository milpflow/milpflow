import java.util.StringTokenizer;

/**
 * Representation of a node from a BRITE topology.
 */
public class Node
{
	/**
	 * The id of the node.
	 */
	private int id = -1;

	/**
	 * The x coordinate.
	 */
	private double x = -1.0;

	/**
	 * The y coordinate.
	 */
	private double y = -1.0;
	
	//Quantidade (grau) de links de entrada
	private int indegree = 1;
	//Quantidade (grau) de links de saida
	private int outdegree = 1;

	/**
	 * Which Autonimous System (AS) does this belong to?
	 */
	private String ASid = "-1";	

	private String tipo="AS_NONE";

	
	/**
	 * Default constructor
	 */
	public Node(final String str){
		StringTokenizer t = new StringTokenizer(str);
		id = Integer.parseInt(t.nextToken());
		x = Double.parseDouble(t.nextToken());
		y = Double.parseDouble(t.nextToken());
		indegree = Integer.parseInt(t.nextToken());
		outdegree = Integer.parseInt(t.nextToken());
		ASid = t.nextToken();
		tipo = t.nextToken();
	}

	public double getID(){
		return id;
	}	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}	
	public double getIndegree(){
		return indegree;
	}
	public double getOutdegree(){
		return outdegree;
	}
	public String getASid(){
		return ASid;
	}		
	public String getTipo(){
		return tipo;
	}
	
}//fim classe
