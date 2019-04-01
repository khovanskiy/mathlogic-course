package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;

public class Proposition extends Approval
{
    public String labelP;

    public Proposition(String name)
    {
        super();
        this.approvalType = ApprovalEnum.PROP;
        this.labelP = name;
    }

    @Override
    public String toString()
    {
        return this.labelP;
    }

    @Override
    public boolean equalsE(F e)
    {
        if (e.type != EEnum.APPROVAL)
        {
        	return false;
        }
        Approval s = (Approval)e;
        if (s.approvalType != ApprovalEnum.PROP)
        {
        	return false;
        }
        return ((Proposition)s).labelP.equals(this.labelP);
    }

    @Override
    public boolean equalsWithAxiom(F e)
    {
        return false;
    }

    @Override
    public boolean isFreeSub(Variable v, Variable substitution)
    {
        return true;
    }

    @Override
    public boolean isFree(Variable v)
    {
        return false;
    }

    @Override
    public F getSubstituted(Variable v, Term substitution)
    {
        return this;
    }

    @Override
    public SResult getSubstitution(Variable v, F substituted)
    {
        if (substituted.type == EEnum.APPROVAL)
        {
            Approval statement = (Approval)substituted;
            if (statement.approvalType == ApprovalEnum.PROP)
            {
                Proposition proposition = (Proposition)statement;
                return new SResult(this.labelP.equals(proposition.labelP), null);
            }
        }
        return new SResult(false, null);
    }
}
