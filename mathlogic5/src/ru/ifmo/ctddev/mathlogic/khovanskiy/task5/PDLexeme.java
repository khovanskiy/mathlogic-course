package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;


public class PDLexeme extends Lex {
    public Predicate predicate;

    public PDLexeme(Predicate predicate) {
        this.predicate = predicate;
        this.type = LexEnum.PRED;
    }

}
