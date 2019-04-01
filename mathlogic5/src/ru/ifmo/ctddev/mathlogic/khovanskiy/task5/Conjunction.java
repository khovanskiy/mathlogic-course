package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;

public class Conjunction extends F
{
    public F left, right;

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
    public boolean equalsWithAxiom(F e)
    {
        if (e.type == EEnum.AND)
        {
            return left.equalsWithAxiom(((Conjunction) e).left) && right.equalsWithAxiom(((Conjunction) e).right);
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
            Term term = leftResult.subion;
            if (rightResult.subion != null)
            {
                term = rightResult.subion;
            }
            if ((leftResult.subion != null) && (rightResult.subion != null))
            {
                check = check && leftResult.subion.equalsT(rightResult.subion);
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
