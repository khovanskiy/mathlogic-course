package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;

import java.util.List;

public class Proof
{
    public List<F> hyp;
    public List<F> sta;
    public F con;

    public Proof(List<F> hypothesis, List<F> statements, F conclusion)
    {
        this.hyp = hypothesis;
        this.sta = statements;
        this.con = conclusion;
    }
}
