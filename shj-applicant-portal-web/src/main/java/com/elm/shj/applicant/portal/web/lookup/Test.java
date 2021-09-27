package com.elm.shj.applicant.portal.web.lookup;

import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        System.out.println("test");


//        System.out.println(maxStreak(2, Arrays.asList(new String[]{"YY", "YY", "YYN", "YY", "YY", "YY", "YY"})));
        System.out.println(maxSubsequenceLength(2, Arrays.asList(new Integer[]{2,1,3,5,2})));
    }

    public static int maxStreak(int m, List<String> data) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < m; i++) {
            stringBuilder.append("Y");
        }

        String expectedAttendance = stringBuilder.toString();

        int maxAttendance = 0;
        boolean isPrevious = false;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).equals(expectedAttendance)) {
                if (isPrevious) {
                    maxAttendance++;
                    isPrevious = true;
                } else {
                    maxAttendance = 1;
                    isPrevious = true;
                }
            } else {
                isPrevious = false;
            }
        }
        return maxAttendance;
    }

    public static int maxSubsequenceLength(int k, List<Integer> arr) {

        int maxSubsequence = 1;
        int curSubsequence = 1;
        boolean isPrevious = false;
        for (int i = 0; i < arr.size(); i++) {

            if (i + 1 < arr.size()) {
                int res = arr.get(i) ^ arr.get(i + 1);
                if (res == k) {
                    if (isPrevious) {
                        curSubsequence++;
                        isPrevious = true;
                    } else {
                        curSubsequence = 2;
                        isPrevious = true;
                    }
                } else {
                    if (curSubsequence > maxSubsequence) {
                        maxSubsequence = curSubsequence;
                    }
                    isPrevious = false;
                    curSubsequence = 1;
                }
            }

        }

        if (curSubsequence > maxSubsequence) {
            maxSubsequence = curSubsequence;
        }
        return maxSubsequence;

    }
}
