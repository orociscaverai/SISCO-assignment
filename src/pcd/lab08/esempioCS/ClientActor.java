package pcd.lab08.esempioCS;

import java.io.IOException;
import java.net.UnknownHostException;

import pcd.actors.Actor;
import pcd.actors.Message;
import pcd.actors.Port;
import pcd.actors.Resource;
import pcd.actors.filters.MsgFilter;

public class ClientActor extends Actor
{
	private static final int ACQUIRE = 0; 
	private static final int RELEASE = 1; 
	private String serverName;
	private MsgFilter filter;
	
	protected ClientActor(String actorName, String serverName, MsgFilter filter)
	{
		super(actorName);
		this.serverName = serverName;
		this.filter = filter;
	}

	public void run()
	{
		while(true)
		{
			Port port = new Port(serverName,"localhost");
			
			// invio la richiesta per acquisire una risorsa
			try
			{				
				send(port, new Message("request", ACQUIRE, this.getLocalPort()));
				System.out.println(getActorName() + " - invio messaggio: request(ACQUIRE, " + port + ")");
			}
			catch (UnknownHostException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			// ricevo il messaggio di risposta
			Message msg = null;
			
			if(filter == null)
				msg = receive();
			else
				msg = receive(filter);
			
			Resource r = (Resource) msg.getArg(0);
			System.out.println(getActorName() + " - ricezione messaggio");
			System.out.println(getActorName() + " - risorsa " + r + " allocata");
			
			try
			{
				Thread.sleep(2000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			// invio la richiesta per rilasciare la risorsa			
			try
			{
				send(port, new Message("request", RELEASE, port, r));
				System.out.println(getActorName() + " - invio messaggio: request(RELEASE, " + port + ", " + r + ")");
			}
			catch (UnknownHostException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
