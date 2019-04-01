
public class Lexeme
{
	public String label = "";
	public LexemeType type;
	
	public Lexeme(LexemeType type)
	{
		this.type = type;
	}
	
	public Lexeme(String label)
	{
		this.label = label;
		this.type = LexemeType.EMPTY;
	}
	
	public Lexeme(String label, LexemeType type)
	{
		this.label = label;
		this.type = type;
	}
	
	@Override
	public String toString()
	{
		return label+" "+type;
	}
}
