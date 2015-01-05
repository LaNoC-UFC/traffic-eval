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
    
    public Packet[] read(String path)
    {
        
        String[] listfiles; //files contem os arquivos do path
        
        HandleFiles handlefiles = new HandleFiles();
        //System.out.println(path);
        listfiles = handlefiles.getFilepaths(path); //Lista todos os arquivos de um diretorio
        
       
        for(int numbfiles = 0 ; numbfiles < listfiles.length; numbfiles ++){   //Percorre todos os arquivos do path
       
             file = handlefiles.OpenFilestoRead(path + "//" + listfiles[numbfiles]); //Abre arquivos no path para leitura file é arquivo que será lido
             //System.out.println(path + "//" + listfiles[numbfiles]);
             System.out.println("file"+listfiles[numbfiles]);
             
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
    
    private Packet ReadOnePacket()
    {        
            String binTarget = String.format("%16s", Integer.toBinaryString(Integer.parseInt(file.next(),16))).replace(" ", "0");
            int size = Integer.parseInt(file.next(),16);
            String source = file.next();
            int nSeq = Integer.parseInt(file.next()+file.next(),16);
            
            double Tpflext = Double.parseDouble(file.next()); 
            double Latency = Double.parseDouble(file.next());
            
            
            file.nextLine(); //Pega o que sobrou da linha
           
            int tX = Integer.parseInt(binTarget.substring(0, binTarget.length()/2),2);
            int tY = Integer.parseInt(binTarget.substring(binTarget.length()/2, binTarget.length()),2);
            
            String Target = tX+"."+tY;
            
            System.out.println("Target: "+Target);
            Packet pck = new Packet (Target,size,source,Latency,Tpflext);
            
            return pck;
     
    }
    
}
