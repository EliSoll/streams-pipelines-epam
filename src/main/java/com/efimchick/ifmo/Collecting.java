package com.efimchick.ifmo;

import com.efimchick.ifmo.util.CourseResult;
import com.efimchick.ifmo.util.Person;
import com.google.common.collect.Table;

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

    public String easiestTask(Stream<CourseResult> stream) {
        Map<String, Double> map = averageScoresPerTask(stream);
        Double res = map.values()
                .stream()
                .max((Double::compare))
                .orElse(0.00);
        return map.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(res))
                .findFirst()
                .get()
                .getKey();

    }
    private boolean areProgramming(CourseResult courseResult){
        return courseResult.getTaskResults().keySet().stream().allMatch(key -> key.startsWith("Lab "));
    }
    private String mark(double avg){
        if (avg > 90) return "A";
        if (avg >= 83) return "B";
        if (avg >= 75) return "C";
        if (avg >= 68) return "D";
        if (avg >= 60) return "E";
        else return "F";
    }
    public Collector<CourseResult, ?, String> printableStringCollector() {
        return new Collector<CourseResult, List<CourseResult>, String>() {
            @Override
            public Supplier<List<CourseResult>> supplier() {
                return ArrayList::new;
            }

            @Override
            public BiConsumer<List<CourseResult>, CourseResult> accumulator() {
                return List::add;
            }

            @Override
            public BinaryOperator<List<CourseResult>> combiner() {
                return null;
            }

            @Override
            public Function<List<CourseResult>, String> finisher() {

                return courseResults -> {
                    String tasks;
                    Map<String,Integer> programTasks = new TreeMap<>() {{
                        put("Lab 1. Figures", "Lab 1. Figures".length());
                        put("Lab 2. War and Peace","Lab 2. War and Peace".length());
                        put("Lab 3. File Tree","Lab 3. File Tree".length());
                    }};

                    Map<String,Integer> historyTasks = new TreeMap<>() {{
                        put("Phalanxing", "Phalanxing".length());
                        put("Shieldwalling","Shieldwalling".length());
                        put("Tercioing","Tercioing".length());
                        put("Wedging","Wedging".length());
                    }};

                    Map<String,Integer> defaultTaskScores = new HashMap<>(){{
                        put("Phalanxing", 0);
                        put("Shieldwalling",0);
                        put("Tercioing",0);
                        put("Wedging",0);
                    }};

                    List<CourseResult> courseResultList = courseResults.stream().map(c -> new CourseResult(c.getPerson(),c.getTaskResults())).collect(Collectors.toList());

                    if (areProgramming(courseResults.get(0)))
                        tasks= String.join(" | ", programTasks.keySet());
                    else {
                        tasks = String.join(" | ", historyTasks.keySet());
                        for (CourseResult c: courseResultList
                        ) {
                            defaultTaskScores.keySet().forEach(k -> c.getTaskResults().putIfAbsent(k, 0));
                        }
                    }

                    double averageTotalScore = averageTotalScore(Stream.of(courseResults.toArray(new CourseResult[0])));

                    Person longestName= courseResults.stream().max((e1,e2)->
                                    e1.getPerson().getFirstName().length()+e1.getPerson().getLastName().length() > e2.getPerson().getFirstName().length()+e2.getPerson().getLastName().length() ? 1:-1)
                            .get().getPerson();
                    int nameLength = longestName.getFirstName().length()+longestName.getLastName().length();

                    String summary="\n"+String.format(Locale.US,"%-"+(nameLength+1)+"s","Average")+" | "+averageScoresPerTask(Stream.of(courseResults.toArray
                            (new CourseResult[0]))).entrySet().stream()
                            .sorted(Map.Entry.comparingByKey())
                            .map(t->String.format(Locale.US,"%"+t.getKey().length()+".2f",Double.valueOf(String.format(Locale.US,"%.2f", t.getValue())))).collect(Collectors.joining(" | "))
                            +" | "+String.format(Locale.US,"%.2f",averageTotalScore) +" |    "+mark(averageTotalScore)+" |";

                    return String.format(Locale.US,"%-"+(nameLength+1)+"s","Student")+" | "+tasks+" | Total | Mark |\n"+courseResultList.stream()
                            .sorted(Comparator.comparing(p -> p.getPerson().getLastName()))
                            .map(D -> D.getPerson().getLastName() + " " + String.format(Locale.US,"%-"+(nameLength-D.getPerson().getLastName().length())+"s",D.getPerson().getFirstName()) + " | "+
                                    D.getTaskResults().entrySet().stream().sorted(Map.Entry.comparingByKey()).map(e->String.format(Locale.US,"%"+e.getKey().length()+"s",e.getValue().toString())).collect(Collectors.joining(" | "))+" | "+
                                    totalScores(Stream.of(D)).values().stream().map(aDouble -> String.format(Locale.US,"%.2f", aDouble)).collect(Collectors.joining())+" |    "+
                                    String.join("", defineMarks(Stream.of(D)).values()) +" |")
                            .collect(Collectors.joining("\n"))+summary;

                };
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Set.of(Characteristics.UNORDERED);
            }
        };
    }
}