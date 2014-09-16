
public class ConfigSettings {

	private String VERSION="26";
	
	//numero de servidores
	private int NUM_SERVERS=1000;
	//
	private int NUM_VMS=500;
	
	private int LINK_BW=10000;

	//Para o Omnet
	//private String DATA_RATE="1000Mbps";	
	//private String DATA_RATE_REDUCED="1000Mbps";
	
	//private int BUFFER_NS2=1000;	
	
	private String APP_TYPE="UDP/CBR";

	//Numero de servidores dentro do Top of Rack (ToR)
	private int NUM_SERVERS_TOR=83;  //Ex.: 83*12=996=NUM_SERVERS
	//private int NUM_SERVERS_TOR=10;
	//Numero de portas do A_SWITCH (Switch de Acesso) para conectar ToRs -> para gerar a topologia
	//private int NUM_PORTAS_A_SWITCH=24;
	//Importante: caso nao haja mais switches, eh iniciado um cascateamento, com
	//ligacao do ToR no proximo ToR (nodo folha)
	private int NUM_PORTAS_A_SWITCH=2;
		
	private int NUM_SERVERS_SMALL_TOR=(int)(NUM_SERVERS_TOR*0.6);
	private int NUM_SERVERS_LARGE_TOR=(int)(NUM_SERVERS_TOR*0.3);
	private int NUM_SERVERS_HUGE_TOR=(int)(NUM_SERVERS_TOR*0.1);
	
	
	//Capacidades do servidor (similar ah Amazon EC2)
	//Unidades
	private int SERVER_SMALL_CPU=4;
	private int SERVER_LARGE_CPU=8;
	private int SERVER_HUGE_CPU=32;	
	//GB
	private int SERVER_SMALL_BW=1000;
	private int SERVER_LARGE_BW=1000;
	private int SERVER_HUGE_BW=1000;
	//GB
	//private int SERVER_SMALL_RAM;
	//private int SERVER_LARGE_RAM;
	//private int SERVER_HUGE_RAM;
	//GB
	//private int SERVER_SMALL_DISK;
	//private int SERVER_LARGE_DISK;
	//private int SERVER_HUGE_DISK;	
	//
	//Custos de CPU
	private int SERVER_COST_SMALL_CPU=1;
	private int SERVER_COST_LARGE_CPU=3;
	private int SERVER_COST_HUGE_CPU=9;
	//Custos de RAM
	//private int SERVER_COST_SMALL_RAM;
	//private int SERVER_COST_LARGE_RAM;
	//private int SERVER_COST_HUGE_RAM;
	//Custos de Disk
	//private int SERVER_COST_SMALL_DISK;
	//private int SERVER_COST_LARGE_DISK;
	//private int SERVER_COST_HUGE_DISK;
	//Custos de Link
	//private int SERVER_COST_SMALL_BW;
	//private int SERVER_COST_LARGE_BW;
	//private int SERVER_COST_HUGE_BW;	

	//
	//VMs
	//Demandas das VMs
	private int VM_SMALL_CPU=4;
	//private int VM_LARGE_CPU;
	//private int VM_HUGE_CPU;
	//GB
	//private int VM_SMALL_RAM;
	//private int VM_LARGE_RAM;
	//private int VM_HUGE_RAM;
	//GB
	//private int VM_SMALL_DISK;
	//private int VM_LARGE_DISK;
	//private int VM_HUGE_DISK;
	//GB	
	private int VM_SMALL_BW=2;
	//private int VM_LARGE_BW=2;
	//private int VM_HUGE_BW=2;
	//GB
	//private double VM_SMALL_FLUXO;
	//private int VM_LARGE_FLUXO;
	//private int VM_HUGE_FLUXO;	
	
	//Custos da matriz de trafego
	//
	//Ate o MT_SERVER_LIMITE o valor do custo eh 1, apos o valor do custo eh MT_VALOR_LIMITE
	private int MT_SERVER_LIMITE=NUM_SERVERS;
	private int MT_VALOR_LIMITE=1;
	
	//private int MT_SERVER_LIMITE=500;
	//private int MT_VALOR_LIMITE=100;	
	
	//
	//
	//Caminho principal
	private String PATH="/home/lucio/";
	private String arquivo = PATH + "modeloLingo_"+
		NUM_SERVERS+"serv_"+
		NUM_VMS+"vm";
	private String ARQUIVO_LINGO_MODELO = arquivo + ".lg4";
	private String ARQUIVO_LINGO_RESULT = arquivo + ".lgr";
	private String ARQUIVO_LINGO_PARSER = arquivo + ".parser";
	//Arquivo da alocacao nos ToRs
	private String ARQUIVO_LINGO_CAMINHOS = arquivo + ".caminhos";
	
	String topologia = PATH + "modeloLingo_"+
		NUM_SERVERS+"serv_"+
		NUM_VMS+"vm";
	//Arquivo da topologia
	private String ARQUIVO_TOPOLOGIA_ORIGINAL = topologia + ".brite";
	//Arquivo da topologia em memoria
	private String ARQUIVO_TOPOLOGIA_DATACENTER = topologia + ".datacenter";
	//Arquivo da alocacao nos ToRs
	private String ARQUIVO_ALOCACAO_DATACENTER = topologia + ".alocacao";
	
	//Mininet
	private String NOME_TOPOLOGIA_MININET = "modeloMininet_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm";
	private String ARQUIVO_TOPOLOGIA_MININET = PATH + NOME_TOPOLOGIA_MININET + ".py";
	private String ARQUIVO_TOPOLOGIA_MININET_RYU = PATH + NOME_TOPOLOGIA_MININET + "_ryu.py";
	private String ARQUIVO_SCRIPT_OF13 = PATH + NOME_TOPOLOGIA_MININET + ".sh";
	
	//NS3
	private String PATH_NS3 = "ns-allinone-3.19/ns-3.19/";
	private String ARQUIVO_NS3_MODELO = PATH + PATH_NS3 + "scratch/" + "modeloNS3_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".cc";
	private String ARQUIVO_NS3_RESULT = PATH + "modeloNS3_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".resultNS3";	
	
	//OpenFlow/NS3
	private String ARQUIVO_OPENFLOW_MODELO = PATH + PATH_NS3 + "scratch/" + "modeloOpenFlow_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".cc";
	private String ARQUIVO_OPENFLOW_RESULT = PATH + "modeloOpenFlow_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".resultOpenFlow";
	private String ARQUIVO_OPENFLOW_REDUCED = PATH + PATH_NS3 + "scratch/" + "modeloOpenFlow_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" +
			"_reduced"+
			".cc";
	private String ARQUIVO_OPENFLOW_RESULT_REDUCED = PATH + "modeloOpenFlow_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			"_reduced"+
			".resultOpenFlow";
	
	//Omnet
	private String PATH_OMNET = "/home/lucio/omnetpp-4.2.2/";
	
	//OpenFlow/Omnet		
	private String ARQUIVO_OMNET_NED = PATH_OMNET + "samples/openflow/scenarios/" + "modeloOpenFlow_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".ned";
	private String ARQUIVO_OMNET_INI = PATH_OMNET + "samples/openflow/scenarios/" + "modeloOpenFlow_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".ini";
	private String ARQUIVO_OMNET_CONFIG_XML = PATH_OMNET + "samples/openflow/scenarios/" + "modeloOpenFlow_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".xml";
	private String ARQUIVO_OMNET_NED_REDUCED = PATH_OMNET + "samples/openflow/scenarios/" + "modeloOpenFlowReduced_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".ned";
	private String ARQUIVO_OMNET_INI_REDUCED = PATH_OMNET + "samples/openflow/scenarios/" + "modeloOpenFlowReduced_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".ini";
	private String ARQUIVO_OMNET_CONFIG_XML_REDUCED = PATH_OMNET + "samples/openflow/scenarios/" + "modeloOpenFlowReduced_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".xml";
	
	//NS2
	private String ARQUIVO_MODELO_NS2 = PATH + "modeloNS2_"+
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".tcl";
	private String ARQUIVO_NS2_NAM = PATH + "modeloNS2_"+
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".nam";
	private String ARQUIVO_NS2_TR = PATH + "modeloNS2_"+
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".tr";
	private String ARQUIVO_NS2_PERDA = PATH + "modeloNS2_"+
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".perda";	
	private String ARQUIVO_NS2_VAZAO = PATH + "modeloNS2_"+
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".vazao";
	
	//NS2 Reduced
	private String ARQUIVO_MODELO_NS2_REDUCED = PATH + "modeloNS2Reduced_"+
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".tcl";
	private String ARQUIVO_NS2_NAM_REDUCED = PATH + "modeloNS2Reduced_"+
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".nam";
	private String ARQUIVO_NS2_TR_REDUCED = PATH + "modeloNS2Reduced_"+
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".tr";
	private String ARQUIVO_NS2_PERDA_REDUCED = PATH + "modeloNS2Reduced_"+
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".perda";	
	private String ARQUIVO_NS2_VAZAO_REDUCED = PATH + "modeloNS2Reduced_"+
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".vazao";
	
	//GraphViz
	private String ARQUIVO_GRAPHVIZ = PATH + "modeloGraphViz_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".graphviz";
	//GraphViz
		private String ARQUIVO_GRAPHVIZ_TRAFEGO = PATH + "modeloGraphVizTrafego_" + 
				NUM_SERVERS+"serv_"+
				NUM_VMS+"vm" + 
				".graphviz";

	//Next-Fit para o NS2
	private String ARQUIVO_NF_ALOCACAO = PATH + "modeloNF_" + 
				NUM_SERVERS+"serv_"+
				NUM_VMS+"vm" + 
				".alocacao";
	private String ARQUIVO_NF = PATH + "modeloNF_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".tcl";	
	private String ARQUIVO_NF_NAM = PATH + "modeloNF_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".nam";
	private String ARQUIVO_NF_TR = PATH + "modeloNF_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".tr";
	private String ARQUIVO_NF_VAZAO = PATH + "modeloNF_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".vazao";
	private String ARQUIVO_NF_PERDA = PATH + "modeloNF_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".perda";
	private String ARQUIVO_NF_VAZAO_UP_GRAPHVIZ = PATH + "modeloNF_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".vazao_up_graphviz";
	private String ARQUIVO_NF_VAZAO_DOWN_GRAPHVIZ = PATH + "modeloNF_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".vazao_down_graphviz";
	private String ARQUIVO_NF_PERDA_GRAPHVIZ = PATH + "modeloNF_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".perda_graphviz";
	
	//Previous Fit
		private String ARQUIVO_PF_ALOCACAO = PATH + "modeloPF_" + 
					NUM_SERVERS+"serv_"+
					NUM_VMS+"vm" + 
					".alocacao";
		private String ARQUIVO_PF = PATH + "modeloPF_" + 
				NUM_SERVERS+"serv_"+
				NUM_VMS+"vm" + 
				".tcl";	
		private String ARQUIVO_PF_NAM = PATH + "modeloPF_" + 
				NUM_SERVERS+"serv_"+
				NUM_VMS+"vm" + 
				".nam";
		private String ARQUIVO_PF_TR = PATH + "modeloPF_" + 
				NUM_SERVERS+"serv_"+
				NUM_VMS+"vm" + 
				".tr";
		private String ARQUIVO_PF_VAZAO = PATH + "modeloPF_" + 
				NUM_SERVERS+"serv_"+
				NUM_VMS+"vm" + 
				".vazao";
		private String ARQUIVO_PF_PERDA = PATH + "modeloPF_" + 
				NUM_SERVERS+"serv_"+
				NUM_VMS+"vm" + 
				".perda";
		private String ARQUIVO_PF_VAZAO_UP_GRAPHVIZ = PATH + "modeloPF_" + 
				NUM_SERVERS+"serv_"+
				NUM_VMS+"vm" + 
				".vazao_up_graphviz";
		private String ARQUIVO_PF_VAZAO_DOWN_GRAPHVIZ = PATH + "modeloPF_" + 
				NUM_SERVERS+"serv_"+
				NUM_VMS+"vm" + 
				".vazao_down_graphviz";
		private String ARQUIVO_PF_PERDA_GRAPHVIZ = PATH + "modeloPF_" + 
				NUM_SERVERS+"serv_"+
				NUM_VMS+"vm" + 
				".perda_graphviz";
		
		private String ARQUIVO_TOPOLOGIA_LINEAR = PATH + "modeloLingo_" + 
				NUM_SERVERS+"serv_"+
				NUM_VMS+"vm" + 
				".lg4";
		/*private String ARQUIVO_TOPOLOGIA_LINEAR_DATA = PATH + "modeloLingo_" + 
				NUM_SERVERS+"serv_"+
				NUM_VMS+"vm" + 
				".datacenter";
				*/
	
		//Mininet
		private String ARQUIVO_PARSER_MININET = PATH + "file.txt";
		
		private String ARQUIVO_PORTAS_MININET = PATH + "modeloLingo_"+
				NUM_SERVERS+"serv_"+
				NUM_VMS+"vm" + "_portasMininet.sh";
		
		private String ARQUIVO_REGRAS_OPENFLOW = PATH + "modeloLingo_"+
				NUM_SERVERS+"serv_"+
				NUM_VMS+"vm" + "_regrasOpenFlow.sh";
		private String ARQUIVO_REGRAS_OPENFLOW_RYU = PATH + "modeloLingo_"+
				NUM_SERVERS+"serv_"+
				NUM_VMS+"vm" + "_regrasOpenFlowRyu.sh";
		
	//Next-Fit para o Omnet
	/*private String ARQUIVO_NF = PATH + "modeloNF_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".log";
	private String ARQUIVO_OMNET_NF = PATH_OMNET + "samples/openflow/scenarios/" + "modeloNF_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".ned";
	private String ARQUIVO_NF_INI = PATH_OMNET + "samples/openflow/scenarios/" + "modeloNF_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".ini";
	private String ARQUIVO_NF_CONFIG_XML = PATH_OMNET + "samples/openflow/scenarios/" + "modeloNF_" + 
			NUM_SERVERS+"serv_"+
			NUM_VMS+"vm" + 
			".xml";
	*/
	
	
	public ConfigSettings(){
		
		super();
		
	}//fim construtor
		
	//Metodos get
	public int getNumServers(){
		return NUM_SERVERS;
	}
	public int getNumVMs(){
		return NUM_VMS;
	}
	public int getNumServersToR(){
		return NUM_SERVERS_TOR;
	}
	public int getNumPortasASwitch(){
		return NUM_PORTAS_A_SWITCH;
	}
	public double getToRBW(){
		return NUM_SERVERS*SERVER_HUGE_BW;
		
	}
	public double getLinkBW(){
		return LINK_BW;	
	}
	public int getNumServersSmallToR(){
		return NUM_SERVERS_SMALL_TOR;
	}
	public int getNumServersLargeToR(){
		return NUM_SERVERS_LARGE_TOR;
	}
	public int getNumServersHugeToR(){
		return NUM_SERVERS_HUGE_TOR;
	}
	public int getServerSmallCPU(){
		return SERVER_SMALL_CPU;
	}
	public int getServerLargeCPU(){
		return SERVER_LARGE_CPU;
	}
	public int getServerHugeCPU(){
		return SERVER_HUGE_CPU;
	}	
	public int getServerSmallBW(){
		return SERVER_SMALL_BW;		
	}
	public int getServerLargeBW(){
		return SERVER_LARGE_BW;		
	}
	public int getServerHugeBW(){
		return SERVER_HUGE_BW;		
	}
	public int getServerCostSmallCPU(){
		return SERVER_COST_SMALL_CPU;		
	}
	public int getServerCostLargeCPU(){
		return SERVER_COST_LARGE_CPU;		
	}
	public int getServerCostHugeCPU(){
		return SERVER_COST_HUGE_CPU;		
	}
	public int getVMSmallCPU(){
		return VM_SMALL_CPU;		
	}
	public int getVMSmallBW(){
		return VM_SMALL_BW;		
	}
	public int getMTServerLimite(){
		return MT_SERVER_LIMITE;
	}
	public int getMTValorLimite(){
		return MT_VALOR_LIMITE;
	}
	public String getPath(){
		return PATH;
	}
	public String getArquivoLingoModelo(){
		return ARQUIVO_LINGO_MODELO;
	}
	public String getArquivoLingoResult(){
		return ARQUIVO_LINGO_RESULT;
	}
	public String getArquivoLingoParser(){
		return ARQUIVO_LINGO_PARSER;
	}
	public String getArquivoLingoCaminhos(){
		return ARQUIVO_LINGO_CAMINHOS;
	}
	public String getArquivoTopologiaOriginal(){
		return ARQUIVO_TOPOLOGIA_ORIGINAL;
	}
	public String getArquivoTopologiaDatacenter(){
		return ARQUIVO_TOPOLOGIA_DATACENTER;
	}
	public String getArquivoAlocacaoDatacenter(){
		return ARQUIVO_ALOCACAO_DATACENTER;
	}
	public String getNomeTopologiaMininet(){
		return NOME_TOPOLOGIA_MININET;
	}
	public String getArquivoTopologiaMininet(){
		return ARQUIVO_TOPOLOGIA_MININET;
	}
	public String getArquivoTopologiaMininetRyu(){
		return ARQUIVO_TOPOLOGIA_MININET_RYU;
	}
	public String getArquivoScriptOF13(){
		return ARQUIVO_SCRIPT_OF13;
	}
	public String getArquivoNS3Modelo(){
		return ARQUIVO_NS3_MODELO;
	}
	public String getArquivoNS3Result(){
		return ARQUIVO_NS3_RESULT;
	}
	public String getArquivoOpenFlowModelo(){
		return ARQUIVO_OPENFLOW_MODELO;
	}
	public String getArquivoOpenFlowResult(){
		return ARQUIVO_OPENFLOW_RESULT;
	}
	public String getArquivoOpenFlowReduced(){
		return ARQUIVO_OPENFLOW_REDUCED;
	}
	public String getArquivoOpenFlowResultReduced(){
		return ARQUIVO_OPENFLOW_RESULT_REDUCED;
	}
	public String getArquivoOmnetNed(){
		return ARQUIVO_OMNET_NED;
	}
	public String getArquivoOmnetIni(){
		return ARQUIVO_OMNET_INI;
	}
	public String getArquivoOmnetConfigXML(){
		return ARQUIVO_OMNET_CONFIG_XML;
	}
	public String getArquivoOmnetNedReduced(){
		return ARQUIVO_OMNET_NED_REDUCED;
	}
	public String getArquivoOmnetIniReduced(){
		return ARQUIVO_OMNET_INI_REDUCED;
	}
	public String getArquivoOmnetConfigXMLReduced(){
		return ARQUIVO_OMNET_CONFIG_XML_REDUCED;
	}
	public String getArquivoGraphViz(){
		return ARQUIVO_GRAPHVIZ;
	}
	public String getArquivoGraphVizTrafego(){
		return ARQUIVO_GRAPHVIZ_TRAFEGO;
	}
	public String getArquivoNF(){
		return ARQUIVO_NF;
	}
	public String getArquivoNFAlocacao(){
		return ARQUIVO_NF_ALOCACAO;
	}
	/*public String getArquivoOmnetNF(){
		return ARQUIVO_OMNET_NF;
	}
	public String getArquivoNFIni(){
		return ARQUIVO_NF_INI;
	}
	public String getArquivoNFConfigXML(){
		return ARQUIVO_NF_CONFIG_XML;
	}*/
	public String getArquivoNFNAM(){
		return ARQUIVO_NF_NAM;
	}
	public String getArquivoNFTracking(){
		return ARQUIVO_NF_TR;
	}
	public String getArquivoNFPerda(){
		return ARQUIVO_NF_PERDA;
	}
	public String getArquivoNFVazao(){
		return ARQUIVO_NF_VAZAO;
	}
	public String getArquivoNFVazaoUpGraphViz(){
		return ARQUIVO_NF_VAZAO_UP_GRAPHVIZ;
	}
	public String getArquivoNFVazaoDownGraphViz(){
		return ARQUIVO_NF_VAZAO_DOWN_GRAPHVIZ;
	}
	public String getArquivoNFPerdaGraphViz(){
		return ARQUIVO_NF_PERDA_GRAPHVIZ;
	}
	
	public String getArquivoPF(){
		return ARQUIVO_PF;
	}
	public String getArquivoPFAlocacao(){
		return ARQUIVO_PF_ALOCACAO;
	}
	/*public String getArquivoOmnetPF(){
		return ARQUIVO_OMNET_PF;
	}
	public String getArquivoPFIni(){
		return ARQUIVO_PF_INI;
	}
	public String getArquivoPFConfigXML(){
		return ARQUIVO_PF_COPFIG_XML;
	}*/
	public String getArquivoPFNAM(){
		return ARQUIVO_PF_NAM;
	}
	public String getArquivoPFTracking(){
		return ARQUIVO_PF_TR;
	}
	public String getArquivoPFPerda(){
		return ARQUIVO_PF_PERDA;
	}
	public String getArquivoPFVazao(){
		return ARQUIVO_PF_VAZAO;
	}
	public String getArquivoPFVazaoUpGraphViz(){
		return ARQUIVO_PF_VAZAO_UP_GRAPHVIZ;
	}
	public String getArquivoPFVazaoDownGraphViz(){
		return ARQUIVO_PF_VAZAO_DOWN_GRAPHVIZ;
	}
	public String getArquivoPFPerdaGraphViz(){
		return ARQUIVO_PF_PERDA_GRAPHVIZ;
	}
	
	public String getArquivoModeloNS2(){
		return ARQUIVO_MODELO_NS2;
	}
	public String getArquivoNS2NAM(){
		return ARQUIVO_NS2_NAM;
	}
	public String getArquivoNS2Tracking(){
		return ARQUIVO_NS2_TR;
	}
	public String getArquivoNS2Perda(){
		return ARQUIVO_NS2_PERDA;
	}
	public String getArquivoNS2Vazao(){
		return ARQUIVO_NS2_VAZAO;
	}
	public String getArquivoModeloNS2Reduced(){
		return ARQUIVO_MODELO_NS2_REDUCED;
	}
	public String getArquivoNS2NAMReduced(){
		return ARQUIVO_NS2_NAM_REDUCED;
	}
	public String getArquivoNS2TrackingReduced(){
		return ARQUIVO_NS2_TR_REDUCED;
	}
	public String getArquivoNS2PerdaReduced(){
		return ARQUIVO_NS2_PERDA_REDUCED;
	}
	public String getArquivoNS2VazaoReduced(){
		return ARQUIVO_NS2_VAZAO_REDUCED;
	}
	
	public String getArquivoTopologiaLinear(){
		return ARQUIVO_TOPOLOGIA_LINEAR;
	}
/*	public String getArquivoTopologiaLinearDatacenter(){
		return ARQUIVO_TOPOLOGIA_LINEAR_DATA;
	}
	*/
	public String getArquivoParserMininet(){
		return ARQUIVO_PARSER_MININET;
	}
	
	public String getAppType(){
		return APP_TYPE;
	}
	
	//public String getDataRate(){
	//	return DATA_RATE;
	//}
	//public String getDataRateReduced(){
	//	return DATA_RATE_REDUCED;
	//}

	//Unused: Multiplica o trafego gerado pela VM
	private double FATOR_VM=1;
	public double getFatorVM(){
		return FATOR_VM;
	}
	//public int getBufferNS2(){
	//	return BUFFER_NS2;
	//}
	
	public String getArquivoPortasMininet(){
		return ARQUIVO_PORTAS_MININET;
	}
	public String getArquivoRegrasOpenFlow(){
		return ARQUIVO_REGRAS_OPENFLOW;
	}
	public String getArquivoRegrasOpenFlowRyu(){
		return ARQUIVO_REGRAS_OPENFLOW_RYU;
	}
	
	public String getVersion(){
		return VERSION;
	}
}//fim classe
