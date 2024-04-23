package Stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class List {
    public static void main(String[] args) {
//        ArrayList<String> cities = new ArrayList<>(Arrays.asList("ShangHai","BeiJin","ShenZheng","GuangDong","XiZhang"));
//        //sort(cities);
//        cities.stream().sorted().forEach(System.out::println);
//
//        ArrayList<Integer> scores = new ArrayList<>(Arrays.asList(1,2,3,44,55,63,75));
//        //map(scores);
//        scores.stream().map(x -> x.toString()).forEach(System.out::println);

        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 5, 312, 4, 31, 56, 66, 54, 23, 12, 76, 342, 231, 7656, 312, 342, 56, 34, 123, 88));
        //��һ��
        numbers.stream().filter(x -> x % 2 == 0).map(x -> x.toString()).forEach(System.out::println);

        ArrayList<String> letters = new ArrayList<>(Arrays.asList("aaaa", "asdasdasda", "sdasdasxcaq", "a", "g", "h"));
        //�ڶ���
        letters.stream().filter(x -> x.length() >= 5).map(String::toUpperCase).forEach(System.out::println);

        //������
        System.out.println(numbers.stream().reduce((x, y) -> x + y).get());

        //������
        System.out.println("�Ƿ����ĳ���ܱ�3��������:" + numbers.stream().anyMatch(x -> x % 3 == 0));

        //������
        System.out.println(numbers.stream().collect(Collectors.toMap(
                number -> number,
                number -> number * number,
                (existing, replacement) -> existing//�����ظ��ļ�������ԭ���ļ�ֵ
        )));

        //������
        System.out.println("letters contains string ��aaaa��:" + letters.stream().anyMatch(s -> s.equals("aaaa")));

        //������
        System.out.println("max is :" + numbers.stream().max(Integer::compareTo).orElseThrow() + " min is :" + numbers.stream().min(Integer::compareTo).orElseThrow());

        //�ڰ���
        System.out.println(numbers.stream().collect(Collectors.toMap(
                number -> number,
                number -> number * number,
                (existing, replacement) -> existing//�����ظ��ļ�������ԭ���ļ�ֵ
        )));
    }


    private static void sort(ArrayList<String> cities) {
        cities.stream().sorted().forEach(System.out::println);
    }

    private static void map(ArrayList<Integer> scores) {
        scores.stream().map(x -> x.toString()).forEach(System.out::println);
    }

}
