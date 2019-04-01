import java.util.HashMap;
import java.util.Vector;


public class Predicate extends F 
{
	public Vector<Term> subjects;
	
	public Predicate(Vector<Term> subjects)
	{
		this.subjects = subjects;
	}

	@Override
	public String java_code() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTypeId() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean isContains(Subject v)
	{
		for (int i = 0; i < subjects.size(); ++i)
		{
			if (subjects.get(i).isContains(v))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isFree(Subject v)
	{
		return isContains(v);
	}

}
