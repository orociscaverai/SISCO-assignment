package pcd.lab08.esempioCS;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import pcd.actors.Actor;
import pcd.actors.Message;
import pcd.actors.Port;
import pcd.actors.Resource;


public class ServerActor extends Actor
{
	private static final int ACQUIRE = 0; 
	private static final int RELEASE = 1;
	
	private int avail;
	
	private List<Port> pending = null;
	private List<Resource> resources = null;
	
	public ServerActor(String actorName)
	{
		super(actorName);
		setAvail(1);
		pending = new ArrayList<Port>();
		resources = new ArrayList<Resource>();	
		
		for(int i = 0; i < getAvail(); i++)
			resources.add(new Resource("resource" + i));
	}	

	private void setAvail(int avail)
	{
		this.avail = avail;
	}

	private int getAvail()
	{
		return avail;
	}
	
	private Resource remove()
	{
		return resources.remove(0);
	}
	
	private void insert(Resource r)
	{
		resources.add(r); 
	}
	
	public void run()
	{
		while(true)
		{
			System.out.println(getActorName() + " - in attesa di richieste ...");
			
			Message message = receive();	
			System.out.println(getActorName() + " - messaggio ricevuto: " + message);
			int kind = (Integer) message.getArg(0);
			Port portActor = (Port) message.getArg(1);		
			
			if(kind == ACQUIRE)
			{
				if(avail > 0)
				{
					avail--;
					Resource r = remove();
					
					try
					{						
						send(portActor, new Message("reply", r));
					}
					catch (UnknownHostException e)
					{
						e.printStackTrace();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				else
					pending.add(portActor);
			}
			else if(kind == RELEASE)
			{
				Resource r = (Resource) message.getArg(2);
				
				if(pending.size() == 0)
				{
					insert(r);
					avail++;
				}
				else
				{
					Port portPendingActor = pending.remove(0);
					try
					{						
						send(portPendingActor, new Message("reply", r));
					}
					catch (UnknownHostException e)
					{
						e.printStackTrace();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
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
