/*
	23/09/11 - Rafael Mota
	26/09/11 - George Harinson
	05/10/11 - João Marcelo
 */
import java.util.*;

public class ReadFiles {
    
    int AUXTOGETLAT = 5; //Auxilia no manuseio da String de cada linha do arquivo.
    Scanner file;
    LinkedList<Packet> pcks = new LinkedList<Packet>();
    Map<String,Integer> nRetranspFLuxs = new HashMap<String,Integer>();
        
    public Map<String,Integer> getnRetranspFluxs()
    {
    	return this.nRetranspFLuxs;
    }
    
    public Packet[] read(String path){
        
        String[] listfiles; //files contem os arquivos do path
        
        HandleFiles handlefiles = new HandleFiles();
        //System.out.println(path);
        listfiles = handlefiles.getFilepaths(path); //Lista todos os arquivos de um diretorio
        
       
        for(int numbfiles = 0 ; numbfiles < listfiles.length; numbfiles ++){   //Percorre todos os arquivos do path
       
             file = handlefiles.OpenFilestoRead(path + "//" + listfiles[numbfiles]); //Abre arquivos no path para leitura file é arquivo que será lido
             //System.out.println(path + "//" + listfiles[numbfiles]);
             
             int a = 0; //Auxiliar para calcular tráfego aceito

             double LastAccTraf = 0;
             
             //Ler primeiro pacote, ler segundo, seta AccTraf do primeiro add ele e chama o segundo de aux...
             Packet aux = ReadOnePacket();
             while(file.hasNext()){
                 
             Packet pck = ReadOnePacket();
             
             
             aux.setAccepTraffic(pck.getTpflext());
             LastAccTraf = aux.getAccepTraff();
             pcks.add(aux); 
                 
             aux = pck;
             } //end of while
             
            aux.setAccepTraffic(LastAccTraf);
            pcks.add(aux); //Adiciona o ultimo auxiliar na Lista Pois ele não está incluido no laço acima
             
        }
        //Collections.sort(pcks, pcks);
        Packet[] Aux = pcks.toArray( new Packet[0]); //Converte Lista pcks em Array Aux e retorna 
         return Aux;
        
    }
    

    //Ler os dados de um PACOTE
    private Packet ReadOnePacket(){
        
            String Target = file.next(); //Pega primeiro nibble do arquivo
            
            String size = file.next(); //Pega segundo nibble do arquivo
           
            String source = file.next(); //Pega terceiro nibble do arquivo
			
            String flux = source.substring(2)+" "+Target.substring(2);
			int numbOfRetrans=0;
			
                        
            for(int a = 0 ; a < Integer.parseInt(size,16) + AUXTOGETLAT ; a++)
			{               
			   if(a==(Integer.parseInt(size,16)-2))
			   {
				   if(!nRetranspFLuxs.containsKey(flux))
				   {
					   numbOfRetrans = Integer.parseInt(file.next());
					   nRetranspFLuxs.put(flux, numbOfRetrans);
				   }
				   else
				   {
					   nRetranspFLuxs.put(flux,nRetranspFLuxs.get(flux)+Integer.parseInt(file.next()));
				   }
				   //System.out.println("Number of Retransmissions: "+numbOfRetrans);
				   continue;
			   }
			   
               file.next();
               
            }
            
            double Tpflext = Double.parseDouble(file.next()); 
            double Latency = Double.parseDouble(file.next());
            
            
            file.nextLine(); //Pega o que sobrou da linha pra não dar erro no proximo laço
           
            
            Packet pck = new Packet (Target,size,source,Latency,Tpflext,numbOfRetrans); //inicializa pacote
            
            return pck;
     
    }
    
}
