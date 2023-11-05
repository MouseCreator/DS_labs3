package org.example.filter;

import java.util.function.Predicate;

public abstract class PredicateParser<T> {
    private interface ConditionNode<T> {
        Predicate<T> toPredicate();
    }

    protected abstract Predicate<T> toPredicate(String statement);
    private static class PredicateNode<T> implements ConditionNode<T> {
        private final ConditionNode<T> left;
        private final String operation;
        private final ConditionNode<T> right;
        public PredicateNode(ConditionNode<T> left, String operation, ConditionNode<T> right) {
            this.left = left;
            this.operation = operation;
            this.right = right;
        }

        @Override
        public Predicate<T> toPredicate() {
            if (operation.equals("&")) {
                return left.toPredicate().and(right.toPredicate());
            }
            if (operation.equals("|")) {
                return left.toPredicate().or(right.toPredicate());
            }
            throw new IllegalStateException("Unknown operation: " + operation);
        }
    }
    private static class SimpleConditionNode<T> implements ConditionNode<T> {
        private final Predicate<T> predicate;
        public SimpleConditionNode(Predicate<T> predicate) {
            this.predicate =  predicate;
        }
        @Override
        public Predicate<T> toPredicate() {
            return predicate;
        }
    }

    private static class InvertConditionNode<T> implements ConditionNode<T> {
        private final Predicate<T> predicate;
        public InvertConditionNode(Predicate<T> predicate) {
            this.predicate =  predicate;
        }
        @Override
        public Predicate<T> toPredicate() {
            return predicate.negate();
        }
    }
    public static String removeSpaces(String input) {
        StringBuilder result = new StringBuilder();
        boolean insideQuotes = false;

        for (char c : input.toCharArray()) {
            if (c == '\'') {
                insideQuotes = !insideQuotes;
            }

            if (!insideQuotes && c == ' ') {
                continue;
            }

            result.append(c);
        }

        return result.toString();
    }


    public String toSql(String s) {
       return removeSpaces(s);
    }
    public String toSqlWithTable(String v, String tName) {
        return "SELECT * FROM " + tName + " WHERE " + toSql(v);
    }
    private ConditionNode<T> parse(String s) {
        s = removeExtraBrackets(s);
        return new SimpleConditionNode<>(e -> false);
    }

    private int operationIndex(String s) {
        if (s.contains("<")) {
            return s.indexOf("<");
        }
        if (s.contains(">")) {
            return s.indexOf(">");
        }
        if (s.contains("=")) {
            return s.indexOf("=");
        }
        throw new IllegalArgumentException("Cannot resolve any operation in expression: " + s);
    }

    private String removeExtraBrackets(String s) {
        while (s.startsWith("(") && s.endsWith(")"))
            s = s.substring(1, s.length()-1);
        return s;
    }

    private String toOperation(char charAt) {
        return switch (charAt) {
            case '&' -> " AND ";
            case '|' ->  " OR ";
            default -> throw new IllegalArgumentException("Invalid bool operator: " + charAt);
        };
    }

    private int findTopOperation(String sub) {
        int bracketLevel = 0;
        int i = 0;
        int result = -1;
        boolean ignore = false;
        for (char ch : sub.toCharArray()) {
            if (ch=='\''){
                ignore = !ignore;
            }
            else if (ch=='(') {
                bracketLevel++;
            }
            else if (ch==')') {
                bracketLevel--;
            }
            else if (bracketLevel == 0) {
                if (ch=='|') {
                    result = i;
                } else if (ch=='&') {
                    return i;
                }
            }
            i++;
        }
        return result;
    }
}
