package pcd.actors.filters;

import pcd.actors.Message;

public class MsgFilterImpl implements MsgFilter
{
	private String type;
	private int nArgs;
	
	public MsgFilterImpl(String type, int nArgs)
	{
		this.type = type;
		this.nArgs = nArgs; 
	}
	
	public boolean match(Message msg)
	{
		if(msg.getType().equals(type) && msg.getArgs().length == nArgs)
			return true;
		
		return false;
	}
}
