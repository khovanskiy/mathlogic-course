package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;


public class Proposition extends Approval
{
    public String label;

    public Proposition(String label)
    {
        super();
        this.approvalType = ApprovalEnum.PROP;
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
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
        return ((Proposition)s).label.equals(this.label);
    }

    @Override
    public boolean equalsWithAxioms(F e)
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
                return new SResult(this.label.equals(proposition.label), null);
            }
        }
        return new SResult(false, null);
    }
}
