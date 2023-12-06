package name.salathiel.genese;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

public class Day06 {
    public static void main(String[] args) {
        long product = 1;
        int[] times = null, distances = null;
        final var loader = Day06.class.getClassLoader();

        try (final var scanner = new Scanner(requireNonNull(loader.getResourceAsStream("day-06-input.txt")))) {
            while (scanner.hasNext()) {
                final var next = scanner.nextLine();
                if (next.startsWith("Time:")) {
                    times = Arrays.stream(next.split(":")[1].strip().split("\\s+"))
                            .mapToInt(Integer::parseInt)
                            .toArray();
                } else if (next.startsWith("Distance:")) {
                    distances = Arrays.stream(next.split(":")[1].strip().split("\\s+"))
                            .mapToInt(Integer::parseInt)
                            .toArray();
                }
            }
        }

        for (var i = 0; i < times.length; i++) {
            product *= getRecordChargeTimes(times[i], distances[i]).size();
        }

        System.out.println(product);
    }

    static Set<Integer> getRecordChargeTimes(int time, int distance) {
        final var chargeTimes = new LinkedHashSet<Integer>();

        for (int i = 1; i < time; i++) {
            if (i * (time - i) > distance) {
                chargeTimes.add(i);
            }
        }
        return chargeTimes;
    }
}
