
public abstract class UnaryOperation extends Operation
{
	public F subject;
	
	public UnaryOperation(F subject)
	{
		this.subject = subject;
	}
	
	@Override
	public String toString()
	{
		return getDelimeter() + "("+subject+")";
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
			if (this.getTypeId() != (((UnaryOperation) other).getTypeId()))
			{
				return false;
			}
			return subject.equals(((UnaryOperation) other).subject);
		}
		return false;
	}
	
	@Override
	public boolean isContains(Subject v)
	{
		if (v.getLabel().equals(v.getLabel()))
		{
			return true;
		}
		return subject.isContains(v);
	}

	@Override
	public boolean isFree(Subject v)
	{
		if (v.getLabel().equals(v.getLabel()))
		{
			return true;
		}
		return subject.isFree(v);
	}
}
