package pcd.actors.filters;

import pcd.actors.Message;

public interface MsgFilter
{
	public boolean match(Message msg);
}
