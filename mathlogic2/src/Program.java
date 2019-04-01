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
			
			String base = in.readLine();
			String[] parts = base.split("\\|\\-");
			if (parts.length > 1)
			{
				proof.setConclusion(parser.parse(parts[1]));
				parts = parts[0].split(",");
				if (parts.length == 1 && parts[0].length() == 0)
				{
					
				}
				else
				{
					for (int i = 0; i < parts.length; ++i)
					{
						Console.print("H "+parts[i]);
						proof.hypothesis.add(parser.parse(parts[i]));
					}
				}
			}
			while (in.ready())
			{
				String token = in.readLine();
				F f = parser.parse(token);
				//Console.print("Read " + f);
				proof.addStatement(f);
			}
			
			// Дополнительная проверка входных данных на корректность
			if (proof.canDoDeductionStep())
			{
				Proof ded = proof.deductionStep();
				//ded.compact();
				String temp = "";
				for (int i = 0; i < ded.getHypothesis().size() - 1; ++i)
				{
					temp += ded.getHypothesis().get(i) + ",";
				}
				if (ded.getHypothesis().size() > 0)
				{
					temp += ded.getHypothesis().get(ded.getHypothesis().size() - 1);
				}
				temp += "|-" + ded.getConclusion();
				out.write(temp+"\n");
				
				for (int i = 0; i < ded.getStatements().size(); ++i)
				{
					out.write(ded.getStatements().get(i)+"\n");
				}
			}
			else
			{
				out.write("Входной файл содержит некорректные данные.");
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
