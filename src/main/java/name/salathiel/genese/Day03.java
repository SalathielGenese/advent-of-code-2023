package name.salathiel.genese;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNull;

public class Day03 {
    record GearRef(int line, int start, int ratio) {
    }

    record PartRef(int line, int start, int value) {
    }

    private static final Pattern SPECIAL_PATTERN = Pattern.compile("[^\\d.]");
    private static final Set<Character> NUMBER_DIGITS = "0123456789".chars().boxed().map(__ -> (char) __.intValue()).collect(Collectors.toSet());

    public static void main(String[] args) {
        // NOTE: Use a sorted set for debugging purposes
        final var gearRefs = new TreeSet<GearRef>((a, b) -> 0 == a.line - b.line ? a.start - b.start : a.line - b.line);
        final var partRefs = new TreeSet<PartRef>((a, b) -> 0 == a.line - b.line ? a.start - b.start : a.line - b.line);
        final var loader = Day03.class.getClassLoader();

        try (final var scanner = new Scanner(requireNonNull(loader.getResourceAsStream("day-03-input.txt")))) {
            var line = 0;
            final var lines = new LinkedList<String>();

            if (scanner.hasNext()) {
                lines.addLast(scanner.nextLine());
                if (!scanner.hasNext()) getPartRefs(lines, 0, line).accept((parts, gears) -> {
                    gearRefs.addAll(gears);
                    partRefs.addAll(parts);
                });
            }

            while (scanner.hasNext()) {
                if (2 < lines.size()) lines.removeFirst();
                lines.addLast(scanner.nextLine());
                getPartRefs(lines, Math.min(line, 1), line).accept((parts, gears) -> {
                    gearRefs.addAll(gears);
                    partRefs.addAll(parts);
                });
                line++;
            }

            if (2 < lines.size()) getPartRefs(lines, 2, line).accept((parts, gears) -> {
                gearRefs.addAll(gears);
                partRefs.addAll(parts);
            });
        }

        // partRefs.stream().collect(Collectors.groupingBy(PartRef::line)).forEach((line, parts) -> System.out.println("[%d] => %s".formatted(line, parts.stream().map(PartRef::value).toList())));
//        System.out.println(partRefs.stream().mapToInt(PartRef::value).sum());
        System.out.println(gearRefs.stream().mapToInt(GearRef::ratio).sum());
    }

    private static Consumer<BiConsumer<List<PartRef>, List<GearRef>>> getPartRefs(List<String> lines, int cursor, int line) {
        final var gearRefs = new LinkedList<GearRef>();
        final var partRefs = new LinkedList<PartRef>();
        final var chars = lines.get(cursor).toCharArray();
        for (int i = 0, cl = chars.length; i < cl; i++) {
            if (SPECIAL_PATTERN.matcher("%c".formatted(chars[i])).matches()) {
                final var specialPartRefs = new LinkedList<PartRef>();
                final var BOTTOM_RIGHT_MATCH = cursor + 1 < lines.size() && i + 1 < lines.get(cursor + 1).length() && NUMBER_DIGITS.contains(lines.get(cursor + 1).charAt(i + 1));
                final var BOTTOM_MATCH = cursor + 1 < lines.size() && i < lines.get(cursor + 1).length() && NUMBER_DIGITS.contains(lines.get(cursor + 1).charAt(i));
                final var BOTTOM_LEFT_MATCH = cursor + 1 < lines.size() && 0 < i && NUMBER_DIGITS.contains(lines.get(cursor + 1).charAt(i - 1));

                final var TOP_RIGHT_MATCH = 0 < cursor && i + 1 < lines.get(cursor - 1).length() && NUMBER_DIGITS.contains(lines.get(cursor - 1).charAt(i + 1));
                final var TOP_MATCH = 0 < cursor && i < lines.get(cursor - 1).length() && NUMBER_DIGITS.contains(lines.get(cursor - 1).charAt(i));
                final var TOP_LEFT_MATCH = 0 < cursor && 0 < i && NUMBER_DIGITS.contains(lines.get(cursor - 1).charAt(i - 1));

                final var RIGHT_MATCH = i + 1 < lines.get(cursor).length() && NUMBER_DIGITS.contains(lines.get(cursor).charAt(i + 1));
                final var LEFT_MATCH = 0 < i - 1 && NUMBER_DIGITS.contains(lines.get(cursor).charAt(i - 1));

                if (BOTTOM_LEFT_MATCH) specialPartRefs.addLast(getPartRef(lines.get(cursor + 1), i - 1, line + 1));
                else if (BOTTOM_MATCH) specialPartRefs.addLast(getPartRef(lines.get(cursor + 1), i, line + 1));
                if (!BOTTOM_MATCH && BOTTOM_RIGHT_MATCH)
                    specialPartRefs.addLast(getPartRef(lines.get(cursor + 1), i + 1, line + 1));

                if (TOP_LEFT_MATCH) specialPartRefs.addLast(getPartRef(lines.get(cursor - 1), i - 1, line - 1));
                else if (TOP_MATCH) specialPartRefs.addLast(getPartRef(lines.get(cursor - 1), i, line - 1));
                if (!TOP_MATCH && TOP_RIGHT_MATCH)
                    specialPartRefs.addLast(getPartRef(lines.get(cursor - 1), i + 1, line - 1));

                if (RIGHT_MATCH) specialPartRefs.addLast(getPartRef(lines.get(cursor), i + 1, line));
                if (LEFT_MATCH) specialPartRefs.addLast(getPartRef(lines.get(cursor), i - 1, line));

                if ('*' == chars[i] && 2 == specialPartRefs.size()) gearRefs.addLast(new GearRef(
                        line,
                        i,
                        specialPartRefs.stream().mapToInt(PartRef::value).reduce(1, (a, b) -> a * b)));

                partRefs.addAll(specialPartRefs);
            }
        }
        return callback -> callback.accept(partRefs, gearRefs);
    }

    private static PartRef getPartRef(String context, int caret, int line) {
        while (0 < caret && NUMBER_DIGITS.contains(context.charAt(caret - 1))) --caret;
        return new PartRef(line, caret, parseInt(context.substring(caret).replaceAll("^(\\d+).*$", "$1")));
    }
}
