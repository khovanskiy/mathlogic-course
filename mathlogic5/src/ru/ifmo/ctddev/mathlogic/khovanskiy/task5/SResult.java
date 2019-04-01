package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;

public class SResult {
    public boolean isSubstituted;
    public Term subion;

    public SResult(boolean substituted, Term substitution) {
        this.isSubstituted = substituted;
        this.subion = substitution;
    }
}
