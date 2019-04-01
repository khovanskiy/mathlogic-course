package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;


public class Negation extends F
{
    private F expression;

    public Negation(F expr)
    {
        this.expression = expr;
        this.type = EEnum.NEG;
    }

    @Override
    public boolean equalsE(F e)
    {
        if (e.type == EEnum.NEG)
        {
            return expression.equalsE(((Negation)e).expression);
        }
        return false;
    }

    @Override
    public boolean equalsWithAxiom(F e)
    {
        if (e.type == EEnum.NEG)
        {
            return expression.equalsWithAxiom(((Negation) e).expression);
        }
        return false;
    }

    @Override
    public boolean isFree(Variable v)
    {
        return expression.isFree(v);
    }

    @Override
    public F getSubstituted(Variable v, Term substitution)
    {
        return neg(expression.getSubstituted(v, substitution));
    }

    @Override
    public SResult getSubstitution(Variable v, F substituted)
    {
        if (substituted.type == EEnum.NEG)
        {
            Negation not = (Negation)substituted;
            return expression.getSubstitution(v, not.expression);
        }
        return new SResult(false, null);
    }

    @Override
    public boolean isFreeSub(Variable v, Variable substitution)
    {
        return expression.isFreeSub(v, substitution);
    }

    @Override
    public String toString()
    {
        return "!" + expression;
    }
}
