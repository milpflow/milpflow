import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IperfParser {

	/*String [] arquivoInput = {
			"/mnt/compartilhado/artigo_im_2015/results2/ryu/result_h18_h29_tcp_stp.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/ryu/result_h19_h28_tcp_stp.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/ryu/result_h20_h27_tcp_stp.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/ryu/result_h21_h26_tcp_stp.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/ryu/result_h22_h25_tcp_stp.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/ryu/result_h23_h24_tcp_stp.txt",
	};*/
/*	String [] arquivoInput = {
			"/mnt/compartilhado/artigo_im_2015/results2/milpflow/result_h18_h29_tcp_5m.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/milpflow/result_h19_h28_tcp_5m.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/milpflow/result_h20_h27_tcp_5m.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/milpflow/result_h21_h26_tcp_5m.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/milpflow/result_h22_h25_tcp_5m.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/milpflow/result_h23_h24_tcp_5m.txt",
	};
	*/
	/*String [] arquivoInput = {
			"/mnt/compartilhado/artigo_im_2015/results2/ryu/result_h18_h29_udp_stp.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/ryu/result_h19_h28_udp_stp.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/ryu/result_h20_h27_udp_stp.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/ryu/result_h21_h26_udp_stp.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/ryu/result_h22_h25_udp_stp.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/ryu/result_h23_h24_udp_stp.txt",
	};*/
	String [] arquivoInput = {
			"/mnt/compartilhado/artigo_im_2015/results2/milpflow/result_h18_h29_udp_5m.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/milpflow/result_h19_h28_udp_5m.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/milpflow/result_h20_h27_udp_5m.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/milpflow/result_h21_h26_udp_5m.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/milpflow/result_h22_h25_udp_5m.txt",
			"/mnt/compartilhado/artigo_im_2015/results2/milpflow/result_h23_h24_udp_5m.txt",
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
