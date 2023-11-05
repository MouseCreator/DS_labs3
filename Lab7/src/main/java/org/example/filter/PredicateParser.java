package org.example.filter;

import java.util.function.Predicate;

public abstract class PredicateParser<T> extends AbstractParser<Predicate<T>>{
    private interface ConditionNode<T> {
        Predicate<T> toPredicate();
    }

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
        private final ConditionNode<T> conditionNode;
        public InvertConditionNode(ConditionNode<T> predicate) {
            this.conditionNode =  predicate;
        }
        @Override
        public Predicate<T> toPredicate() {
            return conditionNode.toPredicate().negate();
        }
    }


    private ConditionNode<T> parseToNode(String s) {
        s = removeExtraBrackets(s);
        s = removeExtraBrackets(s);
        if (s.startsWith("!")) {
            return new InvertConditionNode<>(parseToNode(s.substring(1)));
        }
        int index = findTopOperation(s);
        if (index==-1) {
            return createSimple(s);
        } else {
           return createComplex(s, index);
        }
    }

    private ConditionNode<T> createSimple(String s) {
        OperationTemp operationTemp = operationIndex(s);
        String field = s.substring(0, operationTemp.index());
        String operation = operationTemp.operation();
        String val = s.substring(operationTemp.next());
        return new SimpleConditionNode<>(toPredicate(field, operation, val));
    }
    private ConditionNode<T> createComplex(String s, int index) {
        ConditionNode<T> left = parseToNode(s.substring(0, index));
        String operation = toOperation(s.charAt(index));
        ConditionNode<T> right = parseToNode(s.substring(index+1));
        return new PredicateNode<>(left, operation, right);
    }
    @Override
    public Predicate<T> parse(String s) {
        return parseToNode(s).toPredicate();
    }

    protected abstract Object getExpectedValue(T object, String field);

    protected Predicate<T> toPredicate(String field, String operation, String value) {
        return d -> {
            Object expectedValue = getExpectedValue(d, field);
            Object actualValue;
            if (value.matches("^-?\\d+$")) {
                actualValue = Long.parseLong(value);
            } else if (value.matches("^-?\\d+\\.\\d+$")) {
                actualValue = Double.parseDouble(value);
            } else if (value.startsWith("'") && value.endsWith("'")) {
                actualValue = value.substring(1, value.length() - 1);
            } else {
                throw new RuntimeException("Unable to parse! Invalid input: " + value);
            }

            switch (operation) {
                case "=" -> {
                    return expectedValue.equals(actualValue);
                }
                case "<>", "!=" -> {
                    return !expectedValue.equals(actualValue);
                }
            }
            try {
                long expLong = (long) expectedValue;
                long actLong = (long) actualValue;
                switch (operation) {
                    case "<" -> {
                        return expLong < actLong;
                    }
                    case "<=" -> {
                        return expLong <= actLong;
                    }
                    case ">=" -> {
                        return expLong >= actLong;
                    }
                    case ">" -> {
                        return expLong > actLong;
                    }
                }
            } catch (ClassCastException e) {
                throw new RuntimeException(e);
            }
            throw new IllegalArgumentException(String.format("Unable to handle: %s %s %s", field, operation, value));
        };
    }
}
