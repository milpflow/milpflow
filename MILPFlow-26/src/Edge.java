import java.util.StringTokenizer;
import java.awt.*;

/**
 * Representation of an edge from a BRITE topology.
 */
public class Edge
{

	private int edgeID=1;
	
	/**
	 * The source node id.
	 */
	private int sourceID = -1;

	/**
	 * The destination node ID.
	 */
	private int destinationID = -1;

	private double length = -1;
	
	private double delay = -1;
	
	/**
	 * The destination Bandwidth.
	 */
	private double bandwidth = -1;
	
	private int asFromNode = -1;
	
	private int asToNode = -1;

	private String typeNode = "";
	
	/**
	 * Does this link connect between ASs?
	 */
	private boolean connector = false;
	
	/**
	 * Default constructor
	 */
	public Edge(final String str){
		StringTokenizer t = new StringTokenizer(str);
		//Edge ID
		edgeID = Integer.parseInt(t.nextToken());
		//fromNodeID
		sourceID = Integer.parseInt(t.nextToken());
		//toNodeID
		destinationID = Integer.parseInt(t.nextToken());
		//Length
		length = Double.parseDouble(t.nextToken());
		//Delay
		delay = Double.parseDouble(t.nextToken());
		//Bandwidth
		bandwidth = Double.parseDouble(t.nextToken());
		//ASFromNodeID
		asFromNode = Integer.parseInt(t.nextToken());
		//ASToNodeID
		asToNode = Integer.parseInt(t.nextToken());
		
		typeNode = t.nextToken();
		
		connector = !(asFromNode == asToNode);
	}//fim construtor

	public double getEdgeID(){
		return edgeID;
	}//fim getBW
	
	public int getSource(){
		return sourceID;
	}//fim getSource
	
	public int getDestination(){
		return destinationID;
	}//fim getDestination
	
	public double getLength(){
		return length;
	}//fim getLength
	
	public double getDelay(){
		return delay;
	}//fim getDelay
	
	public double getBW(){
		return bandwidth;
	}//fim getBW
	
	public String getTypeNode(){
		return typeNode;
	}//fim getTypeNode
		
}//fim classe
