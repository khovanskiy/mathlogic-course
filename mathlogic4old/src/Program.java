/*
 * Хованский Виктор, 2539
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class Program
{
	static BufferedReader in;
	static BufferedWriter out;
	
	public static void main(String[] args)
	{
		try
		{
			in = new BufferedReader(new FileReader("a.in"));
			out = new BufferedWriter(new FileWriter("a.out"));
			
			FormulaParser parser = new FormulaParser();
			Console.print(parser.parse("@x@y(P(x,y,z)"));
			
			in.close();
			out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
