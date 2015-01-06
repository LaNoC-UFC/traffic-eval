/*
	23/09/11 - Rafael Mota
	26/09/11 - George Harinson
	05/10/11 - João Marcelo
 */
import java.io.*;

public class TrafficMeasurer 
{
    public static void main(String[] args) 
	{
		//passar pathN1, dimX e dimY pela linha de comando
    	String path = null;
    	if(args.length == 1)
    	{
    		path = "./"+args[0];
    	}
    	else
    	{
    		path = "./evaluate";
    	}	
    	//path = "./evaluate";
                //E:\Marcelo\TestesFev12\ResultadosFev12
		int dimX = 5;
		int dimY = 5;
		/*for( int i = 0; i < traffics.length; i++ )
		{
			String pathN2 = pathN1 + "//" + traffics[i];
			File folderN2 = new File( pathN2 );
			String[] topologies = folderN2.list();
			for( int j = 0; j < topologies.length; j++ )
			{*/
				//String pathN3 = "C://Users//GpNoC//Dropbox//Pasta Pessoal//TCC//evaluate";
				File folderN3 = new File( path );
				new File("./results").mkdirs();
				String graphsPath = "./results"; 
				
				String[] nets = folderN3.list();
				for( int k = 0; k < nets.length; k++)
				{
					String pathN4 = path + "//" + nets[k];
                    System.out.println( pathN4 );
					Evaluation eval;
					eval = new Evaluation( nets[k], pathN4,graphsPath, dimX, dimY);
					eval.makeCNFs();
					//eval.plotCNFs();
					eval.printRetrans();
				}
			//}
		//}
    //}
	}
}
