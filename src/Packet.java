/*
	23/09/11 - Rafael Mota
	26/09/11 - George Harinson
	05/10/11 - João Marcelo
 */

public class Packet 
{
    int size;
    double latency;
    double accepTraffic;
	int priority;
    String source;
    String target;
    double tpflext;
    String net;
	String[] flux = new String[2]; //[0]-src [1]-dst
    
	public String[] getflux()
	{
		return flux;
	}
	
    public int getXt()
	{
		return Integer.parseInt( target.substring(2,3) );
	}
    

    public int getXs()
	{
		return Integer.parseInt( source.substring(2,3) );
	}
	
    public int getYt()
	{
		return Integer.parseInt( target.substring(3,4) );
	}
	
    
    public int getYs()
	{
		return Integer.parseInt( source.substring(3,4) );
	}
    
    public double getLat() 
	{
        return latency;
    }

    public int getPriority() 
	{
        return priority;
    }

    public int getSize() 
	{
        return size;
    }

    public String getSource() 
	{
        return source;
    }

    public String getTarget() 
	{
        return target;
    }
    
    public double getTpflext() 
	{
        return tpflext;
    }
    
    public double getAccepTraff()
	{
        return accepTraffic;
    }
	
    
    /*public void setLatency(double latency) 
	{
        this.latency = latency;
    }*/

	public void setFlux(String src, String dst)
	{
		this.flux[0]=src;
		this.flux[1]=dst;
	}
	
    public void setPriority(int priority) 
	{
        this.priority = priority;
    }
    
    public void setSource(String source) 
	{
        this.source = source;
    }

    public void setTarget(String target) 
	{
        this.target = target;
    }
    
    public void setAccepTraffic(double tpflext1)
	{           
        this.accepTraffic = ((double)this.size+2)/(tpflext1 - this.tpflext); //Calcula tráfego aceito do pacote
    }
    
//    public void setAccepTraffic(double acceptraffic)
//	{           
//        this.accepTraffic = acceptraffic;   
//    }
    
    //Construtor, inicializa todos os atributos do pacote através da classe HandleFiles
    public Packet(String target, int size, String source, double latency, double tpflext) 
	{   
        this.size = size;
        this.latency = latency;
        this.priority = Integer.parseInt(target.substring(0, 1));
        this.source = source;
        this.target = target;
        this.tpflext = tpflext;
    }

    //Verifica se o pacote é de um tipo especificado
       public boolean isType(char type)
	{
        //H ou h alta prioridade, L ou l baixa prioridade else indiferente
        // Se não for H nem L retornará false  
        if ( type == 'H' || type =='h' )
		{
			return this.getPriority() == 1;
		}
		else if ( type == 'L' || type == 'l' )
		{
			return this.getPriority() == 0;
		}
		else
			return true;
    }
    
    
}
