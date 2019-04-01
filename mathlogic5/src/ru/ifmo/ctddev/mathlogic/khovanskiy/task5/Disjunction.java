package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;


public class Disjunction extends F
{
    private F left;
    private F right;

    public Disjunction(F left, F right)
    {
        this.left = left;
        this.right = right;
        this.type = EEnum.OR;
    }

    @Override
    public boolean equalsE(F e)
    {
        if (e.type == EEnum.OR)
        {
            return left.equalsE(((Disjunction)e).left) && right.equalsE(((Disjunction)e).right);
        }
        return false;
    }

    @Override
    public boolean equalsWithAxiom(F e)
    {
        if (e.type == EEnum.OR)
        {
            return left.equalsWithAxiom(((Disjunction) e).left)
                    && right.equalsWithAxiom(((Disjunction) e).right);
        }
        return false;
    }

    @Override
    public boolean isFree(Variable v)
    {
        return left.isFree(v) || right.isFree(v);
    }

    @Override
    public F getSubstituted(Variable v, Term substitution)
    {
        return dis(left.getSubstituted(v, substitution), right.getSubstituted(v, substitution));
    }

    @Override
    public SResult getSubstitution(Variable v, F substituted)
    {
        if (substituted.type == EEnum.OR)
        {
            Disjunction or = (Disjunction)substituted;
            SResult leftResult = left.getSubstitution(v, or.left);
            SResult rightResult = right.getSubstitution(v, or.right);
            boolean check = leftResult.isSubstituted && rightResult.isSubstituted;
            Term term = leftResult.subion;
            if (rightResult.subion != null) {
                term = rightResult.subion;
            }
            if ((leftResult.subion != null) && (rightResult.subion != null)) {
                check = check && leftResult.subion.equalsT(rightResult.subion);
            }
            return new SResult(check, term);
        }
        return new SResult(false, null);
    }

    @Override
    public boolean isFreeSub(Variable v, Variable substitution)
    {
        return left.isFreeSub(v, substitution) && right.isFreeSub(v, substitution);
    }

    @Override
    public String toString()
    {
        return "(" + left + "|" + right + ")";
    }
}
