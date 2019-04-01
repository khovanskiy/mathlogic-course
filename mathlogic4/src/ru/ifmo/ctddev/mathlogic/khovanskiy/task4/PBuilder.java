package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PBuilder
{
    public static List<F> makeSelfImpl(F e)
    {
        List<F> list = new ArrayList<F>();
        list.add(F.impl(e, F.impl(e, e)));
        list.add(F.impl(e, F.impl(F.impl(e, e), e)));
        list.add(F.impl(F.impl(e, F.impl(e, e)),
                F.impl(F.impl(e, F.impl(F.impl(e, e), e)),
                        F.impl(e, e))));
        list.add(F.impl(F.impl(e, F.impl(F.impl(e, e), e)),
                F.impl(e, e)));
        list.add(F.impl(e, e));
        return list;
    }

    public static List<F> makeAxiomImpl(F axiom, F expression)
    {
        List<F> list = new ArrayList<F>();
        F implication = F.impl(expression, axiom);
        list.add(F.impl(axiom, implication));
        list.add(axiom);
        list.add(implication);
        return list;
    }

    public static List<F> makeNegProof(F reason, F contradiction)
    {
        List<F> list = new ArrayList<F>();
        F conclusion = F.neg(reason);
        F notContradiction = F.neg(contradiction);
        list.add(F.impl(
                F.impl(reason, contradiction),
                F.impl(F.impl(reason, notContradiction), conclusion)));
        list.add(F.impl(F.impl(reason, notContradiction), conclusion));
        list.add(conclusion);
        return list;
    }

    public static List<F> makeMPImpl(F l, F r, F e)
    {
        List<F> list = new ArrayList<F>();
        list.add(F.impl(F.impl(e, l), F.impl(F.impl(e, F.impl(l, r)), F.impl(e, r))));
        list.add(F.impl(F.impl(e, F.impl(l, r)), F.impl(e, r)));
        list.add(F.impl(e, r));
        return list;
    }

    public static Proof makePropDeductionStep(Proof s)
    {
        if (s.hyp.size() == 0)
        {
            return s;
        }
        else
        {
            HashMap<String, List<F>> implicationByRightPart = new HashMap<String, List<F>>();
            HashSet<String> correctStatements = new HashSet<String>();
            HashSet<String> foundStatements = new HashSet<String>();
            F statement;
            for (int i = 0; i < s.hyp.size() - 1; ++i)
            {
                statement = s.hyp.get(i);
                correctStatements.add(statement.toString());
            }
            List<F> newHypothesis = new ArrayList<F>();
            for (int i = 0; i < s.hyp.size() - 1; ++i)
            {
                newHypothesis.add(s.hyp.get(i));
            }
            F deductionExpression = s.hyp.get(s.hyp.size() - 1);
            F newConclusion = F.impl(deductionExpression, s.con);
            List<F> newStatements = new ArrayList<F>();
            List<F> selfImplication = makeSelfImpl(deductionExpression);
            for (int i = 0; i < s.sta.size(); ++i)
            {
                statement = s.sta.get(i);
                List<F> newExpressions;
                if (statement.equalsE(deductionExpression))
                {
                    newExpressions = selfImplication;
                }
                else if ((Matcher.equalsA(statement) != -1) || correctStatements.contains(statement.toString()))
                {
                    newExpressions = makeAxiomImpl(statement, deductionExpression);
                }
                else
                {
                    List<F> expressions = implicationByRightPart.get(statement.toString());
                    int k = 0;
                    boolean found = false;
                    F leftPart = null;
                    while (!found && (k < expressions.size()))
                    {
                        found = foundStatements.contains(((Implication) expressions.get(k)).leftPart());
                        if (found) {
                            leftPart = ((Implication) expressions.get(k)).left;
                        }
                        k++;
                    }
                    newExpressions = makeMPImpl(leftPart, statement, deductionExpression);
                }
                newStatements.addAll(newExpressions);
                if (statement.type == EEnum.IMPL)
                {
                    Implication implication = (Implication) statement;
                    String rightPart = implication.rightPart();
                    if (implicationByRightPart.containsKey(rightPart))
                    {
                        implicationByRightPart.get(rightPart).add(implication);
                    }
                    else
                    {
                        List<F> list = new ArrayList<F>();
                        list.add(implication);
                        implicationByRightPart.put(rightPart, list);
                    }
                }
                foundStatements.add(statement.toString());
            }
            return new Proof(newHypothesis, newStatements, newConclusion);
        }
    }

    public static List<F> makeContraposition(F left, F right)
    {
        List<F> hypothesis = new ArrayList<F>();
        F notRight = F.neg(right);
        F implication = F.impl(left, right);
        hypothesis.add(implication);
        hypothesis.add(notRight);
        F conclusion = F.neg(left);
        List<F> statements = new ArrayList<F>();
        statements.addAll(makeAxiomImpl(notRight, left));
        statements.add(implication);
        statements.addAll(makeNegProof(left, right));
        Proof result = makePropDeductionStep(makePropDeductionStep(new Proof(hypothesis, statements, conclusion)));
        return result.sta;
    }

    public static DescriptionP makeImplToAnd(F left, F right, F conclusion)
    {
        List<F> hypothesis = new ArrayList<F>();
        hypothesis.add(F.impl(left, F.impl(right, conclusion)));
        F and = F.con(left, right);
        hypothesis.add(and);
        List<F> statements = new ArrayList<F>();
        statements.add(and);
        statements.add(F.impl(and, left));
        statements.add(left);
        statements.add(F.impl(and, right));
        statements.add(right);
        statements.add(F.impl(left, F.impl(right, conclusion)));
        statements.add(F.impl(right, conclusion));
        statements.add(conclusion);
        Proof proof = new Proof(hypothesis, statements, conclusion);
        Proof result = makePropDeductionStep(makePropDeductionStep(proof));
        return detail(result);
    }

    public static DescriptionP doAndToImplication(F left, F right, F conclusion)
    {
        List<F> hypothesis = new ArrayList<F>();
        F and = F.con(left, right);
        hypothesis.add(F.impl(and, conclusion));
        hypothesis.add(left);
        hypothesis.add(right);
        List<F> statements = new ArrayList<F>();
        statements.add(F.impl(left, F.impl(right, and)));
        statements.add(left);
        statements.add(F.impl(right, and));
        statements.add(right);
        statements.add(and);
        statements.add(F.impl(and, conclusion));
        statements.add(conclusion);
        Proof proof = new Proof(hypothesis, statements, conclusion);
        Proof result = makePropDeductionStep(makePropDeductionStep(makePropDeductionStep(proof)));
        return detail(result);
    }

    public static DescriptionP swapArguments(F left, F right, F conclusion)
    {
        List<F> hypothesis = new ArrayList<F>();
        hypothesis.add(F.impl(left, F.impl(right, conclusion)));
        hypothesis.add(right);
        hypothesis.add(left);
        List<F> statements = new ArrayList<F>();
        statements.add(F.impl(left, F.impl(right, conclusion)));
        statements.add(left);
        statements.add(F.impl(right, conclusion));
        statements.add(right);
        statements.add(conclusion);
        Proof proof = new Proof(hypothesis, statements, conclusion);
        Proof result = makePropDeductionStep(makePropDeductionStep(makePropDeductionStep(proof)));
        return detail(result);
    }

    public static DescriptionP detail(Proof proof)
    {
        Matcher.generatePatterns();
        HashMap<String, List<F>> implicationByRightPart = new HashMap<String, List<F>>();
        HashSet<String> correctStatements = new HashSet<String>();
        List<DescriptionE> edescribes = new ArrayList<DescriptionE>();
        for (int i = 0; i < proof.hyp.size(); ++i)
        {
            F statement = proof.hyp.get(i);
            if (statement.type == EEnum.IMPL)
            {
                String rightPart = ((Implication) statement).rightPart();
                if (implicationByRightPart.containsKey(rightPart))
                {
                    implicationByRightPart.get(rightPart).add(statement);
                }
                else
                {
                    List<F> list = new ArrayList<F>();
                    list.add(statement);
                    implicationByRightPart.put(rightPart, list);
                }
            }
            correctStatements.add(statement.toString());
        }
        for (int i = 0; i < proof.sta.size(); ++i)
        {
            F expression = proof.sta.get(i);
            String statementString = expression.toString();
            int axiomNumber = Matcher.equalsA(expression);
            if (axiomNumber != -1)
            {
                edescribes.add(new DescriptionE(expression, ProofEnum.AXIOM, axiomNumber, null));
            }
            else
            {
                if (correctStatements.contains(statementString))
                {
                    edescribes.add(new DescriptionE(expression, ProofEnum.HYP, -1, null));
                }
                else
                {
                    boolean isMP = false;
                    F implication = null;
                    if (implicationByRightPart.containsKey(statementString))
                    {
                        List<F> expressions = implicationByRightPart.get(statementString);
                        for (int it = 0; it < expressions.size(); it++)
                        {
                            if (correctStatements.contains(((Implication) expressions.get(it)).leftPart()))
                            {
                                isMP = true;
                                implication = (Implication)expressions.get(it);
                            }
                        }
                    }
                    if (isMP)
                    {
                        edescribes.add(new DescriptionE(expression, ProofEnum.MP, -1, implication));
                    }
                    else
                    {
                        boolean correct = false;
                        if (expression.type == EEnum.IMPL)
                        {
                            Implication impl = (Implication)expression;
                            if (impl.right.type == EEnum.APPROVAL)
                            {
                                Approval statement = (Approval)impl.right;
                                if (statement.approvalType == ApprovalEnum.ANY)
                                {
                                    Any any = (Any) statement;
                                    if (correctStatements.contains(F.impl(impl.left, any.exp).toString()))
                                    {
                                        Variable v = any.var;
                                        if (!impl.left.isFree(v))
                                        {
                                            correct = true;
                                            for (int j = 0; j < proof.hyp.size(); ++j)
                                            {
                                                if (proof.hyp.get(j).isFree(v))
                                                {
                                                    correct = false;
                                                }
                                            }
                                        }
                                    }
                                    if (correct)
                                    {
                                        edescribes.add(new DescriptionE(expression, ProofEnum.ANY,
                                                -1, F.impl(impl.left, any.exp)));
                                    }
                                }
                                else if (statement.approvalType == ApprovalEnum.EXISTS)
                                {
                                    Exists exists = (Exists)statement;
                                    SResult result = exists.expression.getSubstitution(exists.variable, impl.left);
                                    if (result.isSubstituted)
                                    {
                                        if ((result.substitution == null)
                                                || exists.expression.isFreeToSubstitute(result.substitution, exists.variable))
                                        {
                                            correct = true;
                                            for (int j = 0; j < proof.hyp.size(); ++j)
                                            {
                                                if (proof.hyp.get(j).isFree(exists.variable))
                                                {
                                                    correct = false;
                                                }
                                            }
                                        }
                                    }
                                    if (correct)
                                    {
                                        edescribes.add(new DescriptionE(expression, ProofEnum.AXIOM, 12, null));
                                    }
                                }
                            }
                            if (!correct && impl.left.type == EEnum.APPROVAL)
                            {
                                Approval statement = (Approval)impl.left;
                                if (statement.approvalType == ApprovalEnum.ANY)
                                {
                                    Any any = (Any)statement;
                                    SResult result = any.exp.getSubstitution(any.var, impl.right);
                                    if (result.isSubstituted)
                                    {
                                        if ((result.substitution == null)
                                                || any.exp.isFreeToSubstitute(result.substitution, any.var))
                                        {
                                            correct = true;
                                            for (int j = 0; j < proof.hyp.size(); ++j)
                                            {
                                                if (proof.hyp.get(j).isFree(any.var))
                                                {
                                                    correct = false;
                                                }
                                            }
                                        }
                                    }
                                    if (correct)
                                    {
                                        edescribes.add(new DescriptionE(expression, ProofEnum.AXIOM, 11, null));
                                    }
                                }
                                else if (statement.approvalType == ApprovalEnum.EXISTS)
                                {
                                    Exists exists = (Exists)statement;
                                    Variable v = exists.variable;
                                    if (correctStatements.contains(F.impl(exists.expression, impl.right).toString()))
                                    {
                                        if (!impl.right.isFree(v))
                                        {
                                            correct = true;
                                            for (int j = 0; j < proof.hyp.size(); ++j)
                                            {
                                                if (proof.hyp.get(j).isFree(v))
                                                {
                                                    correct = false;
                                                }
                                            }
                                        }
                                        if (correct)
                                        {
                                            edescribes.add(new DescriptionE(expression, ProofEnum.EXISTS,
                                                    -1, F.impl(exists.expression, impl.right)));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (expression.type == EEnum.IMPL)
            {
                String rightPart = ((Implication) expression).rightPart();
                if (implicationByRightPart.containsKey(rightPart))
                {
                    implicationByRightPart.get(rightPart).add(expression);
                }
                else
                {
                    List<F> list = new ArrayList<F>();
                    list.add(expression);
                    implicationByRightPart.put(rightPart, list);
                }
            }
            correctStatements.add(statementString);
        }
        return new DescriptionP(proof.hyp, edescribes, proof.con);
    }

    public static DescriptionP makeDeductionStep(Proof proof)
    {
        return doDeductionStep(detail(proof));
    }

    public static DescriptionP doDeductionStep(DescriptionP proof)
    {
        if (proof.hyp.size() == 0)
        {
            return proof;
        }
        else
        {
            List<F> hypothesis = new ArrayList<F>();
            for (int i = 0; i < proof.hyp.size() - 1; ++i)
            {
                hypothesis.add(proof.hyp.get(i));
            }
            F deductionExpression = proof.hyp.get(proof.hyp.size() - 1);
            F conclusion = F.impl(deductionExpression, proof.con);
            List<DescriptionE> statements = new ArrayList<DescriptionE>();
            DescriptionE detailedExpression;
            List<F> exprs;
            F axiom;
            Implication implication;
            DescriptionP proof1, proof2;
            Exists exists;
            Any any;
            for (int i = 0; i < proof.sta.size(); ++i)
            {
                detailedExpression = proof.sta.get(i);
                switch (detailedExpression.type)
                {
                    case AXIOM:
                    {
                        exprs = makeAxiomImpl(detailedExpression.expr, deductionExpression);
                        statements.add(new DescriptionE(exprs.get(0), ProofEnum.AXIOM, 1, null));
                        statements.add(detailedExpression);
                        statements.add(new DescriptionE(exprs.get(2), ProofEnum.MP, -1, exprs.get(0)));
                    } break;
                    case HYP:
                    {
                        if (detailedExpression.expr.equalsE(deductionExpression))
                        {
                            exprs = makeSelfImpl(deductionExpression);
                            statements.add(new DescriptionE(exprs.get(0), ProofEnum.AXIOM, 1, null));
                            statements.add(new DescriptionE(exprs.get(1), ProofEnum.AXIOM, 1, null));
                            statements.add(new DescriptionE(exprs.get(2), ProofEnum.AXIOM, 2, null));
                            statements.add(new DescriptionE(exprs.get(3), ProofEnum.MP, -1, exprs.get(2)));
                            statements.add(new DescriptionE(exprs.get(4), ProofEnum.MP, -1, exprs.get(3)));
                        }
                        else
                        {
                            exprs = makeAxiomImpl(detailedExpression.expr, deductionExpression);
                            statements.add(new DescriptionE(exprs.get(0), ProofEnum.AXIOM, 1, null));
                            statements.add(detailedExpression);
                            statements.add(new DescriptionE(exprs.get(2), ProofEnum.MP, -1, exprs.get(0)));
                        }
                    } break;
                    case MP:
                    {
                        implication = (Implication)detailedExpression.s;
                        axiom = F.impl(
                                F.impl(deductionExpression, implication.left),
                                F.impl(
                                        F.impl(deductionExpression, implication),
                                        F.impl(deductionExpression, detailedExpression.expr)));
                        statements.add(new DescriptionE(axiom, ProofEnum.AXIOM, 2, null));
                        statements.add(new DescriptionE(((Implication)axiom).right, ProofEnum.MP, -1, axiom));
                        statements.add(new DescriptionE(F.impl(deductionExpression, detailedExpression.expr),
                                ProofEnum.MP, -1, ((Implication)axiom).right));
                    } break;
                    case ANY:
                    {
                        any = (Any)((Implication)detailedExpression.expr).right;
                        implication = (Implication)detailedExpression.s;
                        proof1 = makeImplToAnd(deductionExpression, implication.left, implication.right);
                        statements.addAll(proof1.sta);
                        statements.add(new DescriptionE(F.impl(
                                F.con(deductionExpression, implication.left), implication.right),
                                ProofEnum.MP, -1, proof1.con));
                        statements.add(new DescriptionE(F.impl(
                                F.con(deductionExpression, implication.left),
                                any), ProofEnum.ANY, -1, statements.get(statements.size() - 1).expr));
                        proof2 = doAndToImplication(deductionExpression, implication.left, any);
                        statements.addAll(proof2.sta);
                        statements.add(new DescriptionE(F.impl(deductionExpression, detailedExpression.expr),
                                ProofEnum.MP, -1, proof2.con));
                    } break;
                    case EXISTS:
                    {
                        exists = (Exists)((Implication)detailedExpression.expr).left;
                        implication = (Implication)detailedExpression.s;
                        proof1 = swapArguments(deductionExpression, implication.left, implication.right);
                        statements.addAll(proof1.sta);
                        statements.add(new DescriptionE(F.impl(
                                implication.left, F.impl(deductionExpression, implication.right)),
                                ProofEnum.MP, -1, proof1.con));
                        statements.add(new DescriptionE(F.impl(
                                exists, F.impl(deductionExpression, implication.right)),
                                ProofEnum.EXISTS, -1, statements.get(statements.size() - 1).expr));
                        proof2 = swapArguments(exists, deductionExpression, implication.right);
                        statements.addAll(proof2.sta);
                        statements.add(new DescriptionE(F.impl(deductionExpression, detailedExpression.expr),
                                ProofEnum.MP, -1, proof2.con));
                    } break;
                }
            }
            return new DescriptionP(hypothesis, statements, conclusion);
        }
    }
}
