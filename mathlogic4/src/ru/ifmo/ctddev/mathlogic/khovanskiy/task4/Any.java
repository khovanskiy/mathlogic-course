package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;


public class Any extends Approval
{
    public Variable var;
    public F exp;

    public Any(Variable variable, F expression)
    {
        super();
        this.approvalType = ApprovalEnum.ANY;
        this.var = variable;
        this.exp = expression;
    }

    @Override
    public String toString()
    {
        return "@" + var + exp;
    }

    @Override
    public boolean equalsE(F e)
    {
        if (e.type != EEnum.APPROVAL)
        {
        	return false;
        }
        Approval s = (Approval)e;
        if (s.approvalType != ApprovalEnum.ANY)
        {
        	return false;
        }
        Any fa = (Any)s;
        return (fa.var.equalsT(this.var)) && (fa.exp.equalsE(this.exp));
    }

    @Override
    public SResult getSubstitution(Variable v, F substituted)
    {
        if (substituted.type == EEnum.APPROVAL)
        {
            Approval app = (Approval)substituted;
            if (app.approvalType == ApprovalEnum.ANY)
            {
                Any any = (Any)app;
                if (!this.var.name.equals(v) && this.var.name.equals(any.var.name))
                {
                    return exp.getSubstitution(v, any.exp);
                }
            }
        }
        return new SResult(false, null);
    }

    @Override
    public F getSubstituted(Variable v, Term s)
    {
        if (this.var.name.equals(v.name))
        {
            return this;
        }
        return new Any(this.var, exp.getSubstituted(v, s));
    }

    @Override
    public boolean equalsWithAxioms(F e)
    {
        return false;
    }

    @Override
    public boolean isFree(Variable v)
    {
        if (v.equalsT(this.var))
        {
            return false;
        }
        return exp.isFree(v);
    }

    @Override
    public boolean isFreeSub(Variable v, Variable substitution)
    {
        if (this.var.name.equals(substitution.name))
        {
            return !exp.isFree(v);
        }
        else
        {
            if (this.var.name.equals(v.name))
            {
                return true;
            }
            return exp.isFreeSub(v, substitution);
        }
    }
}
