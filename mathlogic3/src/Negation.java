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
	public boolean getVerity(HashMap<String, Boolean> vars) throws Exception
	{
		return !subject.getVerity(vars);
	}

	@Override
	public int getTypeId()
	{
		return 512;
	}

	@Override
	public Proof getProof(HashMap<String, Boolean> vars) throws Exception
	{
		Proof current = subject.getProof(vars);
		boolean cv = subject.getVerity(vars);
		if (!cv)
		{
			current.setConclusion(F.neg(subject));
			current.addStatement(F.neg(subject));
		}
		else
		{
			current.addStatement(subject);
			current.addStatement(F.impl(F.neg(subject), F.impl(F.impl(F.neg(subject), F.neg(subject)), F.neg(subject))));
			current.addStatement(F.impl(F.neg(subject), F.impl(F.neg(subject), F.neg(subject))));
			current.addStatement(F.impl(F.impl(F.neg(subject), F.impl(F.neg(subject), F.neg(subject))), F.impl(F.impl(F.neg(subject), F.impl(F.impl(F.neg(subject), F.neg(subject)), F.neg(subject))), F.impl(F.neg(subject), F.neg(subject)))));
			current.addStatement(F.impl(F.impl(F.neg(subject), F.impl(F.impl(F.neg(subject), F.neg(subject)), F.neg(subject))), F.impl(F.neg(subject), F.neg(subject))));
			current.addStatement(F.impl(F.neg(subject), F.neg(subject)));
			current.addStatement(F.impl(subject, F.impl(F.neg(subject), subject)));
			current.addStatement(F.impl(F.neg(subject), subject));
			current.addStatement(F.impl(F.impl(F.neg(subject), subject), F.impl(F.impl(F.neg(subject), F.neg(subject)), F.neg(F.neg(subject)))));
			current.addStatement(F.impl(F.impl(F.neg(subject), F.neg(subject)), F.neg(F.neg(subject))));
			current.addStatement(F.neg(F.neg(subject)));
		}
		return current;
	}

	@Override
	public String java_code()
	{
		return "F.neg(" + subject.java_code() + ")";
	}
}
