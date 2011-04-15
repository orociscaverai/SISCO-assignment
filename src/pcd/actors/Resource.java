package pcd.actors;
import java.io.Serializable;

@SuppressWarnings("serial")
public class Resource implements Serializable
{
	private String resourceName;
	
	public Resource(String resourceName)
	{
		this.setResourceName(resourceName);
	}

	public void setResourceName(String resourceName)
	{
		this.resourceName = resourceName;
	}

	public String getResourceName()
	{
		return resourceName;
	}
	
	public String toString()
	{
		return getResourceName();
	}
}
