package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Victor Khovanskiy
 *
 */
public class Program
{
    public static void main(String[] args) throws IOException
    {
        String path = args[0];
        BufferedReader in = new BufferedReader(new FileReader(path));
        List<F> statements = new ArrayList<F>();
        List<F> hypothesis = new ArrayList<F>();
        F conclusion = null;
        
        String s = in.readLine();
        
        if (s.matches(".*\\|-.*"))
        {
            String[] tempArray = s.split("\\|-");
            if (tempArray.length > 1)
            {
                conclusion = EParser.stringToExpression(tempArray[1]);
                List<String> hyp = new ArrayList<String>();
                int old = -1;
                int depth = 0;
                for (int i = 0; i < tempArray[0].length(); ++i)
                {
                    if (tempArray[0].charAt(i) == '(')
                    {
                    	depth++;
                    }
                    if (tempArray[0].charAt(i) == ')')
                    {
                    	depth--;
                    }
                    if (tempArray[0].charAt(i) == ',' && depth == 0)
                    {
                        hyp.add(tempArray[0].substring(old + 1, i));
                        old = i;
                    }
                }
                hyp.add(tempArray[0].substring(old + 1));
                for (int i = 0; i < hyp.size(); ++i)
                {
	                if (!hyp.get(i).isEmpty())
	                {
	                    hypothesis.add(EParser.stringToExpression(hyp.get(i)));
	                }
                }
            }
            else
            {
                conclusion = EParser.stringToExpression(tempArray[0]);
            }
        }
        else
        {
            statements.add(EParser.stringToExpression(s));
        }
        try
        {
            while ((s = in.readLine()) != null)
            {
                if (!s.isEmpty())
                {
                    statements.add(EParser.stringToExpression(s));
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("Некорректное выражение #" + (statements.size() + 1) + ".");
            System.out.println(s);
            in.close();
        }
        if (conclusion == null)
        {
            conclusion = statements.get(statements.size() - 1);
        }
        in.close();
        Proof proof = new Proof(hypothesis, statements, conclusion);
        CResult result = PChecker.check(proof);
        String filename = args[0].split("\\.")[0] + ".out";
        PrintWriter out = new PrintWriter(filename);
        if (result.errorsCount == -1)
        {
            out.println("Доказательство корректно.");
        }
        else
        {
            out.print("Вывод некорректен начиная с формулы № " + (result.errorsCount + 1));
            if (!result.debugInfo.isEmpty())
            {
                out.println(": " + result.debugInfo);
            }
            else
            {
                out.println(".");
            }
        }
        out.close();
    }
}
