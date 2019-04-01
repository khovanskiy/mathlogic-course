package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;

import java.util.ArrayList;
import java.util.List;

public class Function extends Term
{
    public String label;
    public List<Term> args;

    public Function(String label, List<Term> args)
    {
        this.label = label;
        this.args = args;
        this.type = TermEnum.FUNCTION;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.label);
        sb.append("(");
        Term t;
        for (int i = 0; i < args.size() - 1; i++) {
            t = args.get(i);
            sb.append(t.toString());
            sb.append(", ");
        }
        sb.append(args.get(args.size() - 1));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equalsT(Term term)
    {
        if (term.type == TermEnum.FUNCTION)
        {
            Function f = (Function)term;
            if (f.label.equals(this.label) && (f.args.size() == this.args.size()))
            {
                boolean b = true;
                for (int i = 0; i < args.size(); i++) {
                    b = b && f.args.get(i).equalsT(this.args.get(i));
                }
                return b;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean contains(Variable v)
    {
        for (int i = 0; i < args.size(); ++i)
        {
        	if (args.get(i).contains(v))
        	{
        		return true;
        	}
        }
        return false;
    }

    @Override
    public Term getSubstituted(Variable v, Term substitution)
    {
        List<Term> args = new ArrayList<Term>();
        for (int i = 0; i < this.args.size(); ++i)
        {
            args.add(this.args.get(i).getSubstituted(v, substitution));
        }
        return func(this.label, args);
    }

    @Override
    public SResult getSubstitution(Variable v, Term substituted)
    {
        if (substituted.type == TermEnum.FUNCTION)
        {
            Function function = (Function)substituted;
            if (this.label.equals(function.label) && this.args.size() == function.args.size())
            {
                Term substitution = null;
                SResult result;
                boolean check = true;
                for (int i = 0; i < args.size(); ++i)
                {
                    result = args.get(i).getSubstitution(v, function.args.get(i));
                    check = check && result.isSubstituted;
                    if (substitution == null)
                    {
                        substitution = result.substitution;
                    }
                    else
                    {
                        if (result.substitution != null)
                        {
                            check = check && substitution.equalsT(result.substitution);
                        }
                    }
                }
                return new SResult(check, substitution);
            }
        }
        return new SResult(false, null);
    }

}
