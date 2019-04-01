package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;

public class Variable extends Term
{
    public String name;

    public Variable(String name)
    {
        this.name = name;
        this.type = TermEnum.VARIABLE;
    }

    @Override
    public String toString()
    {
        return this.name;
    }

    @Override
    public boolean equalsT(Term term)
    {
        if (term.type == TermEnum.VARIABLE)
        {
            return ((Variable)term).name.equals(this.name);
        }
        return false;
    }

    @Override
    public boolean contains(Variable v)
    {
        return this.name.equals(v.name);
    }

    @Override
    public Term getSubstituted(Variable v, Term substitution)
    {
        if (this.name.equals(v.name))
        {
            return substitution;
        }
        return this;
    }

    @Override
    public SResult getSubstitution(Variable v, Term substituted)
    {
        if (this.name.equals(v.name))
        {
            return new SResult(true, substituted);
        }
        return new SResult(true, null);
    }


}
