/*
 * Хованский Виктор, 2539
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
			
			/*Proof proof = new Proof();
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
			*/
			
			/*HashSet<String> table = new HashSet<String>();
			while (in.ready())
			{
				F f = parser.parse(in.readLine());
				String h = f.java_code();
				if (!table.contains(h))
				{
					out.write("current.addStatement("+f.java_code() + ");\n");
					table.add(h);
				}
			}*/
			F target = parser.parse(in.readLine());
			
			Pair<Boolean, HashMap<String, Boolean>> result = ProofFactory.canBeProoved(target);
			if (result.getFirst())
			{
				Proof p = ProofFactory.build(target, out);
				for (int i = 0; i < p.getStatements().size(); ++i)
				{
					out.write(p.getStatements().get(i) + "\n");
				}
			}
			else
			{
				out.write("Высказывание ложно при ");
				for (Map.Entry<String, Boolean> v : result.getSecond().entrySet())
				{
					out.write(v.getKey()+"="+(v.getValue() ? "И" : "Л")+" ");
				}
				out.write("\n");
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
