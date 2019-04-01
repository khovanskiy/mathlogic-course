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
	public boolean getVerity(HashMap<String, Boolean> vars) throws Exception
	{
		return left.getVerity(vars) || right.getVerity(vars);
	}

	@Override
	public int getTypeId()
	{
		return 128;
	}

	@Override
	public Proof getProof(HashMap<String, Boolean> vars) throws Exception 
	{
		Proof lp = left.getProof(vars);
		Proof rp = right.getProof(vars);
		Proof current = lp;
		current.getStatements().addAll(rp.getStatements());
		
		boolean lv = left.getVerity(vars);
		boolean rv = right.getVerity(vars);
		
		if (!lv && !rv)
		{
			current.addStatement(F.neg(left));
			current.addStatement(F.neg(right));
			current.addStatement(F.impl(F.impl(F.neg(left), F.impl(F.neg(right), F.neg(left))), F.impl(left, F.impl(F.neg(left), F.impl(F.neg(right), F.neg(left))))));
			current.addStatement(F.impl(F.neg(left), F.impl(F.neg(right), F.neg(left))));
			current.addStatement(F.impl(left, F.impl(F.neg(left), F.impl(F.neg(right), F.neg(left)))));
			current.addStatement(F.impl(F.neg(left), F.impl(left, F.neg(left))));
			current.addStatement(F.impl(left, F.neg(left)));
			current.addStatement(F.impl(F.impl(left, F.neg(left)), F.impl(F.impl(left, F.impl(F.neg(left), F.impl(F.neg(right), F.neg(left)))), F.impl(left, F.impl(F.neg(right), F.neg(left))))));
			current.addStatement(F.impl(F.impl(left, F.impl(F.neg(left), F.impl(F.neg(right), F.neg(left)))), F.impl(left, F.impl(F.neg(right), F.neg(left)))));
			current.addStatement(F.impl(left, F.impl(F.neg(right), F.neg(left))));
			current.addStatement(F.impl(F.impl(left, F.impl(F.neg(right), left)), F.impl(left, F.impl(left, F.impl(F.neg(right), left)))));
			current.addStatement(F.impl(left, F.impl(F.neg(right), left)));
			current.addStatement(F.impl(left, F.impl(left, F.impl(F.neg(right), left))));
			current.addStatement(F.impl(left, F.impl(F.impl(left, left), left)));
			current.addStatement(F.impl(left, F.impl(left, left)));
			current.addStatement(F.impl(F.impl(left, F.impl(left, left)), F.impl(F.impl(left, F.impl(F.impl(left, left), left)), F.impl(left, left))));
			current.addStatement(F.impl(F.impl(left, F.impl(F.impl(left, left), left)), F.impl(left, left)));
			current.addStatement(F.impl(left, left));
			current.addStatement(F.impl(F.impl(left, left), F.impl(F.impl(left, F.impl(left, F.impl(F.neg(right), left))), F.impl(left, F.impl(F.neg(right), left)))));
			current.addStatement(F.impl(F.impl(left, F.impl(left, F.impl(F.neg(right), left))), F.impl(left, F.impl(F.neg(right), left))));
			current.addStatement(F.impl(F.impl(F.impl(F.neg(right), left), F.impl(F.impl(F.neg(right), F.neg(left)), F.neg(F.neg(right)))), F.impl(left, F.impl(F.impl(F.neg(right), left), F.impl(F.impl(F.neg(right), F.neg(left)), F.neg(F.neg(right)))))));
			current.addStatement(F.impl(F.impl(F.neg(right), left), F.impl(F.impl(F.neg(right), F.neg(left)), F.neg(F.neg(right)))));
			current.addStatement(F.impl(left, F.impl(F.impl(F.neg(right), left), F.impl(F.impl(F.neg(right), F.neg(left)), F.neg(F.neg(right))))));
			current.addStatement(F.impl(F.impl(left, F.impl(F.neg(right), left)), F.impl(F.impl(left, F.impl(F.impl(F.neg(right), left), F.impl(F.impl(F.neg(right), F.neg(left)), F.neg(F.neg(right))))), F.impl(left, F.impl(F.impl(F.neg(right), F.neg(left)), F.neg(F.neg(right)))))));
			current.addStatement(F.impl(F.impl(left, F.impl(F.impl(F.neg(right), left), F.impl(F.impl(F.neg(right), F.neg(left)), F.neg(F.neg(right))))), F.impl(left, F.impl(F.impl(F.neg(right), F.neg(left)), F.neg(F.neg(right))))));
			current.addStatement(F.impl(left, F.impl(F.impl(F.neg(right), F.neg(left)), F.neg(F.neg(right)))));
			current.addStatement(F.impl(F.impl(left, F.impl(F.neg(right), F.neg(left))), F.impl(F.impl(left, F.impl(F.impl(F.neg(right), F.neg(left)), F.neg(F.neg(right)))), F.impl(left, F.neg(F.neg(right))))));
			current.addStatement(F.impl(F.impl(left, F.impl(F.impl(F.neg(right), F.neg(left)), F.neg(F.neg(right)))), F.impl(left, F.neg(F.neg(right)))));
			current.addStatement(F.impl(left, F.neg(F.neg(right))));
			current.addStatement(F.impl(F.impl(F.neg(F.neg(right)), right), F.impl(left, F.impl(F.neg(F.neg(right)), right))));
			current.addStatement(F.impl(F.neg(F.neg(right)), right));
			current.addStatement(F.impl(left, F.impl(F.neg(F.neg(right)), right)));
			current.addStatement(F.impl(F.impl(left, F.neg(F.neg(right))), F.impl(F.impl(left, F.impl(F.neg(F.neg(right)), right)), F.impl(left, right))));
			current.addStatement(F.impl(F.impl(left, F.impl(F.neg(F.neg(right)), right)), F.impl(left, right)));
			current.addStatement(F.impl(left, right));
			current.addStatement(F.impl(right, F.impl(F.impl(right, right), right)));
			current.addStatement(F.impl(right, F.impl(right, right)));
			current.addStatement(F.impl(F.impl(right, F.impl(right, right)), F.impl(F.impl(right, F.impl(F.impl(right, right), right)), F.impl(right, right))));
			current.addStatement(F.impl(F.impl(right, F.impl(F.impl(right, right), right)), F.impl(right, right)));
			current.addStatement(F.impl(right, right));
			current.addStatement(F.impl(F.impl(left, right), F.impl(F.impl(right, right), F.impl(F.dis(left, right), right))));
			current.addStatement(F.impl(F.impl(right, right), F.impl(F.dis(left, right), right)));
			current.addStatement(F.impl(F.dis(left, right), right));
			current.addStatement(F.impl(F.neg(right), F.impl(F.dis(left, right), F.neg(right))));
			current.addStatement(F.impl(F.dis(left, right), F.neg(right)));
			current.addStatement(F.impl(F.impl(F.dis(left, right), right), F.impl(F.impl(F.dis(left, right), F.neg(right)), F.neg(F.dis(left, right)))));
			current.addStatement(F.impl(F.impl(F.dis(left, right), F.neg(right)), F.neg(F.dis(left, right))));
			current.addStatement(F.neg(F.dis(left, right)));
		}
		else if (!lv && rv)
		{
			current.addStatement(F.neg(left));
			current.addStatement(right);
			current.addStatement(F.impl(right, F.dis(left, right)));
			current.addStatement(F.dis(left, right));
		}
		else if (lv && !rv)
		{
			current.addStatement(left);
			current.addStatement(F.neg(right));
			current.addStatement(F.impl(left, F.dis(left, right)));
			current.addStatement(F.dis(left, right));
		}
		else if (lv && rv)
		{
			current.addStatement(left);
			current.addStatement(right);
			current.addStatement(F.impl(left, F.dis(left, right)));
			current.addStatement(F.dis(left, right));
		}
		return current;
	}
	
	@Override
	public String java_code()
	{
		return "F.dis(" + left.java_code() + ", " + right.java_code() + ")";
	}
}
