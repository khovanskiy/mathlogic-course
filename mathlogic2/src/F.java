import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


public abstract class F
{	
	private static HashMap<String, F> hash = new HashMap<String, F>();
	
	public boolean equalsWithAxiom(F axiom)
	{
		return compareWithAxiom(this, axiom);
	}
	
	public static BinaryOperation impl(F left, F right)
	{
		String h = "(" + left + " -> " + right + ")";
		if (hash.containsKey(h))
		{
			return (BinaryOperation) hash.get(h);
		}
		else
		{
			BinaryOperation b = new BinaryOperation(OperationType.IMPLICATION, left, right);
			hash.put(h, b);
			return b;
		}
	}
	
	public static BinaryOperation getDisjunction(F left, F right)
	{
		String h = "(" + left + " | " + right + ")";
		if (hash.containsKey(h))
		{
			return (BinaryOperation) hash.get(h);
		}
		else
		{
			BinaryOperation b = new BinaryOperation(OperationType.DISJUNCTION, left, right);
			hash.put(h, b);
			return b;
		}
	}
	
	public static BinaryOperation getConjunction(F left, F right)
	{
		String h = "(" + left + " & " + right + ")";
		if (hash.containsKey(h))
		{
			return (BinaryOperation) hash.get(h);
		}
		else
		{
			BinaryOperation b = new BinaryOperation(OperationType.CONJUNCTION, left, right);
			hash.put(h, b);
			return b;
		}
	}
	
	public static UnaryOperation getNegation(F subject)
	{
		String h = "(!" + subject + ")";
		if (hash.containsKey(h))
		{
			return (UnaryOperation) hash.get(h);
		}
		else
		{
			UnaryOperation b = new UnaryOperation(OperationType.NEGATION, subject);
			hash.put(h, b);
			return b;
		}
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
					if (ba.type != bb.type)
					{
						return false;
					}
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
					if (ba.type != bb.type)
					{
						return false;
					}
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
