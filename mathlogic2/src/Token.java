
public class Token
{
	public String label = "";
	public OperationType type;
	
	public Token(OperationType type)
	{
		this.type = type;
	}
	
	public Token(String label)
	{
		this.label = label;
		this.type = OperationType.EMPTY;
	}
	
	@Override
	public String toString()
	{
		return label+" "+type;
	}
}
