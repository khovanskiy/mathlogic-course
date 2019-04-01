package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;


public class NLexeme extends Lex
{
    public String label;
    public boolean isupper;

    public NLexeme(String name, boolean isupper)
    {
        this.label = name;
        this.isupper = isupper;
        this.type = LexEnum.NAME;
    }
}
