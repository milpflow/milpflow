import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IperfParser for Iperf results 
 * 
 * How to run:
 * - 1) Use iperf scripts of MILPFlow project to collect its data
 * - 2) Run this file to parse the results
 * - 3) Open a editor to replace the string "0.0- " with "0.0-" (note this this replace is only to remove the space)
 * - 4) Use Gnumeric to plot its results
 * 
 * 
 * @author Lucio A. Rocha
 *
 */

public class IperfParser {

	String PATH="/home/lucio/novos_resultados_artigo_im_2015/milpflow/";
	String [] arquivoInput = {
			PATH + "nova_result_h18_h29_milpflow_udp.txt",
			PATH + "nova_result_h19_h28_milpflow_udp.txt",
			PATH + "nova_result_h20_h27_milpflow_udp.txt",
			PATH + "nova_result_h21_h26_milpflow_udp.txt",
			PATH + "nova_result_h22_h25_milpflow_udp.txt",
			PATH + "nova_result_h23_h24_milpflow_udp.txt"
	};
	
	String [] arquivoOutput = new String [arquivoInput.length];

	public IperfParser() {

		try {

			int i=0;
			while(i<arquivoInput.length){

				StringBuffer output = new StringBuffer();

				BufferedReader file = new BufferedReader(new FileReader(arquivoInput[i]));
				String linha=file.readLine();
				int j=0;
				while(j<4){
					linha=file.readLine();
					j++;
				}//end while

				String REGEX="";			
				Matcher matcher;
				Pattern pattern;
				while((linha=file.readLine())!=null){				
					REGEX = "(.*)connected(.*)";
					pattern = Pattern.compile(REGEX);
					matcher = pattern.matcher(linha);
					if (!matcher.find()){
						REGEX = "(.*)datagrams(.*)";
						pattern = Pattern.compile(REGEX);
						matcher = pattern.matcher(linha);
						if (!matcher.find()){	
							output.append(linha+"\n");
							System.out.println(linha);
						}//end if
					}//end if
				}//end while
				file.close();

				//Grava em arquivo 
				try {
					BufferedWriter out = new BufferedWriter(new FileWriter(arquivoInput[i]+"2",false));
					out.write(output.toString());
					out.close();
				} catch(Exception e){
					System.out.println("13Excecao ao gravar no arquivo." + e.getMessage());
				}//fim catch


				//Next file
				i++;
			}//end if


		} catch (Exception e){
			System.out.println("Excecao: " + e.getMessage());
		}//end catch

	}//end ExtratorIperf

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new IperfParser();

	}

}
