import java.util.HashMap;


public class BinaryOperation extends F
{
	public OperationType type;
	private F left;
	private F right;
	
	public BinaryOperation(OperationType type, F left, F right)
	{
		this.type = type;
		this.left = left;
		this.right = right;
	}
	
	public F getLeft()
	{
		return left;
	}
	
	public F getRight()
	{
		return right;
	}
	
	@Override
	public String toString()
	{
		return "("+left+"" + type.getDelimeter() + ""+right+")";
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (other instanceof BinaryOperation)
		{
			BinaryOperation bo = (BinaryOperation) other;
			if (this.type != bo.type)
			{
				return false;
			}
			return left.equals(bo.left) && right.equals(bo.right);
		}
		return false;
	}
}
