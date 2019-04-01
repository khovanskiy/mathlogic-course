package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;

public class SResult
{
    public boolean isSubstituted;
    public Term substitution;

    public SResult(boolean isSubstituted, Term substitution)
    {
        this.isSubstituted = isSubstituted;
        this.substitution = substitution;
    }
}
