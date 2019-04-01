package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;

public class DescriptionE 
{
    public F expr;
    public ProofEnum type;
    public int axiomId;
    public F s;

    public DescriptionE(F expression, ProofEnum type, int axiomNumber, F s) 
    {
        this.expr = expression;
        this.type = type;
        this.axiomId = axiomNumber;
        this.s = s;
    }

    @Override
    public String toString() 
    {
        StringBuilder sb = new StringBuilder();
        sb.append(expr + " // " + type + " ");
        if (axiomId != -1) 
        {
            sb.append(axiomId + "");
        }
        if (s != null) 
        {
            sb.append("from " + s);
        }
        return sb.toString();
    }
}
