package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;


public class PLexeme extends Lex
{
    public Proposition proposition;

    public PLexeme(Proposition proposition)
    {
        this.type = LexEnum.PROP;
        this.proposition = proposition;
    }
}
