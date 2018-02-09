package tasks.task2;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.Stack;

public class Calculator {

    private static class MyException extends Exception {
        MyException(String message) {
            super(message);
        }
    }


    private static int priority(char operator) {
        int priority = -1;
        switch (operator) {
            case '^':
                priority = 4;
                break;
            case '*':
                priority = 3;
                break;
            case '/':
                priority = 3;
                break;
            case '+':
                priority = 2;
                break;
            case '-':
                priority = 2;
                break;
            case ')':
                priority = 1;
                break;
            case '(':
                priority = 0;
                break;
        }
        return priority;
    }


    private static String correct(String expression) {
        int length = expression.length();
        char symbol;
        boolean oldData = false;
        boolean isNumber = false;
        boolean finish = false;
        boolean start = false;
        StringBuilder stringBuilder = new StringBuilder(expression.length() + 2);
        for (int i = 0; i < length; i++) {
            symbol = expression.charAt(i);
            stringBuilder.append(symbol);
            if (!finish) {
                if (symbol == '^') {
                    stringBuilder.append('(');
                    start = true;
                }
                if (start) {
                    if ((symbol >= '0' && symbol <= '9') || symbol == ',' || symbol == '.')
                        isNumber = true;
                    if (oldData != isNumber) {
                        stringBuilder.append(") ");
                        finish = true;
                    }
                }
            }
            oldData = isNumber;
        }
        return stringBuilder.toString();
    }

    public static BigDecimal calculate(String expression) {
        Stack<Character> stack = new Stack<>();
        int expressionLength = expression.length();
        StringBuilder stringBuilder = new StringBuilder(expressionLength);
        char symbol;
        boolean operator = false;
        for (int i = 0; i < expressionLength; i++) {
            symbol = expression.charAt(i);
            if ((symbol >= '0' && symbol <= '9') || symbol == ',' || symbol == '.') {
                operator = false;
                stringBuilder.append(symbol);
            } else {
                int priorityResult = priority(symbol);

                if (priorityResult > -1) {
                    if (operator || (stringBuilder.length() == 0 && priorityResult > 1)) {
                        if (symbol == '-' || symbol == '+') {
                            stringBuilder.append("0 ");
                        }
                    }
                    stringBuilder.append(' ');
                    if (symbol == '(')
                        operator = true;
                    if ((priorityResult == 0 && !stack.isEmpty()) ||
                            (!stack.isEmpty() && priorityResult > priority(stack.peek()))) {
                        stack.push(symbol);
                    } else {
                        while (!stack.isEmpty() && priority(stack.peek()) >= priorityResult) {
                            stringBuilder.append(' ');
                            stringBuilder.append(stack.pop());
                            stringBuilder.append(' ');
                        }
                        stack.push(symbol);
                    }

                } else {
                    System.err.println("Wrong symbols in expression");
                    System.exit(1);
                }

            }

        }
        while (!stack.isEmpty()) {
            symbol = stack.peek();
            if (symbol != '(' && symbol != ')') {
                stringBuilder.append(' ');
                stringBuilder.append(symbol);
                stringBuilder.append(' ');
            }
            stack.pop();
        }
        Stack<BigDecimal> decimalStack = new Stack<>();
        int outLineLength = stringBuilder.length();
        for (int i = 0; i < outLineLength; i++) {
            symbol = stringBuilder.charAt(i);
            if (symbol >= '0' && symbol <= '9') {
                StringBuilder digitString = new StringBuilder();
                for (; symbol != ' '; i++, symbol = stringBuilder.charAt(i)) {
                    if (symbol == ',')
                        symbol = '.';
                    digitString.append(symbol);
                }
                BigDecimal number = new BigDecimal(digitString.toString());
                number.setScale(5, BigDecimal.ROUND_HALF_UP);
                decimalStack.push(number);
            }
            if (priority(symbol) > 1) {
                BigDecimal number1 = decimalStack.pop();
                BigDecimal number2 = decimalStack.pop();
                switch (symbol) {
                    case '+':
                        decimalStack.push(number2.add(number1));
                        break;
                    case '-':
                        decimalStack.push(number2.subtract(number1));
                        break;
                    case '*':
                        decimalStack.push(number2.multiply(number1));
                        break;
                    case '/':
                        if (number1.equals(0)) {
                            System.err.println("Divided by zero");
                            System.exit(1);
                        }
                        decimalStack.push(number2.divide(number1, 5, BigDecimal.ROUND_HALF_UP));
                        break;
                    case '^':
                        double tempNum1 = Double.parseDouble(number2.toString());
                        double tempNum2 = Double.parseDouble(number1.toString());
                        BigDecimal tempRes = new BigDecimal(Math.pow(tempNum1, tempNum2));
                        decimalStack.push(tempRes.setScale(5, BigDecimal.ROUND_HALF_UP));
                }
            }
        }
        return decimalStack.peek();
    }


    public static void main(String[] args) {
        System.out.println("Input expression: ");
        Scanner scanner = new Scanner(System.in);
        String expression = scanner.nextLine();
        System.out.println("Res: " + calculate(correct(expression)));


    }
}
