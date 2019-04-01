import java.util.HashMap;
import java.util.Vector;

public class Proof
{
	public Vector<Formula> axioms;
	public Vector<Formula> hypothesis;
	private Vector<Formula> statements;
	
	private HashMap<String, Vector<Formula>> right;
	private HashMap<String, Formula> accepted;
	
	public Formula conclusion;
	
	public Proof()
	{
		axioms = new Vector<Formula>();
		hypothesis = new Vector<Formula>();
		statements = new Vector<Formula>();
	}
	
	public void addAxiom(Formula axiom)
	{
		axioms.add(axiom);
	}
	
	public void addStatement(Formula statement)
	{
		this.statements.add(statement);
	}
	
	public void addHypothesis(Formula hypothesis)
	{
		this.hypothesis.add(hypothesis);
	}
	
	public void addImplToCache(Formula f)
	{
		accepted.put(f.toString(), f);
		if (f instanceof BinaryOperation)
		{
			BinaryOperation bi = (BinaryOperation)f;
			if (bi.type == OperationType.IMPLICATION)
			{
				String h = bi.getRight().toString();
				if (!right.containsKey(h))
				{
					right.put(h, new Vector<Formula>());
				}
				//Console.print("Put " + h + " " + bi);
				right.get(h).add(bi);
			}
		}
	}
	
	public void setConclusion(Formula conclusion)
	{
		this.conclusion = conclusion;
	}
	
	public Formula getConclusion()
	{
		return this.conclusion;
	}
	
	public Vector<Formula> getHypothesis()
	{
		return this.hypothesis;
	}
	
	public Vector<Formula> getStatements()
	{
		return this.statements;
	}
	
	public int compareWithAxiom(Formula f)
	{
		for (int i = 0; i < axioms.size(); ++i)
		{
			if (f.equalsWithAxiom(axioms.get(i)))
			{
				return i;
			}
		}
		return -1;
	}
	
	public Formula findModusPonens(Formula current)
	{
		Vector<Formula> v = right.get(current.toString());
		if (v != null)
		{
			for (int j = 0; j < v.size(); ++j)
			{
				Formula k = v.get(j);
				BinaryOperation bi = (BinaryOperation)k;
				String key = bi.getLeft().toString();
				if (accepted.containsKey(key))
				{
					return accepted.get(key);
				}
			}
		}
		return null;
	}
	
	public int compareWithHypothesis(Formula f)
	{
		for (int i = 0; i < hypothesis.size(); ++i)
		{
			if (f.equals(hypothesis.get(i)))
			{
				return i;
			}
		}
		return -1;
	}
	
	public Pair<Boolean, Integer> isCorrect()
	{
		accepted = new HashMap<String, Formula>();
		right = new HashMap<String, Vector<Formula>>();
		
		for (int i = 0; i < hypothesis.size(); ++i)
		{
			//Console.print(hypothesis.get(i));
			addImplToCache(hypothesis.get(i));
		}
		
		for (int i = 0; i < statements.size(); ++i)
		{
			Formula current = statements.get(i);
			//Console.print("Current "+current);
			
			int hypId = compareWithHypothesis(current);
			if (hypId != -1)
			{
				Console.print(current+" Гипотеза № "+ (hypId + 1));
			}
			else
			{
				int axiomId = compareWithAxiom(current);
				if (axiomId != -1)
				{
					Console.print((i+1)+" " +current+" Аксиома № "+(axiomId + 1)+" : "+axioms.get(axiomId));
				}
				else
				{
					if (findModusPonens(current) != null)
					{
						Console.print((i+1)+" "+current + " Modus Ponens");
					}
					else
					{
						return new Pair(false, i);
					}
				}
				addImplToCache(statements.get(i));
				
			}
		}
		return new Pair(true, 0);
	}
}
