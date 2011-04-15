package pcd.lab08.esempioCS;

import pcd.actors.MessageDispatcher;

public class Test
{
	public static void main(String[] args)
	{
		MessageDispatcher.getInstance().start();
		
		ClientActor client1 = new ClientActor("Client1", "Server1", null);
		ClientActor client2 = new ClientActor("Client2", "Server1", null);
		
//		ServerActor server1 = new ServerActor("Server1");
//		server1.start();
		
		try
		{
			Thread.sleep(5000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		client1.start();
		client2.start();				
		
	}
}
