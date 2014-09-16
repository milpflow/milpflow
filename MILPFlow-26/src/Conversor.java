
public class Conversor {

	public Conversor(){
		
		int valor=12;
		String a= Integer.toHexString(valor);
		System.out.println("["+a+"]");
		String b = String.format("%016X", valor);
		System.out.println("["+b+"]");
		String b1 = String.format("%016x", valor);
		System.out.println("["+b1+"]");
		String c = String.format("%016X", valor);
		int i=0;
		int j=1;
		String aux=c.substring(i, j+1);
		System.out.println(aux);
		i+=2;
		j+=2;
		while(j<16){
			aux += ":"+c.substring(i, j+1);				
			i+=2;
			j+=2;
			System.out.println(aux);
		}//end while
		System.out.println(aux);
		
	}
	
	public static void main (String args[]){
		
		new Conversor();
	}
	
}
