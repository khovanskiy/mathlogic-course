package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;

import java.util.*;

public class EParser
{
    private static Queue<Lex> parseString(String s)
    {
        Queue<Lex> result = new ArrayDeque<Lex>();
        boolean implState = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ++i)
        {
            if (s.charAt(i) == '>')
            {
                if (implState)
                {
                    result.add(Lex.getOLexeme(LexEnum.IMPL));
                    implState = false;
                }
            }
            else
            {
                implState = (s.charAt(i) == '-');
                if ((sb.length() == 0) && (Character.isLetter(s.charAt(i)))
                        || (sb.length() > 0) && (Character.isDigit(s.charAt(i))))
                {
                    sb.append(s.charAt(i));
                }
                else if (sb.length() > 0)
                {
                    String name = sb.toString();
                    result.add(Lex.getNLexeme(name, Character.isUpperCase(name.charAt(0))));
                    sb = new StringBuilder();
                    if (Character.isLetter(s.charAt(i)))
                    {
                    	sb.append(s.charAt(i));
                    }
                }
                if (s.charAt(i) == '&')
                {
                	result.add(Lex.getOLexeme(LexEnum.CON));
                }
                else if (s.charAt(i) == '|')
                {
                	result.add(Lex.getOLexeme(LexEnum.DIS));
                }
                else if (s.charAt(i) == '!')
                {
                	result.add(Lex.getOLexeme(LexEnum.NEG));
                }
                else if (s.charAt(i) == '(')
                {
                	result.add(Lex.getOLexeme(LexEnum.OPEN));
                }
                else if (s.charAt(i) == ')')
                {
                	result.add(Lex.getOLexeme(LexEnum.CLOSE));
                }
                else if (s.charAt(i) == ',')
                {
                	result.add(Lex.getOLexeme(LexEnum.COMMA));
                }
                else if (s.charAt(i) == '?')
                {
                	result.add(Lex.getOLexeme(LexEnum.EXISTS));
                }
                else if (s.charAt(i) == '@')
                {
                	result.add(Lex.getOLexeme(LexEnum.ANY));
                }
                else if (s.charAt(i) == '=')
                {
                	result.add(Lex.getOLexeme(LexEnum.EQUALS));
                }
                else if (s.charAt(i) == '\'')
                {
                	result.add(Lex.getOLexeme(LexEnum.INC));
                }
                else if (s.charAt(i) == '+')
                {
                	result.add(Lex.getOLexeme(LexEnum.Plus));
                }
                else if (s.charAt(i) == '*')
                {
                	result.add(Lex.getOLexeme(LexEnum.TIMES));
                }
                else if (s.charAt(i) == '0' && sb.length() == 0)
                {
                	result.add(Lex.getOLexeme(LexEnum.ZERO));
                }
            }
        }
        if (sb.length() > 0)
        {
            String name = sb.toString();
            result.add(Lex.getNLexeme(name, Character.isUpperCase(name.charAt(0))));
        }
        return result;
    }

    private static Queue<Lex> transformExpression(Queue<Lex> queue)
    {
        Stack<Lex> stack1 = new Stack<Lex>();
        Stack<Lex> stack2 = new Stack<Lex>();
        Lex lexeme;
        Lex lex;
        while (!queue.isEmpty())
        {
            lexeme = queue.poll();
            if (lexeme.type != LexEnum.EQUALS)
            {
                stack1.push(lexeme);
            }
            else
            {
                int balance = 0;
                boolean insideTerm = true;
                while (!stack1.isEmpty() && insideTerm)
                {
                    lex = stack1.pop();
                    if (lex.type == LexEnum.OPEN)
                    {
                    	balance--;
                    }
                    if (lex.type == LexEnum.CLOSE)
                    {
                    	balance++;
                    }
                    if ((balance < 0)
                        | (lex.type == LexEnum.CON)
                        | (lex.type == LexEnum.EXISTS)
                        | (lex.type == LexEnum.ANY)
                        | (lex.type == LexEnum.IMPL)
                        | (lex.type == LexEnum.NEG)
                        | (lex.type == LexEnum.DIS))
                    {
                        stack1.push(lex);
                        if (lex.type == LexEnum.EXISTS || lex.type == LexEnum.ANY)
                        {
                        	stack1.push(stack2.pop());
                        }
                        insideTerm = false;
                    }
                    else
                    {
                        stack2.push(lex);
                    }
                }
                stack1.push(lexeme);
                while (!stack2.isEmpty())
                {
                	stack1.push(stack2.pop());
                }
                stack1.push(Lex.getOLexeme(LexEnum.COMMA));
            }
        }
        Queue<Lex> result = new ArrayDeque<Lex>();
        while (!stack1.isEmpty())
        {
        	stack2.push(stack1.pop());
        }
        while (!stack2.isEmpty())
        {
        	result.add(stack2.pop());
        }
        return result;
    }

    private static Term nextTerm(Queue<Lex> queue)
    {
        List<Term> addends = new ArrayList<Term>();
        addends.add(readAddend(queue));
        while (!queue.isEmpty() && queue.peek().type == LexEnum.Plus)
        {
            queue.poll();
            addends.add(readAddend(queue));
        }
        Term result = addends.get(0);
        List<Term> arguments = new ArrayList<Term>();
        for (int i = 1; i < addends.size(); ++i)
        {
            arguments.clear();
            arguments.add(result);
            arguments.add(addends.get(i));
            result = Term.getFunc("add", arguments);
        }
        return result;
    }

    private static Term readAddend(Queue<Lex> queue)
    {
        List<Term> multipliers = new ArrayList<Term>();
        multipliers.add(readMultiplier(queue));
        while (!queue.isEmpty() && queue.peek().type == LexEnum.TIMES)
        {
            queue.poll();
            multipliers.add(readMultiplier(queue));
        }
        Term result = multipliers.get(0);
        List<Term> arguments = new ArrayList<Term>();
        for (int i = 1; i < multipliers.size(); i++)
        {
            arguments.clear();
            arguments.add(result);
            arguments.add(multipliers.get(i));
            result = Term.getFunc("multiply", arguments);
        }
        return result;
    }

    private static Term readMultiplier(Queue<Lex> queue)
    {
        Term result = null;
        List<Term> arguments = new ArrayList<Term>();
        switch (queue.peek().type)
        {
            case NAME:
            {
                NLexeme nameLexeme = (NLexeme) queue.poll();
                if (!queue.isEmpty() && queue.peek().type == LexEnum.OPEN) {
                    queue.poll();
                    while (queue.peek().type != LexEnum.CLOSE) {
                        arguments.add(nextTerm(queue));
                        if (queue.peek().type == LexEnum.COMMA) queue.poll();
                    }
                    queue.poll();
                    result = Term.getFunc(nameLexeme.name, arguments);
                } else {
                    result = Term.getVar(nameLexeme.name);
                }
            } break;
            case OPEN:
            {
                queue.poll();
                result = nextTerm(queue);
                queue.poll();
            } break;
            case ZERO:
            {
                queue.poll();
                result = Term.getFunc("zero", arguments);
            } break;
        }
        while (!queue.isEmpty() && queue.peek().type == LexEnum.INC)
        {
            arguments.clear();
            arguments.add(result);
            result = Term.getFunc("increment", arguments);
            queue.poll();
        }
        return result;
    }

    private static F parseLexemeQueue(Queue<Lex> queue)
    {
        List<Lex> lexArray = new ArrayList<Lex>();
        Stack<Lex> lexStack = new Stack<Lex>();
        Lex lexeme;
        NLexeme var;
        while (!queue.isEmpty()) {
            lexeme = queue.poll();
            switch (lexeme.type) {
                case OPEN:
                    lexStack.add(lexeme);
                    break;
                case CLOSE:
                    while (!lexStack.isEmpty() && (lexStack.peek().type != LexEnum.OPEN)) {
                        lexArray.add(lexStack.pop());
                    }
                    lexStack.pop();
                    while (!lexStack.empty() && ((lexStack.peek().type == LexEnum.NEG)
                            || (lexStack.peek().type == LexEnum.QUAN))) {
                        lexArray.add(lexStack.pop());
                    }
                    break;
                case CON:
                    while (!lexStack.isEmpty() && ((lexStack.peek().type == LexEnum.NEG)
                            || (lexStack.peek().type == LexEnum.QUAN)
                            || (lexStack.peek().type == LexEnum.CON))) {
                        lexArray.add(lexStack.pop());
                    }
                    lexStack.add(lexeme);
                    break;
                case DIS:
                    while (!lexStack.isEmpty() && ((lexStack.peek().type == LexEnum.NEG)
                            || (lexStack.peek().type == LexEnum.QUAN)
                            || (lexStack.peek().type == LexEnum.CON)
                            || (lexStack.peek().type == LexEnum.DIS))) {
                        lexArray.add(lexStack.pop());
                    }
                    lexStack.add(lexeme);
                    break;
                case NEG:
                    lexStack.add(lexeme);
                    break;
                case IMPL:
                    while (!lexStack.isEmpty() && ((lexStack.peek().type == LexEnum.NEG)
                            || (lexStack.peek().type == LexEnum.QUAN)
                            || (lexStack.peek().type == LexEnum.CON)
                            || (lexStack.peek().type == LexEnum.DIS))) {
                        lexArray.add(lexStack.pop());
                    }
                    lexStack.add(lexeme);
                    break;
                case ANY:
                    var = (NLexeme)queue.poll();
                    lexStack.add(Lex.getQuantifierLexeme(ApprovalEnum.ANY, Term.getVar(var.name)));
                    break;
                case EXISTS:
                    var = (NLexeme)queue.poll();
                    lexStack.add(Lex.getQuantifierLexeme(ApprovalEnum.EXISTS, Term.getVar(var.name)));
                    break;
                case NAME:
                    String name = ((NLexeme)lexeme).name;
                    if ((!queue.isEmpty()) && (queue.peek().type == LexEnum.OPEN)) {
                        queue.poll();
                        List<Term> arguments = new ArrayList<Term>();
                        while (queue.peek().type != LexEnum.CLOSE) {
                            arguments.add(nextTerm(queue));
                            if (queue.peek().type == LexEnum.COMMA) queue.poll();
                        }
                        queue.poll();
                        lexArray.add(Lex.getPredicateLexeme(F.pred(name, arguments)));
                    } else {
                        lexArray.add(Lex.getPropositionLexeme(F.prop(name)));
                    }
                    break;
                case EQUALS:
                    List<Term> arguments = new ArrayList<Term>();
                    arguments.add(nextTerm(queue));
                    queue.poll();
                    arguments.add(nextTerm(queue));
                    lexArray.add(Lex.getPredicateLexeme(F.pred("equals", arguments)));
            }
        }
        while (!lexStack.empty()) lexArray.add(lexStack.pop());
        Stack<F> exprStack = new Stack<F>();
        Lex lex;
        F right;
        F left;
        for (int i = 0; i < lexArray.size(); i++) {
            lex = lexArray.get(i);
            switch (lex.type) {
                case CON:
                    right = exprStack.pop();
                    left = exprStack.pop();
                    exprStack.add(F.con(left, right));
                    break;
                case DIS:
                    right = exprStack.pop();
                    left = exprStack.pop();
                    exprStack.add(F.dis(left, right));
                    break;
                case NEG:
                    exprStack.add(F.neg(exprStack.pop()));
                    break;
                case IMPL:
                    right = exprStack.pop();
                    left = exprStack.pop();
                    exprStack.add(F.impl(left, right));
                    break;
                case PRED:
                    exprStack.add(((PDLexeme)lex).predicate);
                    break;
                case PROP:
                    exprStack.add(((PLexeme)lex).proposition);
                    break;
                case QUAN:
                    QLexeme quantifierLexeme = (QLexeme)lex;
                    if (quantifierLexeme.approvalType == ApprovalEnum.ANY) {
                        exprStack.add(F.getAny(quantifierLexeme.variable, exprStack.pop()));
                    } else {
                        exprStack.add(F.getExists(quantifierLexeme.variable, exprStack.pop()));
                    }
                    break;
            }
        }
        return exprStack.pop();
    }

    public static F stringToExpression(String s) {
        return parseLexemeQueue(transformExpression(parseString(s)));
    }
}
