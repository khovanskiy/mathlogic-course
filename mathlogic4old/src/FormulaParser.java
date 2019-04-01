import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;


public class FormulaParser implements IFormulaParser
{	
	private Vector<LexemeType> states;
	
	public FormulaParser()
	{
		states = new Vector<LexemeType>();
		states.add(LexemeType.DOT);
		states.add(LexemeType.IMPLICATION);
		states.add(LexemeType.DISJUNCTION);
		states.add(LexemeType.CONJUNCTION);
		states.add(LexemeType.NEGATION);
		states.add(LexemeType.ANY);
		states.add(LexemeType.EXISTS);
	}
	
	public Vector<Lexeme> split(String expression)
	{
		Vector<Lexeme> result = new Vector<Lexeme>();
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
					if (buffer.toString().toUpperCase().equals(buffer.toString()))
					{
						result.add(new Lexeme(buffer.toString(), LexemeType.PREDICATE));
					}
					else
					{
						result.add(new Lexeme(buffer.toString()));
					}
					buffer.setLength(0);
				}
				result.add(new Lexeme("(", LexemeType.OPEN_BRACKET));
			}
			else if (c == ')')
			{
				if (buffer.toString().length() > 0)
				{
					result.add(new Lexeme(buffer.toString()));
					buffer.setLength(0);
				}
				result.add(new Lexeme(")", LexemeType.CLOSE_BRACKET));
			}
			else 
			{
				boolean shouldSplit = false;
				LexemeType type = LexemeType.EMPTY;
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
						result.add(new Lexeme(buffer.toString()));
						buffer.setLength(0);
					}
					
					result.add(new Lexeme(type));
					
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
			result.add(new Lexeme(buffer.toString()));
		}
		
		boolean isPredicate = false;
		int depth = 0;
		for (int i = 0; i < result.size(); ++i)
		{
			Lexeme current = result.get(i);
			if (!isPredicate)
			{
				if (current.type == LexemeType.EMPTY)
				{
					current.type = LexemeType.VARIABLE;
				}
				else if (current.type == LexemeType.PREDICATE)
				{
					isPredicate = true;
					++depth;
					++i;
				}
				else if (current.type == LexemeType.ANY || current.type == LexemeType.EXISTS)
				{
					current = result.get(++i);
					if (current.type == LexemeType.EMPTY)
					{
						current.type = LexemeType.SUBJECT;
					}
				}
			}
			else
			{
				if (current.type == LexemeType.OPEN_BRACKET)
				{
					++depth;
				}
				else if (current.type == LexemeType.CLOSE_BRACKET)
				{
					--depth;
					if (depth == 0)
					{
						isPredicate = false;
					}
				}
				else if (current.type == LexemeType.EMPTY)
				{
					if (result.get(i+1).type == LexemeType.OPEN_BRACKET)
					{
						current.type = LexemeType.FUNCTION;
					}
					else
					{
						current.type = LexemeType.SUBJECT;
					}
				}
			}
		}
		return result;
	}
	
	public F parse(String expression)
	{
		Vector<Lexeme> root = split(expression);
		Console.print(root);
		Vector<Lexeme> opz = opz(root);
		Console.print(opz);
		//F result = buildTree(opz);
		return null;
	}
	
	private F buildTree(Vector<Lexeme> opz)
	{
		Stack<F> stack = new Stack<F>();
		for (int i = 0; i < opz.size(); ++i)
		{
			Lexeme current = opz.get(i);
			if (current.type != LexemeType.EMPTY)
			{
				F list = null;
				switch (current.type)
				{
					case IMPLICATION:
					{
						F right = stack.pop();
						F left = stack.pop();
						list = F.impl(left, right);
					} break;
					case DISJUNCTION:
					{
						F right = stack.pop();
						F left = stack.pop();
						list = F.dis(left, right);
					} break;
					case CONJUNCTION:
					{
						F right = stack.pop();
						F left = stack.pop();
						list = F.con(left, right);
					} break;
					case NEGATION:
					{
						F subject = stack.pop();
						list = F.neg(subject);
					} break;
					case ANY:
					{
						F subject = stack.pop();
						Variable var = (Variable)stack.pop();
						list = new Any(var, subject);
					} break;
					case EXISTS:
					{
						F subject = stack.pop();
						Variable var = (Variable)stack.pop();
						list = new Exists(var, subject);
					} break;
				}
				stack.add(list);
			}
			else if (current.type == LexemeType.PREDICATE)
			{
				
			}
			else
			{
				stack.add(F.var(current.label));
			}
		}
		return stack.peek();
	}
	
	public Vector<Lexeme> opz(Vector<Lexeme> input)
	{
		Vector<Lexeme> output = new Vector<Lexeme>();
		Stack<Lexeme> stack = new Stack<Lexeme>();
		
		for (int i = 0; i < input.size(); ++i)
		{
			Lexeme current = input.get(i);
			
			switch (current.type)
			{
				case OPEN_BRACKET:
				{
					
				} break;
				case CLOSE_BRACKET:
				{
					
				} break;
				default:
				{
					
				} break;
			}
		}
		/*for (int i = 0; i < input.size(); ++i)
		{
			Lexeme current = input.get(i);
			if (current.type != LexemeType.EMPTY)
			{
				while (	stack.size() > 0 && stack.peek().type != LexemeType.EMPTY &&
					(	stack.peek().type.isRightAssociative() && current.type.getPriority() < stack.peek().type.getPriority() ||
						!stack.peek().type.isRightAssociative() && current.type.getPriority() <= stack.peek().type.getPriority()
				))
				{
					output.add(stack.pop());
				}
				if (stack.size() > 0 && (stack.peek().type == LexemeType.PREDICATE))
				{
					output.add(stack.pop());
				}
				//else
				{
					stack.add(current);
				}
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
		}*/
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
