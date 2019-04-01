package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;

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
        String url = args[0];
        
        BufferedReader in = new BufferedReader(new FileReader(url));
        
        List<F> sta = new ArrayList<F>();
        List<F> hyp = new ArrayList<F>();
        F con = null;
        String s;
        s = in.readLine();
        
        //s = in.readLine();
        
        if (s.matches(".*\\|-.*"))
        {
            String[] arr = s.split("\\|-");
            if (arr.length > 1)
            {
                con = EParser.parseF(arr[1]);
                List<String> list = new ArrayList<String>();
                int old = -1;
                int depth = 0;
                
                for (int i = 0; i < arr[0].length(); ++i)
                {
                    if (arr[0].charAt(i) == '(')
                    {
                    	depth++;
                    }
                    if (arr[0].charAt(i) == ')')
                    {
                    	depth--;
                    }
                    if (arr[0].charAt(i) == ',' && depth == 0)
                    {
                        list.add(arr[0].substring(old + 1, i));
                        old = i;
                    }
                }
                list.add(arr[0].substring(old + 1));
                for (int i = 0; i < list.size(); ++i)
                {
                	if (!list.get(i).isEmpty())
	                {
	                    hyp.add(EParser.parseF(list.get(i)));
	                }
                }
            }
            else
            {
                con = EParser.parseF(arr[0]);
            }
        }
        else
        {
            sta.add(EParser.parseF(s));
        }
        while ((s = in.readLine()) != null)
        {
            try
            {
                sta.add(EParser.parseF(s));
            }
            catch (Exception e)
            {
                in.close();
            }
        }
        if (con == null)
        {
            con = sta.get(sta.size() - 1);
        }
        in.close();
        Proof proof = new Proof(hyp, sta, con);
        CResult result = PChecker.check(proof);
        String filename = args[0].split("\\.")[0] + ".out";
        PrintWriter out = new PrintWriter(filename);
        if (result.errorsCount == -1)
        {
            DescriptionP dp = PBuilder.makeDeductionStep(proof);
            for (int i = 0; i < dp.hyp.size() - 1; ++i)
            {
                out.print(dp.hyp.toString() + ",");
            }
            if (dp.hyp.size() > 0)
            {
                out.print(dp.hyp.get(dp.hyp.size() - 1).toString());
            }
            out.print("|-");
            out.println(dp.con.toString());
            for (int i = 0; i < dp.sta.size(); ++i)
            {
                out.println(dp.sta.get(i).expr.toString());
            }
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
