package org.example.filter;

import java.util.Arrays;
import java.util.Objects;

public class SQLParser {
    private interface ConditionNode {
        String stringExpr();
    }
    private static class TopConditionNode implements ConditionNode{
        private final ConditionNode left;
        private final String operation;
        private final ConditionNode right;

        public TopConditionNode(ConditionNode left, String operation, ConditionNode right) {
            this.left = left;
            this.operation = operation;
            this.right = right;
        }
        private String inBrackets(ConditionNode conditionNode) {
            return "(" + conditionNode.stringExpr() + ")";
        }
        public String stringExpr() {
            return inBrackets(left) + operation + inBrackets(right);
        }
    }

    private static class InvertNode implements ConditionNode{
        private final ConditionNode toInvert;

        public InvertNode(ConditionNode toInvert) {
            this.toInvert = toInvert;
        }
        private String inBrackets(ConditionNode conditionNode) {
            return "(" + conditionNode.stringExpr() + ")";
        }
        public String stringExpr() {
            return "NOT " + inBrackets(toInvert);
        }
    }
    private static class SimpleConditionNode implements ConditionNode{
        private final String field;
        private final String operator;
        private final String value;

        public SimpleConditionNode(String left, String operation, String right) {
            this.field = left;
            this.operator = operation;
            this.value = right;
        }

        public String stringExpr() {
            return field + " " + operator + " " + value;
        }
    }

    private static class BooleanConditionNode implements ConditionNode{
        private final String field;
        private final boolean value;
        private boolean toBool(String op, String r) {
            boolean isTrueCondition = r.contains("T");
            if (op.equals("!=") || op.equals("<>")) {
                return !isTrueCondition;
            }
            return isTrueCondition;
        }
        public BooleanConditionNode(String field, String operation, String boolVal) {
            this.field = field;
            this.value = toBool(operation, boolVal);
        }

        public String stringExpr() {
            return field + " IS " + (value ? "TRUE" : "FALSE");
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
        s = removeSpaces(s);
        return parse(s).stringExpr();
    }
    public String toSqlWithTable(String v, String tName) {
        return "SELECT * FROM " + tName + " WHERE " + toSql(v) + ";";
    }
    private ConditionNode parse(String s) {
        s = removeExtraBrackets(s);
        if (s.startsWith("!")) {
            return new InvertNode(parse(s.substring(1)));
        }
        int index = findTopOperation(s);
        if (index==-1) {
            OperationTemp operationTemp = operationIndex(s);
            String field = s.substring(0, operationTemp.index);
            String operation = operationTemp.operation;
            String val = s.substring(operationTemp.next);
            if (isBool(val)) {
                return new BooleanConditionNode(field, operation, val);
            } else {
                return new SimpleConditionNode(field, operation, val);
            }
        } else {
            ConditionNode left = parse(s.substring(0, index));
            String operation = toOperation(s.charAt(index));
            ConditionNode right = parse(s.substring(index+1));
            return new TopConditionNode(left, operation, right);
        }

    }

    private boolean isBool(String val) {
        String[] booleans = {
                "T", "TRUE", "FALSE", "F"
        };
        return Arrays.asList(booleans).contains(val);
    }

    private static final class OperationTemp {
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

    }
    private OperationTemp operationIndex(String s) {
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
