package name.salathiel.genese;

import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.of;

public class Day04 {
    public static void main(String[] args) {
        var sum = 0;
        final var loader = Day04.class.getClassLoader();

        try (final var scanner = new Scanner(requireNonNull(loader.getResourceAsStream("day-04-input.txt")))) {
            while (scanner.hasNext()) {
                sum += Card.from(scanner.nextLine()).worth();
            }
        }

        System.out.println(sum);
    }

    record Card(int id, int[] winning, int[] haves) {
        public int worth() {
            final var winning = of(this.winning).boxed().collect(toMap(identity(), identity()));
            final var count = (int) of(this.haves).filter(winning::containsKey).count();
            return count < 2 ? count : 2 << (count-2);
        }

        @Override
        public String toString() {
            return "Card[id=%d, winning=%s, haves=%s]".formatted(id, of(winning).boxed().toList(), of(haves).boxed().toList());
        }

        static Card from(String text) {
            return new Card(
                    parseInt(text.replaceAll("^Card\\s+(\\d+).*$", "$1")),
                    stream(text.replaceAll("^.*:\\s+([\\d\\s]+)\\s+\\|.*$", "$1").split("\\s+")).mapToInt(Integer::parseInt).toArray(),
                    stream(text.replaceAll("^.*\\|\\s+([\\d\\s]+)$", "$1").split("\\s+")).mapToInt(Integer::parseInt).toArray());
        }
    }
}
