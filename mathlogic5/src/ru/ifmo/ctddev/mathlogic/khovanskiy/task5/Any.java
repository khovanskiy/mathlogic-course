package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;


public class Any extends Approval
{
    public Variable variable;
    public F expression;

    public Any(Variable variable, F expression)
    {
        super();
        this.approvalType = ApprovalEnum.ANY;
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public String toString()
    {
        return "@" + variable.toString() + expression.toString();
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
        return (fa.variable.equalsT(this.variable)) && (fa.expression.equalsE(this.expression));
    }

    @Override
    public boolean equalsWithAxiom(F e)
    {
        return false;
    }

    @Override
    public boolean isFree(Variable v)
    {
        if (v.equalsT(this.variable))
        {
            return false;
        }
        else
        {
            return expression.isFree(v);
        }
    }

    @Override
    public boolean isFreeSub(Variable v, Variable substitution)
    {
        if (this.variable.name.equals(substitution.name))
        {
            return !expression.isFree(v);
        }
        else
        {
            if (this.variable.name.equals(v.name))
            {
                return true;
            }
            else
            {
                return expression.isFreeSub(v, substitution);
            }
        }
    }

    @Override
    public SResult getSubstitution(Variable v, F substituted)
    {
        if (substituted.type == EEnum.APPROVAL)
        {
            Approval statement = (Approval)substituted;
            if (statement.approvalType == ApprovalEnum.ANY)
            {
                Any any = (Any)statement;
                if (!this.variable.name.equals(v) && this.variable.name.equals(any.variable.name))
                {
                    return expression.getSubstitution(v, any.expression);
                }
            }
        }
        return new SResult(false, null);
    }

    @Override
    public F getSubstituted(Variable v, Term substitution)
    {
        if (this.variable.name.equals(v.name))
        {
            return this;
        }
        else
        {
            return new Any(this.variable, expression.getSubstituted(v, substitution));
        }
    }
}
