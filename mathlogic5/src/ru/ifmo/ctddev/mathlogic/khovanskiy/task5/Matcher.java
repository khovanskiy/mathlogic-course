package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;

import java.util.ArrayList;
import java.util.List;

public class Matcher
{
    public static AxiomPattern[] logicalPatterns;
    public static F[] arithmeticPatterns;
    public static final int LOGICAL_COUNT = 10;
    public static final int ARITHMETIC_COUNT = 8;

    public static void generatePatterns()
    {
        logicalPatterns = new AxiomPattern[LOGICAL_COUNT];
        logicalPatterns[0] = new AxiomPattern(
                new Implication(
                    new AxiomApproval(Letter.A),
                    new Implication(
                            new AxiomApproval(Letter.B),
                            new AxiomApproval(Letter.A)
                )
        ));
        logicalPatterns[1] = new AxiomPattern(
                new Implication(
                        new Implication(
                                new AxiomApproval(Letter.A),
                                new AxiomApproval(Letter.B)
                        ),
                        new Implication(
                                new Implication(
                                        new AxiomApproval(Letter.A),
                                        new Implication(
                                                new AxiomApproval(Letter.B),
                                                new AxiomApproval(Letter.C)
                                        )
                                ),
                                new Implication(
                                        new AxiomApproval(Letter.A),
                                        new AxiomApproval(Letter.C)
                                )
                        )
                )
        );
        logicalPatterns[2] = new AxiomPattern(
                new Implication(
                        new AxiomApproval(Letter.A),
                        new Implication(
                                new AxiomApproval(Letter.B),
                                new Conjunction(
                                        new AxiomApproval(Letter.A),
                                        new AxiomApproval(Letter.B)
                                )
                        )
                )
        );
        logicalPatterns[3] = new AxiomPattern(
                new Implication(
                        new Conjunction(
                                new AxiomApproval(Letter.A),
                                new AxiomApproval(Letter.B)
                        ),
                        new AxiomApproval(Letter.A)
                )
        );
        logicalPatterns[4] = new AxiomPattern(
                new Implication(
                        new Conjunction(
                                new AxiomApproval(Letter.A),
                                new AxiomApproval(Letter.B)
                        ),
                        new AxiomApproval(Letter.B)
                )
        );
        logicalPatterns[5] = new AxiomPattern(
                new Implication(
                        new AxiomApproval(Letter.A),
                        new Disjunction(
                                new AxiomApproval(Letter.A),
                                new AxiomApproval(Letter.B)
                        )
                )
        );
        logicalPatterns[6] = new AxiomPattern(
                new Implication(
                        new AxiomApproval(Letter.B),
                        new Disjunction(
                                new AxiomApproval(Letter.A),
                                new AxiomApproval(Letter.B)
                        )
                )
        );
        logicalPatterns[7] = new AxiomPattern(
                new Implication(
                        new Implication(
                                new AxiomApproval(Letter.A),
                                new AxiomApproval(Letter.C)
                        ),
                        new Implication(
                                new Implication(
                                        new AxiomApproval(Letter.B),
                                        new AxiomApproval(Letter.C)
                                ),
                                new Implication(
                                        new Disjunction(
                                                new AxiomApproval(Letter.A),
                                                new AxiomApproval(Letter.B)
                                        ),
                                        new AxiomApproval(Letter.C)
                                )
                        )
                )
        );
        logicalPatterns[8] = new AxiomPattern(
                new Implication(
                        new Implication(
                                new AxiomApproval(Letter.A),
                                new AxiomApproval(Letter.B)
                        ),
                        new Implication(
                                new Implication(
                                        new AxiomApproval(Letter.A),
                                        new Negation(
                                                new AxiomApproval(Letter.B)
                                        )
                                ),
                                new Negation(
                                        new AxiomApproval(Letter.A)
                                )
                        )
                )
        );
        logicalPatterns[9] = new AxiomPattern(
                new Implication(
                        new Negation(
                                new Negation(
                                        new AxiomApproval(Letter.A)
                                )
                        ),
                        new AxiomApproval(Letter.A)
                )
        );
        arithmeticPatterns = new F[ARITHMETIC_COUNT];
        List<Term> args1 = new ArrayList<Term>(), args2 = new ArrayList<Term>();
        args1.add(Term.getVar("a"));
        args1.add(Term.getVar("b"));
        Predicate p1 = F.pred("equals", args1);
        args1.clear();
        args2.add(Term.getVar("a"));
        args1.add(Term.getFunc("increment", args2));
        args2.clear();
        args2.add(Term.getVar("b"));
        args1.add(Term.getFunc("increment", args2));
        Predicate p2 = F.pred("equals", args1);
        arithmeticPatterns[0] = F.impl(p1, p2);
        args1.clear();
        args1.add(Term.getVar("a"));
        args1.add(Term.getVar("b"));
        p1 = F.pred("equals", args1);
        args1.clear();
        args1.add(Term.getVar("a"));
        args1.add(Term.getVar("c"));
        p2 = F.pred("equals", args1);
        args1.clear();
        args1.add(Term.getVar("b"));
        args1.add(Term.getVar("c"));
        Predicate p3 = F.pred("equals", args1);
        arithmeticPatterns[1] = F.impl(p1, F.impl(p2, p3));
        args1.clear();
        args2.clear();
        args2.add(Term.getVar("a"));
        args1.add(Term.getFunc("increment", args2));
        args2.clear();
        args2.add(Term.getVar("b"));
        args1.add(Term.getFunc("increment", args2));
        p1 = F.pred("equals", args1);
        args1.clear();
        args1.add(Term.getVar("a"));
        args1.add(Term.getVar("b"));
        p2 = F.pred("equals", args1);
        arithmeticPatterns[2] = F.impl(p1, p2);
        args1.clear();
        args2.clear();
        args2.add(Term.getVar("a"));
        args1.add(Term.getFunc("increment", args2));
        args2.clear();
        args1.add(Term.getFunc("zero", args2));
        arithmeticPatterns[3] = F.neg(F.pred("equals", args1));
        args1.clear();
        args2.clear();
        List<Term> args3 = new ArrayList<Term>();
        args3.add(Term.getVar("b"));
        args2.add(Term.getVar("a"));
        args2.add(Term.getFunc("increment", args3));
        args1.add(Term.getFunc("add", args2));
        args3.clear();
        args3.add(Term.getVar("a"));
        args3.add(Term.getVar("b"));
        args2.clear();
        args2.add(Term.getFunc("add", args3));
        args1.add(Term.getFunc("increment", args2));
        arithmeticPatterns[4] = F.pred("equals", args1);
        args1.clear();
        args2.clear();
        args3.clear();
        args2.add(Term.getVar("a"));
        args2.add(Term.getFunc("zero", args3));
        args1.add(Term.getFunc("add", args2));
        args1.add(Term.getVar("a"));
        arithmeticPatterns[5] = F.pred("equals", args1);
        args1.clear();
        args2.clear();
        args3.clear();
        args2.add(Term.getVar("a"));
        args2.add(Term.getFunc("zero", args3));
        args1.add(Term.getFunc("multiply", args2));
        args1.add(Term.getFunc("zero", args3));
        arithmeticPatterns[6] = F.pred("equals", args1);
        args1.clear();
        args2.clear();
        args3.clear();
        args3.add(Term.getVar("b"));
        args2.add(Term.getVar("a"));
        args2.add(Term.getFunc("increment", args3));
        args1.add(Term.getFunc("multiply", args2));
        args3.clear();
        args3.add(Term.getVar("a"));
        args3.add(Term.getVar("b"));
        args2.clear();
        args2.add(Term.getFunc("multiply", args3));
        args2.add(Term.getVar("a"));
        args1.add(Term.getFunc("add", args2));
        arithmeticPatterns[7] = F.pred("equals", args1);
    }

    public static int equalsA(F e) {
        for (int i = 0; i < LOGICAL_COUNT; i++)
        {
            AxiomPattern.release();
            if (logicalPatterns[i].expressionMatches(e))
            {
            	return i + 1;
            }
        }
        for (int i = 0; i < ARITHMETIC_COUNT; i++)
        {
            if (arithmeticPatterns[i].equalsE(e))
            {
            	return i + 13;
            }
        }
        return -1;
    }


}
