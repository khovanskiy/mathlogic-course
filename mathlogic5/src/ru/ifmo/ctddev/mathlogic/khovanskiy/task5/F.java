package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;

import java.util.HashMap;
import java.util.List;

public abstract class F
{

    public static HashMap<String, F> cache = new HashMap<String, F>();
    public EEnum type;

    public abstract boolean equalsE(F e);
    public abstract boolean equalsWithAxiom(F e);
    public abstract boolean isFreeSub(Variable v, Variable substitution);
    public abstract boolean isFree(Variable v);
    public abstract F getSubstituted(Variable v, Term substitution);
    public abstract SResult getSubstitution(Variable v, F substituted);

    public boolean isFreeToSub(Term substitution, Variable v)
    {
        if (substitution.type == TermEnum.VARIABLE)
        {
            return isFreeSub(v, (Variable)substitution);
        }
        else
        {
            boolean b = true;
            Function f = (Function)substitution;
            for (int i = 0; i < f.arguments.size(); i++)
            {
                b = b && isFreeToSub(f.arguments.get(i), v);
            }
            return b;
        }
    }

    public static Implication impl(F left, F right)
    {
        String key = "(" + left + "->" + right + ")";
        if (cache.containsKey(key))
        {
            return (Implication) cache.get(key);
        }
        else
        {
            Implication implication = new Implication(left, right);
            cache.put(key, implication);
            return implication;
        }
    }

    public static Conjunction con(F left, F right)
    {
        String key = "(" + left + "&" + right + ")";
        if (cache.containsKey(key))
        {
            return (Conjunction) cache.get(key);
        }
        else
        {
            Conjunction and = new Conjunction(left, right);
            cache.put(key, and);
            return and;
        }
    }

    public static Disjunction dis(F left, F right)
    {
        String key = "(" + left + "|" + right + ")";
        if (cache.containsKey(key))
        {
            return (Disjunction) cache.get(key);
        }
        else
        {
            Disjunction or = new Disjunction(left, right);
            cache.put(key, or);
            return or;
        }
    }

    public static Negation neg(F argument)
    {
        String key = "!" + argument;
        if (cache.containsKey(key))
        {
            return (Negation) cache.get(key);
        }
        else
        {
            Negation not = new Negation(argument);
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

    public static Predicate pred(String name, List<Term> arguments)
    {
        String key = "";
        if (name.equals("equals"))
        {
            key = "(" + arguments.get(0).toString() + "=" + arguments.get(1).toString() + ")";
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
            key = sb.toString();
        }
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

    public static Exists getExists(Variable variable, F expression)
    {
        String key = "?" + variable.name + expression.toString();
        if (cache.containsKey(key))
        {
            return (Exists)cache.get(key);
        }
        else
        {
            Exists exists = new Exists(variable, expression);
            cache.put(key, exists);
            return exists;
        }
    }

    public static Any getAny(Variable variable, F expression)
    {
        String key = "@" + variable.name + expression;
        if (cache.containsKey(key))
        {
            return (Any)cache.get(key);
        }
        else
        {
            Any any = new Any(variable, expression);
            cache.put(key, any);
            return any;
        }
    }

}
