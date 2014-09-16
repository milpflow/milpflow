import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAutoMininetTopologyRyu {

	//To set IP to hosts in Mininet
	private String IP="10.0.0.0";

	private int MAC_ID=1;

	public CreateAutoMininetTopologyRyu(){

		gerarTopologiaMininet();

		gerarRegrasOpenFlow();

	}//fim construtor

	public void gerarTopologiaMininet(){

		System.out.println("\n---Generates Mininet Topology for Ryu---\n");

		//Adquire as informacoes da topologia que foi salva em arquivo
		ConfigSettings conf = new ConfigSettings();

		try
		{
			int i=0;
			int j=0;

			String linha = new String();
			//try to open the file.
			BufferedReader file = new BufferedReader(new FileReader(conf.getArquivoTopologiaDatacenter()));
			//Cabecalho
			linha=file.readLine();			
			StringTokenizer token = new StringTokenizer(linha, "( )");
			token.nextToken();
			//System.out.println("\nnumNodesOriginal: "+Integer.parseInt(token.nextToken()));			
			int numNodesOriginal = Integer.parseInt(token.nextToken());
			//Salta as proximas linhas do cabecalho
			for (i=0; i<2; i++) {
				file.readLine();
			}//fim for

			//get the number of nodes
			linha = file.readLine();			
			token = new StringTokenizer(linha, "( )");
			token.nextToken();
			int numNodes = Integer.parseInt(token.nextToken());
			//System.out.println("numNodes: " + numNodes);
			//Salta o campo Nodes
			file.readLine();
			//Guarda todas as informacoes dos Nodes
			//nodes_arquivo	[0][id, type]
			//    			[1][id, type]
			//    ...
			String [][] nodes_arquivo = new String[numNodes][2];
			for (i=0; i<numNodes; i++){				
				linha=file.readLine();
				token = new StringTokenizer(linha,"\t");
				//id
				nodes_arquivo[i][0]=token.nextToken().toString();
				//xpos
				token.nextToken();
				//ypos
				token.nextToken();
				//indegree
				token.nextToken();
				//outdegree
				token.nextToken();
				//ASid
				token.nextToken();
				//type
				nodes_arquivo[i][1]=token.nextToken().toString();
			}//fim while

			/*int i1=0;
			int j1=0;
			System.out.println("\n\n--------------");
			while(i1<numNodes){
				j1=0;
				while(j1<2){
					System.out.println(nodes_arquivo[i1][j1]);
					j1++;
				}//fim while
				i1++;
			}//fim while
			 */

			//Adquire o numero de Edges
			for (i=0; i<2; i++) {
				file.readLine();
			}//fim for			
			linha = file.readLine();
			token = new StringTokenizer(linha, "( )");
			token.nextToken();
			int numEdges = Integer.parseInt(token.nextToken());
			//Guarda todas as informacoes dos Edges
			//System.out.println("numEdges: " + numEdges);
			//EDGE[0][EdgeId From To Length Delay Bandwidth queueLimit ASto Type Other]
			//    [1][EdgeId From To Length Delay Bandwidth queueLimit ASto Type Other]
			//    ...
			String [][] edges_arquivo = new String[numEdges][10];
			//Inicializa a matriz
			i=0;
			j=0;
			while(i<edges_arquivo.length){
				j=0;
				while(j<edges_arquivo[i].length){
					edges_arquivo[i][j]="0";
					j++;
				}//fim while
				i++;
			}//fim while
			int campoEdgeId=0;
			int campoEdgeFrom=1;
			int campoEdgeTo=2;
			int campoEdgeCapLink=3;
			int campoEdgeDelay=4;
			int campoEdgeBw=5;
			int campoEdgeQueueLimit=6;
			int campoEdgeAsTo=7;
			int campoEdgeType=8;
			int campoEdgeOther=9;
			//Descricao dos campos
			linha = file.readLine();
			for (i=0; i<numEdges; i++){
				j=0;

				linha = file.readLine();

				token = new StringTokenizer(linha,"\t");
				//id (0)
				edges_arquivo[i][j++]=token.nextToken();
				//from (1)
				edges_arquivo[i][j++]=token.nextToken();
				//to (2)
				edges_arquivo[i][j++]=token.nextToken();
				//Length (3) (capLink atribuida pelo cromossomo)
				edges_arquivo[i][j++] = token.nextToken();
				//Delay (4) 
				edges_arquivo[i][j++] = token.nextToken();
				//Bandwidth (5) 
				edges_arquivo[i][j++] = token.nextToken();
				//queueLimit (6)
				edges_arquivo[i][j++] = token.nextToken();
				//Asto (7)
				edges_arquivo[i][j++] = token.nextToken();
				//Type (8)
				edges_arquivo[i][j++] = token.nextToken();
				//Other (9)
				edges_arquivo[i][j++] = token.nextToken();

			}//fim for

			//---------------------Inicio da Topologia Mininet---------------------						
			i=0;
			j=0;

			//Blocos do arquivo do modelo Lingo
			StringBuffer topologiaMininet = new StringBuffer();

			topologiaMininet.append("#!/usr/bin/env python"+

				"\n\nfrom mininet.cli import CLI"+
				"\nfrom mininet.link import Link"+
				"\nfrom mininet.net import Mininet"+
				"\nfrom mininet.node import RemoteController"+
					"\nfrom mininet.term import makeTerm" );

			topologiaMininet.append("\n\nif '__main__' == __name__:"+
					"\n    net = Mininet(controller=RemoteController)");

			topologiaMininet.append("\n\n    c0 = net.addController('c0')");

			i=0;
			j=1;
			while(i<numNodes){
				if (nodes_arquivo[i][1].equals("TOR"))
					topologiaMininet.append("\n    h"+j+" = net.addHost('h"+j+"')");					
				else 
					topologiaMininet.append("\n    s"+j+" = net.addSwitch('s"+j+"')");					
				i++;
				j++;
			}//fim while
			topologiaMininet.append("\n\n");

			i=0;
			j=1;
			while(i<numEdges){
				if(edges_arquivo[i][campoEdgeType].equals("TOR"))
					topologiaMininet.append("\n    Link(h"+edges_arquivo[i][campoEdgeFrom]+", s"+edges_arquivo[i][campoEdgeTo]+")");					
				else
					topologiaMininet.append("\n    Link(s"+edges_arquivo[i][campoEdgeFrom]+", s"+edges_arquivo[i][campoEdgeTo]+")");
				i++;
				j++;
			}//fim while

			topologiaMininet.append("\n\n    net.build()"+
					"\n    c0.start()");			

			i=0;
			j=1;
			while(i<numNodes){
				if (!nodes_arquivo[i][1].equals("TOR"))
					topologiaMininet.append("\n    s"+j+".start([c0])");
				i++;
				j++;
			}//fim while

			topologiaMininet.append("\n\n    CLI(net)"+
					"\n    net.stop()");		    

			//---Fim da Topologia Mininet---

			//Grava em arquivo 
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(conf.getArquivoTopologiaMininetRyu(),false));			
				out.write(topologiaMininet.toString());
				out.close();
			} catch(Exception e){
				System.out.println("13Excecao ao gravar no arquivo." + e.getMessage());
			}//fim catch

			//close the file			
			file.close();			
		} catch(Exception e){
			System.out.println("Excecao 14 ao abrir o arquivo: " + conf.getArquivoTopologiaDatacenter() + "\n" + e.getMessage());
			e.printStackTrace();
		}//fim catch

	}//fim gerarTopologiaMininet

	public void gerarRegrasOpenFlow(){

		System.out.println("\n---Gerar Regras OpenFlow---\n");

		//Adquire as informacoes da topologia que foi salva em arquivo
		ConfigSettings conf = new ConfigSettings();

		try
		{
			int i=0;
			int j=0;

			String linha = new String();
			//try to open the file.
			BufferedReader file = new BufferedReader(new FileReader(conf.getArquivoTopologiaDatacenter()));
			//Cabecalho
			linha=file.readLine();			
			StringTokenizer token = new StringTokenizer(linha, "( )");
			token.nextToken();
			//System.out.println("\nnumNodesOriginal: "+Integer.parseInt(token.nextToken()));			
			int numNodesOriginal = Integer.parseInt(token.nextToken());
			//Salta as proximas linhas do cabecalho
			for (i=0; i<2; i++) {
				file.readLine();
			}//fim for

			//get the number of nodes
			linha = file.readLine();			
			token = new StringTokenizer(linha, "( )");
			token.nextToken();
			int numNodes = Integer.parseInt(token.nextToken());
			//System.out.println("numNodes: " + numNodes);
			//Salta o campo Nodes
			file.readLine();
			//Guarda todas as informacoes dos Nodes
			//nodes_arquivo	[0][id, type]
			//    			[1][id, type]
			//    ...
			String [][] nodes_arquivo = new String[numNodes][2];
			for (i=0; i<numNodes; i++){				
				linha=file.readLine();
				token = new StringTokenizer(linha,"\t");
				//id
				nodes_arquivo[i][0]=token.nextToken().toString();
				//xpos
				token.nextToken();
				//ypos
				token.nextToken();
				//indegree
				token.nextToken();
				//outdegree
				token.nextToken();
				//ASid
				token.nextToken();
				//type
				nodes_arquivo[i][1]=token.nextToken().toString();
			}//fim while

			/*int i1=0;
			int j1=0;
			System.out.println("\n\n--------------");
			while(i1<numNodes){
				j1=0;
				while(j1<2){
					System.out.println(nodes_arquivo[i1][j1]);
					j1++;
				}//fim while
				i1++;
			}//fim while
			 */

			//Adquire o numero de Edges
			for (i=0; i<2; i++) {
				file.readLine();
			}//fim for			
			linha = file.readLine();
			token = new StringTokenizer(linha, "( )");
			token.nextToken();
			int numEdges = Integer.parseInt(token.nextToken());
			//Guarda todas as informacoes dos Edges
			//System.out.println("numEdges: " + numEdges);
			//EDGE[0][EdgeId From To Length Delay Bandwidth queueLimit ASto Type Other]
			//    [1][EdgeId From To Length Delay Bandwidth queueLimit ASto Type Other]
			//    ...
			String [][] edges_arquivo = new String[numEdges][10];
			//Inicializa a matriz
			i=0;
			j=0;
			while(i<edges_arquivo.length){
				j=0;
				while(j<edges_arquivo[i].length){
					edges_arquivo[i][j]="0";
					j++;
				}//fim while
				i++;
			}//fim while
			int campoEdgeId=0;
			int campoEdgeFrom=1;
			int campoEdgeTo=2;
			int campoEdgeCapLink=3;
			int campoEdgeDelay=4;
			int campoEdgeBw=5;
			int campoEdgeQueueLimit=6;
			int campoEdgeAsTo=7;
			int campoEdgeType=8;
			int campoEdgeOther=9;
			//Descricao dos campos
			linha = file.readLine();
			for (i=0; i<numEdges; i++){
				j=0;

				linha = file.readLine();

				token = new StringTokenizer(linha,"\t");
				//id (0)
				edges_arquivo[i][j++]=token.nextToken();
				//from (1)
				edges_arquivo[i][j++]=token.nextToken();
				//to (2)
				edges_arquivo[i][j++]=token.nextToken();
				//Length (3) (capLink atribuida pelo cromossomo)
				edges_arquivo[i][j++] = token.nextToken();
				//Delay (4) 
				edges_arquivo[i][j++] = token.nextToken();
				//Bandwidth (5) 
				edges_arquivo[i][j++] = token.nextToken();
				//queueLimit (6)
				edges_arquivo[i][j++] = token.nextToken();
				//Asto (7)
				edges_arquivo[i][j++] = token.nextToken();
				//Type (8)
				edges_arquivo[i][j++] = token.nextToken();
				//Other (9)
				edges_arquivo[i][j++] = token.nextToken();

			}//fim for

			//Read all remain lines
			//Jump lines
			i=0;
			while(i<3){
				file.readLine();
				i++;
			}//end while
			//
			//Ex.: 
			//      sourceNode=6		destinationNode=8
			//		sourceNode=7		destinationNode=9
			//
			//      sequenceFlows= 6;8;7;9
			//
			StringBuffer sequenceFlows = new StringBuffer();
			i=0;
			String REGEX="";			
			Matcher matcher;
			Pattern pattern;
			while((linha=file.readLine())!=null){				
				//System.out.println("|"+linha+"\n");
				//sourceNode	destinationNode		
				REGEX = "(.*)\\s(.*)\\s(.*)\\s(.*)";
				pattern = Pattern.compile(REGEX);
				matcher = pattern.matcher(linha);
				if (matcher.find()){
					//System.out.println(matcher.group(1)+";"+matcher.group(2)+";");
					sequenceFlows.append(matcher.group(1)+";"+matcher.group(2)+";"+matcher.group(3)+";"+matcher.group(4)+";");					
				}//end if

				//Amout of flows
				i++;
			}//end while
			//Keep the last index
			i--;

			//Create a well structured list of flows
			//
			//Ex.: 
			//      sourceNode=6		destinationNode=8		aggregatedTraffic=300
			//		sourceNode=7		destinationNode=9		aggregatedTraffic=100
			//
			//     lisFlows[0][0]=6
			//	   lisFlows[0][1]=8			
			//	   lisFlows[0][2]=300
			//	   lisFlows[1][0]=7
			//	   lisFlows[1][1]=9
			//	   lisFlows[1][1]=100
			//     ...
			String[][] listFlows = new String[i][4]; 
			token = new StringTokenizer(sequenceFlows.toString(), ";");
			i=0;
			System.out.println("\nFlows:\n");
			while(token.hasMoreElements()){
				listFlows[i][0]=removerEspacos(token.nextToken());
				listFlows[i][1]=removerEspacos(token.nextToken());
				listFlows[i][2]=removerEspacos(token.nextToken());
				listFlows[i][3]=removerEspacos(token.nextToken());
				System.out.println("SourceNode: ["+listFlows[i][0]+"] DestinationNode: ["+listFlows[i][1]+"] AggregatedTraffic: ["+listFlows[i][2]+"] Path: ["+listFlows[i][3]+"]\n");
				i++;
			}//end while			
			//System.out.println("i: " +i+ listFlows.length);

			//---------------------Inicio da Geracao das Regras---------------------						
			i=0;
			j=0;

			//Armazena as regras OpenFlow
			StringBuffer regrasRyu = new StringBuffer();

			//Ex.:
			//
			//O indice da lista corresponde ao indice do host
			//portHosts[1] = [0]  <-- OpenFlow port of connection of h1. Next port of h1 is port 0.
			//...
			//O indice da lista corresponde ao indice do switch
			//portSwitch[1] = [2]  <-- last increment, according occurences in topology description. Next port of s1 is port 2.
			//...

			int [] portHosts = new int[1000];
			int [] portSwitch = new int[1000];

			initialize(portHosts);
			initialize(portSwitch);

			String [] net = new String[1000];
			//Ex: portTCP[1] = 6634
			//    portTCP[2] = 6635
			//    ...
			String [] portTCP = new String[1000];
			int valuePortTCP = 6633;

			//Ex: hostIP[1] = 10.0.0.1
			//    hostIP[2] = 10.0.0.2
			//    ...
			String [] hostIP = new String[1000];			

			initialize(net);
			initialize(portTCP,valuePortTCP);

			i=0;
			while(i<numEdges){
				if(edges_arquivo[i][campoEdgeType].equals("TOR")){
					//Ex.: h1,0,s1,1
					net[i]="h"+edges_arquivo[i][campoEdgeFrom]+","+
							"0"+","+
							"s"+edges_arquivo[i][campoEdgeTo]+","+
							(portSwitch[Integer.parseInt(edges_arquivo[i][campoEdgeTo])]+1);
					//Increase index of port host 
					portSwitch[Integer.parseInt(edges_arquivo[i][campoEdgeFrom])]++;
					//Increase index of port switch
					portSwitch[Integer.parseInt(edges_arquivo[i][campoEdgeTo])]++;

					//Set TCP port to host 
					//portTCP[Integer.parseInt(edges_arquivo[i][campoEdgeFrom])]=(Integer.parseInt(edges_arquivo[i][campoEdgeTo])+valuePortTCP)+"";
					//Generates new host IP
					//IP is global, and keeps the last IP that was set to a host
					newHostIP();
					hostIP[Integer.parseInt(edges_arquivo[i][campoEdgeFrom])]=IP;

					//System.out.println(k);

				} else {
					//Ex.: s1,2,s2,1
					net[i]="s"+edges_arquivo[i][campoEdgeFrom]+","+
							(portSwitch[Integer.parseInt(edges_arquivo[i][campoEdgeFrom])]+1)+","+
							"s"+edges_arquivo[i][campoEdgeTo]+","+
							(portSwitch[Integer.parseInt(edges_arquivo[i][campoEdgeTo])]+1);
					//Increase index os port switches
					portSwitch[Integer.parseInt(edges_arquivo[i][campoEdgeFrom])]++;
					portSwitch[Integer.parseInt(edges_arquivo[i][campoEdgeTo])]++;

				}//end else
				i++;
			}//fim while

			//Grava em arquivo 
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(conf.getArquivoPortasMininet(),false));
				i=0;
				while(i<net.length){
					if (!net[i].equals("")){
						System.out.println(net[i]);
						out.write("\n"+net[i]);
					}//end if
					i++;
				}//end while				
				out.close();
			} catch(Exception e){
				System.out.println("13Excecao ao gravar no arquivo." + e.getMessage());
			}//fim catch


			///////
			//Aqui comeca a extrair dados para os caminhos

			i=0;
			j=0; 


			//F1_6_8
			//F2_9_7
			//
			//For F1:
			//F[0][6][4]=200
			//F[0][4][5]=200
			//...
			//For F2:
			//F[1][6][4]=100
			//F[1][4][5]=100
			//...
			String [][][] F = new String[listFlows.length][numNodes+1][numNodes+1];			
			inicializar(F);

			int f=0;
			while(f<listFlows.length){

				//Abre o arquivo com o resultado
				try{
					BufferedReader arquivo = new BufferedReader(new FileReader(conf.getArquivoLingoResult()));
					//StringTokenizer token;
					//StringBuffer fluxoArquivo = new StringBuffer();

					REGEX="";				

					//fluxoArquivo.append("digraph G {\n");

					double fluxo=0;				

					//Primeiro, adquire todos os fluxo
					while((linha=arquivo.readLine())!=null){
						//Duas possibilidades de resposta no Lingo:
						//F ( 123, 234)		1.0000		0.0000
						//F ( 123, 234)		1.0000		
						REGEX = "F"+(f+1)+"_"+listFlows[f][0]+"_"+listFlows[f][1]+"\\(\\s(.*),\\s(.*)\\)\\s(.*)"; //f+1 because Lingo index begin at 1
						//System.out.println(REGEX);
						pattern = Pattern.compile(REGEX);
						matcher = pattern.matcher(linha);
						if (matcher.find()){

							i = Integer.parseInt(matcher.group(1));
							j = Integer.parseInt(matcher.group(2));

							token = new StringTokenizer(matcher.group(3)," ");
							fluxo = Double.parseDouble(removerEspacos(token.nextToken()));

							if(fluxo>0.1){//To avoid keep links with very low traffic
								F[f][i][j] = fluxo+"";
								System.out.println("F["+(f+1)+"]["+i+"]["+j+"]: "+F[f][i][j]);							
							}//fim if

						}//fim if

					}//end while

					//close the file			
					arquivo.close();			
				} catch(Exception e){
					System.out.println("Excecao 17 ao abrir o arquivo: " + conf.getArquivoLingoResult() + "\n" + e.getMessage());
					e.printStackTrace();
				}//fim catch

				//Next flow
				f++;
			}//end while

			///////
			//Remove loops simples (1--2, 2--1)
			i=0;
			j=0;
			int k=0;
			while(i<F.length){
				j=0;
				while(j<F[i].length){
					k=0;
					while(k<F[i][j].length){
						//Remove loop
						if(!F[i][j][k].equals("0") && !F[i][k][j].equals("0")){
							F[i][j][k]="0";
							F[i][k][j]="0";
						}//end if								
						k++;
					}//end while
					j++;
				}//end while						
				i++;
			}//end while


			System.out.println("\nAmount of Flows: "+F.length);			

			//-------------
			// Flows:
			//
			// Source node: 		6
			// Destination node:	8
			//
			//     lisFlows[0][0]=6
			//	   lisFlows[0][1]=8			
			//	   lisFlows[0][2]=300
			//     
			// Source node: 		9
			// Destination node:	7
			//
			//     lisFlows[1][0]=9
			//	   lisFlows[1][1]=7			
			//	   lisFlows[1][2]=100
			//-------------
			// MILPFlow links (Not in logical sequence. So, we need to mount the PATH.
			// Ex.: PATH[0] = 6:4:5:...:8
			//      PATH[1] = 9:3:5:...:7
			//
			//		F1_6_8
			//		F2_9_7
			//
			//		For F1:
			//		F[0][6][4]=200
			//		F[0][5][6]=200
			//		F[0][4][5]=200
			//		...
			//		For F2:
			//		F[1][9][3]=100
			//		F[1][5][6]=100			
			//		F[1][3][5]=100
			//		...
			//------------

			//F possui a rota embaralhada
			//Tem que gerar o PATH dos switches com base no resultado do MILPFlow
			//Para cada link do PATH, obter as portas (na estrutura net) 
			//fazer isso apenas para os switches do PATH

			//Cria o PATH dos switches com a informacao que esta na estrutura F
			//PATH_before[0] = 6:4|5:6|...20:8|
			//PATH_before[1] = 9:3|5:6|...15:7|
			StringBuffer [] PATH_before = new StringBuffer[F.length];
			inicializar(PATH_before);

			//Forma a rota na estrutura PATH
			i=0;
			//Keep record about how many links are in path[i]
			int [] numLinks=new int[F.length];
			inicializar(numLinks);

			while(i<F.length){				
				j=0;
				while(j<F[i].length){
					k=0;
					while(k<F[i][j].length){
						if (!F[i][j][k].equals("0")){
							PATH_before[i].append(j+":"+k+"|");
							numLinks[i]++; //Amount of links of this PATH_before[i]
						}//end if
						k++;
					}//end while					
					j++;
				}//end while
				i++;
			}//end while

			System.out.println("\nPATH before:");
			i=0;
			while(i<PATH_before.length){
				System.out.println("\n"+PATH_before[i]);
				i++;
			}//end while

			///////
			StringBuffer [] PATH_after = new StringBuffer[F.length];
			inicializar(PATH_after);

			SortSplittedPath s;			
			i=0;
			while(i<listFlows.length){
				s = new SortSplittedPath();
				s.readPath(PATH_before[i],listFlows[i][0],listFlows[i][1]);
				PATH_after[i] = s.getPath_after();
				System.out.println("----");
				i++;
			}//end while


			//Here we have the PATHs in the correct sequence
			System.out.println("\nPATH after:");
			i=0;
			while(i<PATH_after.length){
				System.out.println("\n"+PATH_after[i]);
				i++;
			}//end while

			//System.exit(0);

			/////////////////

			StringTokenizer t, t_split;
			StringTokenizer t_sub, t_split_sub;
			String a="";
			String b="";
			String c="";
			String d="";
			String aux="";			

			String inPort="";
			String outPort="";

			StringBuffer auxToken = new StringBuffer();


			int groupTableID=1;

			//For each path	
			i=0;
			while(i<PATH_after.length){

				//---Source---

				//PATH_after[i] = 6:4|4:2|2:7|7:8|
				t = new StringTokenizer(PATH_after[i].toString(),"|");

				//6:4
				t_sub = new StringTokenizer(t.nextToken(),":");
				//6
				a = t_sub.nextToken();
				//4
				b = t_sub.nextToken();

				//4:2
				t_sub = new StringTokenizer(t.nextToken(),":");
				//4
				c = t_sub.nextToken();
				//2
				d = t_sub.nextToken();

				System.out.println("Source a:["+a+"] b:["+b+"] c:["+c+"] d:["+d+"]");				

				//Source					
				SortSplittedPath obj = new SortSplittedPath();
				//listSplit=12:6|12:7|
				StringBuffer listSplit=obj.hasSplit(PATH_after[i], b);
				//t_split= 6:7
				t_split = new StringTokenizer(listSplit.toString(),":");

				//Has split
				if (t_split.countTokens()==2){
					System.out.println("Split at source: "+listSplit);

					regrasRyu.append(
							"\n\n#F"+(i+1)+"_"+listFlows[i][0]+"_"+listFlows[i][1]+
							"\n#Source" +
							"\n#s" + b);
					/*regrasRyu.append("\n#(1<--) ARP");
					regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
							portTCP[Integer.parseInt(b)]+
							" flow-mod table=0,cmd=add eth_type=0x806,arp_tpa="+
							hostIP[Integer.parseInt(a)] +
							" apply:output="+
							getOutPort("h"+a,"s"+b,net));
					 */



					//Split
					//18:12|12:6|12:7...
					//a=18, b=12, c=12, d=6
					//listSplit = 12:6|12:7|
					while(t_split.hasMoreTokens()){
						regrasRyu.append("\n#(2<--) Split: ICMP and all others:");
						regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
								portTCP[Integer.parseInt(b)]+
								" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
								hostIP[Integer.parseInt(a)] +",in_port="+
								//" flow-mod table=0,cmd=add in_port="+
								getInPort("s"+b,"s"+t_split.nextToken(),net)+
								" apply:output="+
								getOutPort("h"+a,"s"+b,net));
					}//end while

					//System.exit(0);

					//#Group table
					//dpctl tcp:127.0.0.1:6634 group-mod cmd=add,type=1,group=1 
					//		weight=7,port=1,group=all output=4 
					//		weight=2,port=1,group=all output=3 
					//		weight=1,port=1,group=all output=2
					t_split = new StringTokenizer(listSplit.toString(),":");
					regrasRyu.append("\n#(3-->) Split: Group table");
					regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
							portTCP[Integer.parseInt(b)]+
							" group-mod cmd=add,type=1,group="+groupTableID);

					while(t_split.hasMoreElements()){
						aux = t_split.nextToken();
						regrasRyu.append(" \\\n weight="+getWeight(aux,listSplit,F)+
								",port="+getOutPort("h"+a,"s"+b,net)+
								",group=all output="+getInPort("s"+b,"s"+aux,net));
					}//end while					

					regrasRyu.append("\n#(4-->) ARP");
					regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
							portTCP[Integer.parseInt(b)]+
							" flow-mod table=0,cmd=add eth_type=0x806,arp_tpa="+
							hostIP[Integer.parseInt(listFlows[i][1])] +
							" apply:group="+groupTableID);

					//Ex.: dpctl tcp:127.0.0.1:6637 flow-mod table=0,cmd=add in_port=1 apply:output=3
					regrasRyu.append("\n#(5-->) ICMP and all others:");
					regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
							portTCP[Integer.parseInt(b)]+
							" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
							hostIP[Integer.parseInt(listFlows[i][1])] +							
							" apply:group="+groupTableID);

					groupTableID++;

					System.out.println("\nEnd step 1");

					//System.out.println(regrasRyu.toString());
					//System.exit(0);

				} else 
					//Only one path
					if(t_split.countTokens()==1){

						//Node without split
						//
						//Rule for the first A_SWITCH connected at source TOR
						//Ex.: dpctl tcp:127.0.0.1:6637 flow-mod table=0,cmd=add eth_type=0x806,arp_tpa=10.0.0.1 apply:output=3						
						regrasRyu.append(
								"\n\n#F"+(i+1)+"_"+listFlows[i][0]+"_"+listFlows[i][1]+
								"\n#Source" +
								"\n#s" + b);
						/*regrasRyu.append("\n#(6<--) ARP");
						regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
								portTCP[Integer.parseInt(b)]+
								" flow-mod table=0,cmd=add eth_type=0x806,arp_tpa="+
								hostIP[Integer.parseInt(a)] +
								" apply:output="+
								getOutPort("h"+a,"s"+b,net));								

						//Ex.: dpctl tcp:127.0.0.1:6637 flow-mod table=0,cmd=add in_port=1 apply:output=3
						/*regrasRyu.append("\n#(7<--) ICMP and all others:");
						regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
								portTCP[Integer.parseInt(b)]+
								" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
								hostIP[Integer.parseInt(a)] +",in_port="+
								//" flow-mod table=0,cmd=add in_port="+
								getInPort("s"+c,"s"+d,net)+
								" apply:output="+
								getOutPort("h"+a,"s"+b,net));
						 */

						regrasRyu.append("\n#a:["+a+"] b:["+b+"] c:["+c+"] d:["+d+"]");
						regrasRyu.append("\n#(6<--) ARP");
						regrasRyu.append("\ncurl -X POST -d '{\"dpid\": \""+b+"\",\"match\": {\"in_port\":\""+
								getInPort("s"+c,"s"+d,net)+"\", \"dl_type\":\"2054\", \"nw_dst\":\""+
								hostIP[Integer.parseInt(listFlows[i][0])]+"\"}, \"actions\": [{\"type\": \"OUTPUT\", \"port\": \""+
								getOutPort("h"+a,"s"+b,net)+"\"}]}' http://localhost:8080/stats/flowentry/add");
						
						regrasRyu.append("\n#(6<--) IP");
						regrasRyu.append("\ncurl -X POST -d '{\"dpid\": \""+b+"\",\"match\": {\"in_port\":\""+
								getInPort("s"+c,"s"+d,net)+"\", \"dl_type\":\"2048\", \"nw_dst\":\""+
								hostIP[Integer.parseInt(listFlows[i][0])]+"\"}, \"actions\": [{\"type\": \"OUTPUT\", \"port\": \""+
								getOutPort("h"+a,"s"+b,net)+"\"}]}' http://localhost:8080/stats/flowentry/add");

						//-->
						/*regrasRyu.append("\n#(8-->) ARP");
						regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
								portTCP[Integer.parseInt(b)]+
								" flow-mod table=0,cmd=add eth_type=0x806,arp_tpa="+
								hostIP[Integer.parseInt(listFlows[i][1])] +
								" apply:output="+
								getInPort("s"+c,"s"+d,net));

						//Ex.: dpctl tcp:127.0.0.1:6637 flow-mod table=0,cmd=add in_port=1 apply:output=3
						regrasRyu.append("\n#(9-->) ICMP and all others:");
						regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
								portTCP[Integer.parseInt(b)]+
								" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
								hostIP[Integer.parseInt(listFlows[i][1])] +
								" apply:output="+
								getInPort("s"+c,"s"+d,net));
						 */

						regrasRyu.append("\n#(8-->) ARP");
						regrasRyu.append("\ncurl -X POST -d '{\"dpid\": \""+b+"\",\"match\": {\"in_port\":\""+
								getOutPort("h"+a,"s"+b,net)+"\", \"dl_type\":\"2054\", \"nw_dst\":\""+
								hostIP[Integer.parseInt(listFlows[i][1])]+"\"}, \"actions\": [{\"type\": \"OUTPUT\", \"port\": \""+
								getInPort("s"+c,"s"+d,net)+"\"}]}' http://localhost:8080/stats/flowentry/add");
						
						regrasRyu.append("\n#(8-->) IP");
						regrasRyu.append("\ncurl -X POST -d '{\"dpid\": \""+b+"\",\"match\": {\"in_port\":\""+
								getOutPort("h"+a,"s"+b,net)+"\", \"dl_type\":\"2048\", \"nw_dst\":\""+
								hostIP[Integer.parseInt(listFlows[i][1])]+"\"}, \"actions\": [{\"type\": \"OUTPUT\", \"port\": \""+
								getInPort("s"+c,"s"+d,net)+"\"}]}' http://localhost:8080/stats/flowentry/add");

						System.out.println("\nEnd step 2");
						//---
					} else
						//More then 2 paths
						return;

				//
				//---Middle nodes


				//Before
				//
				//a=4
				//b=2
				//
				a = c;
				b = d;

				//Get new link
				//
				//4:3
				t_sub = new StringTokenizer(t.nextToken(),":");
				//4
				c = t_sub.nextToken();
				//3
				d = t_sub.nextToken();				

				System.out.println("--a:["+a+"] b:["+b+"] c:["+c+"] d:["+d+"]");
				String c_aux="", d_aux="";

				//Rules for all other nodes

				//While has more tokens AND
				//  not found the destination node
				while(t.hasMoreTokens() && !d.equals(listFlows[i][1])){ 

					//Split here
					//Ex.: ...4:2|4:3|...|2:5...|3:6...
					if(a.equals(c)){

						//Do tasks for the first split 

						//Copy t
						auxToken = new StringBuffer(copy(t));
						System.out.println("1["+auxToken+"]");
						t_split = new StringTokenizer(auxToken.toString(),"|");

						//a=4
						//b=2
						//c=4
						//d=3						
						System.out.println("Split 1: s"+a+":s"+b+" s"+c+":s"+d);
						c_aux=c;
						d_aux=d;

						//Go until the correct path after split
						//...4:2|...|2:5...
						while(!b.equals(c))
							if(t_split.hasMoreTokens()){								
								t_split_sub=new StringTokenizer(t_split.nextToken(),":");
								c=t_split_sub.nextToken();
								d=t_split_sub.nextToken();
							}

						//Here I am in the path after split
						regrasRyu.append("\n#a:["+a+"] b:["+b+"] c:["+c+"] d:["+d+"]");
						regrasRyu.append(
								"\n#s"+b+
								"\n# (10<--) ICMP and all others"); 
						//Ex.: dpctl tcp:127.0.0.1:6635 flow-mod table=0,cmd=add in_port=4 apply:output=3					
						/*regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
								portTCP[Integer.parseInt(b)]+
								//" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
								//hostIP[Integer.parseInt(listFlows[i][0])]+",in_port="+
								" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+hostIP[Integer.parseInt(listFlows[i][0])]+",in_port="+							
								getInPort("s"+c,"s"+d,net)+
								" apply:output="+
								getOutPort("s"+a,"s"+b,net));
						regrasRyu.append(
								"\n#s"+b+
								"\n# (11<--) ARP");
						regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
								portTCP[Integer.parseInt(b)]+
								//" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
								//hostIP[Integer.parseInt(listFlows[i][0])]+",in_port="+
								" flow-mod table=0,cmd=add eth_type=0x806,arp_tpa="+hostIP[Integer.parseInt(listFlows[i][0])]+",in_port="+							
								getInPort("s"+c,"s"+d,net)+
								" apply:output="+
								getOutPort("s"+a,"s"+b,net));
						 */
						regrasRyu.append(
								"\n#s"+b+
								"\n# (10<--) ICMP and all others");
						regrasRyu.append("\ncurl -X PUT -d '{\"mac\" : \""+getMAC()+"\",\"port\" : "+
								getInPort("s"+c,"s"+d,net)+"}' http://127.0.0.1:8080/simpleswitch/mactable/"+getMAC(c));

						///////
						regrasRyu.append(
								"\n#(12-->) ICMP and all others");
						//Ex.: dpctl tcp:127.0.0.1:6635 flow-mod table=0,cmd=add in_port=3 apply:output=4
						regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
								portTCP[Integer.parseInt(b)]+
								//" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
								//hostIP[Integer.parseInt(listFlows[i][1])]+",in_port="+
								" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+hostIP[Integer.parseInt(listFlows[i][1])]+",in_port="+
								getInPort("s"+b,"s"+a,net)+
								" apply:output="+
								getOutPort("s"+d,"s"+c,net));
						regrasRyu.append(
								"\n#(13-->) ARP");
						//Ex.: dpctl tcp:127.0.0.1:6635 flow-mod table=0,cmd=add in_port=3 apply:output=4
						regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
								portTCP[Integer.parseInt(b)]+
								//" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
								//hostIP[Integer.parseInt(listFlows[i][1])]+",in_port="+
								" flow-mod table=0,cmd=add eth_type=0x806,arp_tpa="+hostIP[Integer.parseInt(listFlows[i][1])]+",in_port="+
								getInPort("s"+b,"s"+a,net)+
								" apply:output="+
								getOutPort("s"+d,"s"+c,net));

						//
						// Do tasks for the second split

						//Keep last link
						// 4:3
						a = c_aux;
						b = d_aux;

						//Copy t						
						//System.out.println("2["+auxToken+"]");
						//auxToken = new StringBuffer(copy(auxToken));
						//System.out.println("3["+auxToken+"]");
						t_split = new StringTokenizer(auxToken.toString(),"|");
						System.out.println("4["+auxToken+"]");

						System.out.println("Split 2: s"+a+":s"+b+" s"+c+":s"+d);
						//c_aux=c;
						//d_aux=d;

						//Go until the correct path after split
						//...4:3|...|3:5...
						while(!b.equals(c))
							if(t_split.hasMoreTokens()){								
								t_split_sub=new StringTokenizer(t_split.nextToken(),":");
								c=t_split_sub.nextToken();
								d=t_split_sub.nextToken();
							}

						//Here I am in the path after split
						regrasRyu.append("\n#a:["+a+"] b:["+b+"] c:["+c+"] d:["+d+"]");
						regrasRyu.append(
								"\n#s"+b+
								"\n# (14<--) ICMP and all others"); 
						//Ex.: dpctl tcp:127.0.0.1:6635 flow-mod table=0,cmd=add in_port=4 apply:output=3					
						regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
								portTCP[Integer.parseInt(b)]+
								//" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
								//hostIP[Integer.parseInt(listFlows[i][0])]+",in_port="+
								" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+hostIP[Integer.parseInt(listFlows[i][0])]+",in_port="+							
								getInPort("s"+c,"s"+d,net)+
								" apply:output="+
								getOutPort("s"+a,"s"+b,net));
						regrasRyu.append(
								"\n#s"+b+
								"\n# (15<--) ARP"); 
						//Ex.: dpctl tcp:127.0.0.1:6635 flow-mod table=0,cmd=add in_port=4 apply:output=3					
						regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
								portTCP[Integer.parseInt(b)]+
								//" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
								//hostIP[Integer.parseInt(listFlows[i][0])]+",in_port="+
								" flow-mod table=0,cmd=add eth_type=0x806,arp_tpa="+hostIP[Integer.parseInt(listFlows[i][0])]+",in_port="+							
								getInPort("s"+c,"s"+d,net)+
								" apply:output="+
								getOutPort("s"+a,"s"+b,net));

						regrasRyu.append(
								"\n#(16-->) ICMP and all others");
						//Ex.: dpctl tcp:127.0.0.1:6635 flow-mod table=0,cmd=add in_port=3 apply:output=4
						regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
								portTCP[Integer.parseInt(b)]+
								//" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
								//hostIP[Integer.parseInt(listFlows[i][1])]+",in_port="+
								" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+hostIP[Integer.parseInt(listFlows[i][1])]+",in_port="+
								getInPort("s"+b,"s"+a,net)+
								" apply:output="+
								getOutPort("s"+d,"s"+c,net));
						regrasRyu.append(
								"\n#(17-->) ARP");
						//Ex.: dpctl tcp:127.0.0.1:6635 flow-mod table=0,cmd=add in_port=3 apply:output=4
						regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
								portTCP[Integer.parseInt(b)]+
								//" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
								//hostIP[Integer.parseInt(listFlows[i][1])]+",in_port="+
								" flow-mod table=0,cmd=add eth_type=0x806,arp_tpa="+hostIP[Integer.parseInt(listFlows[i][1])]+",in_port="+
								getInPort("s"+b,"s"+a,net)+
								" apply:output="+
								getOutPort("s"+d,"s"+c,net));


						System.out.println("---*a:["+a+"] b:["+b+"] c:["+c+"] d:["+d+"]");

						t = new StringTokenizer(auxToken.toString(),"|");
						////////////////
						//Keep last link
						//...2:5...
						t_sub = new StringTokenizer(t.nextToken(),":");
						//2
						a = t_sub.nextToken();
						b = t_sub.nextToken();						

						if(t.hasMoreTokens()){
							//5:8
							t_sub = new StringTokenizer(t.nextToken(),":");
							//5
							c = t_sub.nextToken();
							//8
							d = t_sub.nextToken(); 
						}//end if

						System.out.println("---*a:["+a+"] b:["+b+"] c:["+c+"] d:["+d+"]");

						System.out.println("\nEnd step 3");

					} else {

						System.out.println("No split");
						//System.exit(0);

						regrasRyu.append("\n#a:["+a+"] b:["+b+"] c:["+c+"] d:["+d+"]");
						regrasRyu.append("\n#s"+b);
						regrasRyu.append("\n# (18<--) IP");
						/*
						//Ex.: dpctl tcp:127.0.0.1:6635 flow-mod table=0,cmd=add in_port=4 apply:output=3					
						regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
								portTCP[Integer.parseInt(b)]+
								//" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
								//hostIP[Integer.parseInt(listFlows[i][0])]+",in_port="+
								" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+hostIP[Integer.parseInt(listFlows[i][0])]+",in_port="+							
								getInPort("s"+c,"s"+d,net)+
								" apply:output="+
								getOutPort("s"+a,"s"+b,net));
						regrasRyu.append(
								"\n#s"+b+
								"\n# (19<--) ARP"); 
						//Ex.: dpctl tcp:127.0.0.1:6635 flow-mod table=0,cmd=add in_port=4 apply:output=3					
						regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
								portTCP[Integer.parseInt(b)]+
								//" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
								//hostIP[Integer.parseInt(listFlows[i][0])]+",in_port="+
								" flow-mod table=0,cmd=add eth_type=0x806,arp_tpa="+hostIP[Integer.parseInt(listFlows[i][0])]+",in_port="+							
								getInPort("s"+c,"s"+d,net)+
								" apply:output="+
								getOutPort("s"+a,"s"+b,net));
						 */
						
						regrasRyu.append("\ncurl -X POST -d '{\"dpid\": \""+b+"\",\"match\": {\"in_port\":\""+
								getInPort("s"+c,"s"+d,net)+"\", \"dl_type\":\"2048\", \"nw_dst\":\""+
								hostIP[Integer.parseInt(listFlows[i][0])]+"\"}, \"actions\": [{\"type\": \"OUTPUT\", \"port\": \""+
								getOutPort("s"+a,"s"+b,net)+"\"}]}' http://localhost:8080/stats/flowentry/add");
						
						regrasRyu.append("\n# (18<--) ARP");
						regrasRyu.append("\ncurl -X POST -d '{\"dpid\": \""+b+"\",\"match\": {\"in_port\":\""+
								getInPort("s"+c,"s"+d,net)+"\", \"dl_type\":\"2054\", \"nw_dst\":\""+
								hostIP[Integer.parseInt(listFlows[i][0])]+"\"}, \"actions\": [{\"type\": \"OUTPUT\", \"port\": \""+
								getOutPort("s"+a,"s"+b,net)+"\"}]}' http://localhost:8080/stats/flowentry/add");
						
						regrasRyu.append("\n#(20-->) IP");
						/*//Ex.: dpctl tcp:127.0.0.1:6635 flow-mod table=0,cmd=add in_port=3 apply:output=4
						regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
								portTCP[Integer.parseInt(b)]+
								//" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
								//hostIP[Integer.parseInt(listFlows[i][1])]+",in_port="+
								" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+hostIP[Integer.parseInt(listFlows[i][1])]+",in_port="+
								getOutPort("s"+a,"s"+b,net)+
								" apply:output="+
								getInPort("s"+c,"s"+d,net));					
						regrasRyu.append(
								"\n#(21-->) ARP");
						//Ex.: dpctl tcp:127.0.0.1:6635 flow-mod table=0,cmd=add in_port=3 apply:output=4
						regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
								portTCP[Integer.parseInt(b)]+
								//" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
								//hostIP[Integer.parseInt(listFlows[i][1])]+",in_port="+
								" flow-mod table=0,cmd=add eth_type=0x806,arp_tpa="+hostIP[Integer.parseInt(listFlows[i][1])]+",in_port="+
								getOutPort("s"+a,"s"+b,net)+
								" apply:output="+
								getInPort("s"+c,"s"+d,net));
						 */
						
						regrasRyu.append("\ncurl -X POST -d '{\"dpid\": \""+b+"\",\"match\": {\"in_port\":\""+
								getOutPort("s"+a,"s"+b,net)+"\", \"dl_type\":\"2048\", \"nw_dst\":\""+
								hostIP[Integer.parseInt(listFlows[i][1])]+"\"}, \"actions\": [{\"type\": \"OUTPUT\", \"port\": \""+
								getInPort("s"+c,"s"+d,net)+"\"}]}' http://localhost:8080/stats/flowentry/add");						

						regrasRyu.append("\n#(20-->) ARP");
						regrasRyu.append("\ncurl -X POST -d '{\"dpid\": \""+b+"\",\"match\": {\"in_port\":\""+
								getOutPort("s"+a,"s"+b,net)+"\", \"dl_type\":\"2054\", \"nw_dst\":\""+
								hostIP[Integer.parseInt(listFlows[i][1])]+"\"}, \"actions\": [{\"type\": \"OUTPUT\", \"port\": \""+
								getInPort("s"+c,"s"+d,net)+"\"}]}' http://localhost:8080/stats/flowentry/add");
						
						
						//Keep last link
						a = c;
						b = d;

						//Get new link
						//
						//2:5
						t_sub = new StringTokenizer(t.nextToken(),":");
						//2
						c = t_sub.nextToken();
						//5
						d = t_sub.nextToken();

						System.out.println("---a:["+a+"] b:["+b+"] c:["+c+"] d:["+d+"]");

						System.out.println("\nEnd step 4");
					}//end else
				}//end while
				//---

				//
				//---Destination---

				obj = new SortSplittedPath();					
				StringBuffer listJoin=obj.hasJoin(PATH_after[i], b);
				StringTokenizer t_join = new StringTokenizer(listJoin.toString(),":");

				//Has split
				if (t_join.countTokens()>=2){
					System.out.println("Split destination: "+listJoin);					
					regrasRyu.append(
							"\n#Destination" +
									"\n#s" + b);					
					regrasRyu.append(
							"\n#a:"+a+" b:"+b+" c:"+c+" d:"+d);
					//#Group table
					//dpctl tcp:127.0.0.1:6634 group-mod cmd=add,type=1,group=1 
					//		weight=7,port=1,group=all output=4 
					//		weight=2,port=1,group=all output=3 
					//		weight=1,port=1,group=all output=2
					t_join = new StringTokenizer(listJoin.toString(),":");
					regrasRyu.append("\n#(22<--) Split: Group table");
					regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
							portTCP[Integer.parseInt(b)]+
							" group-mod cmd=add,type=1,group="+groupTableID);

					while(t_join.hasMoreElements()){
						aux = t_join.nextToken();
						regrasRyu.append(" \\\n weight="+getWeight(aux,listJoin,F)+
								",port="+getOutPort("h"+d,"s"+b,net)+
								",group=all output="+getInPort("s"+b,"s"+aux,net));
					}//end while	

					regrasRyu.append("\n#(23<--) ARP");
					regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
							portTCP[Integer.parseInt(b)]+
							" flow-mod table=0,cmd=add eth_type=0x806,arp_tpa="+
							hostIP[Integer.parseInt(listFlows[i][0])] +
							" apply:group="+groupTableID);
					regrasRyu.append("\n#(24-->) ARP");
					regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
							portTCP[Integer.parseInt(b)]+
							" flow-mod table=0,cmd=add eth_type=0x806,arp_tpa="+
							hostIP[Integer.parseInt(listFlows[i][1])] +
							" apply:output="+getOutPort("h"+d,"s"+b,net));

					regrasRyu.append("\n#(25<--) Split: ICMP and all others:");
					regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
							portTCP[Integer.parseInt(b)]+
							" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
							hostIP[Integer.parseInt(listFlows[i][0])] +
							" apply:group="+groupTableID);
					regrasRyu.append("\n#(26-->) Split: ICMP and all others:");
					regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
							portTCP[Integer.parseInt(b)]+
							" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
							hostIP[Integer.parseInt(listFlows[i][1])] +
							" apply:output="+getOutPort("h"+d,"s"+b,net));

					groupTableID++;

					System.out.println("\nEnd step 5");

				} else {
					//Rule for the last A_SWITCH connected at destination TOR
					//Ex.: dpctl tcp:127.0.0.1:6638 flow-mod table=0,cmd=add eth_type=0x806,arp_tpa=10.0.0.1 apply:output=1
					regrasRyu.append(
							"\n#Destination" +
									"\n#s" + b);
					regrasRyu.append(
							"\n#a:"+a+" b:"+b+" c:"+c+" d:"+d);
					regrasRyu.append("\n#(27<--) IP");
					/*regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
							portTCP[Integer.parseInt(b)]+
							" flow-mod table=0,cmd=add eth_type=0x806,arp_tpa="+
							hostIP[Integer.parseInt(listFlows[i][0])] +
							" apply:output="+
							getOutPort("s"+a,"s"+b,net));

					//Ex.: dpctl tcp:127.0.0.1:6637 flow-mod table=0,cmd=add in_port=1 apply:output=3
					regrasRyu.append("\n#(28<--) ICMP and all others:");
					regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
							portTCP[Integer.parseInt(b)]+
							" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
							hostIP[Integer.parseInt(listFlows[i][0])] +
							" apply:output="+
							getOutPort("s"+a,"s"+b,net));
					 */
					regrasRyu.append("\ncurl -X POST -d '{\"dpid\": \""+b+"\",\"match\": {\"in_port\":\""+
							getInPort("s"+c,"h"+d,net)+"\", \"dl_type\":\"2048\", \"nw_dst\":\""+
							hostIP[Integer.parseInt(listFlows[i][0])]+"\"}, \"actions\": [{\"type\": \"OUTPUT\", \"port\": \""+
							getOutPort("s"+a,"s"+b,net)+"\"}]}' http://localhost:8080/stats/flowentry/add");
					
					regrasRyu.append("\n#(27<--) ARP");
					regrasRyu.append("\ncurl -X POST -d '{\"dpid\": \""+b+"\",\"match\": {\"in_port\":\""+
							getInPort("s"+c,"h"+d,net)+"\", \"dl_type\":\"2054\", \"nw_dst\":\""+
							hostIP[Integer.parseInt(listFlows[i][0])]+"\"}, \"actions\": [{\"type\": \"OUTPUT\", \"port\": \""+
							getOutPort("s"+a,"s"+b,net)+"\"}]}' http://localhost:8080/stats/flowentry/add");

					//-->
					regrasRyu.append("\n#(29-->) IP");
					/*regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
							portTCP[Integer.parseInt(b)]+
							" flow-mod table=0,cmd=add eth_type=0x806,arp_tpa="+
							hostIP[Integer.parseInt(listFlows[i][1])] +
							" apply:output="+
							getOutPort("h"+d,"s"+c,net));

					//Ex.: dpctl tcp:127.0.0.1:6637 flow-mod table=0,cmd=add in_port=1 apply:output=3
					regrasRyu.append("\n#(30-->) ICMP and all others:");
					regrasRyu.append("\ndpctl tcp:127.0.0.1:" + 
							portTCP[Integer.parseInt(b)]+
							" flow-mod table=0,cmd=add eth_type=0x800,ip_dst="+
							hostIP[Integer.parseInt(listFlows[i][1])] +
							" apply:output="+
							getOutPort("h"+d,"s"+c,net));
					 */
					regrasRyu.append("\ncurl -X POST -d '{\"dpid\": \""+b+"\",\"match\": {\"in_port\":\""+
							getOutPort("s"+a,"s"+b,net)+"\", \"dl_type\":\"2048\", \"nw_dst\":\""+
							hostIP[Integer.parseInt(listFlows[i][1])]+"\"}, \"actions\": [{\"type\": \"OUTPUT\", \"port\": \""+
							getInPort("s"+c,"h"+d,net)+"\"}]}' http://localhost:8080/stats/flowentry/add");
					
					regrasRyu.append("\n#(29-->) ARP");
					regrasRyu.append("\ncurl -X POST -d '{\"dpid\": \""+b+"\",\"match\": {\"in_port\":\""+
							getOutPort("s"+a,"s"+b,net)+"\", \"dl_type\":\"2054\", \"nw_dst\":\""+
							hostIP[Integer.parseInt(listFlows[i][1])]+"\"}, \"actions\": [{\"type\": \"OUTPUT\", \"port\": \""+
							getInPort("s"+c,"h"+d,net)+"\"}]}' http://localhost:8080/stats/flowentry/add");
					
					//---
					System.out.println("\nEnd step 6");

				}//end else

				//Next flow
				i++;
			}//end while


			//---Fim da Geracao das regrasRyu---

			//Grava em arquivo 
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(conf.getArquivoRegrasOpenFlowRyu(),false));
				out.write("\n#--- OpenFlow1.3 Rules: Automatically Generated by MILPFlow Version " + conf.getVersion()+" ---");
				out.write("\n#");
				out.write("\n#--- How to run Mininet: ---");
				out.write("\n#");
				out.write("\n# ./modeloMininet_"+conf.getNumServers()+"serv_"+conf.getNumVMs()+"vm_ryu.py");
				out.write("\n#");
				out.write("\n#--- How to run this rules: ---");
				out.write("\n#");
				out.write("\n# chmod +x "+
						"modeloMininet_"+conf.getNumServers()+"serv_"+conf.getNumVMs()+"vm_regrasOpenFlowRyu.sh");
				out.write("\n#./modeloMininet_"+conf.getNumServers()+"serv_"+conf.getNumVMs()+"vm_regrasOpenFlowRyu.sh");

				//Write rules
				out.write(regrasRyu.toString());
				out.close();
			} catch(Exception e){
				System.out.println("13Excecao ao gravar no arquivo." + e.getMessage());
			}//fim catch

			//close the file			
			file.close();			
		} catch(Exception e){
			System.out.println("Excecao 14 ao abrir o arquivo: " + conf.getArquivoTopologiaDatacenter() + "\n" + e.getMessage());
			e.printStackTrace();
		}//fim catch

	}//fim gerarregrasOpenFlow

	public void initialize(String [] portTCP, int valuePortTCP){

		int i=0;

		//System.out.println(net.length + " " + net[i].length);

		while(i<portTCP.length){
			portTCP[i] = valuePortTCP+i+"";
			i++;
		}//end while

	}//end initialize

	public void initialize(int [] lista){

		int i=0;

		//System.out.println(net.length + " " + net[i].length);

		while(i<lista.length){
			lista[i] = 0;			
			i++;
		}//end while

	}//end initialize

	public void initialize(String [] lista){

		int i=0;

		//System.out.println(net.length + " " + net[i].length);

		while(i<lista.length){
			lista[i] = "";			
			i++;
		}//end while

	}//end initialize

	public int getInPort(String source, String destination, String [] net){

		int inPort=0;

		int i=0;
		StringTokenizer token;
		boolean found=false;
		String s1="";
		String p1="";
		String s2="";
		String p2="";
		while(i<net.length && !found){
			//Ex.:
			//Source = s4
			//Destination = s2
			//net[i] = s1,1,s2,2 or
			//net[i] = s2,2,s1,1 
			token = new StringTokenizer(net[i],",");			
			while(token.hasMoreTokens() && !found){

				//s1
				s1 = token.nextToken();
				//1
				p1 = token.nextToken();
				//s2
				s2 = token.nextToken();
				//2
				p2 = token.nextToken();

				//s1 -- s2
				if(s1.equals(source) && s2.equals(destination) ){
					found=true;
					//1
					inPort = Integer.parseInt(p1);
				} else 
					//s2 -- s1
					if (s2.equals(source) && s1.equals(destination)){					
						found=true;
						//2
						inPort = Integer.parseInt(p2);
					}//end if

			}//end while
			i++;
		}//end while

		return inPort;

	}//end getInPort

	public int getOutPort(String source, String destination, String [] net){

		int outPort=0;

		int i=0;
		StringTokenizer token;
		boolean found=false;
		String s1="";
		String p1="";
		String s2="";
		String p2="";
		while(i<net.length && !found){
			//Ex.:
			//Source = s4
			//Destination = s2
			//net[i] = s1,1,s2,2 or
			//net[i] = s2,2,s1,1 
			token = new StringTokenizer(net[i],",");			
			while(token.hasMoreTokens() && !found){

				//s1
				s1 = token.nextToken();
				//1
				p1 = token.nextToken();
				//s2
				s2 = token.nextToken();
				//2
				p2 = token.nextToken();

				//s1 -- s2
				if(s1.equals(source) && s2.equals(destination) ){
					found=true;
					//2
					outPort = Integer.parseInt(p2);
				} else 
					//s2 -- s1
					if (s2.equals(source) && s1.equals(destination)){					
						found=true;
						//1
						outPort = Integer.parseInt(p1);
					}//end if

			}//end while
			i++;
		}//end while

		return outPort;

	}//end getOutPort


	public int getOutputASwitch(String host, String [] net){

		int port=0;

		int i=0;
		StringTokenizer token;
		boolean found=false;
		String value="";
		while(i<net.length && !found){
			//Ex.:
			//net[i] = h6,2,s4,1
			token = new StringTokenizer(net[i],",");			
			while(token.hasMoreTokens() && !found){
				value=token.nextToken();
				if(value.equals("h"+host)){
					found=true;
					//Get port of the access switch to this ToR
					//2
					token.nextToken();
					//s4
					token.nextToken();
					//1
					port = Integer.parseInt(token.nextToken());
				}//end if

			}//end while
			i++;
		}//end while

		return port;		

	}//end getOutputASwitch

	public int getSwitchHost(String host, String [] net){

		int switchIndex=0;

		int i=0;
		StringTokenizer token;
		boolean found=false;
		String value="";
		while(i<net.length && !found){
			//Ex.:
			//net[i] = h6,2,s4,1
			token = new StringTokenizer(net[i],",");			
			while(token.hasMoreTokens() && !found){
				value=token.nextToken();
				if(value.equals("h"+host)){
					found=true;
					//Get port of the access switch to this ToR
					//2
					token.nextToken();
					//s4
					value = token.nextToken();
					StringTokenizer t = new StringTokenizer(value,"s");
					switchIndex = Integer.parseInt(t.nextToken());

				}//end if

			}//end while
			i++;
		}//end while

		return switchIndex;

	}//end getSwitchHost

	public void newHostIP(){

		//Parse the last IP that was set
		StringTokenizer token = new StringTokenizer(IP,".");
		int p1 = Integer.parseInt(token.nextToken());
		int p2 = Integer.parseInt(token.nextToken());
		int p3 = Integer.parseInt(token.nextToken());
		int p4 = Integer.parseInt(token.nextToken());

		//New IP
		p4++;

		//Mount the new IP
		if(p4==254){
			p4=1;
			p3+=1;
		} else
			if(p3==254){
				p2+=1;
				p3=0;
				p4=1;
			} else 
				if(p2==254){
					//More IPs to ToRs here!
					return;
				}

		IP = p1 + "." + p2 + "." + p3 + "." + p4;

	}//end newHostIP

	public void inicializar(String [][][] A){

		int i=0;
		int j=0;		
		int k=0;
		while (i<A.length){

			j=0;
			while(j<A[i].length){

				k=0;
				while(k<A[i][j].length){				
					A[i][j][k]="0";				
					k++;
				}//fim while
				j++;
			}
			i++;
		}

	}//fim inicializar

	public void inicializar(StringBuffer [] A){

		int i=0;
		while (i<A.length){				
			A[i]=new StringBuffer("");
			i++;
		}

	}//fim inicializar

	public void inicializar(int [] A){

		int i=0;
		while (i<A.length){				
			A[i]=0;
			i++;
		}

	}//fim inicializar

	public void inicializar(String [] queue){

		int i=0;

		while (i<queue.length){
			queue[i]="";
			i++;
		}//end while

	}//fim inicializar	

	public String removerEspacos(String s) {
		StringTokenizer st = new StringTokenizer(s," ",false);
		String t="";
		while (st.hasMoreElements()) t += st.nextElement();
		return t;
	}

	public StringBuffer copy(StringTokenizer t){

		StringBuffer auxToken = new StringBuffer();

		while(t.hasMoreTokens())
			auxToken.append(t.nextToken()+"|");

		return auxToken;

	}//end copy

	public int getWeight(String c, StringBuffer listSplit, String [][][] F){

		int weight=1;

		return weight;
	}

	public String getMAC(){

		int size=12;
		String c = String.format("%0"+size+"x", MAC_ID);
		int i=0;
		int j=1;
		String mac=c.substring(i, j+1);
		//System.out.println(aux);
		i+=2;
		j+=2;
		while(j<size){
			mac += ":"+c.substring(i, j+1);				
			i+=2;
			j+=2;
			//System.out.println(aux);
		}//end while
		//System.out.println(aux);
		
		MAC_ID++;

		return mac;

	}//end getMAC

	//Full MAC for datapath IDs are in hexadecimal. See: /ryu/lib/dpid.py
	public String getMAC(String b){

		String mac=String.format("%016x", Integer.parseInt(b));		

		return mac;

	}//end getMAC


}//fim classe
