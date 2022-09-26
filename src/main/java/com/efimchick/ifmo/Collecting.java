package com.efimchick.ifmo;

import com.efimchick.ifmo.util.CourseResult;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Collecting {
    public int sum(IntStream stream) {
        return stream.sum();
    }

    public int production(IntStream stream) {
        return stream.reduce(1, (a, b) -> a * b);
    }

    public int oddSum(IntStream stream) {
        return stream.filter(e -> (e % 2) != 0).sum();
    }


    public double averageTotalScore(Stream<CourseResult> programmingResults) {
        return 0;
    }

    public Map<Object, Integer> sumByRemainder(int divisor, IntStream stream) {
            return stream.boxed().collect(Collectors.groupingBy(s -> s % divisor,
                    Collectors.summingInt(x -> x)));
        }


    public <V, K> Map<K, V> totalScores(Stream<CourseResult> programmingResults) {
        return null;
    }

    public Map<String, Double> averageScoresPerTask(Stream<CourseResult> historyResults) {
        return null;
    }

    public <V, K> Map<K, V> defineMarks(Stream<CourseResult> programmingResults) {
        return null;
    }

    public String easiestTask(Stream<CourseResult> programmingResults) {
        return null;
    }

    public Collector printableStringCollector() {
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
