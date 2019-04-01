package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;

public class Matcher 
{
    public static AxiomPattern[] patterns;
    public static final int AXIOMS_COUNT = 10;

    public static void generatePatterns()
    {
        patterns = new AxiomPattern[AXIOMS_COUNT];
        patterns[0] = new AxiomPattern(
                new Implication(
                    new AxiomApproval(Letter.A),
                    new Implication(
                            new AxiomApproval(Letter.B),
                            new AxiomApproval(Letter.A)
                )
        ));
        patterns[1] = new AxiomPattern(
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
        patterns[2] = new AxiomPattern(
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
        patterns[3] = new AxiomPattern(
                new Implication(
                        new Conjunction(
                                new AxiomApproval(Letter.A),
                                new AxiomApproval(Letter.B)
                        ),
                        new AxiomApproval(Letter.A)
                )
        );
        patterns[4] = new AxiomPattern(
                new Implication(
                        new Conjunction(
                                new AxiomApproval(Letter.A),
                                new AxiomApproval(Letter.B)
                        ),
                        new AxiomApproval(Letter.B)
                )
        );
        patterns[5] = new AxiomPattern(
                new Implication(
                        new AxiomApproval(Letter.A),
                        new Disjunction(
                                new AxiomApproval(Letter.A),
                                new AxiomApproval(Letter.B)
                        )
                )
        );
        patterns[6] = new AxiomPattern(
                new Implication(
                        new AxiomApproval(Letter.B),
                        new Disjunction(
                                new AxiomApproval(Letter.A),
                                new AxiomApproval(Letter.B)
                        )
                )
        );
        patterns[7] = new AxiomPattern(
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
        patterns[8] = new AxiomPattern(
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
        patterns[9] = new AxiomPattern(
                new Implication(
                        new Negation(
                                new Negation(
                                        new AxiomApproval(Letter.A)
                                )
                        ),
                        new AxiomApproval(Letter.A)
                )
        );
    }

    public static int equalsA(F e) 
    {
        for (int i = 0; i < AXIOMS_COUNT; ++i) 
        {
            AxiomPattern.release();
            if (patterns[i].expressionMatches(e))
            {
            	return i + 1;
            }
        }
        return -1;
    }
}
