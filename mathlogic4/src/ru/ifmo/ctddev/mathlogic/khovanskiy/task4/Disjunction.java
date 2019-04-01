package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;

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
    public boolean equalsWithAxioms(F e) 
    {
        if (e.type == EEnum.OR)
        {
            return left.equalsWithAxioms(((Disjunction) e).left) && right.equalsWithAxioms(((Disjunction) e).right);
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
            boolean isSubstituted = leftResult.isSubstituted && rightResult.isSubstituted;
            Term term = leftResult.substitution;
            if (rightResult.substitution != null) 
            {
                term = rightResult.substitution;
            }
            if ((leftResult.substitution != null) && (rightResult.substitution != null)) 
            {
                isSubstituted = isSubstituted && leftResult.substitution.equalsT(rightResult.substitution);
            }
            return new SResult(isSubstituted, term);
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
