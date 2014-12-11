/*
	23/09/11 - Rafael Mota
	26/09/11 - George Harinson
	05/10/11 - João Marcelo
 */
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

class GNUcmds 
 {
	public static void exec(String[] comando) 
	{
		try 
		{
			Process p = Runtime.getRuntime().exec("./gp440win32//gnuplot//binary//gnuplot.exe//");//Mudar para o path do GNUPLOT comandos final
            OutputStream outputStream = p.getOutputStream(); //process p
            PrintWriter gp = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
			//System.out.println("COMANDO: "+ comando.length);
            for(int i = 0;i<comando.length;i++)
			{
                gp.println(comando[i]);
               // System.out.println(i);
               // System.out.println("COMANDO: " + comando[i]);
                gp.flush();
            }
            gp.close();
            
            //p.destroy(); //Forced to terminate. Check if with this the plots are completely done
            
        } 
		catch (Exception x) 
		{
			System.out.println(x.getMessage());
        }
    }
}
 