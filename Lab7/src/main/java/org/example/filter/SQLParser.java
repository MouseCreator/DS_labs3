package org.example.filter;

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
        return parse(s).stringExpr() + ";";
    }
    public String toSqlWithTable(String v, String tName) {
        return "SELECT * FROM " + tName + " WHERE " + toSql(v);
    }
    private ConditionNode parse(String s) {
        s = removeExtraBrackets(s);
        int index = findTopOperation(s);
        if (index==-1) {
            int operationIndex = operationIndex(s);
            String left = s.substring(0, operationIndex);
            String operation = (s.charAt(operationIndex)) + "";
            String right = s.substring(operationIndex+1);
            return new SimpleConditionNode(left, operation, right);
        } else {
            ConditionNode left = parse(s.substring(0, index));
            String operation = toOperation(s.charAt(index));
            ConditionNode right = parse(s.substring(index+1));
            return new TopConditionNode(left, operation, right);
        }

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
