import java.util.HashMap;


public class Subject extends Term
{
	public String label;
	
	public Subject(String label)
	{
		this.label = label;
	}
	
	public String getLabel()
	{
		return this.label;
	}

	@Override
	public boolean isContains(Subject v)
	{
		return this.label.equals(v.getLabel());
	}
}
