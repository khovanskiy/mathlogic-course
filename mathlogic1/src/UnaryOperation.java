
public class UnaryOperation extends Formula
{
	public OperationType type;
	public Formula subject;
	
	public UnaryOperation(OperationType type, Formula subject)
	{
		this.type = type;
		this.subject = subject;
	}
	
	@Override
	public String toString()
	{
		return "("+type.getDelimeter() + subject+")";
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
