package pcd.actors;

public class Choice
{
	private int value;
	private Message message = null;
	
	public Choice(int value)
	{
		this.setValue(value);
	}

	private void setValue(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public void setMessage(Message message)
	{
		this.message = message;
	}

	public Message getMessage()
	{
		return message;
	}
}
