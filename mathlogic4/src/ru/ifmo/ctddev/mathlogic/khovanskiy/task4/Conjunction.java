package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;

public class Conjunction extends F
{
    private F left, right;

    public Conjunction(F left, F right)
    {
        this.left = left;
        this.right = right;
        this.type = EEnum.AND;
    }

    @Override
    public boolean equalsE(F e)
    {
        if (e.type == EEnum.AND)
        {
            return left.equalsE(((Conjunction)e).left) && right.equalsE(((Conjunction)e).right);
        }
        return false;
    }

    @Override
    public boolean equalsWithAxioms(F e)
    {
        if (e.type == EEnum.AND)
        {
            return left.equalsWithAxioms(((Conjunction) e).left) && right.equalsWithAxioms(((Conjunction) e).right);
        }
        return false;
    }

    @Override
    public boolean isFreeSub(Variable v, Variable substitution)
    {
        return left.isFreeSub(v, substitution) && right.isFreeSub(v, substitution);
    }

    @Override
    public boolean isFree(Variable v)
    {
        return left.isFree(v) || right.isFree(v);
    }

    @Override
    public F getSubstituted(Variable v, Term substitution)
    {
        return con(left.getSubstituted(v, substitution), right.getSubstituted(v, substitution));
    }

    @Override
    public SResult getSubstitution(Variable v, F substituted)
    {
        if (substituted.type == EEnum.AND)
        {
            Conjunction and = (Conjunction)substituted;
            SResult leftResult = left.getSubstitution(v, and.left);
            SResult rightResult = right.getSubstitution(v, and.right);
            boolean check = leftResult.isSubstituted && rightResult.isSubstituted;
            Term term = leftResult.substitution;
            if (rightResult.substitution != null)
            {
                term = rightResult.substitution;
            }
            if ((leftResult.substitution != null) && (rightResult.substitution != null))
            {
                check = check && leftResult.substitution.equalsT(rightResult.substitution);
            }
            return new SResult(check, term);
        }
        return new SResult(false, null);
    }

    @Override
    public String toString()
    {
        return "(" + left + "&" + right + ")";
    }
}
