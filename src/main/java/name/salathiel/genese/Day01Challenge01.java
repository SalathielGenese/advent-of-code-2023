package name.salathiel.genese;

import java.math.BigInteger;
import java.util.*;

public class Day01Challenge01 {
    //    public static void main(String[] args) {
//        final var INPUT = "day-01-challenge-01-input.txt";
//        var sum = BigInteger.ZERO;
//
//        try (final var scanner = new Scanner(Day01Challenge01.class.getClassLoader().getResourceAsStream(INPUT))) {
//            while (scanner.hasNext()) {
//                final var nextLine = scanner.nextLine();
//                final var first = nextLine.replaceAll("^[^[0-9]]*([0-9]).*$", "$1");
//                final var last = nextLine.replaceAll("^.*([0-9])[^[0-9]]*$", "$1");
//                sum = sum.add(new BigInteger(first + last));
//            }
//        }
//
//        System.out.println(sum);
//    }
    public static void main(String[] args) {
        final var DIGITS = Set.of("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        final var MAP = Map.of("zero", "0", "one", "1", "two", "2", "three", "3", "four", "4", "five", "5", "six", "6", "seven", "7", "eight", "8", "nine", "9");

        final var INPUT = "day-01-challenge-01-input.txt";
        var sum = BigInteger.ZERO;

        try (final var scanner = new Scanner(Day01Challenge01.class.getClassLoader().getResourceAsStream(INPUT))) {
            while (scanner.hasNext()) {
                final var nextLine = scanner.nextLine();
                if (!nextLine.isBlank()) {
                    final var first = DIGITS.stream()
                            .map(digit -> new AbstractMap.SimpleEntry<>(digit, nextLine.indexOf(digit)))
                            .filter(entry -> 0 <= entry.getValue())
                            .min(Comparator.comparingInt(Map.Entry::getValue))
                            .map(Map.Entry::getKey)
                            .map(digit -> MAP.getOrDefault(digit, digit))
                            .orElseThrow();
                    final var last = DIGITS.stream()
                            .map(digit -> new AbstractMap.SimpleEntry<>(digit, nextLine.lastIndexOf(digit)))
                            .max(Comparator.comparingInt(Map.Entry::getValue))
                            .map(Map.Entry::getKey)
                            .map(digit -> MAP.getOrDefault(digit, digit))
                            .orElseThrow();

                    System.out.println("[%s] %s => %s".formatted(sum, nextLine, first + last));


//                final var first = nextLine.replaceAll("^[^[0-9]]*([0-9]).*$", "$1");
//                final var last = nextLine.replaceAll("^.*([0-9])[^[0-9]]*$", "$1");
                    sum = sum.add(new BigInteger(first + last));
                }
            }
        }

        System.out.println(sum);
    }
}
