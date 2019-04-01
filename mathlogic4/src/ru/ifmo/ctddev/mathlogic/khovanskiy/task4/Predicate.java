package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;

import java.util.ArrayList;
import java.util.List;

public class Predicate extends Approval
{
    public String label;
    public List<Term> args;

    public Predicate(String label, List<Term> args)
    {
        super();
        this.approvalType = ApprovalEnum.PRED;
        this.label = label;
        this.args = args;
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
    public boolean equalsE(F e)
    {
        if (e.type != EEnum.APPROVAL)
        {
        	return false;
        }
        Approval s = (Approval)e;
        if (s.approvalType != ApprovalEnum.PRED)
        {
        	return false;
        }
        Predicate p = (Predicate)s;
        if (p.label.equals(this.label) && (p.args.size() == this.args.size()))
        {
            boolean b = true;
            for (int i = 0; i < this.args.size(); ++i)
            {
                b = b && this.args.get(i).equalsT(p.args.get(i));
            }
            return b;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean equalsWithAxioms(F e)
    {
        return false;
    }

    @Override
    public boolean isFreeSub(Variable v, Variable s)
    {
        return true;
    }

    @Override
    public boolean isFree(Variable v)
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
    public F getSubstituted(Variable v, Term substitution)
    {
        List<Term> arguments = new ArrayList<Term>();
        for (int i = 0; i < this.args.size(); ++i)
        {
            arguments.add(this.args.get(i).getSubstituted(v, substitution));
        }
        return new Predicate(this.label, arguments);
    }

    @Override
    public SResult getSubstitution(Variable v, F substituted)
    {
        if (substituted.type == EEnum.APPROVAL)
        {
            Approval statement = (Approval)substituted;
            if (statement.approvalType == ApprovalEnum.PRED)
            {
                Predicate predicate = (Predicate)statement;
                if (this.label.equals(predicate.label) && this.args.size() == predicate.args.size())
                {
                    Term substitution = null;
                    SResult result;
                    boolean check = true;
                    for (int i = 0; i < args.size(); ++i)
                    {
                        result = args.get(i).getSubstitution(v, predicate.args.get(i));
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
        }
        return new SResult(false, null);
    }
}
