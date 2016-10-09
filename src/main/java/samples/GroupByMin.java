package samples;


import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jpc on 08-10-16.
 */
public class GroupByMin {
    public String name;
    public int value;

    public GroupByMin(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "GroupByMin{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    public static void main(String[] args) {
        final GroupByMin[] groupByMins = new GroupByMin[]{
                new GroupByMin("one", 2),
                new GroupByMin("one", 1),
                new GroupByMin("two", 3),
                new GroupByMin("two", 5),
                new GroupByMin("three", 165)
        };
        Arrays.stream(groupByMins).forEach(g -> System.out.printf("%s%n", g));
        Map<String, List<GroupByMin>> groups = Arrays.stream(groupByMins).collect(Collectors.groupingBy(GroupByMin::getName, Collectors.toList()));
        Map<String, GroupByMin> min = groups.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().min((g1, g2) -> g1.getValue() - g2.getValue()).orElse(null)));
        System.out.println(min);
    }
}
