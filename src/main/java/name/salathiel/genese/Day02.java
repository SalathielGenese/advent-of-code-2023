package name.salathiel.genese;

import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNull;

public class Day02 {
    public static void main(String[] args) {
        var sum = 0;
        final var RED = 12;
        final var BLUE = 14;
        final var GREEN = 13;

        try (final var scanner = new Scanner(requireNonNull(
                Day01Challenge01.class.getClassLoader().getResourceAsStream("day-02-input.txt")))) {
            while (scanner.hasNext()) {
                final var next = scanner.nextLine();
                final var parts = next.split(":");
                final var POSSIBLE = Arrays.stream(parts[1].strip().split(";"))
                        .map(String::strip)
                        .flatMap(set -> Arrays.stream(set.strip().split(",")))
                        .map(String::strip)
                        .allMatch(subset -> switch (subset) {
                            case String __ when subset.endsWith("red") -> parseInt(subset.split(" ")[0]) <= RED;
                            case String __ when subset.endsWith("blue") -> parseInt(subset.split(" ")[0]) <= BLUE;
                            case String __ when subset.endsWith("green") -> parseInt(subset.split(" ")[0]) <= GREEN;
                            default -> throw new UnsupportedOperationException();
                        });
                if (POSSIBLE) sum += Integer.parseInt(parts[0].split(" ")[1]);
            }
        }

        System.out.println(sum);
    }
}
