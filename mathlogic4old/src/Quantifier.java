
public abstract class Quantifier extends Operation
{
	private Variable v;
	private F f;
	
	public Quantifier(Variable v, F f)
	{
		this.v = v;
		this.f = f;
	}
	
	public Variable getV()
	{
		return v;
	}
	
	public F getF()
	{
		return f;
	}
	
	@Override
	public boolean isContains(Subject v)
	{
		if (v.getLabel().equals(v.getLabel()))
		{
			return true;
		}
		return getF().isContains(v);
	}

	@Override
	public boolean isFree(Subject v)
	{
		if (v.getLabel().equals(v.getLabel()))
		{
			return true;
		}
		return getF().isFree(v);
	}
}
