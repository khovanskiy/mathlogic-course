package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;


public class Implication extends F {
    public F left, right;

    public Implication(F left, F right) {
        this.left = left;
        this.right = right;
        this.type = EEnum.IMPL;
    }

    @Override
    public boolean equalsE(F e) {
        if (e.type == EEnum.IMPL)
            return left.equalsE(((Implication)e).left)
                    && right.equalsE(((Implication)e).right);

        else
            return false;
    }

    @Override
    public boolean equalsWithAxiom(F e) {
        if (e.type == EEnum.IMPL)
            return left.equalsWithAxiom(((Implication) e).left)
                    && right.equalsWithAxiom(((Implication) e).right);

        else
            return false;
    }

    @Override
    public boolean isFree(Variable v) {
        return left.isFree(v) || right.isFree(v);
    }

    @Override
    public F getSubstituted(Variable v, Term substitution) {
        return impl(left.getSubstituted(v, substitution), right.getSubstituted(v, substitution));
    }

    @Override
    public SResult getSubstitution(Variable v, F substituted) {
        if (substituted.type == EEnum.IMPL) {
            Implication impl = (Implication)substituted;
            SResult res1 = left.getSubstitution(v, impl.left);
            SResult res2 = right.getSubstitution(v, impl.right);
            boolean check = res1.isSubstituted && res2.isSubstituted;
            Term term = res1.subion;
            if (res2.subion != null) {
                term = res2.subion;
            }
            if ((res1.subion != null) && (res2.subion != null)) {
                check = check && res1.subion.equalsT(res2.subion);
            }
            return new SResult(check, term);
        }
        return new SResult(false, null);
    }

    @Override
    public boolean isFreeSub(Variable v, Variable substitution) {
        return left.isFreeSub(v, substitution) && right.isFreeSub(v, substitution);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "->" + right.toString() + ")";
    }

    public String rightPart() {
        return this.right.toString();
    }

    public String leftPart() {
        return this.left.toString();
    }
}
