package name.salathiel.genese;

import java.util.Scanner;
import java.util.TreeMap;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.of;

public class Day04 {
    public static void main(String[] args) {
//        var sum = 0;
        var count = 0;
        final var loader = Day04.class.getClassLoader();
        final var wins = new TreeMap<Integer, Integer>(Integer::compareTo);

        try (final var scanner = new Scanner(requireNonNull(loader.getResourceAsStream("day-04-input.txt")))) {
            while (scanner.hasNext()) {
//                sum += Card.from(scanner.nextLine()).worth();
                final var card = Card.from(scanner.nextLine());
                wins.put(card.id, 1 + wins.getOrDefault(card.id, 0));
                while (0 < wins.get(card.id)) {
                    for (int i = 0, id = card.id, l = card.winsCount(), __ = wins.put(id, wins.get(id) - 1); i < l; i++) {
                        wins.put(id + i + 1, 1 + wins.getOrDefault(id + i + 1, 0));
                    }
                    ++count;
                }
            }
        }

//        System.out.println(sum);
        System.out.println(count);
    }

    record Card(int id, int[] winning, int[] haves) {
        public int worth() {
            final var count = winsCount();
            return count < 2 ? count : 2 << (count - 2);
        }

        private int winsCount() {
            final var winning = of(this.winning).boxed().collect(toMap(identity(), identity()));
            return (int) of(this.haves).filter(winning::containsKey).count();
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
