package name.salathiel.genese;

import java.math.BigInteger;
import java.util.Scanner;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.valueOf;
import static java.util.Objects.requireNonNull;

public class Day06 {
    public static void main(String[] args) {
//        long product = 1;
//        int[] times = null, distances = null;
        BigInteger[] times = null, distances = null;
        final var loader = Day06.class.getClassLoader();

        try (final var scanner = new Scanner(requireNonNull(loader.getResourceAsStream("day-06-input.txt")))) {
            while (scanner.hasNext()) {
                final var next = scanner.nextLine();
                if (next.startsWith("Time:")) {
//                    times = Arrays.stream(next.split(":")[1].strip().split("\\s+"))
//                            .mapToInt(Integer::parseInt)
//                            .toArray();
                    times = new BigInteger[]{new BigInteger(next.split(":")[1].replaceAll("\\s+", ""))};
                } else if (next.startsWith("Distance:")) {
//                    distances = Arrays.stream(next.split(":")[1].strip().split("\\s+"))
//                            .mapToInt(Integer::parseInt)
//                            .toArray();
                    distances = new BigInteger[]{new BigInteger(next.split(":")[1].replaceAll("\\s+", ""))};
                }
            }
        }

//        for (var i = 0; i < times.length; i++) {
//            product *= getRecordChargeTimes(times[i], distances[i]).size();
//        }

//        System.out.println(product);
        System.out.println(getRecordChargeTimes(times[0], distances[0]));
    }

//    static Set<Integer> getRecordChargeTimes(int time, int distance) {
//        final var chargeTimes = new LinkedHashSet<Integer>();
//
//        for (int i = 1; i < time; i++) {
//            if (i * (time - i) > distance) {
//                chargeTimes.add(i);
//            }
//        }
//        return chargeTimes;
//    }

    static BigInteger getRecordChargeTimes(BigInteger time, BigInteger distance) {
        var count = BigInteger.ZERO;

        for (BigInteger i = valueOf(1); i.compareTo(time) <= 0; i = i.add(ONE)) {
            if (i.multiply(time.subtract(i)).compareTo(distance) > 0) {
                count = count.add(ONE);
            }
        }
        return count;
    }
}
