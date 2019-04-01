
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
}
