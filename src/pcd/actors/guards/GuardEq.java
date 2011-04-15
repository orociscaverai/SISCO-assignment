package pcd.actors.guards;

import pcd.actors.Choice;
import pcd.actors.filters.MsgFilter;

public class GuardEq
{
	private int value1;
	private int value2;
	private Choice choice;
	private MsgFilter filter;
	
	public GuardEq(int value1, int value2, Choice choice, MsgFilter filter)
	{
		this.value1 = value1;
		this.value2 = value2;
		this.setChoice(choice);
		this.setFilter(filter);
	}
	
	public boolean evaluateBoolExpression()
	{
		if(value1 == value2)
			return true;
		
		return false;
	}

	private void setChoice(Choice choice)
	{
		this.choice = choice;
	}

	public Choice getChoice()
	{
		return choice;
	}

	public void setFilter(MsgFilter filter)
	{
		this.filter = filter;
	}

	public MsgFilter getFilter()
	{
		return filter;
	}
}
