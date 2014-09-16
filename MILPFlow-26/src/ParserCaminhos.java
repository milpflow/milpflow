import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserCaminhos {

	public ParserCaminhos(){

		executarParserCaminhos();

	}//fim construtor

	public void executarParserCaminhos(){

		System.out.println("\n---Parser dos Caminhos Resultantes do Modelo Lingo---\n");

		//Adquire as informacoes da topologia que foi salva em arquivo
		ConfigSettings conf = new ConfigSettings();
		
		String linha = new String();
		int i=0;
		int j=0; 
		
		String [][] F = new String[conf.getNumServers()+1][conf.getNumServers()+1];  
		inicializar(F);
		
		//Abre o arquivo com o resultado
		try{
			BufferedReader arquivo = new BufferedReader(new FileReader(conf.getArquivoLingoResult()));
			String REGEX="";			
			Matcher matcher;
			Pattern pattern;
			
			StringTokenizer token;
			StringBuffer fluxoArquivo = new StringBuffer();
			double fluxo=0;

			while((linha=arquivo.readLine())!=null){
				//Duas possibilidades de resposta no Lingo:
				//F ( 123, 234)		1.0000		0.0000
				//F ( 123, 234)		1.0000		
				REGEX = "F\\(\\s(.*),\\s(.*)\\)\\s(.*)";
				pattern = Pattern.compile(REGEX);
				matcher = pattern.matcher(linha);
				if (matcher.find()){

					i = Integer.parseInt(matcher.group(1));
					j = Integer.parseInt(matcher.group(2));

					token = new StringTokenizer(matcher.group(3)," ");
					fluxo = Double.parseDouble(removerEspacos(token.nextToken()));

					F[i][j] = fluxo+"";
					
					//Para gravar a alocacao em arquivo
					if(Double.parseDouble(F[i][j])!=0){
						System.out.println("F["+i+"]["+j+"]: "+F[i][j]);
						fluxoArquivo.append("F["+i+"]["+j+"]: "+F[i][j]+"\n");
					}
					
				}//fim if

			}//fim while

			//exibirFluxos(F);
			
			//Grava em arquivo os caminhos resultantes do modelo Lingo
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(conf.getArquivoLingoCaminhos(),false));			
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
		
	}//fim executarParserCaminhos

	public String removerEspacos(String s){
		StringTokenizer st = new StringTokenizer(s, " ", false);
		String t="";
		while (st.hasMoreElements()) t += st.nextElement();
		return t;		
	}//fim removerEspacos

	public void inicializar(String [][] F){

		int i=0;
		int j=0;
		while (i<F.length){
			j=0;
			while(j<F[i].length){				
				F[i][j]=0+"";				
				j++;
			}//fim while
			i++;
		}//fim while				
	}//fim inicializar

	public void exibirFluxos(String [][] F){

		int i=0;
		int j=0;
		while (i<F.length){
			j=0;
			while(j<F[i].length){
				if(Double.parseDouble(F[i][j])!=0)
					System.out.println("F["+i+"]["+j+"]="+F[i][j]);				
				j++;
			}//fim while
			i++;
		}//fim while				
	}//fim exibir

}//fim classe
