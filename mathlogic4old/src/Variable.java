import java.util.HashMap;
import java.util.Vector;


public class Variable extends F
{
	private String label;
	
	public Variable(String label)
	{
		this.label = label;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	@Override
	public String toString()
	{
		return label;
	}

	@Override
	public boolean equals(Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (other instanceof Variable)
		{
			return this.label.equals(((Variable)other).getLabel());
		}
		return false;
	}

	@Override
	public int getTypeId()
	{
		return 1024;
	}

	@Override
	public String java_code()
	{
		if (label.equals("A"))
		{
			return "subject";
		}
		else
		{
			return "right";
		}
	}
	
	@Override
	public boolean isContains(Subject v)
	{
		return false;
	}

	@Override
	public boolean isFree(Subject v)
	{
		return false;
	}
}
