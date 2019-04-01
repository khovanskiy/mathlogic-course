package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;


public class Negation extends F
{
    private F expr;

    public Negation(F expr)
    {
        this.expr = expr;
        this.type = EEnum.NEG;
    }

    @Override
    public boolean equalsE(F e)
    {
        if (e.type == EEnum.NEG)
        {
            return expr.equalsE(((Negation)e).expr);
        }
        return false;
    }

    @Override
    public boolean equalsWithAxioms(F e)
    {
        if (e.type == EEnum.NEG)
        {
            return expr.equalsWithAxioms(((Negation) e).expr);
        }
        return false;
    }

    @Override
    public boolean isFree(Variable v)
    {
        return expr.isFree(v);
    }

    @Override
    public F getSubstituted(Variable v, Term substitution)
    {
        return neg(expr.getSubstituted(v, substitution));
    }

    @Override
    public SResult getSubstitution(Variable v, F substituted)
    {
        if (substituted.type == EEnum.NEG)
        {
            Negation not = (Negation)substituted;
            return expr.getSubstitution(v, not.expr);
        }
        return new SResult(false, null);
    }

    @Override
    public boolean isFreeSub(Variable v, Variable substitution)
    {
        return expr.isFreeSub(v, substitution);
    }

    @Override
    public String toString()
    {
        return "!" + expr;
    }
}
