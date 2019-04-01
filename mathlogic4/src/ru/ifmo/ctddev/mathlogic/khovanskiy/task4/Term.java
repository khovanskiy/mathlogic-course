package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;

import java.util.HashMap;
import java.util.List;

public abstract class Term
{
    public static HashMap<String, Term> cache = new HashMap<String, Term>();
    public TermEnum type;

    public abstract boolean equalsT(Term term);
    public abstract boolean contains(Variable v);
    public abstract Term getSubstituted(Variable v, Term substitution);
    public abstract SResult getSubstitution(Variable v, Term substituted);

    public static Function func(String label, List<Term> args)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(label);
        sb.append("(");
        Term t;
        for (int i = 0; i < args.size() - 1; ++i)
        {
            t = args.get(i);
            sb.append(t.toString());
            sb.append(", ");
        }
        sb.append(args.get(args.size() - 1));
        sb.append(")");
        String key = sb.toString();
        if (cache.containsKey(key))
        {
            return (Function)cache.get(key);
        }
        else
        {
            Function f = new Function(label, args);
            cache.put(key, f);
            return f;
        }
    }

    public static Variable var(String name)
    {
        if (cache.containsKey(name))
        {
            return (Variable)cache.get(name);
        }
        else
        {
            Variable v = new Variable(name);
            cache.put(name, v);
            return v;
        }
    }
}
