import java.util.HashMap;
import java.util.Vector;


public class Variable extends F
{
	private String label;
	
	public Variable(String label)
	{
		this.label = label;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	@Override
	public String toString()
	{
		return label;
	}

	@Override
	public boolean equals(Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (other instanceof Variable)
		{
			return this.label.equals(((Variable)other).getLabel());
		}
		return false;
	}

	@Override
	public boolean getVerity(HashMap<String, Boolean> vars) throws Exception
	{
		for (int i = 0; i < vars.size(); ++i)
		{
			if (vars.containsKey(label))
			{
				return vars.get(label);
			}
		}
		throw new Exception("Unknown variable`s value");
	}

	@Override
	public int getTypeId()
	{
		return 1024;
	}

	@Override
	public Proof getProof(HashMap<String, Boolean> vars) throws Exception
	{
		Proof p = new Proof();
		if (getVerity(vars))
		{
			p.setConclusion(this);
			p.addStatement(this);
		}
		else
		{
			p.setConclusion(F.neg(this));
			p.addStatement(F.neg(this));
		}
		return p;
	}

	@Override
	public String java_code()
	{
		if (label.equals("A"))
		{
			return "subject";
		}
		else
		{
			return "right";
		}
	}
}
