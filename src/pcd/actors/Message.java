package pcd.actors;
import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable
{
	private String type;
	private Object[] args;
	
	public Message(String type, Object...args)
	{
		this.setType(type);
		this.setArgs(args);
		//System.out.println(args[0]);
	}

	private void setType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}
	
	private void setArgs(Object[] args)
	{
		this.args = args;
		//System.out.println(this.args[0]);
	}

	public Object getArg(int index)
	{
		return args[index];
	}
	
	public Object[] getArgs()
	{
		return args;
	}
	
	public String toString()
	{		
		String args = getType() + " ";
		 
		for(Object arg: getArgs())
		{
			args += arg + " ";
		}
		
		return args;
	}
}
