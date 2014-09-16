import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

public class CustomTopology {

	public CustomTopology(){

		gerarTopologiaDatacenter();

	}//fim construtor

	public void gerarTopologiaDatacenter(){
		
		System.out.println("\n---Gerar Topologia---\n");

		StringBuffer modeloDatacenter = new StringBuffer();

		ConfigSettings conf = new ConfigSettings();
		
		//Escreve apos o campo nodos
		try {
			
			String linha = new String();
			//try to open the file.
			BufferedReader file = new BufferedReader(new FileReader(conf.getArquivoTopologiaOriginal()));

			int i=0;
			for (i=0; i<3; i++) {
				modeloDatacenter.append(file.readLine()+"\n");
			}//fim for

			//get the number of nodes
			linha = file.readLine();
			//Nao guarda a linha com o numero antigo de nodos
			//Armazena os proximos nodos
			StringBuffer aux1 = new StringBuffer();
			//Descricao dos campos
			aux1.append("#NodeId xpos ypos indegree outdegree ASid type\n");
			StringTokenizer t1 = new StringTokenizer(linha, "( )");
			t1.nextToken();
			int numNodes = Integer.parseInt(t1.nextToken());
			//System.out.println("numNodes: " + numNodes);
			Node [] nodes = new Node[numNodes];

			StringBuffer [] accessSwitch = new StringBuffer[numNodes+1]; //+1 because index brite beguns at 1
			inicializar(accessSwitch);
			
			//for the number of nodes
			for (i=0; i<nodes.length; i++){
				//construct each node
				//
				//Aqui recupera a posicao
				linha = file.readLine();
				aux1.append(linha+"\n");
				//A classe Node faz o parser dos parametros de cada linha do node
				nodes[i] = new Node(linha);
				if(nodes[i].getTipo().equals("A_SWITCH"))
					accessSwitch[i+1].append("1"); 	//+1 because index brite beguns at 1
			}//end for

			//
			exibir(accessSwitch);
			
			
			
			//Preciso dos indices dos novos nodos para criar as arestas de conexao
			//Acrescenta os nodos para router e servidores
			//
			//Ex.: nodoDatacenter = [indice conectadoAoNodo]
			//                		[101    100] <-- ToR1 conectado ao A_SWITCH 100
			//                		[102    100] <-- ToR2 conectado ao A_SWITCH 100
			//                		...
			//Numero de ToRs
			int numToRs = conf.getNumServers() / conf.getNumServersToR();
			String [][] nodoDatacenter = new String[numToRs][2];
			
			//No arquivo original da topologia eh necessario informar quais nodos permitem conexao com os ToRs (A_SWITCH)
			//Os novos nodos sao ToRs conectados ahs portas de cada A_SWITCH

			//Busca o indice do 1o. node A_SWITCH (ao completar as conexoes de um grupo no ToR, incrementa o indice)
			int indiceSwitch=0;	
			boolean achouIndice=false;
			i=0;
			while(i<numNodes&&!achouIndice){
				if(nodes[i].getTipo().equals("A_SWITCH")){
					indiceSwitch=i+1; //Pq indices comecam em 1
					achouIndice=true;
				}//end if
				i++;
			}//fim while
			
			i=0;
			int j=0;
			int indiceToR=numNodes+1; //contagem ah partir do ultimo nodo da topologia
			//Linhas de nodoDatacenter
			int m=0;

			String ip_tor="";
			while(i<numToRs){
				j=0;
				while (j<conf.getNumPortasASwitch()&&i<numToRs){

					ip_tor="10.1." + indiceToR + ".0"; 
					
					//Ex.: nodoDatacenter = [indiceToR conectadoAoNodo_A_SWITCH]
					//                		[100    101] <-- ToR1 conectado ao A_SWITCH 100
					//                		[100    102] <-- ToR2 conectado ao A_SWITCH 100
					//                		...
					//
					//Conexao do ToR com o nodo da topologia
					nodoDatacenter[m][0]=indiceSwitch+"";
					//Indice do nodo do ToR 
					nodoDatacenter[m][1]=indiceToR+"";  
					//No arquivo: ToRconectadoAoNodo_A_SWITCH 		indiceToR       porta  ...
					aux1.append(nodoDatacenter[m][1] + "	" + nodoDatacenter[m][1] + "	" + j + "	0	0	"+
							ip_tor +
							"	TOR\n");
					//Proximo indice do ToR
					indiceToR++;
					//Proximo indice da linha de nodoDatacenter
					m++;
					//Proximo ToR
					i++;
					//Proxima porta do A_SWITCH 
					j++;				
				}//fim while
				//Conexoes com o proximo A_SWITCH
				indiceSwitch=getNextASwitch(indiceSwitch,accessSwitch);
			}//fim while

			//Atualiza a informacao sobre o novo numero de nodos
			numNodes=numNodes+numToRs;
			modeloDatacenter.append("Nodes: ( " + numNodes + " )\n");
			modeloDatacenter.append(aux1);			
			
			//two blank lines
			file.readLine( );
			file.readLine( );
			modeloDatacenter.append("\n\n");

			//get the number of edges
			linha = file.readLine();
			//Nao guarda a linha com o numero antigo de nodos
			//Armazena os proximos nodos
			StringBuffer aux2 = new StringBuffer();
			aux2.append("#EdgeId From 	To 	CapLink	Delay	Bandwidth QueueLimit ASto Type Other\n");

			t1 = new StringTokenizer(linha, "( )");
			t1.nextToken();
			int numEdges= Integer.parseInt(t1.nextToken());
			//System.out.println("numEdges: " + numEdges);
			Edge [] edges = new Edge[numEdges];

			//for the number of edges
			for (i=0; i<edges.length; i++){
				//construct each edge
				linha = file.readLine();
				aux2.append(linha+"\n");
				edges[i] = new Edge(linha); //Faz o parser da linha no objeto edges
			}//fim for

			//Datacenter: adiciona as arestas (respeitando os indices)
			//Replica a informacao guardada na estrutura de dados anterior
			i=edges.length+1;	
			j=0;				
			//k=0;
			while (j<nodoDatacenter.length){
				//largura de banda do router (primeiro elemento)
				aux2.append( i + "	" +						
						//Destination
						nodoDatacenter[j][1] + "	" +
						//Source
						nodoDatacenter[j][0] + "	" +
						//Custo do enlace
						"1" + "	" +
						//Delay
						"0" + "	" +
						//Bandwidth (default)
						conf.getLinkBW() + "	" +
						//QueueLimit
						"1000"+
						//Outros campos
				"	0	TOR	U\n");
				//Proximo indice
				i++;					
				//Para linha do nodoDatacenter
				j++;

			}//fim while

			//Update the number of edges
			int novoNumArestas = edges.length+nodoDatacenter.length;
			modeloDatacenter.append("Edges: ( " + novoNumArestas + " )\n");
			modeloDatacenter.append(aux2);
			
			//Acquire the amount of flows
			modeloDatacenter.append("\n\nFlows: sourceTOR destinationTOR aggregatedTraffic path\n");
			//Jump lines
			i=0;
			while(i<2){
				file.readLine();
				i++;
			}//end while
			
			//Read all remain lines
			while((linha=file.readLine())!=null)
				modeloDatacenter.append(linha+"\n");			
			
			//close the file
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}//fim catch

		//System.out.println(modeloDatacenter.toString());

		//Grava a configuracao em arquivo
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(conf.getArquivoTopologiaDatacenter(),false));			
			out.write(modeloDatacenter.toString());
			out.close();
		} catch(Exception e){
			System.out.println("12Excecao ao gravar no arquivo." + e.getMessage());
		}//fim catch

	}//fim gerarTopologiaDatacenter
	
	public int getNextASwitch(int indiceSwitch, StringBuffer [] accessSwitch){
		
		int index=0;
		
		int i=indiceSwitch+1;
		boolean found=false;
		while(i<accessSwitch.length&&!found){
			if(!accessSwitch[i].toString().equals("")){
				found=true;
				index=i;
			}//end if				
			i++;
		}//end while
		
		return index;
	}//end getNextASwitch
	
	public void inicializar(StringBuffer [] A_SWITCH){
		
		int i=0;
		while(i<A_SWITCH.length){
			A_SWITCH[i] = new StringBuffer("");
			i++;
		}//end while
		
	}//end inicializar
	
	public void exibir(StringBuffer [] A_SWITCH){
		
		int i=0;
		while(i<A_SWITCH.length){
			if(A_SWITCH[i].toString().equals("1"))
				System.out.println("A_SWITCH["+i+"]");
			i++;
		}//end while
		
	}//end inicializar

}//fim classe
