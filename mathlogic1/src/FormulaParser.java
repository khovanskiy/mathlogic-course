import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;


public class FormulaParser implements IFormulaParser
{	
	private Vector<OperationType> states;
	
	public FormulaParser()
	{
		states = new Vector<OperationType>();
		states.add(OperationType.IMPLICATION);
		states.add(OperationType.DISJUNCTION);
		states.add(OperationType.CONJUNCTION);
		states.add(OperationType.NEGATION);
	}
	
	public Vector<Token> split(String expression)
	{
		Vector<Token> result = new Vector<Token>();
		StringBuilder buffer = new StringBuilder();
		
		for (int i = 0; i < expression.length(); ++i)
		{
			char c = expression.charAt(i);
			if (c == ' ')
			{
				continue;
			}
			else if (c == '(')
			{
				if (buffer.toString().length() > 0)
				{
					result.add(new Token(buffer.toString()));
					buffer.setLength(0);
				}
				result.add(new Token("("));
			}
			else if (c == ')')
			{
				if (buffer.toString().length() > 0)
				{
					result.add(new Token(buffer.toString()));
					buffer.setLength(0);
				}
				result.add(new Token(")"));
			}
			else 
			{
				boolean shouldSplit = false;
				OperationType type = OperationType.EMPTY;
				for (int j = 0; j < states.size(); ++j)
				{
					if (check(expression, states.get(j).getDelimeter(), i))
					{
						type = states.get(j);
						shouldSplit = true;
						break;
					}
				}
				if (shouldSplit)
				{
					if (buffer.toString().length() > 0)
					{
						result.add(new Token(buffer.toString()));
						buffer.setLength(0);
					}
					
					result.add(new Token(type));
					
					i += type.getDelimeter().length() - 1;
				}
				else
				{
					buffer.append(c);
				}
			}
		}
		if (buffer.toString().length() > 0)
		{
			result.add(new Token(buffer.toString()));
		}
		return result;
	}
	
	public Formula parse(String expression)
	{
		Vector<Token> root = split(expression);
		Vector<Token> opz = opz(root);
		Formula result = buildTree(opz);
		return result;
	}
	
	private Formula buildTree(Vector<Token> opz)
	{
		Stack<Formula> stack = new Stack<Formula>();
		for (int i = 0; i < opz.size(); ++i)
		{
			Token current = opz.get(i);
			if (current.type != OperationType.EMPTY)
			{
				Formula list = null;
				switch (current.type)
				{
					case IMPLICATION:
					{
						Formula right = stack.pop();
						Formula left = stack.pop();
						list = Formula.getImplication(left, right);
					} break;
					case DISJUNCTION:
					{
						Formula right = stack.pop();
						Formula left = stack.pop();
						list = Formula.getDisjunction(left, right);
					} break;
					case CONJUNCTION:
					{
						Formula right = stack.pop();
						Formula left = stack.pop();
						list = Formula.getConjunction(left, right);
					} break;
					case NEGATION:
					{
						Formula subject = stack.pop();
						list = Formula.getNegation(subject);
					} break;
				}
				stack.add(list);
			}
			else
			{
				stack.add(new Variable(current.label));
			}
		}
		return stack.peek();
	}
	
	public Vector<Token> opz(Vector<Token> input)
	{
		Vector<Token> output = new Vector<Token>();
		Stack<Token> stack = new Stack<Token>();
		
		for (int i = 0; i < input.size(); ++i)
		{
			Token current = input.get(i);
			if (current.type != OperationType.EMPTY)
			{
				while (	stack.size() > 0 && stack.peek().type != OperationType.EMPTY &&
					(	stack.peek().type.isRightAssociative() && current.type.getPriority() < stack.peek().type.getPriority() ||
						!stack.peek().type.isRightAssociative() && current.type.getPriority() <= stack.peek().type.getPriority()
				))
				{
					output.add(stack.pop());
				}
				stack.add(current);
			}
			else
			{
				if (current.label.equals("("))
				{
					stack.add(current);
				}
				else if (current.label.equals(")"))
				{
					while (!stack.peek().label.equals("("))
					{
						output.add(stack.pop());
					}
					if (stack.peek().label.equals("("))
					{
						stack.pop();
					}
				}
				else
				{
					output.add(current);
				}
			}
		}
		while (stack.size() > 0)
		{
			output.add(stack.pop());
		}
		return output;
	}
	
	private boolean check(String a, String b, int j)
	{
		for (int i = 0; i < b.length(); ++i)
		{
			if (a.charAt(j) != b.charAt(i))
			{
				return false;
			}
			else
			{
				++j;
				if (a.length() == j)
				{
					return false;
				}
			}
		}
		return true;
	}
}
