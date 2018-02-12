package tasks.task1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static void error(String error) {
        System.err.println(error);
        System.exit(1);
    }


    private static boolean test(String line, String param) {
        Pattern paramPattern = Pattern.compile("[a-zA-z\\s]+");
        Matcher paramMatcher = paramPattern.matcher(param);
        if (paramMatcher.matches()) {
            String[] params = param.split("\\s+");
            if (params.length == 0) {
                error("Wrong input parameter");
            } else {
                for (String par : params) {
                    Pattern pattern = Pattern.compile("(^" + par + "\\s+.*)|" +
                            "(.*\\s+" + par + "\\s+.*)|" + "(.*\\s+" + par + "$)");
                    if (pattern.matcher(line).find())
                        return true;
                }
            }
        } else {
            String[] words = line.split("\\s+");
            Pattern pattern = Pattern.compile(param);
            for (String word : words) {
                if (pattern.matcher(word).matches())
                    return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        System.out.print("Input program parameter: ");
        Scanner scanner = new Scanner(System.in);
        String param = scanner.nextLine();
        List<String> stringList = new ArrayList<>();
        String line;
        System.out.println("Input strings");
        while ((line = scanner.nextLine()).length() != 0) {
            if (test(line, param))
                stringList.add(line);
        }
        System.out.print("Program parameter: " + param + "\n" +
                "Strings" + "\n");
        stringList.forEach(System.out::println);


    }
}
