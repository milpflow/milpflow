import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grafo {

	//Guarda a vazao no link
	double [][] VAZAO;
	double [][] PERDA;

	public Grafo(){

		//Grafo do trafego SEM NS2
		//gerarModeloNS2Reduced();
		//executarModeloNS2();
		//executarParserPerda();
		//executarParserVazao();
		executarParserCaminhos();

		//Grafo do trafego COM NS2
		//executarParserTrafego();
		//gerarPDFTrafego();

	}//fim construtor

	public void executarParserCaminhos(){

		System.out.println("\n---Grafo dos Caminhos Resultantes do Modelo Lingo---\n");

		//Adquire as informacoes da topologia que foi salva em arquivo
		ConfigSettings conf = new ConfigSettings();		

		try
		{
			int i=0;
			int j=0;
			int k=0;
			int p=0;
			int q=0;

			int colY=0;
			int linX=0;

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
			token = new StringTokenizer(linha, "( )"); //Se erro aqui, verifique o numero de edges do arquivo brite
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
					StringBuffer fluxoArquivo = new StringBuffer();

					REGEX="";				

					fluxoArquivo.append("digraph G {\n");
					fluxoArquivo.append("fixedsize=true\n");

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

					}//fim while
					
					//Remove loops
					i=0;
					j=0;
					k=0;
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
					

					//Gera o grafo com base na sequencia de nodos informados pela topologia
					i=0;
					j=0;
					int from=0;
					int to=0;
					String stat="";
					double vazaoCombinada=0;
					double perdaCombinada=0;

					boolean verificouUplink = false;					

					while(i<edges_arquivo.length){

						from = Integer.parseInt(edges_arquivo[i][campoEdgeFrom]);
						to = Integer.parseInt(edges_arquivo[i][campoEdgeTo]);
						//if(!edges_arquivo[from][to].equals("0")){
						fluxoArquivo.append("\n"+(from) + " -> " + (to) + " [arrowhead=\"none\", ");
						//Nodes percorridos
						
						//Uplink
						verificouUplink=false;
						if (Double.parseDouble(F[f][from][to])!=0){
							fluxoArquivo.append("style=bold,label=\""+
									"U"+(f+1)+":"+String.format("%1$.2f",Double.parseDouble(F[f][from][to])));
							//Downlink
							if (Double.parseDouble(F[f][to][from])!=0)							
								fluxoArquivo.append(
										",D"+(f+1)+":"+String.format("%1$.2f",Double.parseDouble(F[f][to][from])));
							//Sempre fecha o label
							fluxoArquivo.append("\"];");
							verificouUplink=true;
						}//enf if 
						
						//Downlink
						if (Double.parseDouble(F[f][to][from])!=0&&verificouUplink==false){							
							fluxoArquivo.append("style=bold,label=\""+
									"D"+(f+1)+":"+String.format("%1$.2f",Double.parseDouble(F[f][to][from])));
							if (Double.parseDouble(F[f][from][to])!=0)
								fluxoArquivo.append("style=bold,label=\""+
										",U"+(f+1)+":"+String.format("%1$.2f",Double.parseDouble(F[f][from][to])));
							//Sempre fecha o label
							fluxoArquivo.append("\"];");
						}//fim if

						if (Double.parseDouble(F[f][from][to])!=0||Double.parseDouble(F[f][to][from])!=0){
							fluxoArquivo.append("\n"+(from)+" [shape=circle,width=0.5,style=filled,color=\".7 .3 1.0\"];"); 
							fluxoArquivo.append("\n"+(to)+" [shape=circle,width=0.5,style=filled,color=\".7 .3 1.0\"];");
						} else {
							//Nodes nao percorridos
							fluxoArquivo.append("style=dotted];");
						}//fim else
						
						i++;
					}//fim while

					//Legenda
					fluxoArquivo.append("\n{"+
							"\nrank=max;"+
							"\nforcelabels=true;"+
							"\nLegend [shape=none, margin=0, label=<"+
							"\n<table border=\"1\" cellborder=\"0\" cellspacing=\"0\" cellpadding=\"4\">"+
							"\n<tr>"+
							"\n<td>U,D:</td><td align=\"left\">Uplink,Downlink</td>"+
							"\n</tr>"+						
							"\n<tr>"+
							"\n<td>Active</td>"+
							"\n<td cellpadding=\"4\">"+
							"\n<table border=\"1\" cellborder=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
							"\n<tr>"+
							"\n<td bgcolor=\".7 .3 1.0\"></td>"+
							"\n</tr>"+
							"\n</table>"+
							"\n</td>"+
							"\n</tr>"+
							"\n<tr>"+
							"\n<td>Inactive</td>"+
							"\n<td cellpadding=\"4\">"+
							"\n<table border=\"1\" cellborder=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
							"\n<tr>"+
							"\n<td bgcolor=\"white\"></td>"+
							"\n</tr>"+
							"\n</table>"+
							"\n</td>"+
							"\n</tr>"+
							"\n</table>"+
							"\n>];"+
					"\n}");

					fluxoArquivo.append("\n}");

					//exibirFluxos(F);

					//Grava em arquivo os caminhos resultantes do modelo Lingo
					try {
						BufferedWriter out = new BufferedWriter(new FileWriter(conf.getArquivoGraphViz()+"_"+(f+1),false)); //+1 to generate files with index beginning at 1, i.e., F1 -> ..._1.pdf			
						out.write(fluxoArquivo.toString());
						out.close();
					} catch(Exception e){
						System.out.println("13Excecao ao gravar no arquivo." + e.getMessage());
					}//fim catch					

					//close the file			
					arquivo.close();			
				} catch(Exception e){
					System.out.println("Excecao 17 ao abrir o arquivo: " + conf.getArquivoLingoResult() + "\n" + e.getMessage());
					e.printStackTrace();
				}//fim catch

				System.out.println("--Gerar PDF dos caminhos--");
				String comando = conf.getPath() + "/geradorGrafo.sh " + conf.getArquivoGraphViz()+"_"+(f+1);

				try {
					String line;
					Process p1 = Runtime.getRuntime().exec(comando);
					BufferedReader bri = new BufferedReader
					(new InputStreamReader(p1.getInputStream()));
					BufferedReader bre = new BufferedReader
					(new InputStreamReader(p1.getErrorStream()));
					while ((line = bri.readLine()) != null) {
						System.out.println(line);
					}
					bri.close();
					while ((line = bre.readLine()) != null) {
						System.out.println(line);
					}
					bre.close();
					p1.waitFor();
					System.out.println("Fim da geracao do arquivo PDF do grafo.\n\n");
				} catch (Exception e){
					System.out.println("Excecao: Erro ao executar o programa GraphViz.");
				}//fim catch

				//Next flow
				f++;
			}//end while

			//close the file			
			file.close();
		} catch(Exception e){
			System.out.println("Excecao 16 ao abrir o arquivo: " + conf.getArquivoTopologiaDatacenter() + "\n" + e.getMessage());
			e.printStackTrace();
		}//fim catch

	}//fim executarParserCaminhos





	public int adquirirServidorVM(int k, String [][] A){

		int indiceServidor=0;

		boolean achou=false;

		int j=0;
		while(j<A[k].length && !achou){

			//A VM k estah alocada no servidor j?
			if (A[k][j].equals("1")){
				indiceServidor=k;
				achou=true;
			}//fim if

			j++;
		}//fim while

		return indiceServidor;

	}//fim adquirirServidorVM

	public int getIndiceToR(int j, String [][]A, int numNodesOriginal){

		int indiceToR=0;

		ConfigSettings conf = new ConfigSettings();

		int i=1;
		int k=0;
		while(i<A.length){

			indiceToR = 0;						
			//Todo indiceToR eh definido dentro do while abaixo
			//
			//VM k
			k=0;
			while(k<j){ //Server j
				//Anda o intervalo de servidores do ToR
				k+=conf.getNumServersToR(); //Ex.: k=0+40 --> k=40 (salto de 40 servers)
				indiceToR++; //proximo ToR
			}//fim while
			i++;
		}//fim while					

		indiceToR+=numNodesOriginal;

		return indiceToR;

	}//fim getIndiceToR

	public String removerEspacos(String s){
		StringTokenizer st = new StringTokenizer(s, " ", false);
		String t="";
		while (st.hasMoreElements()) t += st.nextElement();
		return t;		
	}//fim removerEspacos

	public void inicializar(String [][][] A){

		int i=0;
		int j=0;		
		int k=0;
		while (i<A.length){

			j=0;
			while(j<A[i].length){

				k=0;
				while(k<A[i][j].length){				
					A[i][j][k]=0+"";				
					k++;
				}//fim while
				j++;
			}
			i++;
		}

	}//fim inicializar

	public void exibirAllocs(String [][] A){

		int i=0;
		int j=0;
		while (i<A.length){
			j=0;
			while(j<A[i].length){
				if(A[i][j].equals("1"))
					System.out.println("A["+i+"]["+j+"]="+A[i][j]);				
				j++;
			}//fim while
			i++;
		}//fim while				
	}//fim exibir

	public void executarModeloNS2(){

		System.out.println("\n---Reduced: Executar modelo no NS2---");

		ConfigSettings conf = new ConfigSettings();

		String comando = "ns " + conf.getArquivoModeloNS2Reduced();

		try {
			String line;
			Process p = Runtime.getRuntime().exec(comando);
			BufferedReader bri = new BufferedReader
			(new InputStreamReader(p.getInputStream()));
			BufferedReader bre = new BufferedReader
			(new InputStreamReader(p.getErrorStream()));
			while ((line = bri.readLine()) != null) {
				//System.out.println(line);
			}
			bri.close();
			while ((line = bre.readLine()) != null) {
				//System.out.println(line);
			}
			bre.close();
			p.waitFor();
			System.out.println("\n---Reduced: Fim da execucao do modelo no NS2---");
		} catch (Exception e){
			System.out.println("Excecao: Erro ao executar o modelo NS2.");
		}//fim catch

	}//fim executarModeloNS2

	public void executarParserPerda(){

		System.out.println("\n---Reduced: Parser Perda nos Links---\n");

		//Adquire as informacoes da topologia que foi salva em arquivo
		ConfigSettings conf = new ConfigSettings();

		double porcentagemPerdaTotal=0;
		double somatorioPacotesEnviados=0;
		double somatorioPacotesDescartados=0;

		//Para cada aresta, calcula a perda de pacotes	
		//
		//Adquire as arestas do arquivo
		try
		{
			//Guarda as informacoes do arquivo
			StringBuffer info = new StringBuffer();
			//Linhas do arquivo
			String linha = new String();
			//try to open the file.
			BufferedReader file = new BufferedReader(new FileReader(conf.getArquivoTopologiaDatacenter()));
			//Cabecalho
			linha = file.readLine();
			StringTokenizer token = new StringTokenizer(linha, "( )");
			token.nextToken();
			int numNodosOriginal = Integer.parseInt(token.nextToken());
			//System.out.println("NumNodosOriginal: "+ numNodosOriginal);
			token.nextToken();
			int numEdgesOriginal = Integer.parseInt(token.nextToken());
			//System.out.println("NumEdgesOriginal: " + numEdgesOriginal);
			info.append(linha+"\n");

			for (int i=0; i<2; i++) {
				info.append(file.readLine()+"\n");
			}//fim for

			//get the number of nodes
			linha = file.readLine();
			info.append(linha+"\n");
			token = new StringTokenizer(linha, "( )");
			token.nextToken();
			int numNodes = Integer.parseInt(token.nextToken());
			//Guarda todas as informacoes dos nodos
			//System.out.println("numNodes: " + numNodes);
			//Descricao dos campos
			linha = file.readLine();			
			info.append(linha+"\n");
			for (int i=0; i<numNodes; i++){				
				info.append(file.readLine()+"\n");
			}//fim while

			//Adquire o numero de Edges
			for (int i=0; i<2; i++) {
				info.append(file.readLine()+"\n");
			}//fim for			
			linha = file.readLine();
			info.append(linha+"\n");
			token = new StringTokenizer(linha, "( )");
			token.nextToken();
			int numEdges = Integer.parseInt(token.nextToken());
			//Guarda todas as informacoes dos Edges
			//System.out.println("numEdges: " + numEdges);

			//Le o conteudo do arquivo resultante da simulacao no NS2
			//
			//Pacotes enviados apenas no link
			//numNodes+numEdges por causa dos indices (tenho indices para os roteadores de borda tb)
			//numNodes+numEdges eh mais do que suficiente (um novo link pode ter um router jah usado anteriormente) 
			int [][] packetsSendLink = new int[numNodes+numEdges][numNodes+numEdges];
			int i=0;
			int j=0;
			while(i<packetsSendLink.length){
				j=0;
				while(j<packetsSendLink[i].length){
					packetsSendLink[i][j]=0;
					j++;
				}//fim while
				i++;
			}//fim while

			//Pacotes enviados pela origem
			//int [] packetsSend = new int[numNodes+numEdges];

			//Pacotes descartados apenas no link
			i=0;
			j=0;			
			PERDA = new double[numNodes+numEdges][numNodes+numEdges];
			int p=0;
			int q=0;
			while(p<PERDA.length){
				q=0;
				while(q<PERDA[p].length){
					PERDA[p][q]=0;
					q++;
				}//fim while				
				p++;
			}//fim while

			//Pacotes descartados apenas no link
			i=0;
			j=0;
			int [][] packetsDropLink = new int[numNodes+numEdges][numNodes+numEdges];			
			while(i<packetsDropLink.length){
				j=0;
				while(j<packetsDropLink[i].length){
					packetsDropLink[i][j]=0;
					j++;
				}//fim while
				i++;
			}//fim while

			String [] t = new String[8];
			StringBuffer parserPerda = new StringBuffer("From-To PacketsSend PacketsDrop PercentOfDrop");
			//System.out.println("\nFrom-To PacketsSend PacketsDrop");

			try {
				//try to open the file.
				BufferedReader fileResult = new BufferedReader(new FileReader(conf.getArquivoNS2TrackingReduced()));				
				String linhaResult = fileResult.readLine();				
				while (linhaResult != null) {

					//Ignora as linhas que nao respeitam o padrao (linhas com link-down)
					try {
						//Le todas as linhas do arquivo					
						token = new StringTokenizer(linhaResult, " ");

						t[0] = token.nextToken();
						t[1] = token.nextToken();
						t[2] = token.nextToken();
						t[3] = token.nextToken();
						t[4] = token.nextToken();
						t[5] = token.nextToken();
						t[6] = token.nextToken();
						t[7] = token.nextToken();

						if (t[0].equals("+"))

							//Pacotes enviados apenas no link
							packetsSendLink[Integer.parseInt(t[2].toString())][Integer.parseInt(t[3].toString())]++;

						if (t[0].equals("d")){

							//Pacotes descartados apenas no link
							packetsDropLink[Integer.parseInt(t[2].toString())][Integer.parseInt(t[3].toString())]++;

						}//fim if

					} catch(Exception e){
						//Quando os links sao desativados, o arquivo de log do NS2 (*.tr) fica com 
						//um formato diferente. Disparo esse warning para exibir essa informacao
						//
						//System.out.println("Aviso 1: Encontrada linha fora do padrao de envio/recepcao de pacotes");						
					}//fim catch
					linhaResult = fileResult.readLine();															

				}//fim while				

				//Resultados dos links				
				i=0;
				j=0;
				double porcentagemPerdaLink=0;
				while(i<packetsSendLink.length){
					j=0;
					while(j<packetsSendLink[i].length){
						if (packetsSendLink[i][j]!=0){
							//System.out.println(i + "--" + j + " " + packetsSendLink[i][j] + " " + packetsDropLink[i][j]);
							PERDA[i][j] = ( packetsDropLink[i][j] * 100 ) / packetsSendLink[i][j];
							parserPerda.append("\n" + i + "--" + j + " " + packetsSendLink[i][j] + " " + packetsDropLink[i][j] + " " + PERDA[i][j]);							

							somatorioPacotesEnviados += packetsSendLink[i][j];
							somatorioPacotesDescartados += packetsDropLink[i][j];

							//Ao desativar links pacotes sao perdidos, mas
							//quero saber se ocorre perda nos links que permaneceram ativos
							//somatorioPorcentagemPerda+=porcentagemPerdaLink;

						}//fim if
						j++;
					}//fim while 
					i++;
				}//fim while

				porcentagemPerdaTotal = ( somatorioPacotesDescartados * 100 ) / somatorioPacotesEnviados;

				//Grava o resultado da perda do link em arquivo				
				try {
					BufferedWriter out = new BufferedWriter(new FileWriter(conf.getArquivoNS2PerdaReduced(),false));			
					out.write(parserPerda.toString());
					out.close();
				} catch(Exception e){
					System.out.println("11Excecao ao gravar no arquivo." + e.getMessage());
				}//fim catch

				//close the file			
				fileResult.close();	
			} catch (Exception e){
				System.out.println("Excecao 12: Erro ao ler o arquivo: " + conf.getArquivoNS2TrackingReduced() + " Excecao: " + e.getMessage());
			}//fim catch

			//close the file			
			file.close();			
		} catch(Exception e){
			System.out.println("Excecao 13 ao abrir o arquivo: " + conf.getArquivoTopologiaDatacenter() + "\n" + e.getMessage());
		}//fim catch

	}//fim executarParserPerda

	public void executarParserVazao(){

		System.out.println("\n---Reduced: Parser Vazao nos Links---\n");

		//Para cada aresta, calcula a vazao
		//
		//String [][] EDGES=null;		

		//double vazaoTotalVMs=0;

		ConfigSettings conf = new ConfigSettings();

		//
		//Adquire as arestas do arquivo
		try
		{
			//Guarda as informacoes do arquivo
			StringBuffer info = new StringBuffer();
			//Linhas do arquivo
			String linha = new String();
			//try to open the file.
			BufferedReader file = new BufferedReader(new FileReader(conf.getArquivoTopologiaDatacenter()));
			//Cabecalho
			linha = file.readLine();
			StringTokenizer token = new StringTokenizer(linha, "( )");
			token.nextToken();
			int numNodosOriginal = Integer.parseInt(token.nextToken());
			//System.out.println("NumNodosOriginal: "+ numNodosOriginal);
			token.nextToken();
			int numEdgesOriginal = Integer.parseInt(token.nextToken());
			//System.out.println("NumEdgesOriginal: " + numEdgesOriginal);
			info.append(linha+"\n");

			for (int i=0; i<2; i++) {
				info.append(file.readLine()+"\n");
			}//fim for

			//get the number of nodes
			linha = file.readLine();
			info.append(linha+"\n");
			token = new StringTokenizer(linha, "( )");
			token.nextToken();
			int numNodes = Integer.parseInt(token.nextToken());
			//Guarda todas as informacoes dos nodos
			//System.out.println("numNodes: " + numNodes);
			//Descricao dos campos
			linha = file.readLine();			
			info.append(linha+"\n");
			for (int i=0; i<numNodes; i++){				
				info.append(file.readLine()+"\n");
			}//fim while

			//Adquire o numero de Edges
			for (int i=0; i<2; i++) {
				info.append(file.readLine()+"\n");
			}//fim for			
			linha = file.readLine();
			info.append(linha+"\n");
			token = new StringTokenizer(linha, "( )");
			token.nextToken();
			int numEdges = Integer.parseInt(token.nextToken());
			//Guarda todas as informacoes dos Edges
			//System.out.println("numEdges: " + numEdges);

			//Le o conteudo do arquivo resultante da simulacao no NS2
			//Para cada aresta
			int i=0;
			String [][] fromNode = new String[numNodes+numEdges][numNodes+numEdges];
			String [][] toNode = new String[numNodes+numEdges][numNodes+numEdges];
			int [][] totalBits = new int[numNodes+numEdges][numNodes+numEdges];

			String [] t = new String[7];
			double [][] timeBegin = new double[numNodes+numEdges][numNodes+numEdges];
			double [][] timeEnd = new double[numNodes+numEdges][numNodes+numEdges]; 
			double [][] duration = new double[numNodes+numEdges][numNodes+numEdges];
			VAZAO = new double[numNodes+numEdges][numNodes+numEdges];

			//Inicializa a vazao
			int p=0;
			int q=0;
			while(p<VAZAO.length){
				q=0;
				while(q<VAZAO[p].length){
					VAZAO[p][q]=0;
					q++;
				}//fim while
				p++;
			}//fim while

			StringBuffer parserVazao = new StringBuffer("From-To TotalBitsTransmitted Duration Throughput(Kbps)");
			//System.out.println("From-To TotalBitsTransmitted Duration Throughput");

			try {
				//try to open the file.
				BufferedReader fileResult = new BufferedReader(new FileReader(conf.getArquivoNS2TrackingReduced()));
				String linhaResult = fileResult.readLine();				
				while (linhaResult != null) {

					//Le todas as linhas do arquivo					
					token = new StringTokenizer(linhaResult, " ");
					t[0] = token.nextToken();
					t[1] = token.nextToken();
					t[2] = token.nextToken();					

					if(!t[2].equals("link-down")){//jump log about link-down

						t[3] = token.nextToken();
						t[4] = token.nextToken();
						t[5] = token.nextToken();

						//System.out.println(linhaResult);

						if (t[0].equals("r")){
							totalBits[Integer.parseInt(t[2].toString())][Integer.parseInt(t[3].toString())] += 8*Integer.parseInt(t[5]);
							//Se jah contabilizou algum bit anteriormente
							if (totalBits[Integer.parseInt(t[2])][Integer.parseInt(t[3])]!=0)
								timeBegin[Integer.parseInt(t[2])][Integer.parseInt(t[3])] = Double.parseDouble(t[1]);
							else
								timeEnd[Integer.parseInt(t[2])][Integer.parseInt(t[3])] = Double.parseDouble(t[1]);

						}//fim if

					}//fim if

					linhaResult = fileResult.readLine();

				}//fim while

				//Resultados
				i=0;
				int j=0;
				while (i<numNodes+numEdges){
					j=0;
					while (j<numNodes+numEdges){
						if (totalBits[i][j]!=0){
							duration[i][j] = Math.abs(timeEnd[i][j] - timeBegin[i][j]);
							VAZAO[i][j] = totalBits[i][j]/duration[i][j]/1e3; 
							//System.out.println(i + "--" + j + " " + totalBits[i][j] + " " + String.format("%1$,.2f",duration[i][j]) + " " + String.format("%1$,.2f",throughput[i][j]));
							parserVazao.append("\n" + i + "--" + j + " " + totalBits[i][j] + " " + String.format("%1$.2f",duration[i][j]) + " " + String.format("%1$.2f",VAZAO[i][j]));
						}//fim if
						j++;
					}//fim while					
					i++;
				}//fim while

				/*System.out.println("\n\nFrom: " + fromNode + " To: " + toNode);
				System.out.println("Total bits transmitted: " + totalBits);
				System.out.println("Duration: " + String.format("%1$,.2f",duration));
				System.out.println("Throughput: " + String.format("%1$,.2f",throughput) + "kbps");
				 */				

				//close the file			
				fileResult.close();	
			} catch (Exception e){
				System.out.println("Excecao 8: Erro ao ler o arquivo: " + conf.getArquivoNS2TrackingReduced() + " " + e.getMessage());
				e.printStackTrace();
			}//fim catch

			//Grava o resultado em arquivo
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(conf.getArquivoNS2VazaoReduced(),false));			
				out.write(parserVazao.toString());
				out.close();
			} catch(Exception e){
				System.out.println("10Excecao ao gravar no arquivo." + e.getMessage());
			}//fim catch

			//close the file			
			file.close();			
		} catch(Exception e){
			System.out.println("Excecao 9 ao abrir o arquivo: " + conf.getArquivoTopologiaDatacenter() + "\n" + e.getMessage());
		}//fim catch

	}//fim executarParserVazao

	//Apenas verificar o fluxo em 1 sentido eh suficiente para saber se o link deve estar ativo ou nao
	public boolean linkAtivo(String [][] F, int from, int to){

		boolean achou = false;

		//System.out.println("Passei por aquiS");

		int i=0;
		while(i<F.length&&!achou){
			if (Double.parseDouble(F[from][to])!=0){
				achou=true;
				//System.out.println("F["+i+"]["+indice+"]= "+F[i][indice]);
			}
			i++;
		}//fim while

		return achou;

	}//fim linkAtivo

}//fim classe
