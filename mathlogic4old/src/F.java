import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;


public abstract class F
{	
	private static HashMap<String, F> hash = new HashMap<String, F>();
	
	public abstract boolean isContains(Subject v);
	
	public abstract boolean isFree(Subject v);
	
	public boolean equalsWithAxiom(F axiom)
	{
		return compareWithAxiom(this, axiom);
	}
	
	public abstract String java_code();
	
	public abstract int getTypeId();
	
	public static BinaryOperation impl(F left, F right)
	{
		String h = "(" + left + " -> " + right + ")";
		if (hash.containsKey(h))
		{
			return (BinaryOperation) hash.get(h);
		}
		else
		{
			BinaryOperation b = new Implication(left, right);
			hash.put(h, b);
			return b;
		}
	}
	
	public static BinaryOperation dis(F left, F right)
	{
		String h = "(" + left + " | " + right + ")";
		if (hash.containsKey(h))
		{
			return (BinaryOperation) hash.get(h);
		}
		else
		{
			BinaryOperation b = new Disjunction(left, right);
			hash.put(h, b);
			return b;
		}
	}
	
	public static BinaryOperation con(F left, F right)
	{
		String h = "(" + left + " & " + right + ")";
		if (hash.containsKey(h))
		{
			return (BinaryOperation) hash.get(h);
		}
		else
		{
			BinaryOperation b = new Conjunction(left, right);
			hash.put(h, b);
			return b;
		}
	}
	
	public static UnaryOperation neg(F subject)
	{
		String h = "(!" + subject + ")";
		if (hash.containsKey(h))
		{
			return (UnaryOperation) hash.get(h);
		}
		else
		{
			UnaryOperation b = new Negation(subject);
			hash.put(h, b);
			return b;
		}
	}
	
	public static Variable var(String label)
	{
		if (hash.containsKey(label))
		{
			return (Variable) hash.get(label);
		}
		else
		{
			Variable b = new Variable(label);
			hash.put(label, b);
			return b;
		}
	}
	
	public Vector<Variable> getVariables()
	{
		Vector<Variable> result = new Vector<Variable>();
		HashSet<String> table = new HashSet<String>();
		Queue<F> q = new LinkedList<F>();
		q.add(this);
		while (q.size() > 0)
		{
			F c = q.poll();
			if (c instanceof BinaryOperation)
			{
				BinaryOperation bi = (BinaryOperation)c;
				q.add(bi.getLeft());
				q.add(bi.getRight());
			}
			else if (c instanceof UnaryOperation)
			{
				UnaryOperation un = (UnaryOperation)c;
				q.add(un.subject);
			}
			else if (c instanceof Variable)
			{
				Variable v = (Variable)c;
				if (!table.contains(v.getLabel()))
				{
					result.add(v);
					table.add(v.getLabel());
				}
			}
		}
		Collections.sort(result, new Comparator<Variable>()
		{

			@Override
			public int compare(Variable o1, Variable o2)
			{
				return o1.getLabel().compareTo(o2.getLabel());
			}
			
		});
		return result;
	}
	
	public static boolean compareWithAxiom(F formula, F axiom)
	{
		HashMap<String, F> table = new HashMap<String, F>();
		Queue<F> one = new LinkedList<F>();
		Queue<F> two = new LinkedList<F>();
		
		one.add(formula);
		two.add(axiom);
		
		while (one.size() > 0)
		{
			F left = one.poll();
			F right = two.poll();
			//Console.print("Compare "+left+ " vs "+right);
			
			if (right instanceof Variable)
			{
				Variable rv = (Variable) right;
				//Console.print("Right = "+right+" is variable...");
				if (table.containsKey(rv.getLabel()))
				{
					//Console.print("...and I have ALREADY seen it");
					F cashed = table.get(rv.getLabel());
					if (!left.equals(cashed))
					{
						return false;
					}
				}
				else
				{
					//Console.print("...and I have NEVER seen it");
					table.put(rv.getLabel(), left);
				}
			}
			else
			{
				if (left instanceof BinaryOperation && right instanceof BinaryOperation)
				{
					//Console.print("Both of them are binary");
					BinaryOperation ba = (BinaryOperation)left;
					BinaryOperation bb = (BinaryOperation)right;
					//Console.print("To ONE put "+ba.getLeft()+" "+ba.getRight());
					//Console.print("To TWO put "+bb.getLeft()+" "+bb.getRight());
					one.add(ba.getLeft());
					one.add(ba.getRight());
					two.add(bb.getLeft());
					two.add(bb.getRight());
				}
				else if (left instanceof UnaryOperation && right instanceof UnaryOperation)
				{
					//Console.print("Both of them are unary");
					UnaryOperation ba = (UnaryOperation)left;
					UnaryOperation bb = (UnaryOperation)right;
					one.add(ba.subject);
					two.add(bb.subject);
				}
				else
				{
					return false;
				}
				//Console.print(left+" and "+right+" are same at the top");
			}
		}
		return true;
	}

}
