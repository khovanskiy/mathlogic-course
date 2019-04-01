package ru.ifmo.ctddev.mathlogic.khovanskiy.task4;


import java.util.HashMap;

public abstract class Lex
{
    public static HashMap<LexEnum, OLexeme> cacheOther = new HashMap<LexEnum, OLexeme>();
    public static HashMap<String, NLexeme> cacheName = new HashMap<String, NLexeme>();
    public static HashMap<String, PDLexeme> cachePred = new HashMap<String, PDLexeme>();
    public static HashMap<String, PLexeme> cacheProp = new HashMap<String, PLexeme>();
    public static HashMap<String, QLexeme> cacheQuan = new HashMap<String, QLexeme>();

    public LexEnum type;

    public static OLexeme getOLexeme(LexEnum type)
    {
        if (cacheOther.containsKey(type))
        {
            return cacheOther.get(type);
        }
        else
        {
            OLexeme otherLexeme = new OLexeme(type);
            cacheOther.put(type, otherLexeme);
            return otherLexeme;
        }
    }

    public static NLexeme getNameLexeme(String name, boolean isupper)
    {
        if (cacheName.containsKey(name))
        {
            return cacheName.get(name);
        }
        else
        {
            NLexeme termLexeme = new NLexeme(name, isupper);
            cacheName.put(name, termLexeme);
            return termLexeme;
        }
    }

    public static PDLexeme getPredicateLexeme(Predicate predicate)
    {
        String key = predicate.toString();
        if (cachePred.containsKey(key))
        {
            return cachePred.get(key);
        }
        else
        {
            PDLexeme predicateLexeme = new PDLexeme(predicate);
            cachePred.put(key, predicateLexeme);
            return predicateLexeme;
        }
    }

    public static PLexeme getPropositionLexeme(Proposition proposition) {
        if (cacheProp.containsKey(proposition.label)) {
            return cacheProp.get(proposition.label);
        } else {
            PLexeme lexeme = new PLexeme(proposition);
            cacheProp.put(proposition.label, lexeme);
            return lexeme;
        }
    }

    public static QLexeme getQuantifierLexeme(ApprovalEnum type, Variable variable) {
        String s;
        if (type == ApprovalEnum.ANY) {
            s = "@" + variable.name;
        } else {
            s = "?" + variable.name;
        }
        if (cacheQuan.containsKey(s)) {
            return cacheQuan.get(s);
        } else {
            QLexeme lexeme = new QLexeme(type, variable);
            cacheQuan.put(s, lexeme);
            return lexeme;
        }
    }
}
