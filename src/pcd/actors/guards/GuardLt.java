package pcd.actors.guards;

import pcd.actors.Choice;

public class GuardLt
{
	private int value1;
	private int value2;
	private Choice choice;
	
	public GuardLt(int value1, int value2, Choice choice)
	{
		this.value1 = value1;
		this.value2 = value2;
		this.setChoice(choice);
	}
	
	public boolean evaluateBoolExpression()
	{
		if(value1 < value2)
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
}
