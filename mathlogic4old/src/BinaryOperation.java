import java.util.HashMap;


public abstract class BinaryOperation extends Operation
{
	protected F left;
	protected F right;
	
	public BinaryOperation(F left, F right)
	{
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
		return "("+left+" " + getDelimeter() + " "+right+")";
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
			if (this.getTypeId() != bo.getTypeId())
			{
				return false;
			}
			return left.equals(bo.left) && right.equals(bo.right);
		}
		return false;
	}
	
	@Override
	public boolean isContains(Subject v)
	{
		return left.isContains(v) || right.isContains(v);
	}

	@Override
	public boolean isFree(Subject v)
	{
		return left.isFree(v) || right.isFree(v);
	}
}
