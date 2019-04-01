package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;

public class NLexeme extends Lex {
    public String name;
    public boolean upperCase;

    public NLexeme(String name, boolean upperCase) {
        this.name = name;
        this.upperCase = upperCase;
        this.type = LexEnum.NAME;
    }
}
