package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;


public class AxiomApproval extends F 
{
    private Letter letter;

    public AxiomApproval(Letter letter) 
    {
        this.letter = letter;
        this.type = EEnum.AXIOM;
    }

    @Override
    public boolean equalsE(F e) 
    {
        if (e.type == EEnum.AXIOM)
        {
            return letter == ((AxiomApproval)e).letter;
        }
        return false;
    }

    @Override
    public boolean equalsWithAxioms(F e) 
    {
        switch (letter) 
        {
            case A:
            {
                if (AxiomPattern.a == null) 
                {
                    AxiomPattern.a = e;
                    return true;
                } 
                return AxiomPattern.a.equalsE(e);
            }
            case B:
            {
                if (AxiomPattern.b == null) 
                {
                    AxiomPattern.b = e;
                    return true;
                } 
                return AxiomPattern.b.equalsE(e);
            }
            case C:
            {
                if (AxiomPattern.c == null) 
                {
                    AxiomPattern.c = e;
                    return true;
                }
                return AxiomPattern.c.equalsE(e);
            }
        }
        return true;
    }

    @Override
    public boolean isFreeSub(Variable v, Variable substitution) 
    {
        return false;
    }

    @Override
    public boolean isFree(Variable v) 
    {
        return false;
    }

    @Override
    public F getSubstituted(Variable v, Term substitution) 
    {
        return null;
    }

    @Override
    public SResult getSubstitution(Variable v, F substituted) 
    {
        return null;
    }
}
