package org.example.filter;

class SQLParser extends AbstractParser<String> {
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

    @Override
    protected String toOperation(char charAt) {
        return switch (charAt) {
            case '&' -> " AND ";
            case '|' ->  " OR ";
            default -> throw new IllegalArgumentException("Invalid bool operator: " + charAt);
        };
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

    @Override
    public String parse(String string) {
        return toSql(string);
    }
    public String toSql(String s) {
        s = removeSpaces(s);
        return parseToNode(s).stringExpr();
    }
    public String toSqlWithTable(String v, String tName) {
        return "SELECT * FROM " + tName + " WHERE " + toSql(v) + ";";
    }
    private ConditionNode parseToNode(String s) {
        s = removeExtraBrackets(s);
        if (s.startsWith("!")) {
            return new InvertNode(parseToNode(s.substring(1)));
        }
        int index = findTopOperation(s);
        if (index==-1) {
            OperationTemp operationTemp = operationIndex(s);
            String field = s.substring(0, operationTemp.index());
            String operation = operationTemp.operation();
            String val = s.substring(operationTemp.next());
            if (isBool(val)) {
                return new BooleanConditionNode(field, operation, val);
            } else {
                return new SimpleConditionNode(field, operation, val);
            }
        } else {
            ConditionNode left = parseToNode(s.substring(0, index));
            String operation = toOperation(s.charAt(index));
            ConditionNode right = parseToNode(s.substring(index+1));
            return new TopConditionNode(left, operation, right);
        }

    }



}
