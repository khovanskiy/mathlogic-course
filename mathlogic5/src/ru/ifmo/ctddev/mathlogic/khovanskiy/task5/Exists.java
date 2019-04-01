package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;


public class Exists extends Approval
{
    public Variable variable;
    public F expression;

    public Exists(Variable variable, F expression)
    {
        super();
        this.approvalType = ApprovalEnum.EXISTS;
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public String toString()
    {
        return "?" + variable.toString() + expression.toString();
    }

    @Override
    public boolean equalsE(F e)
    {
        if (e.type != EEnum.APPROVAL)
        {
        	return false;
        }
        Approval s = (Approval)e;
        if (s.approvalType != ApprovalEnum.EXISTS)
        {
        	return false;
        }
        Exists ex = (Exists)s;
        return (ex.variable.equalsT(this.variable)) && (ex.expression.equalsE(this.expression));
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
    public F getSubstituted(Variable v, Term substitution)
    {
        if (this.variable.name.equals(v.name))
        {
            return this;
        }
        else
        {
            return new Exists(this.variable, expression.getSubstituted(v, substitution));
        }
    }

    @Override
    public SResult getSubstitution(Variable v, F substituted)
    {
        if (substituted.type == EEnum.APPROVAL)
        {
            Approval statement = (Approval)substituted;
            if (statement.approvalType == ApprovalEnum.EXISTS)
            {
                Exists exists = (Exists)statement;
                if (!this.variable.name.equals(v) && this.variable.name.equals(exists.variable.name))
                {
                    return expression.getSubstitution(v, exists.expression);
                }
            }
        }
        return new SResult(false, null);
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
}
