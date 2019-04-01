package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;

import java.util.List;

public class DescriptionP 
{
    public List<F> hyp;
    public List<DescriptionE> sta;
    public F con;

    public DescriptionP(List<F> hypothesis, List<DescriptionE> statements, F conclusion)
    {
        this.hyp = hypothesis;
        this.sta = statements;
        this.con = conclusion;
    }
}
