package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;

import java.util.HashMap;
import java.util.List;

public abstract class F
{
    public static HashMap<String, F> cache = new HashMap<String, F>();
    public EEnum type;

    public abstract boolean equalsE(F e);
    public abstract boolean equalsWithAxioms(F e);
    public abstract boolean isFreeSub(Variable v, Variable substitution);
    public abstract boolean isFree(Variable v);
    public abstract F getSubstituted(Variable v, Term substitution);
    public abstract SResult getSubstitution(Variable v, F substituted);

    public static Predicate pred(String name, List<Term> arguments)
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
        String key = sb.toString();
        if (cache.containsKey(key))
        {
            return (Predicate)cache.get(key);
        }
        else
        {
            Predicate predicate = new Predicate(name, arguments);
            cache.put(key, predicate);
            return predicate;
        }
    }

    public static Exists getExists(Variable v, F e)
    {
        String key = "?" + v.name + e;
        if (cache.containsKey(key))
        {
            return (Exists)cache.get(key);
        }
        else
        {
            Exists exists = new Exists(v, e);
            cache.put(key, exists);
            return exists;
        }
    }

    public static Any getAny(Variable v, F e)
    {
        String key = "@" + v.name + e;
        if (cache.containsKey(key))
        {
            return (Any)cache.get(key);
        }
        else
        {
            Any any = new Any(v, e);
            cache.put(key, any);
            return any;
        }
    }
    
    public boolean isFreeToSubstitute(Term sub, Variable v)
    {
        if (sub.type == TermEnum.VARIABLE)
        {
            return isFreeSub(v, (Variable)sub);
        }
        else
        {
            boolean ok = true;
            Function f = (Function)sub;
            for (int i = 0; i < f.args.size(); ++i)
            {
                ok = ok && isFreeToSubstitute(f.args.get(i), v);
            }
            return ok;
        }
    }

    public static Implication impl(F l, F r)
    {
        String key = "(" + l + "->" + r + ")";
        if (cache.containsKey(key))
        {
            return (Implication) cache.get(key);
        }
        else
        {
            Implication implication = new Implication(l, r);
            cache.put(key, implication);
            return implication;
        }
    }

    public static Conjunction con(F l, F r)
    {
        String key = "(" + l + "&" + r + ")";
        if (cache.containsKey(key))
        {
            return (Conjunction) cache.get(key);
        }
        else
        {
            Conjunction and = new Conjunction(l, r);
            cache.put(key, and);
            return and;
        }
    }

    public static Disjunction dis(F l, F r)
    {
        String key = "(" + l + "|" + r + ")";
        if (cache.containsKey(key))
        {
            return (Disjunction) cache.get(key);
        }
        else
        {
            Disjunction or = new Disjunction(l, r);
            cache.put(key, or);
            return or;
        }
    }

    public static Negation neg(F arg)
    {
        String key = "!" + arg;
        if (cache.containsKey(key))
        {
            return (Negation) cache.get(key);
        }
        else
        {
            Negation not = new Negation(arg);
            cache.put(key, not);
            return not;
        }
    }

    public static Proposition prop(String name)
    {
        if (cache.containsKey(name))
        {
            return (Proposition)cache.get(name);
        }
        else
        {
            Proposition proposition = new Proposition(name);
            cache.put(name, proposition);
            return proposition;
        }
    }
}
