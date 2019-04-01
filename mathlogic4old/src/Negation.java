import java.util.HashMap;
import java.util.Vector;


public class Negation extends UnaryOperation
{
	public Negation(F subject)
	{
		super(subject);
	}

	@Override
	public String getDelimeter()
	{
		return "!";
	}
	
	@Override
	public int getTypeId()
	{
		return 512;
	}

	@Override
	public String java_code()
	{
		return "F.neg(" + subject.java_code() + ")";
	}
}
