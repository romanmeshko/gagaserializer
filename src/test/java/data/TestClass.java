package data;

import java.util.*;

public class TestClass {

    private int i;
    private String s;
    private boolean meow;
    private Integer objInt;

    private InnerTestClass b;
    private ArrayList<String> stringArrayList;
    private HashSet<Integer> integers;
    private String[] arrStr;
    private A[] excArr;
    private char[] arr;

    public static TestClass newInstance() {
        TestClass testClass = new TestClass();
        testClass.i = 2;
        testClass.s = getRandomStr();
        testClass.meow = true;
        testClass.objInt = Integer.MAX_VALUE;

        testClass.arrStr = new String[3];
        for (int i = 0; i < 3; i++) {
            testClass.arrStr[i] = getRandomStr();
        }

        testClass.excArr = new A[3];
        for (int i = 0; i < 3; i++) {
            testClass.excArr[i] = new A(getRandomStr());
            testClass.excArr[i] = new A(getRandomStr());
            testClass.excArr[i] = new A(getRandomStr());
        }

        testClass.arr = new char[3];
        testClass.arr[0] = 'q';
        testClass.arr[1] = 'd';
        testClass.arr[2] = '1';

        testClass.b = new InnerTestClass();
        testClass.stringArrayList = new ArrayList<>();
        testClass.stringArrayList.add(getRandomStr());
        testClass.stringArrayList.add(getRandomStr());
        testClass.stringArrayList.add(getRandomStr());

        testClass.integers = new HashSet<>();
        testClass.integers.add(1);
        testClass.integers.add(2);
        testClass.integers.add(4);
        testClass.integers.add(3);

        return testClass;
    }

    private static String getRandomStr() {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestClass)) return false;
        TestClass testClass = (TestClass) o;
        return i == testClass.i &&
                meow == testClass.meow &&
                Objects.equals(s, testClass.s) &&
                Objects.equals(objInt, testClass.objInt) &&
                Objects.equals(b, testClass.b) &&
                Objects.equals(stringArrayList, testClass.stringArrayList) &&
                Objects.equals(integers, testClass.integers) &&
                Arrays.equals(arrStr, testClass.arrStr) &&
                Arrays.equals(excArr, testClass.excArr) &&
                Arrays.equals(arr, testClass.arr);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(i, s, meow, objInt, b, stringArrayList, integers);
        result = 31 * result + Arrays.hashCode(arrStr);
        result = 31 * result + Arrays.hashCode(excArr);
        result = 31 * result + Arrays.hashCode(arr);
        return result;
    }
}
