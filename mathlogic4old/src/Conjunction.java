import java.util.HashMap;
import java.util.Vector;


public class Conjunction extends BinaryOperation
{
	public Conjunction(F left, F right)
	{
		super(left, right);
	}

	@Override
	public String getDelimeter()
	{
		return "&";
	}

	@Override
	public int getTypeId()
	{
		return 256;
	}
	
	@Override
	public String java_code()
	{
		return "F.con(" + left.java_code() + ", " + right.java_code() + ")";
	}
}
