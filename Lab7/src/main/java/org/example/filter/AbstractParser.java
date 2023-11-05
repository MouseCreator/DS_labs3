package org.example.filter;

import java.util.Arrays;
import java.util.Objects;

public abstract class AbstractParser<T> {
    protected static final class OperationTemp {
        private final int index;
        private final String operation;
        private final int next;
        private OperationTemp(int index, String operation) {
            this.index = index;
            this.operation = operation;
            this.next = this.index + this.operation.length();
        }
        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (OperationTemp) obj;
            return this.index == that.index &&
                    Objects.equals(this.operation, that.operation);
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, operation);
        }

        @Override
        public String toString() {
            return "OperationTemp[" +
                    "index=" + index + ", " +
                    "operation=" + operation + ']';
        }

        public int next() {
            return next;
        }
        public int index() {
            return index;
        }
        public String operation() {
            return operation;
        }
    }
    protected OperationTemp operationIndex(String s) {
        final String[] operations = {
                "<=", "<>", ">=", "!=", "<", "=", ">"
        };
        for (String o : operations) {
            if (s.contains(o)) {
                return new OperationTemp(s.indexOf(o), o);
            }
        }
        throw new IllegalArgumentException("Cannot resolve any operation in expression: " + s);
    }

    protected String removeExtraBrackets(String s) {
        while (s.startsWith("(") && s.endsWith(")"))
            s = s.substring(1, s.length()-1);
        return s;
    }

    protected String toOperation(char charAt) {
        return switch (charAt) {
            case '&' -> " AND ";
            case '|' ->  " OR ";
            default -> throw new IllegalArgumentException("Invalid bool operator: " + charAt);
        };
    }

    protected String removeSpaces(String input) {
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

    protected int findTopOperation(String sub) {
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
    protected boolean isBool(String val) {
        String[] booleans = {
                "T", "TRUE", "FALSE", "F"
        };
        return Arrays.asList(booleans).contains(val);
    }
    public abstract T parse(String s);
}
