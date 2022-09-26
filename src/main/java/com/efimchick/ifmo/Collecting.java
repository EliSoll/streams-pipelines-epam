package com.efimchick.ifmo;

import com.efimchick.ifmo.util.CourseResult;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Collecting {
    public int sum(IntStream limit) {
        return limit.sum();
    }

    public int production(IntStream limit) {
        return 0;
    }

    public int oddSum(IntStream limit) {
        return 0;
    }



    public double averageTotalScore(Stream<CourseResult> programmingResults) {
        return 0;
    }

    public <V, K> Map<K, V> sumByRemainder(int i, IntStream limit) {
        return null;
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
