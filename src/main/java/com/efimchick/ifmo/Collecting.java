package com.efimchick.ifmo;

import com.efimchick.ifmo.util.CourseResult;
import com.efimchick.ifmo.util.Person;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.*;

public class Collecting {
    int counter = 0;

    public int sum(IntStream stream) {
        return stream.sum();
    }

    public int production(IntStream stream) {
        return stream.reduce(1, (a, b) -> a * b);
    }

    public int oddSum(IntStream stream) {
        return stream.filter(e -> (e % 2) != 0).sum();
    }


    public double averageTotalScore(Stream<CourseResult> stream) {
        return totalScores(stream).values().stream().collect(Collectors.averagingDouble(s -> s));
    }

    public Map<Object, Integer> sumByRemainder(int divisor, IntStream stream) {
        return stream.boxed().collect(Collectors.groupingBy(s -> s % divisor,
                Collectors.summingInt(x -> x)));
    }


    public Map<Person, Double> totalScores(Stream<CourseResult> stream) {
        List<CourseResult> b = stream.collect(Collectors.toList());

        Map<Person, Integer> mapSum = b.stream().collect(Collectors.toMap(CourseResult::getPerson,
                courseResult -> courseResult.getTaskResults()
                        .values()
                        .stream()
                        .mapToInt(e -> e)
                        .sum()));
        long counter = b.stream().map(courseResult -> courseResult.getTaskResults()
                        .keySet())
                .collect(Collectors.toSet())
                .stream()
                .flatMap(Collection::stream)
                .distinct()
                .count();
        return mapSum.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> 1.00 * e.getValue() / counter));
    }


    public Map<String, Double> averageScoresPerTask(Stream<CourseResult> stream) {
        Set<CourseResult> list = stream.collect(Collectors.toSet());
        long counter = list.stream().map(CourseResult::getPerson).count();

        Map<String, Double> mapSum = list.stream().map(CourseResult::getTaskResults)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.summingDouble(Map.Entry::getValue)));

        return mapSum.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() / counter));
    }


    public Map<Person, String> defineMarks(Stream<CourseResult> results) {
        return totalScores(results).entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        x -> {
                            double avg = x.getValue();
                            if (avg > 90) return "A";
                            if (avg >= 83) return "B";
                            if (avg >= 75) return "C";
                            if (avg >= 68) return "D";
                            if (avg >= 60) return "E";
                            else return "F";
                        }
                ));
    }

    public String easiestTask(Stream<CourseResult> results) {
        return results.map(CourseResult::getTaskResults)
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.averagingInt(Map.Entry::getValue)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No easy task found");
    }

    public Collector printableStringCollector() {
        /*String.format(Locale.US,"\n%20s|%s| %15.2f|%10s","Average",averageScores, averageTotalScore, mark(averageTotalScore));*/
        return new Collector() {
            @Override
            public Supplier supplier() {
                return null;
            }

            @Override
            public BiConsumer accumulator() {
                return null;
            }

            @Override
            public BinaryOperator combiner() {
                return null;
            }

            @Override
            public Function finisher() {
                return null;
            }

            @Override
            public Set<Characteristics> characteristics() {
                return null;
            }
        };
    }
}
