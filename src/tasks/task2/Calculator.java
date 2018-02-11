package tasks.task2;

import java.math.BigDecimal;
import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

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


    private static boolean allowedChar(String line) {
        Pattern pattern = Pattern.compile("[0-9,.+/*^\\-\\s()]*");
        Matcher matcher = pattern.matcher(line);
        return matcher.matches();
    }

    private static void errorMessage(String message) {
        System.err.println(message);
        System.exit(1);
    }

    private static boolean numberPart(char symbol) {
        return (symbol >= '0' && symbol <= '9') || symbol == ',' || symbol == '.' || symbol == '@';
    }


    private static boolean validate1(String line) {
        Pattern pattern = Pattern.compile("([0-9]\\s+[0-9])|" +
                "([+\\-*/^]\\s*[+\\-*/^])|" +
                "[0-9][,.]((\\s)|([0-9]*[,.]))()");
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    private static boolean validate2(String line) {
        Pattern pattern = Pattern.compile("(([,.*/^].*)|(.*[,.+\\-*/^]))");
        Matcher matcher = pattern.matcher(line);
        return matcher.matches();
    }


    private static boolean validate(String expression) {
        return validate1(expression) || validate2(expression);
    }


    public static BigDecimal calculate(String expression) throws EmptyStackException, StringIndexOutOfBoundsException, NumberFormatException {
        Stack<Character> stack = new Stack<>();
        int expressionLength = expression.length();
        StringBuilder stringBuilder = new StringBuilder(expressionLength);
        char symbol;
        for (int i = 0; i < expressionLength; i++) {
            symbol = expression.charAt(i);
            if (numberPart(symbol)) {
                stringBuilder.append(symbol);
            } else {
                int priorityResult = priority(symbol);
                if (priorityResult > -1) {
                    stringBuilder.append(' ');
                    if(symbol == ')'){
                        while(stack.peek()!='('){
                            stringBuilder.append(' ');
                            stringBuilder.append(stack.pop());
                            stringBuilder.append(' ');
                        }
                        stack.pop();
                    }
                    else{
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
                    }
                }
            }
        }
        while (!stack.isEmpty()) {
            symbol = stack.peek();
            stringBuilder.append(' ');
            stringBuilder.append(symbol);
            stringBuilder.append(' ');
            stack.pop();
        }
        Stack<BigDecimal> decimalStack = new Stack<>();
        int outLineLength = stringBuilder.length();
        boolean negative = false;
        for (int i = 0; i < outLineLength; i++) {
            symbol = stringBuilder.charAt(i);
            if (symbol == '@') {
                negative = true;
            }
            if (symbol >= '0' && symbol <= '9') {
                StringBuilder digitString = new StringBuilder();
                for (; symbol != ' '; i++, symbol = stringBuilder.charAt(i)) {
                    if (symbol == ',')
                        symbol = '.';
                    digitString.append(symbol);
                }
                BigDecimal number = new BigDecimal(negative ? "-" + digitString.toString() : digitString.toString());
                decimalStack.push(number);
                negative = false;
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
                        if (number1.compareTo(new BigDecimal(0)) == 0) {
                            errorMessage("Divided by zero");
                        }
                        decimalStack.push(number2.divide(number1, 9, BigDecimal.ROUND_DOWN));
                        break;
                    case '^':
                        double tempNum1 = Double.parseDouble(number2.toString());
                        double tempNum2 = Double.parseDouble(number1.toString());
                        BigDecimal tempRes = new BigDecimal(Math.pow(tempNum1, tempNum2));
                        decimalStack.push(tempRes.setScale(9, BigDecimal.ROUND_DOWN));
                }
            }
        }
        return decimalStack.peek();
    }


    private static String correct(String line) {
        return line.replaceAll("\\^\\s*\\-", "^ @")
                .replaceAll("[(]\\s*[+]", "(")
                .replaceAll("^\\s*[+]", "")
                .replaceAll("[(]\\s*[-]", "(@")
                .replaceAll("^\\s*[-]", "@")
                .replaceAll("\\^\\s*\\+", "^ ");
    }

    public static void main(String[] args) {
        System.out.print("Input expression: ");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String expression = correct(line);
        if (validate(expression) || !allowedChar(line)) {
            errorMessage("Wrong input symbols");
        }
        try {
            System.out.println("Res: " + calculate(expression));
        } catch (EmptyStackException | StringIndexOutOfBoundsException e) {
            errorMessage("Wrong input expression");
        }
    }
}
