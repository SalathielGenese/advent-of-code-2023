package name.salathiel.genese;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

public class Day02 {
    public static void main(String[] args) {
        var sum = 0;
        final var RED = 12;
        final var BLUE = 14;
        final var GREEN = 13;

        try (final var scanner = new Scanner(requireNonNull(
                Day01Challenge01.class.getClassLoader().getResourceAsStream("day-02-input.txt")))) {
            while (scanner.hasNext()) {
                final var game = scanner.nextLine();
                final var parts = game.split(":");
                final var subsets = Arrays.stream(parts[1].strip().split(";"))
                        .map(String::strip)
                        .map(set -> {
                            final var subsetMap = Arrays
                                    .stream(set.strip().split(","))
                                    .map(String::strip)
                                    .map(subset -> switch (subset) {
                                        case String __ when subset.endsWith("red") ->
                                                new SimpleEntry<>("red", parseInt(subset.split(" ")[0]));
                                        case String __ when subset.endsWith("blue") ->
                                                new SimpleEntry<>("blue", parseInt(subset.split(" ")[0]));
                                        case String __ when subset.endsWith("green") ->
                                                new SimpleEntry<>("green", parseInt(subset.split(" ")[0]));
                                        default -> throw new UnsupportedOperationException();
                                    })
                                    .collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue));
                            return new Subset(
                                    subsetMap.getOrDefault("red", 0),
                                    subsetMap.getOrDefault("blue", 0),
                                    subsetMap.getOrDefault("green", 0));
                        })
                        // .filter(__ -> __.red <= RED && __.blue <= BLUE && __.green <= GREEN)
                        .toList();
                final var requirements = new Subset(
                        subsets.stream().map(Subset::red).max(Integer::compareTo).orElse(0),
                        subsets.stream().map(Subset::blue).max(Integer::compareTo).orElse(0),
                        subsets.stream().map(Subset::green).max(Integer::compareTo).orElse(0));
                sum += requirements.red * requirements.blue * requirements.green;
//                final var POSSIBLE = Arrays.stream(parts[1].strip().split(";"))
//                        .map(String::strip)
//                        .flatMap(set -> Arrays.stream(set.strip().split(",")))
//                        .map(String::strip)
//                        .allMatch(subset -> switch (subset) {
//                            case String __ when subset.endsWith("red") -> parseInt(subset.split(" ")[0]) <= RED;
//                            case String __ when subset.endsWith("blue") -> parseInt(subset.split(" ")[0]) <= BLUE;
//                            case String __ when subset.endsWith("green") -> parseInt(subset.split(" ")[0]) <= GREEN;
//                            default -> throw new UnsupportedOperationException();
//                        });
//                if (POSSIBLE) sum += Integer.parseInt(parts[0].split(" ")[1]);
            }
        }

        System.out.println(sum);
    }

    record Subset(int red, int blue, int green) {
    }
}
