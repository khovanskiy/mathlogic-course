import java.util.HashMap;


public class Any extends Quantifier
{
	public Any(Variable v, F subject) 
	{
		super(v, subject);
	}

	@Override
	public String getDelimeter()
	{
		return LexemeType.ANY.getDelimeter();
	}

	@Override
	public String java_code() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTypeId()
	{
		return 2048;
	}
	
	@Override
	public String toString()
	{
		return "@" + getV() + "(" + getF() + ")";
	}
}
