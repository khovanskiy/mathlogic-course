import java.util.Vector;


public class Function extends Term
{
	public Vector<Term> terms;
	
	public Function(Vector<Term> terms)
	{
		this.terms = terms;
	}

	@Override
	public boolean isContains(Subject v)
	{
		for (int i = 0; i < terms.size(); ++i)
		{
			if (terms.get(i).isContains(v))
			{
				return true;
			}
		}
		return false;
	}
}
