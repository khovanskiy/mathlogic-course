import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

public class ProofFactory
{
	public static Pair<Boolean, HashMap<String, Boolean>> canBeProoved(F statement) throws Exception
	{
		Vector<Variable> vars = statement.getVariables();
		int n2size = 1 << vars.size();
		
		for (int i = 0; i < n2size; ++i)
		{
			HashMap<String, Boolean> h = new HashMap<>();
			for (int j = vars.size() - 1; j >= 0; --j)
			{
				boolean t = (i >> (vars.size() - j - 1)) % 2 != 0;
				h.put(vars.get(j).getLabel(), t);
			}
			if (!statement.getVerity(h))
			{
				return new Pair(false, h);
			}
		}
		return new Pair(true, null);
	}
	
	public static Proof build(F statement, BufferedWriter out) throws Exception
	{
		Vector<Variable> vars = statement.getVariables();
		int n2size = 1 << vars.size();
		/*Console.print("Variables:");
		for (int i = 0; i < vars.size(); ++i)
		{
			Console.print(vars.get(i));
		}*/
		Vector<Proof> steps = new Vector<Proof>();
		for (int i = 0; i < n2size; ++i)
		{
			HashMap<String, Boolean> h = new HashMap<>();
			Vector<F> hyp = new Vector<F>();
			int k = i;
			for (int j = 0; j < vars.size(); ++j)
			{
				boolean t = k % 2 != 0;
				k /= 2;
				//Console.print(vars.get(j) + " " + t);
				h.put(vars.get(j).getLabel(), t);
				hyp.add(t ? F.var(vars.get(j).getLabel()) : F.neg(F.var(vars.get(j).getLabel())));
			}
			Vector<F> temp = new Vector<F>();
			for (int j = hyp.size() - 1; j >= 0; --j)
			{
				temp.add(hyp.get(j));
			}
			Proof t = nth_step(temp, h, statement);
			t.compact();
			steps.add(t);
			//out.write(steps.lastElement()+"");
		}
		while (steps.size() > 1)
		{
			Vector<Proof> temp = new Vector<Proof>();
			//out.write("=====================================\n");
			/*for (int i = 0; i < steps.size(); ++i)
			{
				out.write(steps.get(i)+"\n");
			}*/
			for (int i = 0; i < steps.size(); i+=2)
			{
				F N = steps.get(i).getLastHyp();
				F M = steps.get(i + 1).getLastHyp();
				
				Console.print(steps.get(i).getConclusion()+" "+N);
				Console.print(steps.get(i).getHypothesis());
				Console.print(steps.get(i + 1).getConclusion()+" "+M);
				Console.print(steps.get(i + 1).getHypothesis()+"\n");
				//Console.print(steps.get(i));
				//Console.print(steps.get(i + 1));
				//out.write("Merge*******************************************************:\n");
				//out.write("Prev one:\n");
				//out.write(steps.get(i)+"\n");
				Proof one = steps.get(i).deductionStep();
				one.compact();

				//out.write("One:\n");
				//out.write(one + "\n");
				//out.write("Prev two:\n");
				//out.write(steps.get(i + 1)+"\n");
				Proof two = steps.get(i + 1).deductionStep();
				
				two.compact();
				//out.write("Two:\n");
				//out.write(two + "\n");
				one.setConclusion(statement);
				one.getStatements().addAll(two.getStatements());
				//(A->C)->((B->C)->(A|B->C))
				one.addStatement(F.impl(F.impl(M, steps.get(i).getConclusion()), F.impl(F.impl(N, steps.get(i).getConclusion()), F.impl(F.dis(M, N), steps.get(i).getConclusion()))));
				one.addStatement(F.impl(F.impl(N, steps.get(i).getConclusion()), F.impl(F.dis(M, N), steps.get(i).getConclusion())));
				one.addStatement(F.impl(F.dis(M, N), steps.get(i).getConclusion()));
				
				
				//M = N;
				// A|!A
				one.addStatement(F.impl(M, F.dis(M, F.neg(M))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))))))));
				one.addStatement(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))))));
				one.addStatement(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))), F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))))));
				one.addStatement(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))))))));
				one.addStatement(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M)))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M)))), F.impl(M, F.dis(M, F.neg(M))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M))))), F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M)))), F.impl(M, F.dis(M, F.neg(M))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M)))), F.impl(M, F.dis(M, F.neg(M))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M)))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M)))), F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M)))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M)))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))), F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M))))), F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))))), F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(M)))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(M)))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(M))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M)))))), F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(M))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(M, F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(M))))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(M))))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(M)))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M)))), F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(M)))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(M))))));
				one.addStatement(F.impl(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(M, F.neg(F.dis(M, F.neg(M)))), F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(M)))), F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(M)))));
				one.addStatement(F.impl(F.impl(M, F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(M))));
				one.addStatement(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(M)));
				one.addStatement(F.impl(F.neg(M), F.dis(M, F.neg(M))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))))))));
				one.addStatement(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))))));
				one.addStatement(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))))))));
				one.addStatement(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M)))), F.impl(F.neg(M), F.dis(M, F.neg(M))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M))))), F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M)))), F.impl(F.neg(M), F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M)))), F.impl(F.neg(M), F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M)))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M)))), F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M)))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M)))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))), F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))))))));
				one.addStatement(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M))))), F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.dis(M, F.neg(M)))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))))), F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.neg(M))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.neg(M))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))))), F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.neg(M)))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.neg(M))))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M))))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.neg(M)))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.neg(M))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M))))), F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.neg(M))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.impl(F.impl(F.neg(M), F.neg(F.dis(M, F.neg(M)))), F.neg(F.neg(M)))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.neg(M))))), F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.neg(M))))));
				one.addStatement(F.impl(F.impl(F.neg(M), F.dis(M, F.neg(M))), F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.neg(M)))));
				one.addStatement(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.neg(M))));
				one.addStatement(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(M)), F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.neg(M))), F.neg(F.neg(F.dis(M, F.neg(M)))))));
				one.addStatement(F.impl(F.impl(F.neg(F.dis(M, F.neg(M))), F.neg(F.neg(M))), F.neg(F.neg(F.dis(M, F.neg(M))))));
				one.addStatement(F.neg(F.neg(F.dis(M, F.neg(M)))));
				one.addStatement(F.impl(F.neg(F.neg(F.dis(M, F.neg(M)))), F.dis(M, F.neg(M))));
				one.addStatement(F.dis(M, F.neg(M)));
				
				//one.addStatement(steps.get(i).getConclusion());
				one.addStatement(statement);
				one.compact();
				//out.write("Result\n");
				//out.write(one + "\n");
				temp.add(one);
				//break;
			}
			steps = temp;
		}
		/*out.write("=====================================\n");
		for (int i = 0; i < steps.size(); ++i)
		{
			out.write(steps.get(i)+"\n");
		}*/
		//out.write("MAIN CONCLUSION");
		//out.write(steps.get(0)+"\n");
		//steps.get(0).addStatement(statement);
		return steps.get(0);
	}
	
	private static Proof nth_step(Vector<F> hyp, HashMap<String, Boolean> h, F statement) throws Exception
	{
		//Console.print("N-th step");
		Proof result = statement.getProof(h);
		result.getHypothesis().addAll(hyp);
		result.setConclusion(statement);
		return result;
	}
	
	public static Proof contrPosition(F left, F right)
	{
		return null;
		//(A->B)->(!B->!A)
		
		/*A->B, !B |- !A
		(A->B)->(A->!B)->!A
		(A->!B)->!A
		!B->(A->!B)
		A->!B
		!A
		
		doDecutionStep x2
		
		=> (A->B)->(!B->!A)*/
	}
}
