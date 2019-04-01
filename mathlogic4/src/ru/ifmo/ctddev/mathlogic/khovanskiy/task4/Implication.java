package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;


public class Implication extends F {
    public F left, right;

    public Implication(F left, F right) {
        this.left = left;
        this.right = right;
        this.type = EEnum.IMPL;
    }

    @Override
    public boolean equalsE(F e)
    {
        if (e.type == EEnum.IMPL)
        {
            return left.equalsE(((Implication)e).left) && right.equalsE(((Implication)e).right);
        }
        return false;
    }

    @Override
    public boolean equalsWithAxioms(F e)
    {
        if (e.type == EEnum.IMPL)
        {
            return left.equalsWithAxioms(((Implication) e).left) && right.equalsWithAxioms(((Implication) e).right);
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
        return impl(left.getSubstituted(v, substitution), right.getSubstituted(v, substitution));
    }

    @Override
    public SResult getSubstitution(Variable v, F substituted)
    {
        if (substituted.type == EEnum.IMPL)
        {
            Implication impl = (Implication)substituted;
            SResult a = left.getSubstitution(v, impl.left);
            SResult b = right.getSubstitution(v, impl.right);
            boolean check = a.isSubstituted && b.isSubstituted;
            Term term = a.substitution;
            if (b.substitution != null)
            {
                term = b.substitution;
            }
            if ((a.substitution != null) && (b.substitution != null))
            {
                check = check && a.substitution.equalsT(b.substitution);
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
        return "(" + left + "->" + right + ")";
    }

    public String rightPart()
    {
        return this.right.toString();
    }

    public String leftPart()
    {
        return this.left.toString();
    }
}
