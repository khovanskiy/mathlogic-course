package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PChecker
{
    public static CResult check(Proof proof)
    {
        Matcher.generatePatterns();
        HashMap<String, List<F>> implicationByRightPart = new HashMap<String, List<F>>();
        HashSet<String> correctStatements = new HashSet<String>();
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
            if (Matcher.equalsA(expression) == -1)
            {
                if (!correctStatements.contains(statementString))
                {
                    boolean isModusPonens = false;
                    if (implicationByRightPart.containsKey(statementString))
                    {
                        List<F> expressions = implicationByRightPart.get(statementString);
                        for (int it = 0; it < expressions.size(); it++)
                        {
                            isModusPonens |= correctStatements.contains(((Implication) expressions.get(it)).leftPart());
                        }
                    }
                    if (!isModusPonens)
                    {
                        String info = "";
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
                                                    info = "используется правило с квантором по переменной " + v.toString()
                                                            + ", входящей свободно в допущение " + proof.hyp.get(j).toString() + ".";
                                                }
                                            }
                                        }
                                        else
                                        {
                                            info = "переменная " + v.toString() + " входит свободно в формулу " + impl.left.toString() + ".";
                                        }
                                    }
                                }
                                else if (statement.approvalType == ApprovalEnum.EXISTS)
                                {
                                    Exists exists = (Exists)statement;
                                    SResult result = exists.expression.getSubstitution(exists.variable, impl.left);
                                    if (result.isSubstituted)
                                    {
                                        if ((result.substitution == null) || exists.expression.isFreeToSubstitute(result.substitution, exists.variable))
                                        {
                                            correct = true;
                                            for (int j = 0; j < proof.hyp.size(); ++j)
                                            {
                                                if (proof.hyp.get(j).isFree(exists.variable))
                                                {
                                                    correct = false;
                                                    info = "используется схема аксиом с квантором по переменной " + exists.variable.toString()
                                                            + ", входящей свободно в допущение " + proof.hyp.get(j).toString() + ".";
                                                }
                                            }
                                        }
                                        else
                                        {
                                            info = "терм " + result.substitution.toString() + " не свободен для подстановки в формулу "
                                                    + exists.expression.toString() + " вместо переменной " + exists.variable.toString() + ".";
                                        }
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
                                                || any.exp.isFreeToSubstitute(result.substitution, any.var)) {
                                            correct = true;
                                            for (int j = 0; j < proof.hyp.size(); ++j)
                                            {
                                                if (proof.hyp.get(j).isFree(any.var))
                                                {
                                                    correct = false;
                                                    info = "используется схема аксиом с квантором по переменной " + any.var.toString()
                                                            + ", входящей свободно в допущение " + proof.hyp.get(j).toString() + ".";
                                                }
                                            }
                                        }
                                        else
                                        {
                                            info = "терм " + result.substitution.toString() + " не свободен для подстановки в формулу "
                                                    + any.exp.toString() + " вместо переменной " + any.var.toString() + ".";
                                        }
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
                                                    info = "используется правило с квантором по переменной " + v + ", входящей свободно в допущение " + proof.hyp.get(j) + ".";
                                                }
                                            }
                                        }
                                        else
                                        {
                                            info = "переменная " + v + " входит свободно в формулу " + impl.right + ".";
                                        }
                                    }
                                }
                            }
                        }
                        if (!correct)
                        {
                            return new CResult(i, info);
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
        return new CResult(-1, "");
    }
}
