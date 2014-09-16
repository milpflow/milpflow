import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Automatic creation of fat-tree topologies:
 * 
 *       1 ------------- 30 -------------- 60
 *    /     \        /       \          /     \
 *      ...             ...               ...
 *    |     |        |       |
 *   12 ... 17      42  ...  47
 *    |     |        |       |
 *   18     29      48       59
 *   +++++++++      +++++++++++       +++++++++++
 *      ToRs           ToRs              ToRs
 *   
 *      POD1     |      POD2       |     POD3      | ... 
 *   
 * 
 * @author Lucio A. Rocha
 *
 */

public class GeneratorFatTreeTopology {

	//PoD: Portion of the Data Center
	//Each PoD has until 12 ToRs connected in a FatTree
	//
	//If error, verify increases/reduce NUM_PORTAS_A_SWITCH because
	//you can need more ToR to allocate your servers :-)
	//
	private int NUM_PODS=1;

	public GeneratorFatTreeTopology(){

		run();

	}//fim construtor

	public void run(){

		//fixed values for each PoD
		int num_switches_pod=17;
		int num_links_pod=36;

		int numSwitches = (NUM_PODS*num_switches_pod);
		int numLinks = 	
				//Edges of PoD
				(NUM_PODS*num_links_pod+
				//Connection between PoDs
				(NUM_PODS-1));
		
		System.out.println("\n---Automatic Generator of FatTree Topology---");
		System.out.println("---NUM_PODS: "+NUM_PODS+"---");
		System.out.println("---NUM_SWITCHES: "+numSwitches+"---");
		System.out.println("---NUM_LINKS: "+numLinks+"---");

		//
		ConfigSettings conf = new ConfigSettings();

		StringBuffer topology = new StringBuffer();
		
		topology.append("Topology: ( "+numSwitches+" Nodes, "+numLinks+" Edges )");
		topology.append("\nModel: (Fat-tree: Automatically generated with MILPFlow version "+conf.getVersion()+")");
		topology.append("\n\nNodes: ( "+numSwitches+" )");

		String type="";
		int indexToRs=0;
		int i=1;
		int j=1;
		while(i<=NUM_PODS){
			j=1;
			while(j<=num_switches_pod){
				topology.append("\n"+(j+indexToRs)+"	100	100	1	1	1	");
				if(j==1)
					type="CORE_SWITCH";
				else
					if(j>=12 && j<=17)
						type="A_SWITCH";
					else
						type="AS_NONE";
				topology.append(type);
				j++;
			}//end while
			//To keep index on last
			j--;
			//jump the number of possible ToRs on each PoD
			indexToRs+=j;
			i++;
		}//end while

		//Edges
		topology.append("\n\n\nEdges: ( "+numLinks+" )");
		
		i=1;
		j=0;		
		int k=1;
		while(k<=NUM_PODS){
			
			topology.append("\n"+i+"	"+(2+j)+"	"+(1+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(3+j)+"	"+(1+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(4+j)+"	"+(1+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;			
			topology.append("\n"+i+"	"+(5+j)+"	"+(1+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(2+j)+"	"+(3+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(3+j)+"	"+(4+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(4+j)+"	"+(5+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(6+j)+"	"+(2+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(6+j)+"	"+(3+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(7+j)+"	"+(4+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(7+j)+"	"+(5+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(8+j)+"	"+(2+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(8+j)+"	"+(3+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(9+j)+"	"+(4+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(9+j)+"	"+(5+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(10+j)+"	"+(2+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(10+j)+"	"+(3+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(11+j)+"	"+(4+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(11+j)+"	"+(5+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(6+j)+"	"+(7+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(7+j)+"	"+(8+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(8+j)+"	"+(9+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(9+j)+"	"+(10+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(10+j)+"	"+(11+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(12+j)+"	"+(6+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(12+j)+"	"+(7+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(13+j)+"	"+(6+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(13+j)+"	"+(7+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(14+j)+"	"+(8+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(14+j)+"	"+(9+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(15+j)+"	"+(8+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(15+j)+"	"+(9+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(16+j)+"	"+(10+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(16+j)+"	"+(11+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(17+j)+"	"+(10+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			topology.append("\n"+i+"	"+(17+j)+"	"+(11+j)+"	1.0	0.1	"+conf.getLinkBW()+"	10	1	E_AS_NONE	U"); i++;
			
			k++;
			
			j+=17;
		}//end while

		//Link PoDs
		j=1;
		k=1;
		while(k<NUM_PODS){
			topology.append("\n"+i+"	"+(j)+"	"+((num_switches_pod+1)*k)+"	1.0	0.1	1000.0	10	1	E_AS_NONE	U"); 
			//Next index
			i++;
			//First index of ToR, in next PoD
			j=((num_switches_pod+1)*k); 
			//Next PoD
			k++;
		}//end while

		topology.append("\n\n\nFlows:");
		
		//Write in file 
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(conf.getArquivoTopologiaOriginal(),false));			
			out.write(topology.toString());
			out.close();
		} catch(Exception e){
			System.out.println("13Excecao ao gravar no arquivo." + e.getMessage());
		}//fim catch

		System.out.println("\n---Finished---\n");

	}//end run

	public static void main(String args[]){

		new GeneratorFatTreeTopology();

	}//end main


}//fim classe
