import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModeloLingo {

	//Matriz de trafego
	public static String [][] CUSTO;

	public ModeloLingo(){

		gerarModeloLingo();

	}//fim construtor

	public void gerarModeloLingo(){

		System.out.println("\n---Gerar Modelo Lingo---\n");

		//Adquire as informacoes da topologia que foi salva em arquivo
		ConfigSettings conf = new ConfigSettings();

		try
		{
			int i=0;
			int j=0;
			int k=0;

			String linha = new String();
			//try to open the file.
			BufferedReader file = new BufferedReader(new FileReader(conf.getArquivoTopologiaDatacenter()));
			//Cabecalho
			linha=file.readLine();			
			StringTokenizer token = new StringTokenizer(linha, "( )");
			token.nextToken();
			//System.out.println("\nnumNodesOriginal: "+Integer.parseInt(token.nextToken()));			
			int numNodesOriginal = Integer.parseInt(token.nextToken());
			token.nextToken(); //Salta a string "Nodes,"
			int numEdgesOriginal = Integer.parseInt(token.nextToken());
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
				//System.out.println("#"+linha+"\n");
				//sourceNode	destinationNode		
				REGEX = "(.*)\\s(.*)\\s(.*)\\s(.*)";
				pattern = Pattern.compile(REGEX);
				matcher = pattern.matcher(linha);
				if (matcher.find()){
					//System.out.println(matcher.group(1)+";"+matcher.group(2)+";");
					sequenceFlows.append(matcher.group(1)+";"+matcher.group(2)+";"+matcher.group(3)+";");					
				}//end if

				//Amout of flows
				i++;
			}//end while

			//Create a well structured list of flows
			//
			//Ex.: 
			//      sourceNode=6		destinationNode=8
			//		sourceNode=7		destinationNode=9
			//
			//     lisFlows[0][0]=6
			//	   lisFlows[0][1]=8
			//	   lisFlows[1][0]=7
			//	   lisFlows[1][1]=9
			//     ...
			String[][] listFlows = new String[i-1][3]; //-1 because last increment is not considered
			token = new StringTokenizer(sequenceFlows.toString(), ";");
			i=0;
			System.out.println("\nFlows:\n");
			while(token.hasMoreElements()){
				listFlows[i][0]=removerEspacos(token.nextToken());
				listFlows[i][1]=removerEspacos(token.nextToken());
				listFlows[i][2]=removerEspacos(token.nextToken());
				System.out.println("SourceNode: ["+listFlows[i][0]+"] DestinationNode: ["+listFlows[i][1]+"] AggregatedTraffic: ["+listFlows[i][2]+"]\n");
				i++;
			}//end while			
			//System.out.println("i: " +i+ listFlows.length);

			//---------------------Inicio do Modelo Lingo---------------------
			i=0;
			j=0;

			//Blocos do arquivo do modelo Lingo
			StringBuffer [] passo = new StringBuffer[18];
			int indice=0;

			int numToRs=conf.getNumServers()/conf.getNumServersToR();

			//Passo0: sets			
			passo[indice] = new StringBuffer();
			passo[indice].append("\nMODEL:");
			passo[indice].append("\n\nSETS:");

			passo[indice].append("\n\nSERVERS /1.." + (numNodesOriginal + numToRs) + "/: SBW;"); //To keep the index of ToRs after numNodesOriginal

			passo[indice].append("\n\nTORS /1.." + (numNodesOriginal + numToRs) + "/: TORBW;"); //To keep the index of ToRs after numNodesOriginal			

			passo[indice].append("\n\nMATRIZ_TRAFEGO( SERVERS, TORS ): CUSTO, DADOS;");

			passo[indice].append("\n\n!SWITCHS (Here are included ToRs too);");
			passo[indice].append("\nSWITCHS /1.." + (numNodesOriginal+numToRs) + "/;");			
			passo[indice].append("\n\n!LINKS;");
			passo[indice].append("\nLINKS(SWITCHS, SWITCHS): L ");

			//Create the entry for a new flow
			StringBuffer flows= new StringBuffer();
			//System.out.println(listFlows.length);
			i=0;
			while(i<listFlows.length){

				//Ex.: 
				//      sourceNode=6		destinationNode=8
				//		sourceNode=7		destinationNode=9
				//
				//		f1_6_8
				//     	f2_9_7
				//
				flows.append("\n, F"+(i+1)+"_"+listFlows[i][0]+"_"+listFlows[i][1]); //+1 because Lingo index begin at 1
				i++;
			}//end while
			//Insert entry of flows into the model
			passo[indice].append(flows+";");

			passo[indice].append("\n\nENDSETS");
			indice++; 


			//Gera a matriz de links APENAS para os switches internos			
			passo[indice] = new StringBuffer();

			passo[indice].append("\n!LINK RESTRICTIONS: UPLINKS AND DOWNLINKS;");
			i=0;
			j=0;
			int from=0;
			int to=0;
			while(i<edges_arquivo.length){

				from = Integer.parseInt(edges_arquivo[i][campoEdgeFrom]);
				to = Integer.parseInt(edges_arquivo[i][campoEdgeTo]);

				passo[indice].append("\nL("+ from + "," + to + ") = " + edges_arquivo[i][campoEdgeBw] + ";");
				passo[indice].append("\nL("+ to + "," + from + ") = " + edges_arquivo[i][campoEdgeBw] + ";");
				i++;
			}//fim while			
			indice++; 

			passo[indice] = new StringBuffer();			
			passo[indice].append("\n[OBJ] MIN = \n" +
					"@SUM( MATRIZ_TRAFEGO: CUSTO * DADOS ) +\n" +					
					"@SUM(SERVERS(I):\n" +
			"@SUM(TORS(J): CUSTO(I,J)));");

			//Sem matriz de trafego
			//passo[indice].append("\n[OBJ] MIN = @SUM(SERVERS: COST *X );");

			//////////////////////////////
			i=1;			
			j=1; //indices no Lingo comecam em 1
			int indiceToR=1;

			/*while(indiceToR<=numToRs){  

				k=1;
				passo[indice].append("\n\n!COST SMALL SERVERS ON TOR " + indiceToR + "." + k + ";");
				passo[indice].append("\n@FOR(SERVERS(J) | J #GT# ");
				if (i!=1){
					passo[indice].append("P(" + i + ")");
					i++;
				}
				else 
					passo[indice].append("0"); //primeiro elemento
				passo[indice].append(" #AND# J #LE# P(" + j + "):");
				passo[indice].append("\nCOST(J)=Q("+j+")+0.01*J);");				

				j++;
				passo[indice].append("\n!COST LARGE SERVERS ON TOR " + indiceToR + "." + ++k + ";");				
				passo[indice].append("\n@FOR(SERVERS(J) | J #GT# P(" + i + ") #AND# J #LE# P(" + j + "):");
				passo[indice].append("\nCOST(J)=Q("+j+")+0.01*J);");
				i++;
				j++;
				passo[indice].append("\n!COST HUGE SERVERS ON TOR " + indiceToR + "." + ++k + ";");
				passo[indice].append("\n@FOR(SERVERS(J) | J #GT# P(" + i + ") #AND# J #LE# P(" + j + "):");
				passo[indice].append("\nCOST(J)=Q("+j+")+0.01*J);");
				i++;
				j++;

				//Proximo ToR
				indiceToR++;

			}//fim while
			 */
			indice++; 

			//////////////////////////////			
			passo[indice] = new StringBuffer();
			passo[indice].append("\n\n!CAPACITY OF DESTINATIONS;");
			passo[indice].append("\n@FOR( TORS(J): ");
			passo[indice].append("\n@SUM( SERVERS(I): DADOS(I, J)) >= TORBW(J));");

			passo[indice].append("\n\n!DEMANDS OF SERVERS;");
			passo[indice].append("\n@FOR( SERVERS(I): ");
			passo[indice].append("\n@SUM( TORS(J): DADOS(I, J)) <= TORBW(I));");
			indice++; 

			passo[indice] = new StringBuffer();
			indice++; 
			//System.out.println(passo[3].toString());	

			passo[indice] = new StringBuffer();			
			indice++; 
			//System.out.println(passo[5].toString());

			//Flows from sourceToR to destinationToR
			passo[indice] = new StringBuffer();			
			i=0;
			while(i<listFlows.length){

				//Ex.: 
				//      sourceNode=6		destinationNode=8
				//		sourceNode=7		destinationNode=9
				//
				//		f1_6_8
				//     	f2_9_7
				//
				System.out.println("\nF"+(i+1)+"_"+listFlows[i][0]+"_"+listFlows[i][1]); //+1 because Lingo index begin at 1
				i++;
			}//end while			
			i=0;
			while(i<listFlows.length){
				passo[indice].append("\n\n!UPLINK: FLOW GENERATED BY TOR " + listFlows[i][0] + ";");
				passo[indice].append("\n@SUM(TORS(J)| J #EQ# " + listFlows[i][0] + ":" +
						"\n@SUM(SERVERS(I)| I #EQ# " + listFlows[i][0] + ":SBW(I))) = " +
						"F"+(i+1)+"_"+listFlows[i][0]+"_"+listFlows[i][1]+"("+listFlows[i][0]+","+connectionAccessSwitch(edges_arquivo, listFlows[i][0])+");");
				i++;
			}//fim while
			indice++; 

			//
			passo[indice] = new StringBuffer();
			//None
			indice++;


			passo[indice] = new StringBuffer();
			passo[indice].append("\n\n!CONSERVATION FLOWS;");


			//Restricoes de continuidade
			//System.out.println("\nPasso14: Restricoes de Continuidade");
			i=1;
			j=0;
			//Adquire todos os nodos source para cada nodo
			//listaChegaNode = [ 1 | 2 3 4 ... ]
			//                  [ 2 | 1 3 ]
			//                  ...		
			/*Node [] nodes = topologiaRede.getNodes();
			 */
			StringBuffer [][] listaChegaNode = new StringBuffer[numNodes+1][1];
			while (i<listaChegaNode.length){
				listaChegaNode[i][0]=new StringBuffer();
				j=0;
				//Para cada nodo, varre todas as arestas
				while (j<numEdges){
					//Exemplo:
					//ID: 2
					//(Source) 1 --> 2 (Destination)
					//Se o destino da aresta eh o nodo atual
					//guarda o nodo de origem da aresta
					if (Integer.parseInt(edges_arquivo[j][campoEdgeTo])==i){
						listaChegaNode[i][0].append( edges_arquivo[j][campoEdgeFrom] + " ");
						//System.out.println("Chega no Node" + i + ": [" + listaChegaNode[i][0] + "]");
					}//fim if
					j++;
				}//fim while			
				i++;
				//System.out.print(i + " ");
			}//fim while					

			//Adquire todos os nodos com destino para cada nodo
			//listaDestinationNode = [ 1 | 2 3 4 ... ]
			//                       [ 2 | 1 3 ]
			//                       ...		
			StringBuffer [][] listaSaiNode = new StringBuffer[numNodes+1][1];
			i=1;
			while (i<listaSaiNode.length){
				listaSaiNode[i][0]=new StringBuffer();
				j=0;
				//Para cada nodo, varre todas as arestas
				while (j<numEdges){
					//Exemplo:
					//ID: 1
					//(Source) 1 --> 2 (Destination)
					//Se a origem da aresta eh o nodo atual
					//guarda o nodo de destino da aresta
					if (Integer.parseInt(edges_arquivo[j][campoEdgeFrom])==i){
						listaSaiNode[i][0].append( edges_arquivo[j][campoEdgeTo] + " ");
						//System.out.println("Sai do Node" + i + ": [" + listaSaiNode[i][0] + "]");
					}//fim if
					j++;
				}//fim while			
				i++;
				//System.out.print(i + " ");
			}//fim while

			//System.exit(0);

			//
			//For each flow
			int f=0;
			while(f<listFlows.length){

				//Para cada nodo intermediario, a soma dos fluxos de entrada menos a soma dos fluxos de saida eh 0 (zero)
				i=0;
				StringTokenizer listaDestino;
				StringBuffer [] restricoesContinuidadeDestino = new StringBuffer[numNodes+1];
				StringBuffer [] restricoesContinuidadeDestino_inv = new StringBuffer[numNodes+1];
				String valor="";
				while(i<restricoesContinuidadeDestino.length){
					restricoesContinuidadeDestino[i]=new StringBuffer();
					restricoesContinuidadeDestino_inv[i]=new StringBuffer();
					//listaDestino armazena cada token
					//listaDestino = 2 3 4 ...
					//Ex.: 1 --> 2 3 4 ...
					//
					//A conexao com o destino acess switch eh um caso especial
					//Ex.: 6	8  //6 eh o ToR com fluxo de origem
					//
					//No arquivo: modeloLingo.datacenter:
					//
					//8		5	//5 eh o ToR de destino
					if(listaSaiNode[i][0]!=null){ 

						//Root is an special case
						/*if (i==1){
							restricoesContinuidadeDestino[i].append(" ROOT ");
							restricoesContinuidadeDestino_inv[i].append(" ROOT ");
						}*/

						listaDestino = new StringTokenizer(listaSaiNode[i][0].toString());
						while (listaDestino.hasMoreTokens()) {
							valor = listaDestino.nextToken();
							restricoesContinuidadeDestino[i].append(" F"+(f+1)+"_" + listFlows[f][0] + "_" + listFlows[f][1] + 
									"(" + i + "," + valor + ") ");
							restricoesContinuidadeDestino_inv[i].append(" F"+(f+1)+"_" + listFlows[f][0] + "_" + listFlows[f][1] + 
									"(" + valor + "," + i + ") ");						

							if(listaDestino.hasMoreTokens()){
								restricoesContinuidadeDestino[i].append(" +\n");
								restricoesContinuidadeDestino_inv[i].append(" +\n");
							}//fim if
						}//fim while

						/*} else {
						System.out.println("i: " + i + " listaSaiNode: "+listaSaiNode[i][0]);
						restricoesContinuidadeDestino[i].append(" F"+(f+1)+"_" + listFlows[f][0] + "_" + listFlows[f][1] + 
								"(" + i + "," + listFlows[f][1] + ") ");
						restricoesContinuidadeDestino_inv[i].append(" F"+(f+1)+"_" + listFlows[f][0] + "_" + listFlows[f][1] + 
								"(" + listFlows[f][1] + "," + i + ") ");
					}//end else
						 */

					}//fim if
					i++;
					//System.out.print(i + " ");
				}//fim while

				i=0;
				StringTokenizer listaOrigem;
				StringBuffer [] restricoesContinuidadeOrigem = new StringBuffer[numNodes+1];
				StringBuffer [] restricoesContinuidadeOrigem_inv = new StringBuffer[numNodes+1];
				valor="";
				boolean achouConexaoInicial=false;
				while(i<restricoesContinuidadeOrigem.length){
					restricoesContinuidadeOrigem[i]=new StringBuffer();
					restricoesContinuidadeOrigem_inv[i]=new StringBuffer();
					achouConexaoInicial=false;
					//listaOrigem armazena cada token
					//listaOrigem = 2 3 4 ...
					//Ex.: 1 --> 2 3 4 ...
					//
					//
					//A conexao com o primeiro acess switch eh um caso especial
					//Ex.: 6	8  //6 eh o ToR com fluxo de origem
					//
					//No arquivo: modeloLingo.datacenter:
					//
					//6		4	//4 eh o ToR de origem, ao qual o ToR 6 se conecta
					if(listaChegaNode[i][0]!=null){

						//Root is a special case
						/*if (i==1){
							restricoesContinuidadeOrigem[i].append(" ROOT ");
							restricoesContinuidadeOrigem_inv[i].append(" ROOT ");
						} 
						else
						 */						
						//Source of flows
						//
						//---If error here, verify that .brite not has an empty line in last line of file
						//
						if (i==Integer.parseInt(connectionAccessSwitch(edges_arquivo, listFlows[f][0]))){ //Source==4

							System.out.println("i: " + i + " listaChegaNode: "+listaChegaNode[i][0]);
							restricoesContinuidadeOrigem[i].append(" F"+(f+1)+"_" + listFlows[f][0] + "_" + listFlows[f][1] + 
									"(" + listFlows[f][0] + "," + i + ") ");
							restricoesContinuidadeOrigem_inv[i].append(" F"+(f+1)+"_" + listFlows[f][0] + "_" + listFlows[f][1] + 
									"(" + i + "," + listFlows[f][0] + ") ");
						} else
							//Destination of flows
							if (i==Integer.parseInt(connectionAccessSwitch(edges_arquivo, listFlows[f][1]))){ //destination==5

								System.out.println("i: " + i + " listaChegaNode: "+listaChegaNode[i][0]);
								restricoesContinuidadeOrigem[i].append(" F"+(f+1)+"_" + listFlows[f][0] + "_" + listFlows[f][1] + 
										"(" + listFlows[f][1] + "," + i + ") ");
								restricoesContinuidadeOrigem_inv[i].append(" F"+(f+1)+"_" + listFlows[f][0] + "_" + listFlows[f][1] + 
										"(" + i + "," + listFlows[f][1] + ") ");
							} else {
								//All other cases
								//
								//System.out.println("i: " + i + " listaChegaNode: "+listaChegaNode[i][0]);
								listaOrigem = new StringTokenizer(listaChegaNode[i][0].toString());

								while (listaOrigem.hasMoreTokens()) {
									valor=listaOrigem.nextToken();
									restricoesContinuidadeOrigem[i].append(" F"+(f+1)+"_" + listFlows[f][0] + "_" + listFlows[f][1] + 
											"(" + valor + "," + i + ") ");
									restricoesContinuidadeOrigem_inv[i].append(" F"+(f+1)+"_" + listFlows[f][0] + "_" + listFlows[f][1] + 
											"(" + i + "," + valor + ") ");						

									if(listaOrigem.hasMoreTokens()){
										restricoesContinuidadeOrigem[i].append(" +\n");
										restricoesContinuidadeOrigem_inv[i].append(" +\n");
									}//end if						
								}//fim while

							}//end else


					}//end if
					i++;
					//System.out.print(i + " ");
				}//fim while

				//Monta as restricoes				
				i=0;
				StringBuffer restricoesContinuidade = new StringBuffer();
				StringBuffer restricoesContinuidade_inv = new StringBuffer();
				while(i<restricoesContinuidadeOrigem.length){
					if ( (restricoesContinuidadeOrigem[i]!=null&&restricoesContinuidadeDestino[i]!=null) &&
							//Multiple destinations
							(!restricoesContinuidadeOrigem[i].toString().equals("")&&!restricoesContinuidadeDestino[i].toString().equals(""))){					
						restricoesContinuidade.append("(" + restricoesContinuidadeOrigem[i] + ") - (" + restricoesContinuidadeDestino[i] + ") = 0;\n");
						restricoesContinuidade_inv.append("(" + restricoesContinuidadeOrigem_inv[i] + ") - (" + restricoesContinuidadeDestino_inv[i] + ") = 0;\n");
					}//fim if
					i++;
					//System.out.print(i + " ");
				}//fim while

				passo[indice].append("\n!UPLINKS;\n"+restricoesContinuidade.toString());
				passo[indice].append("\n\n!DOWNLINKS;\n"+restricoesContinuidade_inv.toString());

				//Next flow
				f++;
			}//end while
			indice++; 
			//System.out.println(passo[7].toString());

			passo[indice] = new StringBuffer();
			//None			
			indice++;

			//Join between UPLINKS and DOWNLINKS 
			passo[indice] = new StringBuffer();
			passo[indice].append("\n!JOIN UPLINKS AND DOWNLINKS OF FLOWS;\n");
			i=0;
			String sourceNode="";
			String destinationNode="";
			while(i<listFlows.length){
				/*System.out.println("\ni: F" +listFlows[i][0]+" "+listFlows[i][1]);
				System.out.println("Source: " +connectionAccessSwitch(edges_arquivo, listFlows[i][0]));
				System.out.println("Destination: "+connectionAccessSwitch(edges_arquivo, listFlows[i][1]));
				 */
				sourceNode=listFlows[i][0];
				destinationNode=listFlows[i][1];
				//UP and DOWN 
				//SOURCE of flow
				passo[indice].append("\n!UP AND DOWN;");
				passo[indice].append("\nF"+(i+1)+"_"+sourceNode+"_"+destinationNode+
						"("+sourceNode+","+connectionAccessSwitch(edges_arquivo, sourceNode)+") - ");
				//DESTINATION of flow
				passo[indice].append("\nF"+(i+1)+"_"+sourceNode+"_"+destinationNode+
						"("+connectionAccessSwitch(edges_arquivo, destinationNode)+","+destinationNode+") = 0;");				

				//DOWN AND UP
				//SOURCE of flow
				passo[indice].append("\n!DOWN AND UP;");
				passo[indice].append("\nF"+(i+1)+"_"+sourceNode+"_"+destinationNode+
						"("+connectionAccessSwitch(edges_arquivo, sourceNode)+","+sourceNode+") - ");
				//DESTINATION of flow
				passo[indice].append("\nF"+(i+1)+"_"+sourceNode+"_"+destinationNode+
						"("+destinationNode+","+connectionAccessSwitch(edges_arquivo, destinationNode)+") = 0;");

				i++;
			}//end while
			/////////////////////////////
			indice++; 

			//Usa a tabela de links
			passo[indice] = new StringBuffer();
			passo[indice].append("\n\n!LINK CAPACITY;");			
			passo[indice].append("\n@FOR(LINKS(I,J):");
			i=0;
			while(i<listFlows.length){
				passo[indice].append("\nF"+(i+1)+"_"+listFlows[i][0]+"_"+listFlows[i][1]+"(I,J) ");//+1 because Lingo index begin at 1
				i++;
				if (i<listFlows.length)
					passo[indice].append("+");
			}//end while
			passo[indice].append("<= L(I,J));");
			indice++; 			

			passo[indice] = new StringBuffer();
			/*passo[indice].append("\n!X: SLACK;");
			passo[indice].append("\n@FOR(SERVERS(J):");
			passo[indice].append("\n@SUM(VMS(I): A(I,J)*VMBW(I)) + X(J) = SCPU(J));");
			 */
			indice++;  

			//Relaxamento para nao usar variaveis binarias			
			passo[indice] = new StringBuffer();
			/*passo[indice].append("\n\n!BINARY VARIABLES;");
			passo[indice].append("\n@FOR(SERVERS(J):");
			passo[indice].append("\n@FOR(VMS(I): ");
			passo[indice].append("\n@BIN(A(I,J))));");
			 */
			indice++;

			//DATA Section
			passo[indice] = new StringBuffer();
			passo[indice].append("\nDATA:");
			//passo[indice].append("\nVMCPU, VMBW = " + conf.getVMSmallCPU() + ", " + conf.getVMSmallBW() + ";");			

			//Update SBW 			
			int limiteSuperior=0;
			//Insert variables to on demand 
			i=0;
			//Index in Lingo begin at 1
			j=1;
			//Only to improve legibility
			k=0;
			int matrixSize=10;
			passo[indice].append("\n\n!GENERATED FLOWS OF TORS;");
			passo[indice].append("\nSBW = ");
			boolean achouAgregado=false;
			//For each ToR evaluate if exist flows from it
			i=0;
			while(i<(numNodes)){
				j=0;
				achouAgregado=false;
				//Exist flows for the current ToR?
				while(j<listFlows.length){					
					sourceNode=listFlows[j][0];
					System.out.println("i: "+i+" sourceNode: "+sourceNode);
					//Exist flows for the current ToR?
					if ((i+1)==Integer.parseInt(sourceNode)){
						passo[indice].append(listFlows[j][2]+ " ");
						achouAgregado=true;
					}//end if					
					//Next flow
					j++;
				}//end while
				if(!achouAgregado)
					//Amount of aggregated flow generated by ToR (default) 
					passo[indice].append("0 ");
				//Next ToR
				i++;
			}//fim while
			passo[indice].append(";\n");

			//Matriz de trafego
			passo[indice].append("\n!TORS x SERVERS;");
			passo[indice].append("\nCUSTO =1");

			/*//Matriz de trafego
			CUSTO = new String[conf.getNumVMs()+1][conf.getNumServers()+1];
			inicializar(CUSTO);

			i=0;
			j=0;
			while(i<(numNodesOriginal + numToRs)){
				j=0;
				while(j<(numNodesOriginal + numToRs)){
					passo[indice].append("1 ");
					j++;
				}//end while
				passo[indice].append("\n");
				i++;
			}//end while
*/
			passo[indice].append(";");
			passo[indice].append("\nENDDATA");
			indice++;

			passo[indice] = new StringBuffer();
			//None			
			indice++;

			//DATA Section for result file
			passo[indice] = new StringBuffer();
			passo[indice].append("\nDATA:");
			//passo[indice].append("\n\n!Status;");
			passo[indice].append("\n\n@TEXT('"+conf.getArquivoLingoResult()+"') =");
			passo[indice].append("\n@WRITE( 'Status:',@STATUS(),");
			passo[indice].append("\n@NEWLINE( 2));");

			//passo[indice].append("\n\n!Indicates if global solution was found;");
			passo[indice].append("\n\n@TEXT('"+conf.getArquivoLingoResult()+"') ="); 
			passo[indice].append("\n@WRITE("); 
			passo[indice].append("\n@IF( @STATUS() #EQ# 0, 'Global optimal', '**** Non-global ***'),");
			passo[indice].append("\n' aolution found at iteration:', @FORMAT( @ITERS(), '14.14G'),");
			passo[indice].append("\n@NEWLINE( 2));");

			//passo[indice].append("\n\n!Objective value;");
			passo[indice].append("\n\n@TEXT('"+conf.getArquivoLingoResult()+"') ="); 			
			passo[indice].append("\n@WRITE('Objective value:', @FORMAT( OBJ, '#41.7G'),");
			passo[indice].append("\n@NEWLINE( 2));");

			passo[indice].append("\n\n@TEXT('"+conf.getArquivoLingoResult()+"') ="); 
			passo[indice].append("\n@WRITE('Variable           Value        Reduced Cost',");
			passo[indice].append("\n@NEWLINE( 1));");
			
			/*passo[indice].append("\n\n!LINKS;");
			passo[indice].append("\n@TEXT('"+conf.getArquivoLingoResult()+"') ="); 
			passo[indice].append("\n@WRITEFOR( LINKS(I,J):"); 
			passo[indice].append("\n@NAME(L(I,J)),"); 
			passo[indice].append("\n@FORMAT( L(I,J), '#16.7G'),");
			passo[indice].append("\n@FORMAT( @DUAL( L(I,J)), '#20.6G'), @NEWLINE(1));");
			*/
			
			i=0;
			while(i<listFlows.length){
				passo[indice].append("\n\n!F;");
				passo[indice].append("\n@TEXT('"+conf.getArquivoLingoResult()+"') ="); 
				passo[indice].append("\n@WRITEFOR( LINKS(I, J):");
				passo[indice].append("\n@NAME( F"+(i+1)+"_"+listFlows[i][0]+"_"+listFlows[i][1]+"(I,J)),");
				passo[indice].append("\n@FORMAT( F"+(i+1)+"_"+listFlows[i][0]+"_"+listFlows[i][1]+"(I,J), '#16.7G'),");
				passo[indice].append("\n@FORMAT( @DUAL( F"+(i+1)+"_"+listFlows[i][0]+"_"+listFlows[i][1]+"(I,J)), '#20.6G'), @NEWLINE(1));");
				i++;
			}//end if

			passo[indice].append("\n\nENDDATA");			

			indice++;


			passo[indice] = new StringBuffer();
			passo[indice].append("\n\nEND");
			passo[indice].append("\n! Terse output mode;" +
					"\nSET TERSEO 1" + 
					"\n! Solve the model;" +
					"\nGO" + 

					"\n! Open a file;" + 
					"\n!DIVERT " + conf.getArquivoLingoResult() + 
					"\n! Send solution to the file;" + 
					"\n!SOLUTION" + 
					"\n! Close solution file;" + 
					"\n!RVRT" + 
					"\n! Quit LINGO;" + 
			"\nQUIT");			
			//System.out.println(passo[13].toString());

			StringBuffer modeloLingo = new StringBuffer();
			i=0;
			while(i<passo.length){
				modeloLingo.append(passo[i].toString()+"\n");				
				i++;
			}//fim while
			//	System.out.println(modeloLingo.toString());

			//---Fim do Modelo Lingo---

			//Grava em arquivo o modelo Lingo
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(conf.getArquivoLingoModelo(),false));			
				out.write(modeloLingo.toString());
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

	}//fim gerarModeloLingo

	/*public String [] getTrafegoToR(String [][] edgesArquivo, int indiceToR){

		//[src dst]
		String [] fonteTrafego=new String[2];

		int campoEdgeId=0;
		int campoEdgeFrom=1;
		int campoEdgeTo=2;
		int campoEdgeCost=3;
		int campoEdgeDelay=4;
		int campoEdgeBw=5;
		int campoEdgeQueueLimit=6;
		int campoEdgeAsTo=7;
		int campoEdgeType=8;
		int campoEdgeOther=9;

		//Busque os nodos folha que sejam do tipo ToR
		int p=0;
		boolean achouFonteTrafego=false;
		while (p<edgesArquivo.length && !achouFonteTrafego){
			if(	edgesArquivo[p][campoEdgeType].equals("TOR") &&
					edgesArquivo[p][campoEdgeTo].equals(indiceToR+"")){

				achouFonteTrafego=true;

				fonteTrafego[0]=edgesArquivo[p][campoEdgeFrom];
				fonteTrafego[1]=edgesArquivo[p][campoEdgeTo];				

			}//fim if
			p++;
		}//fim while

		return fonteTrafego;

	}//getTrafegoToR
*/
	public String connectionAccessSwitch(String [][] edges_arquivo, String node){

		int campoEdgeId=0;
		int campoEdgeFrom=1;
		int campoEdgeTo=2;
		int campoEdgeCost=3;
		int campoEdgeDelay=4;
		int campoEdgeBw=5;
		int campoEdgeQueueLimit=6;
		int campoEdgeAsTo=7;
		int campoEdgeType=8;
		int campoEdgeOther=9;

		String result="";

		int i=0;
		boolean achouNode=false;
		while (i<edges_arquivo.length && !achouNode){

			if(edges_arquivo[i][campoEdgeFrom].equals(node)){
				achouNode=true;
				result=edges_arquivo[i][campoEdgeTo];
			}//fim if			
			i++;			
		}//fim while

		return result;

	}//fim connectionAccessSwitch

	/*public boolean ehSink(String [][] nodes_arquivo, String node){

		boolean result=false;

		int i=0;
		boolean achouNode=false;
		while (i<nodes_arquivo.length && !achouNode){

			if(nodes_arquivo[i][0].equals(node)){
				achouNode=true;
				if(nodes_arquivo[i][1].equals("CORE_SWITCH"))
					result=true;				
			}//fim if			
			i++;			
		}//fim while

		return result;

	}//fim ehSink

	public boolean ehToR(String [][] nodes_arquivo, String node){

		boolean result=false;

		int i=0;
		boolean achouNode=false;
		while (i<nodes_arquivo.length && !achouNode){

			if(nodes_arquivo[i][0].equals(node)){
				achouNode=true;
				if(nodes_arquivo[i][1].equals("TOR"))
					result=true;				
			}//fim if			
			i++;			
		}//fim while

		return result;

	}//fim ehToR

	public boolean ehAccessSwitch(String [][] nodes_arquivo, String node){

		boolean result=false;

		int i=0;
		boolean achouNode=false;
		while (i<nodes_arquivo.length && !achouNode){

			if(nodes_arquivo[i][0].equals(node)){
				achouNode=true;
				if(nodes_arquivo[i][1].equals("A_SWITCH"))
					result=true;				
			}//fim if			
			i++;			
		}//fim while

		return result;

	}//fim ehAccessSwitch

	public boolean ehCoreSwitch(String [][] nodes_arquivo, String node){

		boolean result=false;

		int i=0;
		boolean achouNode=false;
		while (i<nodes_arquivo.length && !achouNode){

			if(nodes_arquivo[i][0].equals(node)){
				achouNode=true;
				if(nodes_arquivo[i][1].equals("CORE_SWITCH"))
					result=true;				
			}//fim if			
			i++;			
		}//fim while

		return result;

	}//fim ehCoreSwitch
*/
	public String removerEspacos(String s) {
		StringTokenizer st = new StringTokenizer(s," ",false);
		String t="";
		while (st.hasMoreElements()) t += st.nextElement();
		return t;
	}

	public void inicializar(String [][] MT){

		int i=0;
		int j=0;
		while (i<MT.length){
			j=0;
			while(j<MT[i].length){				
				MT[i][j]=0+"";				
				j++;
			}//fim while
			i++;
		}//fim while				
	}//fim inicializar



}//fim classe
