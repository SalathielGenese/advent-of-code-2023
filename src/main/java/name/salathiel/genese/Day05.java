package name.salathiel.genese;

import java.math.BigInteger;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Stream.iterate;

public class Day05 {
    public static void main(String[] args) {
        TreeSet<Mapping> anchor = null;
//        final var seeds = new TreeSet<>(BigInteger::compareTo);
        final var loader = Day05.class.getClassLoader();
        final var seedToSoil = new TreeSet<>(comparing(Mapping::source));
        final var waterToLight = new TreeSet<>(comparing(Mapping::source));
        final var soilToFertilizer = new TreeSet<>(comparing(Mapping::source));
        final var fertilizerToWater = new TreeSet<>(comparing(Mapping::source));
        final var humidityToLocation = new TreeSet<>(comparing(Mapping::source));
        final var lightToTemperature = new TreeSet<>(comparing(Mapping::source));
        final var temperatureToHumidity = new TreeSet<>(comparing(Mapping::source));
        final var seeds = new TreeMap<BigInteger, BigInteger>(BigInteger::compareTo);
        try (final var scanner = new Scanner(requireNonNull(loader.getResourceAsStream("day-05-input.txt")))) {
            while (scanner.hasNext()) {
                final var next = scanner.nextLine();
                if (next.isBlank()) {
                    anchor = null;
                } else if (next.startsWith("seeds:")) {
                    final var chunks = Stream.of(next.split(": ")[1].split(" "))
                            .map(BigInteger::new)
                            .toArray(BigInteger[]::new);

                    for (int i = 0, l = chunks.length; i < l; i += 2) {
                        seeds.put(chunks[i], chunks[i + 1]);
                    }
                } else if (next.startsWith("seed-to-soil map:")) {
                    anchor = seedToSoil;
                } else if (next.startsWith("soil-to-fertilizer map:")) {
                    anchor = soilToFertilizer;
                } else if (next.startsWith("fertilizer-to-water map:")) {
                    anchor = fertilizerToWater;
                } else if (next.startsWith("water-to-light map:")) {
                    anchor = waterToLight;
                } else if (next.startsWith("light-to-temperature map:")) {
                    anchor = lightToTemperature;
                } else if (next.startsWith("temperature-to-humidity map:")) {
                    anchor = temperatureToHumidity;
                } else if (next.startsWith("humidity-to-location map:")) {
                    anchor = humidityToLocation;
                } else if (null != anchor) {
                    anchor.add(Mapping.from(next));
                }
            }
        }

//        seeds.stream()

        final var min = new AtomicReference<>(BigInteger.valueOf(Long.MAX_VALUE));
        seeds.entrySet()
                .stream()
                .parallel()
                .flatMap(entry -> {
                    final var left = entry.getKey();
                    final var right = left.add(entry.getValue());

                    return iterate(left, __ -> __.compareTo(right) < 0, __ -> __.add(ONE)).parallel();
                })

                .map(__ -> resolve(seedToSoil, __).translate(__))
                .map(__ -> resolve(soilToFertilizer, __).translate(__))
                .map(__ -> resolve(fertilizerToWater, __).translate(__))
                .map(__ -> resolve(waterToLight, __).translate(__))
                .map(__ -> resolve(lightToTemperature, __).translate(__))
                .map(__ -> resolve(temperatureToHumidity, __).translate(__))
                .map(__ -> resolve(humidityToLocation, __).translate(__))
                .filter(__ -> __.compareTo(min.get()) < 0)
                .forEach(min::set);

        System.out.println(min);

//        System.out.println("SEEDS: " + seeds);
//        System.out.println("SEEDS TO SOIL: " + seedToSoil);
//        System.out.println("WATER TO LIGHT" + waterToLight);
//        System.out.println("SOIL to FERTILIZER: " + soilToFertilizer);
//        System.out.println("FERTILIZER TO WATER: " + fertilizerToWater);
//        System.out.println("HUMIDITY TO LOCATION: " + humidityToLocation);
//        System.out.println("LIGHT TO TEMPERATURE: " + lightToTemperature);
//        System.out.println("TEMPERATURE TO HUMIDITY: " + temperatureToHumidity);
    }

    static Mapping resolve(TreeSet<Mapping> mappings, BigInteger source) {
        for (final var mapping : mappings) {
            if (mapping.containsSource(source))
                return mapping;
        }

        return Mapping.empty(source);
    }

    record Mapping(BigInteger source, BigInteger destination, BigInteger range) {
        BigInteger translate(BigInteger source) {
            return destination.subtract(this.source).add(source);
        }

        boolean containsSource(BigInteger source) {
            return this.source.compareTo(source) <= 0 && source.compareTo(this.source.add(range)) < 0;
        }

        static Mapping empty(BigInteger value) {
            return new Mapping(value, value, ONE);
        }

        static Mapping from(String text) {
            final var parsed = Stream
                    .of(text.split(" "))
                    .map(BigInteger::new)
                    .toArray(BigInteger[]::new);
            return new Mapping(parsed[1], parsed[0], parsed[2]);
        }
    }
}
