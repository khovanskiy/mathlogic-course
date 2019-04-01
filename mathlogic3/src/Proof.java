import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class Proof
{
	public Vector<F> axioms;
	public Vector<F> hypothesis;
	private Vector<F> statements;
	
	private HashMap<String, Vector<F>> right;
	private HashMap<String, F> accepted;
	
	public F conclusion;
	
	public Proof()
	{
		axioms = new Vector<F>();
		hypothesis = new Vector<F>();
		statements = new Vector<F>();
		FormulaParser parser = new FormulaParser();
		addAxiom(parser.parse("A->(B->A)"));//1
		addAxiom(parser.parse("(A->B)->((A->B->C)->(A->C))"));//2
		addAxiom(parser.parse("A->(B->A&B)"));//3
		addAxiom(parser.parse("(A&B)->A"));//4
		addAxiom(parser.parse("(A&B)->B"));//5
		addAxiom(parser.parse("A->(A|B)"));//6
		addAxiom(parser.parse("B->(A|B)"));//7
		addAxiom(parser.parse("(A->C)->((B->C)->(A|B->C))"));//8
		addAxiom(parser.parse("(A->B)->((A->!B)->!A)"));//9
		addAxiom(parser.parse("!(!A)->A"));//10
	}
	
	public void compact()
	{
		HashSet<String> set = new HashSet<String>();
		Vector<F> temp = new Vector<F>();
		for (int i = 0; i < statements.size(); ++i)
		{
			if (!set.contains(statements.get(i).toString()))
			{
				set.add(statements.get(i).toString());
				temp.add(statements.get(i));
			}
		}
		statements = temp;
	}
	
	public void addAxiom(F axiom)
	{
		axioms.add(axiom);
	}
	
	public void mergeLeft(Proof other)
	{
		for (int i = 0; i < other.getStatements().size(); ++i)
		{
			addStatement(other.getStatements().get(i));
		}
	}
	
	public void addStatement(F statement)
	{
		this.statements.add(statement);
	}
	
	public void addHypothesis(F hypothesis)
	{
		this.hypothesis.add(hypothesis);
	}
	
	public void addImplToCache(F f)
	{
		accepted.put(f.toString(), f);
		if (f instanceof BinaryOperation)
		{
			BinaryOperation bi = (BinaryOperation)f;
			if (bi instanceof Implication)
			{
				String h = bi.getRight().toString();
				if (!right.containsKey(h))
				{
					right.put(h, new Vector<F>());
				}
				//Console.print("Put " + h + " " + bi);
				right.get(h).add(bi);
			}
		}
	}
	
	public void setConclusion(F conclusion)
	{
		this.conclusion = conclusion;
	}
	
	public F getConclusion()
	{
		return this.conclusion;
	}
	
	public Vector<F> getHypothesis()
	{
		return this.hypothesis;
	}
	
	public Vector<F> getStatements()
	{
		return this.statements;
	}
	
	public F getLastHyp()
	{
		return hypothesis.get(hypothesis.size() - 1);
	}
	
	public int compareWithAxiom(F f)
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
	
	public F findModusPonens(F current)
	{
		String h = current.toString();
		//Console.print("Right "+right);
		Vector<F> v = right.get(h);
		if (v != null)
		{
			for (int j = 0; j < v.size(); ++j)
			{
				F k = v.get(j);
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
	
	public int compareWithHypothesis(F f)
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
	
	public boolean isCorrect()
	{
		accepted = new HashMap<String, F>();
		right = new HashMap<String, Vector<F>>();
		
		for (int i = 0; i < hypothesis.size(); ++i)
		{
			addImplToCache(hypothesis.get(i));
		}
		
		for (int i = 0; i < statements.size(); ++i)
		{
			F current = statements.get(i);
			int hypId = compareWithHypothesis(current);
			if (hypId != -1)
			{
				//Console.print(current+" Гипотеза № "+ (hypId + 1));
			}
			else
			{
				int axiomId = compareWithAxiom(current);
				if (axiomId != -1)
				{
					//Console.print(current+" Аксиома № "+(axiomId + 1)+" : "+axioms.get(axiomId));
				}
				else
				{
					if (findModusPonens(current) != null)
					{
						//Console.print(current + " Modus Ponens");
					}
					else
					{
						Console.print("Неверное доказательство, начиная с "+(i+1)+" строчки "+current);
						return false;
					}
				}
				addImplToCache(statements.get(i));
			}
		}
		return true;
	}
	
	public boolean canDoDeductionStep()
	{
		return hypothesis.size() > 0 && isCorrect();
	}
	
	/*
	 * Сделать шаг дедукции
	 */
	public Proof deductionStep() throws Exception
	{
		
		/*if (!isCorrect())
		{
			Console.print(this);
			throw new Exception("not correct "+getConclusion()+" "+getHypothesis());
		}*/
		
		Proof proof = new Proof();
		F q = hypothesis.get(hypothesis.size() - 1);
		hypothesis.setSize(hypothesis.size() - 1);
		
		accepted = new HashMap<String, F>();
		right = new HashMap<String, Vector<F>>();
		for (int i = 0; i < hypothesis.size(); ++i)
		{
			addImplToCache(hypothesis.get(i));
		}
		
		//Console.print("|-"+Formula.getImplication(q, conclusion));
		for (int i = 0; i < statements.size(); ++i)
		{
			F current = statements.get(i);
			//Console.print("Current " + current);
			
			if (compareWithAxiom(current) != -1 || compareWithHypothesis(current) != -1)
			{
				//Console.print("Axiom or hypothesis");
				proof.addStatement(F.impl(current, F.impl(q, current)));
				proof.addStatement(current);
				proof.addStatement(F.impl(q, current));
			}
			else if (current.equals(q))
			{
				//Console.print("Equals with Q");
				proof.addStatement(F.impl(q,F.impl(F.impl(q, q),q)));
				proof.addStatement(F.impl(q,F.impl(q,q)));
				proof.addStatement(F.impl(F.impl(q,F.impl(q,q)),F.impl(F.impl(q,F.impl(F.impl(q,q),q)),F.impl(q,q))));
				proof.addStatement(F.impl(F.impl(q,F.impl(F.impl(q,q),q)),F.impl(q,q)));
				proof.addStatement(F.impl(q,q));
			}
			else
			{
				//Console.print("Else");
				F r = findModusPonens(current);
				
				proof.addStatement(F.impl(F.impl(q,r),F.impl(F.impl(q,F.impl(r,current)),F.impl(q,current))));
				proof.addStatement(F.impl(F.impl(q,F.impl(r,current)),F.impl(q,current)));
				proof.addStatement(F.impl(q,current));
			}
			addImplToCache(current);
		}
		
		proof.setConclusion(F.impl(q, this.conclusion));
		
		for (int i = 0; i < axioms.size(); ++i)
		{
			proof.addAxiom(axioms.get(i));
		}
		
		for (int i = 0; i < hypothesis.size(); ++i)
		{
			proof.addHypothesis(hypothesis.get(i));
		}
		
		hypothesis.add(q);
		return proof;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("---------\nProof for " + conclusion + "\nHypothesis:\n");
		for (int i = 0; i < hypothesis.size(); ++i)
		{
			sb.append(hypothesis.get(i) + "\n");
		}
		sb.append("Statements:\n");
		for (int i = 0; i < statements.size(); ++i)
		{
			if (statements.get(i) == null)
			{
				Console.print("------------------------------------------------------------------------------------------");
			}
			sb.append(statements.get(i) + "\n");
		}
		sb.append("---------\n");
		return sb.toString();
	}
}
