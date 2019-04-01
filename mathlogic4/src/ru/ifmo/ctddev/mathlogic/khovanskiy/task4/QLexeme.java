package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;


public class QLexeme extends Lex
{
    public ApprovalEnum approvalType;
    public Variable variable;

    public QLexeme(ApprovalEnum type, Variable variable)
    {
        this.type = LexEnum.QUAN;
        this.approvalType = type;
        this.variable = variable;
    }
}
