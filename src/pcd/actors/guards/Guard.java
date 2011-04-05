package pcd.actors.guards;

import pcd.actors.Choice;
import pcd.actors.filters.MsgFilter;

public interface Guard
{
	public Choice getChoice();
	
	public MsgFilter getFilter();
	
	public boolean evaluate();
}
