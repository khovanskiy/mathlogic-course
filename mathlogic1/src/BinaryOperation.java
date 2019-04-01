import java.util.HashMap;


public class BinaryOperation extends Formula
{
	public OperationType type;
	private Formula left;
	private Formula right;
	
	public BinaryOperation(OperationType type, Formula left, Formula right)
	{
		this.type = type;
		this.left = left;
		this.right = right;
	}
	
	public Formula getLeft()
	{
		return left;
	}
	
	public Formula getRight()
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
