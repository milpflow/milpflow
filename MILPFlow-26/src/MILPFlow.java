
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.LimitExceededException;
import javax.swing.*;
import java.awt.Event.*;

/** 
 * 
 * MILPFlow (see version in Configuracao.java)
 * 
 * - Modelling of SDN networks with OpenFlow
 * - Operating Research
 * - Includes support to Network Simulation
 * - OpenFlow network topology simulator
 * 
 * Note: servers are set to leaf nodes of topology
 * 
 * @author Lucio Agostinho Rocha
 * Last update: 06/09/2014
 *
 */

public class MILPFlow { 		

	public MILPFlow(){  

		long tempoInicio=0, tempoFim=0;		
		tempoInicio = System.currentTimeMillis();		

		//Create a new logical topology (switchs, servers and network links) --> and save in file .datacenter
		new CustomTopology();
		
		//From the saved topology in file, generates a Lingo model --> and save in file .lg4
		new ModeloLingo();
		//System.exit(0);

		//Executar modelo --> e salva o resultado em arquivo
		executarModeloLingo();
		boolean factivel = realizarParserResultLingo();		

		if (factivel){
		
			//Gerar grafo dos caminhos para o GraphViz		
			//new Grafo();
			
			//Generates a topology model for Mininet 
			//AND
			//Generates OpenFlow rules according to Mininet topology
			new CreateAutoMininetTopologyDpctl();
			
			new CreateAutoMininetTopologyRyu();
			//new CreateManualMininetTopology();
					
		}		
		else 
			System.out.println("\n\n---Modelo infactivel.");
		
		//new ModeloNS2Reduced();	//Cria o trafego com base nas rotas do MILP		

		tempoFim = System.currentTimeMillis();		
		System.out.println("\nTempo de execucao (ms): " + (tempoFim-tempoInicio));
		System.out.println("Tempo de execucao (s): " + (tempoFim-tempoInicio)/1000);		

		System.out.println("\n---\nFim da execucao.\n---");

		System.exit(0);

	}//fim construtor

	public void executarModeloLingo(){

		System.out.println("--Executar modelo no Lingo--");

		ConfigSettings conf = new ConfigSettings();

		//Eh necessario mudar a data apenas uma vez, na execucao sequencial 
		//Muda a data para executar o Lingo				
		try{
			String comando = conf.getPath() + "/mudarDataParaLingo.sh";
			Process p = Runtime.getRuntime().exec(comando);
			p.waitFor();			
		} catch (Exception e){
			System.out.println("Excecao ao mudar a data do servidor para o Lingo.");
		}//fim catch		

		String comando = conf.getPath() + "/executarModeloLingo.sh " + conf.getArquivoLingoModelo();

		try {
			String line;
			Process p = Runtime.getRuntime().exec(comando);
			BufferedReader bri = new BufferedReader
			(new InputStreamReader(p.getInputStream()));
			BufferedReader bre = new BufferedReader
			(new InputStreamReader(p.getErrorStream()));
			while ((line = bri.readLine()) != null) {
				System.out.println(line);
			}
			bri.close();
			while ((line = bre.readLine()) != null) {
				System.out.println(line);
			}
			bre.close();
			p.waitFor();
			System.out.println("Fim da execucao do modelo Lingo.\n\n");
		} catch (Exception e){
			System.out.println("Excecao: Erro ao executar o programa Lingo.");
		}//fim catch

	}//fim executarModeloLingo

	public boolean realizarParserResultLingo(){

		System.out.println("\n---RealizarParserResultLingo---");

		boolean factivel=false;

		String REGEX = "";
		Matcher matcher;
		Pattern pattern;

		ConfigSettings conf = new ConfigSettings();

		//Primeiro verifica se a solucao eh factivel
		try {
			//Abre o arquivo com o resultado Lingo			
			BufferedReader arquivoResult = new BufferedReader(new FileReader(conf.getArquivoLingoResult()));
			String linhaResult=arquivoResult.readLine();
			StringTokenizer t1;
			StringTokenizer t2;
			while (linhaResult!=null && !factivel){

				REGEX = "Status:(.*)";
				pattern = Pattern.compile(REGEX);
				matcher = pattern.matcher(linhaResult);
				if (matcher.find()){
					if(matcher.group(1).equals("0"))
						factivel=true;
				}//fim if
				//Proxima linha do arquivo
				linhaResult = arquivoResult.readLine();

			}//fim while

			//close the file
			arquivoResult.close();			
		} catch(Exception e){
			System.out.println("Excecao 16 ao abrir o arquivo: " + conf.getArquivoLingoResult() + "\n" + e.getMessage());
		}//fim catch

		//System.out.println("Factivel: " + factivel);
		//System.exit(0);

		return factivel;

	}

	//Exibe os valores sem alteracao
	public void exibir(String [][] A){

		System.out.println("---");

		int i=0;
		int j=0;
		while (i<A.length){

			while (j<A[i].length){

				System.out.print(A[i][j] + " ");
				j++;

			}//fim while

			//Salta uma linha
			System.out.println();

			i++;
			j=0;
		}//fim while		

	}//fim exibir	

	public static void main(String[] args) {

		new MILPFlow();

	}//fim main

}//fim classe
