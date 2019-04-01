package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;


public class DescriptionE
{
    public F expression;
    public ProofEnum type;
    public int axiomNumber;
    public F source;

    public DescriptionE(F expression, ProofEnum type,
                              int axiomNumber, F source) {
        this.expression = expression;
        this.type = type;
        this.axiomNumber = axiomNumber;
        this.source = source;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(expression.toString() + " // " + type + " ");
        if (axiomNumber != -1) {
            builder.append(axiomNumber + "");
        }
        if (source != null) {
            builder.append("from " + source.toString());
        }
        return builder.toString();
    }
}
