package pcd.actors;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SuppressWarnings("serial")
public class Port implements Serializable
{
	private String actorName;
	private String hostName;
	
	public Port(String actorName)
	{
		this.setActorName(actorName);
				
		String hostname = null;
		try
		{
			InetAddress addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		
		this.setHostName(hostname);
	}
	
	public Port(String actorName, String addr)
	{
		this.setActorName(actorName);
		this.setHostName(addr);				
	}

	private void setActorName(String actorName)
	{
		this.actorName = actorName;
	}

	public String getActorName()
	{
		return actorName;
	}

	private void setHostName(String hostName)
	{
		this.hostName = hostName;
	}

	public String getHostName()
	{
		return hostName;
	}
	
	public String toString()
	{
		return this.getActorName() + "@" + this.getHostName();
	}
}
