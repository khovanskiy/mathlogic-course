package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PChecker {

    public static CResult check(Proof proof)
    {
        Matcher.generatePatterns();
        HashMap<String, List<F>> implicationByRightPart = new HashMap<String, List<F>>();
        HashSet<String> correctStatements = new HashSet<String>();
        for (int i = 0; i < proof.hyp.size(); i++)
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
        for (int i = 0; i < proof.sta.size(); i++)
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
                                    Any forAll = (Any) statement;
                                    if (correctStatements.contains(F.impl(impl.left, forAll.expression).toString()))
                                    {
                                        Variable v = forAll.variable;
                                        if (!impl.left.isFree(v)) {
                                            correct = true;
                                            for (int j = 0; j < proof.hyp.size(); j++) {
                                                if (proof.hyp.get(j).isFree(v)) {
                                                    correct = false;
                                                    info = "используется правило с квантором по переменной " + v.toString()
                                                            + ", входящей свободно в допущение " + proof.hyp.get(j).toString() + ".";
                                                }
                                            }
                                        } else {
                                            info = "переменная " + v.toString() + " входит свободно в формулу " + impl.left.toString() + ".";
                                        }
                                    }
                                } else if (statement.approvalType == ApprovalEnum.EXISTS) {
                                    Exists exists = (Exists)statement;
                                    SResult result = exists.expression.getSubstitution(exists.variable, impl.left);
                                    if (result.isSubstituted) {
                                        if ((result.subion == null)
                                                || exists.expression.isFreeToSub(result.subion, exists.variable)) {
                                            correct = true;
                                            for (int j = 0; j < proof.hyp.size(); j++) {
                                                if (proof.hyp.get(j).isFree(exists.variable)) {
                                                    correct = false;
                                                    info = "используется схема аксиом с квантором по переменной " + exists.variable.toString()
                                                            + ", входящей свободно в допущение " + proof.hyp.get(j).toString() + ".";
                                                }
                                            }
                                        } else {
                                            info = "терм " + result.subion.toString() + " не свободен для подстановки в формулу "
                                                    + exists.expression.toString() + " вместо переменной " + exists.variable.toString() + ".";
                                        }
                                    }
                                }
                            }
                            if (!correct && impl.left.type == EEnum.APPROVAL) {
                                Approval statement = (Approval)impl.left;
                                if (statement.approvalType == ApprovalEnum.ANY) {
                                    Any forAll = (Any)statement;
                                    SResult result = forAll.expression.getSubstitution(forAll.variable, impl.right);
                                    if (result.isSubstituted) {
                                        if ((result.subion == null)
                                                || forAll.expression.isFreeToSub(result.subion, forAll.variable)) {
                                            correct = true;
                                            for (int j = 0; j < proof.hyp.size(); j++) {
                                                if (proof.hyp.get(j).isFree(forAll.variable)) {
                                                    correct = false;
                                                    info = "используется схема аксиом с квантором по переменной " + forAll.variable.toString()
                                                            + ", входящей свободно в допущение " + proof.hyp.get(j).toString() + ".";
                                                }
                                            }
                                        } else {
                                            info = "терм " + result.subion.toString() + " не свободен для подстановки в формулу "
                                                    + forAll.expression.toString() + " вместо переменной " + forAll.variable.toString() + ".";
                                        }
                                    }
                                } else if (statement.approvalType == ApprovalEnum.EXISTS) {
                                    Exists exists = (Exists)statement;
                                    Variable v = exists.variable;
                                    if (correctStatements.contains(F.impl(exists.expression, impl.right).toString())) {
                                        if (!impl.right.isFree(v)) {
                                            correct = true;
                                            for (int j = 0; j < proof.hyp.size(); j++) {
                                                if (proof.hyp.get(j).isFree(v)) {
                                                    correct = false;
                                                    info = "используется правило с квантором по переменной " + v.toString()
                                                            + ", входящей свободно в допущение " + proof.hyp.get(j).toString() + ".";
                                                }
                                            }
                                        } else {
                                            info = "переменная " + v.toString() + " входит свободно в формулу " + impl.right.toString() + ".";
                                        }
                                    }
                                }
                            }
                        }
                        if (!correct) {
                            if (expression.type == EEnum.IMPL) {
                                Implication implication = (Implication)expression;
                                if (implication.left.type == EEnum.AND) {
                                    Conjunction and = (Conjunction)implication.left;
                                    if (and.right.type == EEnum.APPROVAL) {
                                        Approval statement = (Approval)and.right;
                                        if (statement.approvalType == ApprovalEnum.ANY) {
                                            Any forAll = (Any)statement;
                                            if (forAll.expression.type == EEnum.IMPL) {
                                                Implication inner = (Implication)forAll.expression;
                                                if (inner.left.equalsE(implication.right)) {
                                                    Variable v = forAll.variable;
                                                    SResult res1 = implication.right.getSubstitution(v, and.left);
                                                    SResult res2 = implication.right.getSubstitution(v, inner.right);
                                                    if (res1.isSubstituted && res2.isSubstituted) {
                                                        List<Term> args = new ArrayList<Term>();
                                                        Term zero = Term.getFunc("zero", args);
                                                        if (res1.subion.equalsT(zero)) {
                                                            args.add(v);
                                                            Term succ = Term.getFunc("increment", args);
                                                            if (res2.subion.equalsT(succ)) {
                                                                correct = true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (!correct) {
                                return new CResult(i, info);
                            }
                        }
                    }
                }
            }
            if (expression.type == EEnum.IMPL) {
                String rightPart = ((Implication) expression).rightPart();
                if (implicationByRightPart.containsKey(rightPart)) {
                    implicationByRightPart.get(rightPart).add(expression);
                } else {
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
