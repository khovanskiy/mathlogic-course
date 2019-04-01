package ru.ifmo.ctddev.mathlogic.khovanskiy.task5;

import java.util.ArrayList;
import java.util.List;

public class Function extends Term {
    public String name;
    public List<Term> arguments;

    public Function(String name, List<Term> arguments) {
        this.name = name;
        this.arguments = new ArrayList<Term>();
        this.arguments.addAll(arguments);
        this.type = TermEnum.FUNCTION;
    }

    @Override
    public String toString() {
        if (name.equals("zero")) {
            return "0";
        } else if (name.equals("increment")) {
            return arguments.get(0).toString() + "\'";
        } else if (name.equals("add")) {
            return "(" + arguments.get(0).toString() + "+" + arguments.get(1).toString() + ")";
        } else if (name.equals("multiply")) {
            return "(" + arguments.get(0).toString() + "*" + arguments.get(1).toString() + ")";
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append(this.name);
            builder.append("(");
            Term t;
            for (int i = 0; i < arguments.size() - 1; i++) {
                t = arguments.get(i);
                builder.append(t.toString());
                builder.append(", ");
            }
            builder.append(arguments.get(arguments.size() - 1));
            builder.append(")");
            return builder.toString();
        }
    }

    @Override
    public boolean equalsT(Term term) {
        if (term.type == TermEnum.FUNCTION) {
            Function f = (Function)term;
            if (f.name.equals(this.name) && (f.arguments.size() == this.arguments.size())) {
                boolean b = true;
                for (int i = 0; i < arguments.size(); i++) {
                    b = b && f.arguments.get(i).equalsT(this.arguments.get(i));
                }
                return b;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean contains(Variable v) {
        for (int i = 0; i < arguments.size(); i++) {
           if (arguments.get(i).contains(v)) return true;
        }
        return false;
    }

    @Override
    public Term getSubstituted(Variable v, Term substitution) {
        List<Term> arguments = new ArrayList<Term>();
        for (int i = 0; i < this.arguments.size(); i++) {
            arguments.add(this.arguments.get(i).getSubstituted(v, substitution));
        }
        return getFunc(this.name, arguments);
    }

    @Override
    public SResult getSubstitution(Variable v, Term substituted) {
        if (substituted.type == TermEnum.FUNCTION) {
            Function function = (Function)substituted;
            if (this.name.equals(function.name) && this.arguments.size() == function.arguments.size()) {
                Term substitution = null;
                SResult result;
                boolean check = true;
                for (int i = 0; i < arguments.size(); i++) {
                    result = arguments.get(i).getSubstitution(v, function.arguments.get(i));
                    check = check && result.isSubstituted;
                    if (substitution == null) {
                        substitution = result.subion;
                    } else {
                        if (result.subion != null) {
                            check = check && substitution.equalsT(result.subion);
                        }
                    }
                }
                return new SResult(check, substitution);
            }
        }
        return new SResult(false, null);
    }

}
