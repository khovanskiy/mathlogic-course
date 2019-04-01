import java.util.HashMap;


public class Exists extends Quantifier
{
	public Exists(Variable v, F subject) 
	{
		super(v, subject);
	}

	@Override
	public String getDelimeter()
	{
		return LexemeType.EXISTS.getDelimeter();
	}

	@Override
	public String java_code() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTypeId()
	{
		return 4096;
	}
	
	@Override
	public String toString()
	{
		return "?" + getV() + "(" + getF() + ")";
	}
}
