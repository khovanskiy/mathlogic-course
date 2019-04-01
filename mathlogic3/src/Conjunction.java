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
	public boolean getVerity(HashMap<String, Boolean> vars) throws Exception
	{
		return left.getVerity(vars) && right.getVerity(vars);
	}

	@Override
	public int getTypeId()
	{
		return 256;
	}

	@Override
	public Proof getProof(HashMap<String, Boolean> vars) throws Exception
	{
		Proof current = left.getProof(vars);
		current.getStatements().addAll(right.getProof(vars).getStatements());
		boolean lv = left.getVerity(vars);
		boolean rv = right.getVerity(vars);
		if (!lv && !rv)
		{
			current.addStatement(F.neg(left));
			current.addStatement(F.neg(right));
			current.addStatement(F.impl(F.con(left, right), left));
			current.addStatement(F.impl(F.neg(left), F.impl(F.con(left, right), F.neg(left))));
			current.addStatement(F.impl(F.con(left, right), F.neg(left)));
			current.addStatement(F.impl(F.impl(F.con(left, right), left), F.impl(F.impl(F.con(left, right), F.neg(left)), F.neg(F.con(left, right)))));
			current.addStatement(F.impl(F.impl(F.con(left, right), F.neg(left)), F.neg(F.con(left, right))));
			current.addStatement(F.neg(F.con(left, right)));
		}
		else if (!lv && rv)
		{
			current.addStatement(F.neg(left));
			current.addStatement(right);
			current.addStatement(F.impl(F.con(left, right), left));
			current.addStatement(F.impl(F.neg(left), F.impl(F.con(left, right), F.neg(left))));
			current.addStatement(F.impl(F.con(left, right), F.neg(left)));
			current.addStatement(F.impl(F.impl(F.con(left, right), left), F.impl(F.impl(F.con(left, right), F.neg(left)), F.neg(F.con(left, right)))));
			current.addStatement(F.impl(F.impl(F.con(left, right), F.neg(left)), F.neg(F.con(left, right))));
			current.addStatement(F.neg(F.con(left, right)));
		}
		else if (lv && !rv)
		{
			current.addStatement(left);
			current.addStatement(F.neg(right));
			current.addStatement(F.impl(F.con(left, right), right));
			current.addStatement(F.impl(F.neg(right), F.impl(F.con(left, right), F.neg(right))));
			current.addStatement(F.impl(F.con(left, right), F.neg(right)));
			current.addStatement(F.impl(F.impl(F.con(left, right), right), F.impl(F.impl(F.con(left, right), F.neg(right)), F.neg(F.con(left, right)))));
			current.addStatement(F.impl(F.impl(F.con(left, right), F.neg(right)), F.neg(F.con(left, right))));
			current.addStatement(F.neg(F.con(left, right)));
		}
		else if (lv && rv)
		{
			current.addStatement(left);
			current.addStatement(right);
			current.addStatement(F.impl(left, F.impl(right, F.con(left, right))));
			current.addStatement(F.impl(right, F.con(left, right)));
			current.addStatement(F.con(left, right));
		}
		return current;
	}
	
	@Override
	public String java_code()
	{
		return "F.con(" + left.java_code() + ", " + right.java_code() + ")";
	}
}
