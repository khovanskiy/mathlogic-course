import java.util.HashMap;
import java.util.Vector;


public class Disjunction extends BinaryOperation
{
	public Disjunction(F left, F right)
	{
		super(left, right);
	}

	@Override
	public String getDelimeter()
	{
		return "|";
	}

	@Override
	public int getTypeId()
	{
		return 128;
	}
	
	@Override
	public String java_code()
	{
		return "F.dis(" + left.java_code() + ", " + right.java_code() + ")";
	}
}
