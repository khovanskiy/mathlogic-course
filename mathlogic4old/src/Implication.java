import java.util.HashMap;
import java.util.Vector;


public class Implication extends BinaryOperation
{
	public Implication(F left, F right)
	{
		super(left, right);
	}

	@Override
	public String getDelimeter()
	{
		return "->";
	}

	@Override
	public int getTypeId()
	{
		return 64;
	}
	
	@Override
	public String java_code()
	{
		return "F.impl(" + left.java_code() + ", " + right.java_code() + ")";
	}
}
