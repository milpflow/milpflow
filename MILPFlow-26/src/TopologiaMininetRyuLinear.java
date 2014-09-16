import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

public class TopologiaMininetRyuLinear {

	public TopologiaMininetRyuLinear(){

		gerarTopologiaMininet();

	}//fim construtor

	public void gerarTopologiaMininet(){

		System.out.println("\n---Gerar Topologia Mininet---\n");

		//System.exit(0);
		
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
					
			int valor=10;
			i=1;
			j=1;
			//h1 -- s1 -- s2 -- ... -- sn -- h2
			topologiaMininet.append("\n    h"+i+" = net.addHost('h"+i+"')");
			i++;
			topologiaMininet.append("\n    h"+i+" = net.addHost('h"+i+"')");			
			j=1;
			while(j<=valor){
					topologiaMininet.append("\n    s"+j+" = net.addSwitch('s"+j+"')");
				j++;
			}//fim while
			topologiaMininet.append("\n\n");

			//
			i=1;
			j=1;
			topologiaMininet.append("\n    Link(h"+i+", s"+j+")");
			j++;
			while(j<valor){			
					topologiaMininet.append("\n    Link(s"+j+", s"+(j+1)+")");
				j++;
			}//fim while

			i=2;
			topologiaMininet.append("\n    Link(h"+i+", s"+j+")");
			
		    topologiaMininet.append("\n\n    net.build()"+
		    		"\n    c0.start()");
		    		    
			j=1;
			while(j<=valor){
				
					topologiaMininet.append("\n    s"+j+".start([c0])");
				j++;
			}//fim while
		    		    	
		    topologiaMininet.append("\n\n    CLI(net)"+
		    		"\n    net.stop()");			

			//---Fim da Topologia Mininet---

			//Grava em arquivo 
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(conf.getArquivoTopologiaMininet(),false));			
				out.write(topologiaMininet.toString());
				out.close();
			} catch(Exception e){
				System.out.println("13Excecao ao gravar no arquivo." + e.getMessage());
			}//fim catch

			//Cria script para habilitar switches OpenFlow1.3 (o protocolo spanning-tree nao funciona sem esse passo)
			StringBuffer script = new StringBuffer();
			script.append("#!/bin/bash\n");
		    i=0;
			j=1;
			while(j<=valor){

					script.append("\novs-vsctl set Bridge s"+j+" protocols=OpenFlow13");
				j++;
			}//fim while
			
			//Grava em arquivo 
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(conf.getArquivoScriptOF13(),false));			
				out.write(script.toString());
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

	public String [] getFonteTrafegoToR(String [][] edgesArquivo, int indiceSource){

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
					edgesArquivo[p][campoEdgeFrom].equals(indiceSource+"")){

				achouFonteTrafego=true;

				fonteTrafego[0]=edgesArquivo[p][campoEdgeFrom];
				fonteTrafego[1]=edgesArquivo[p][campoEdgeTo];				

			}//fim if
			p++;
		}//fim while

		return fonteTrafego;

	}//getFonteTrafegoToR

	public String removerEspacos(String s) {
		StringTokenizer st = new StringTokenizer(s," ",false);
		String t="";
		while (st.hasMoreElements()) t += st.nextElement();
		return t;
	}

}//fim classe
