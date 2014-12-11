/*
	23/09/11 - Rafael Mota
	26/09/11 - George Harinson
	05/10/11 - João Marcelo
	24/06/14 - Rafael Mota (Locate of graphs)
 */

public class PlotGraphics
{	
	public static void plotED_Lat( String path, String arq, double mean, double dp, String net )
	{
		String[] plot = new String[11];
		int i = 0;

		plot[i++] = "reset\n";
		plot[i++] = "set grid\n";
		plot[i++] = "set key bmargin left horizontal\n";
		plot[i++] = "set title \" Teste: " + net + " Distribution of latencies         Mean: " + mean + "//" + dp + " \"";
		plot[i++] = "set xlabel \"latency (clock cycles)\" \n";
		plot[i++] = "set ylabel \"#pcks\" \n";
		plot[i++] = "set terminal png size 1300,1000";
		plot[i++] = "set term png transparent\n";
		plot[i++] = "set output \"" + path + arq + ".png\" \n";
		plot[i++] = "plot \"" + path + arq + ".txt\" using 2:1 t\"" + net + " \" with linespoints 20 7 \n";
		plot[i++] = "quit";
			
		GNUcmds.exec( plot );  //Modified to destroy process after finished
	}
	
	public static void plotED_LatN( String path, String arq, double mean, double dp, String net )
	{
		String[] plot = new String[11];
		int i = 0;

		plot[i++] = "reset\n";
		plot[i++] = "set grid\n";
		plot[i++] = "set key bmargin left horizontal\n";
		plot[i++] = "set title \" Teste: " + net + " Distribution of latencies//HC         Mean: " + mean + "//" + dp + " \"";
		plot[i++] = "set xlabel \"latency//HC (clock cycles hop count)\" \n";
		plot[i++] = "set ylabel \"#pcks\" \n";
		plot[i++] = "set terminal png size 1300,1000";
		plot[i++] = "set term png transparent\n";
		plot[i++] = "set output \"" + path + arq + ".png\" \n";
		plot[i++] = "plot \"" + path + arq + ".txt\" using 2:1 t\"" + net + " \" with linespoints 20 7 \n";
		plot[i++] = "quit";
			
		GNUcmds.exec( plot );  //Modified to destroy process after finished
	}
	
	public static void plotED_AT( String path, String arq, double mean, double dp, String net )
	{
		String[] plot = new String[11];
		int i = 0;

		plot[i++] = "reset\n";
		plot[i++] = "set grid\n";
		plot[i++] = "set key bmargin left horizontal\n";
		plot[i++] = "set title \" Teste: " + net + " Distribution of Accepted Traffic         Mean: " + mean + "//" + dp + " \"";
		plot[i++] = "set ylabel \"#pcks \" \n";
		plot[i++] = "set xlabel \"accepted traffic\" \n";
		plot[i++] = "set terminal png size 1300,1000";
		plot[i++] = "set term png transparent\n";
		plot[i++] = "set output \"" + path + arq + ".png\" \n";
		plot[i++] = "plot \"" + path + arq + ".txt\" using 2:1 t\"" + net + " \" with linespoints 20 7 \n";
		plot[i++] = "quit";
			
		GNUcmds.exec( plot );  //Modified to destroy process after finished
	}
	
	public static void plotCNF_Lat( String path, String arq, String net )
	{
		//System.out.println(path+arq+".txt");
		String[] plot =  new String[11];
		int i = 0;

		plot[i++] = "reset\n";
		plot[i++] = "set grid\n";
		plot[i++] = "set key bmargin left horizontal\n";
		plot[i++] = "set title \" Teste: "+ net+ "\"";
		plot[i++] = "set ylabel \"latency mean\n";
		plot[i++] = "set xlabel \"Offered load\" \n";
		plot[i++] = "set terminal png size 1300,1000";
		plot[i++] = "set term png transparent\n";
		plot[i++] = "set output \""+path+arq+".png\" \n";
		plot[i++] = "plot \""+path+arq+".txt\" using 1:2 t\"" + net + " \" with linespoints 20 7 \n";
		plot[i++] = "quit";
	
		GNUcmds.exec(plot);   //Modified to destroy process after finished
  	
	}
	
	public static void plotCNF_LatN( String path, String arq, String net )
	{
		String[] plot =  new String[11];
		int i = 0;

		plot[i++] = "reset\n";
		plot[i++] = "set grid\n";
		plot[i++] = "set key bmargin left horizontal\n";
		plot[i++] = "set title \" Teste: "+ net+ "\"";
		plot[i++] = "set ylabel \"latency normalized\n";
		plot[i++] = "set xlabel \"Offered load\" \n";
		plot[i++] = "set terminal png size 1300,1000";
		plot[i++] = "set term png transparent\n";
		plot[i++] = "set output \""+path+arq+".png\" \n";
		plot[i++] = "plot \""+path+arq+".txt\" using 1:2 t\""+net+" \" with linespoints 20 7 \n";
		plot[i++] = "quit";
	
		GNUcmds.exec(plot);   //Modified to destroy process after finished
	}
	
	public static void plotCNF_AT( String path, String arq, String net )
	{
		String[] plot =  new String[11];
		int i = 0;
			
		plot[i++] = "reset\n";
		plot[i++] = "set grid\n";
		plot[i++] = "set key bmargin left horizontal\n";
		plot[i++] = "set title \" Teste: "+net+"  \"";
		plot[i++] = "set ylabel \"accepttraffic \" (clock cycles)\n";
		plot[i++] = "set xlabel \"Offered load\" \n";
		plot[i++] = "set terminal png size 1300,1000";
		plot[i++] = "set term png transparent\n";
		plot[i++] = "set output \""+path+arq+".png\" \n";
		plot[i++] = "plot \""+path+arq+".txt\" using 1:2 t\""+net+" \" with linespoints 20 7 \n";
		plot[i++] = "quit";
			
		GNUcmds.exec(plot);   //Modified to destroy process after finished
	}
	
	 public static void plotED_Traf3D(String path, String arq, String net){
      

           String[] plot = new String[13];
           int i = 0;

           plot[i++] =  "reset\n";
           plot[i++] =  "set dgrid3d 35,35,35\n";
           plot[i++] =  "set view 45,45,1,1\n";
           plot[i++] =  "set hidden3d\n";
           plot[i++] =  "set xlabel \"Routers in x axis\"\n";
           plot[i++] =  "set ylabel \"Routers in y axis\"\n";
           plot[i++] =  "set zlabel \"            accepttraffic \"\n";
           plot[i++] = "set terminal png size 1300,1000";
           plot[i++] = "set term png transparent\n";
           plot[i++] = "set output \""+path+arq+".png\" \n";
		   plot[i++] =  "splot \""+path+arq+".txt\" using 1:2:3 t\"" +net+ "\" w lines 1\n";
           plot[i++] = "quit";

           GNUcmds.exec(plot); //Modified to destroy process after finished



    }	
	
	
}