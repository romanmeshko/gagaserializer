package io.shooroop.gaga.benchmarks;

import java.util.*;

public class TestData {
    static String getRandomStr() {
        return UUID.randomUUID().toString();

    }

    static TestClass create() {
        return new TestClass();
    }
}


class TestClass {
    private int i = 2;

    private String s = TestData.getRandomStr();

    private boolean meow = true;

    Letters let = Letters.A;

    InnerTestClass b = new InnerTestClass();
    private Map<String, Exception> exceptionMap = new HashMap<>();
    private Set<Exception> exceptionSet = new HashSet<>();

    private String[] arrStr = {TestData.getRandomStr(), TestData.getRandomStr(), TestData.getRandomStr()};

    private Exception[] excArr = {new Exception(TestData.getRandomStr()), new Exception(TestData.getRandomStr())};

    char[] arr = {'q', 'd', '1'};

    {
        for (int i = 3; i < 100; i++) {
            exceptionMap.put(TestData.getRandomStr(), new Exception(TestData.getRandomStr()));
            exceptionSet.add(new Exception(TestData.getRandomStr()));
        }
    }

    public int getI() {
        return i;
    }

    public String getS() {
        return s;
    }

    public boolean isMeow() {
        return meow;
    }

    public Letters getLet() {
        return let;
    }

    public InnerTestClass getB() {
        return b;
    }

    public Map<String, Exception> getExceptionMap() {
        return exceptionMap;
    }

    public Set<Exception> getExceptionSet() {
        return exceptionSet;
    }

    public String[] getArrStr() {
        return arrStr;
    }

    public Exception[] getExcArr() {
        return excArr;
    }

    public char[] getArr() {
        return arr;
    }
}

enum Letters {

    A(1), B(2), C(3);

    int i;

    Letters(int i) {
        this.i = i;
    }
}

class InnerTestClass {

    private Map<String, Object> standardMap = new HashMap<>();

    {
        for (int i = 1; i < 3; i++) {
            standardMap.put(TestData.getRandomStr(), new Exception(TestData.getRandomStr()));
        }
    }

    public Map<String, Object> getStandardMap() {
        return standardMap;
    }
}

