package pcd.actors;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MessageBox implements Iterator<Message>
{
	private ArrayBlockingQueue<Message> queue = null;
	private Actor actor = null;
	private Object token = null;
	
	public MessageBox(Actor actor, Object token)
	{
		this.actor = actor;
		this.token = token;
		setQueue(new ArrayBlockingQueue<Message>(1000));
	}
	
	private void setQueue(ArrayBlockingQueue<Message> queue)
	{
		this.queue = queue;
	}

	public Queue<Message> getQueue()
	{
		return queue;
	}
	
	public boolean insert(Message message)
	{
		synchronized (token)
		{
			token.notify();
		}
		//actor.unblock();
		try
		{
			((ArrayBlockingQueue<Message>) queue).put(message);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean hasNext()
	{
		return queue.iterator().hasNext();
	}
	
	public Message next()
	{
		return queue.iterator().next();
	}
	
	public Message get()
	{
		try
		{
			return queue.poll(50L, TimeUnit.SECONDS);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void remove(Message msg)
	{
		queue.remove(msg);
	}

	public void remove(){}
}
