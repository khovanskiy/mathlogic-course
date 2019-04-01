package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;

import java.util.*;

public class EParser
{
    private static Queue<Lex> parseS(String s)
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
                    result.add(Lex.getNameLexeme(name, Character.isUpperCase(name.charAt(0))));
                    sb = new StringBuilder();
                    if (Character.isLetter(s.charAt(i))) sb.append(s.charAt(i));
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
            }
        }
        if (sb.length() > 0)
        {
            String name = sb.toString();
            result.add(Lex.getNameLexeme(name, Character.isUpperCase(name.charAt(0))));
        }
        return result;
    }

    private static Term nextT(Queue<Lex> q)
    {
        String name = ((NLexeme)q.poll()).label;
        if (!q.isEmpty() && (q.peek().type == LexEnum.OPEN))
        {
            List<Term> arguments = new ArrayList<Term>();
            q.poll();
            while (q.peek().type != LexEnum.CLOSE)
            {
                arguments.add(nextT(q));
                if (q.peek().type == LexEnum.COMMA)
                {
                	q.poll();
                }
            }
            q.poll();
            return Term.func(name, arguments);
        }
        else
        {
            return Term.var(name);
        }
    }

    private static F parseQ(Queue<Lex> q)
    {
        List<Lex> arr = new ArrayList<Lex>();
        Stack<Lex> lstack = new Stack<Lex>();
        Lex lexeme;
        NLexeme var;
        while (!q.isEmpty())
        {
            lexeme = q.poll();
            switch (lexeme.type)
            {
                case OPEN:
                {
                    lstack.add(lexeme);
                } break;
                case CLOSE:
                {
                    while (!lstack.isEmpty() && (lstack.peek().type != LexEnum.OPEN))
                    {
                        arr.add(lstack.pop());
                    }
                    lstack.pop();
                    while (!lstack.empty() && ((lstack.peek().type == LexEnum.NEG) || (lstack.peek().type == LexEnum.QUAN)))
                    {
                        arr.add(lstack.pop());
                    }
                } break;
                case CON:
                {
                    while (!lstack.isEmpty() && ((lstack.peek().type == LexEnum.NEG)
                            || (lstack.peek().type == LexEnum.QUAN)
                            || (lstack.peek().type == LexEnum.CON)))
                    {
                        arr.add(lstack.pop());
                    }
                    lstack.add(lexeme);
                } break;
                case DIS:
                {
                    while (!lstack.isEmpty() && ((lstack.peek().type == LexEnum.NEG)
                            || (lstack.peek().type == LexEnum.QUAN)
                            || (lstack.peek().type == LexEnum.CON)
                            || (lstack.peek().type == LexEnum.DIS))) {
                        arr.add(lstack.pop());
                    }
                    lstack.add(lexeme);
                } break;
                case NEG:
                {
                    lstack.add(lexeme);
                } break;
                case IMPL:
                {
                    while (!lstack.isEmpty() && ((lstack.peek().type == LexEnum.NEG)
                            || (lstack.peek().type == LexEnum.QUAN)
                            || (lstack.peek().type == LexEnum.CON)
                            || (lstack.peek().type == LexEnum.DIS))) {
                        arr.add(lstack.pop());
                    }
                    lstack.add(lexeme);
                } break;
                case ANY:
                {
                    var = (NLexeme)q.poll();
                    lstack.add(Lex.getQuantifierLexeme(ApprovalEnum.ANY, Term.var(var.label)));
                } break;
                case EXISTS:
                {
                    var = (NLexeme)q.poll();
                    lstack.add(Lex.getQuantifierLexeme(ApprovalEnum.EXISTS, Term.var(var.label)));
                } break;
                case NAME:
                {
                    String name = ((NLexeme)lexeme).label;
                    if ((!q.isEmpty()) && (q.peek().type == LexEnum.OPEN))
                    {
                        q.poll();
                        List<Term> args = new ArrayList<Term>();
                        while (q.peek().type != LexEnum.CLOSE)
                        {
                            args.add(nextT(q));
                            if (q.peek().type == LexEnum.COMMA)
                            {
                            	q.poll();
                            }
                        }
                        q.poll();
                        arr.add(Lex.getPredicateLexeme(F.pred(name, args)));
                    }
                    else
                    {
                        arr.add(Lex.getPropositionLexeme(F.prop(name)));
                    }
                } break;
            }
        }
        while (!lstack.empty())
        {
        	arr.add(lstack.pop());
        }
        Stack<F> estack = new Stack<F>();
        Lex lex;
        F r;
        F l;
        for (int i = 0; i < arr.size(); ++i)
        {
            lex = arr.get(i);
            switch (lex.type)
            {
                case CON:
                {
                    r = estack.pop();
                    l = estack.pop();
                    estack.add(F.con(l, r));
                } break;
                case DIS:
                {
                    r = estack.pop();
                    l = estack.pop();
                    estack.add(F.dis(l, r));
                } break;
                case NEG:
                {
                    estack.add(F.neg(estack.pop()));
                } break;
                case IMPL:
                {
                    r = estack.pop();
                    l = estack.pop();
                    estack.add(F.impl(l, r));
                } break;
                case PRED:
                {
                    estack.add(((PDLexeme)lex).predicate);
                } break;
                case PROP:
                {
                    estack.add(((PLexeme)lex).proposition);
                } break;
                case QUAN:
                {
                    QLexeme quantifierLexeme = (QLexeme)lex;
                    if (quantifierLexeme.approvalType == ApprovalEnum.ANY)
                    {
                        estack.add(F.getAny(quantifierLexeme.variable, estack.pop()));
                    }
                    else
                    {
                        estack.add(F.getExists(quantifierLexeme.variable, estack.pop()));
                    }
                } break;
            }
        }
        return estack.pop();
    }

    public static F parseF(String s)
    {
        return parseQ(parseS(s));
    }
}
