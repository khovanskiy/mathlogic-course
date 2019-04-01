package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;

public class AxiomPattern
{
    public static F a, b, c;
    private F e;

    public AxiomPattern(F e)
    {
        this.e = e;
    }

    public boolean expressionMatches(F e)
    {
        return this.e.equalsWithAxiom(e);
    }

    public static void release()
    {
        a = null;
        b = null;
        c = null;
    }
}
