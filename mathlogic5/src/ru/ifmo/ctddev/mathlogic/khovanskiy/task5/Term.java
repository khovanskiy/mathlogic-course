package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;

import java.util.HashMap;
import java.util.List;

public abstract class Term {

    public static HashMap<String, Term> cacheT = new HashMap<String, Term>();

    public TermEnum type;

    public abstract boolean equalsT(Term term);

    public abstract boolean contains(Variable v);

    public abstract Term getSubstituted(Variable v, Term substitution);

    public abstract SResult getSubstitution(Variable v, Term substituted);

    public static Function getFunc(String name, List<Term> arguments)
    {
        String s = "";
        if (name.equals("zero"))
        {
            s = "0";
        } else if (name.equals("increment"))
        {
            s = "(" + arguments.get(0).toString() + ")\'";
        }
        else if (name.equals("add"))
        {
            s = "((" + arguments.get(0).toString() + ")+(" + arguments.get(1).toString() + "))";
        }
        else if (name.equals("multiply"))
        {
            s = "((" + arguments.get(0).toString() + ")*(" + arguments.get(1).toString() + "))";
        }
        else
        {
            StringBuilder sb = new StringBuilder();
            sb.append(name);
            sb.append("(");
            Term t;
            for (int i = 0; i < arguments.size() - 1; ++i)
            {
                t = arguments.get(i);
                sb.append(t.toString());
                sb.append(", ");
            }
            sb.append(arguments.get(arguments.size() - 1));
            sb.append(")");
            s = sb.toString();
        }
        if (cacheT.containsKey(s))
        {
            return (Function)cacheT.get(s);
        }
        else
        {
            Function f = new Function(name, arguments);
            cacheT.put(s, f);
            return f;
        }
    }

    public static Variable getVar(String name)
    {
        if (cacheT.containsKey(name))
        {
            return (Variable)cacheT.get(name);
        }
        else
        {
            Variable v = new Variable(name);
            cacheT.put(name, v);
            return v;
        }
    }
}
