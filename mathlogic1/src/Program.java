/*
 * Хованский Виктор, гр. 2539
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Vector;

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
			
			Proof proof = new Proof();
			proof.addAxiom(parser.parse("A->(B->A)"));//1
			proof.addAxiom(parser.parse("(A->B)->((A->B->C)->(A->C))"));//2
			proof.addAxiom(parser.parse("A->(B->A&B)"));//3
			proof.addAxiom(parser.parse("(A&B)->A"));//4
			proof.addAxiom(parser.parse("(A&B)->B"));//5
			proof.addAxiom(parser.parse("A->(A|B)"));//6
			proof.addAxiom(parser.parse("B->(A|B)"));//7
			proof.addAxiom(parser.parse("(A->C)->((B->C)->(A|B->C))"));//8
			proof.addAxiom(parser.parse("(A->B)->((A->!B)->!A)"));//9
			proof.addAxiom(parser.parse("!(!A)->A"));//10
			
			//Formula f1 = parser.parse("((!(B) & (!(B) -> (!(A) -> !(B)))) -> (!(B) -> (!(A) -> !(B))))");
			//Formula f2 = parser.parse("(A&B)->B");
			//Console.print(f1.equalsWithAxiom(f2));
			while (in.ready())
			{
				String token = in.readLine();
				Formula f = parser.parse(token);
				proof.addStatement(f);
			}
			
			Pair<Boolean, Integer> result = proof.isCorrect();
			if (result.getFirst())
			{
				out.write("Доказательство корректно.");
			}
			else
			{
				out.write("Доказательство некорректно, начиная с высказывания №" + (result.getSecond()+1));
			}
			
			in.close();
			out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
