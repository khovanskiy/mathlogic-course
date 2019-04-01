
public class UnaryOperation extends F
{
	public OperationType type;
	public F subject;
	
	public UnaryOperation(OperationType type, F subject)
	{
		this.type = type;
		this.subject = subject;
	}
	
	@Override
	public String toString()
	{
		return "("+ type.getDelimeter() + subject+")";
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (other instanceof UnaryOperation)
		{
			if (this.type != ((UnaryOperation) other).type)
			{
				return false;
			}
			return subject.equals(((UnaryOperation) other).subject);
		}
		return false;
	}
}
