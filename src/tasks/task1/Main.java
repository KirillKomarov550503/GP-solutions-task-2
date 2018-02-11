package tasks.task1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Main {

    private static boolean test(String line, String param) {
        String[] params = param.split("\\s+");
        String[] words = line.split("\\s+");
        for (String par : params) {
            try {
                Pattern pattern = Pattern.compile(par);
                Matcher matcher;
                for (String word : words) {
                    matcher = pattern.matcher(word);
                    if (matcher.matches())
                        return true;
                }
            } catch (PatternSyntaxException e) {
                for (String word : words) {
                    if (param.equals(word))
                        return true;
                }
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
