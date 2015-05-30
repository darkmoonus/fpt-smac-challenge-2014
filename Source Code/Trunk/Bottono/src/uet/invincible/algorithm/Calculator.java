package uet.invincible.algorithm;

import java.util.ArrayList;
import java.util.List;

public class Calculator {
    public static int fromStringToExpression(String s) {
        final int CONG = 1;
        final int TRU = 2;
        final int NHAN = 3;
        final int CHIA = 4;
        final String[] oneDigitNumber = {"không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"};

        int result = -10000;
        // Separate String s to words
        String delims = "[ ]+";
        String[] words = s.split(delims);

        // testing
        System.out.println("words: ");
        for (int i = 0; i < words.length; i++) {
            System.out.print(words[i] + " ");
        }
        System.out.println();

        //check oparators
        List<Integer> oparatorsPosition = new ArrayList<Integer>();
        List<Integer> oparatorsValue = new ArrayList<Integer>();
        for (int i = 0; i < words.length; i++) {
            if ("cộng".equals(words[i])) {
                oparatorsPosition.add(i);
                oparatorsValue.add(CONG);
            }
            if ("trừ".equals(words[i])) {
                oparatorsPosition.add(i);
                oparatorsValue.add(TRU);
            }
            if ("nhân".equals(words[i])) {
                oparatorsPosition.add(i);
                oparatorsValue.add(NHAN);
            }
            if ("chia".equals(words[i])) {
                oparatorsPosition.add(i);
                oparatorsValue.add(CHIA);
            }
        }

        if (oparatorsPosition.size() == 0) return result;

        //testing
        System.out.println("Number of operator position: ");
        System.out.println(oparatorsPosition.size());
        System.out.println("Number of operator value: ");
        System.out.println(oparatorsValue.size());

        //Converting number
        List<Integer> numbers = new ArrayList<Integer>();
        //check special cases
        int temp = 0;
        for (int i = 0; i < words.length; i++) {
            if (words[i].charAt(0) >= '0' && words[i].charAt(0) <= '9') {
                temp = 0;
                for (int j = 0; j < words[i].length(); j++) {
                    temp = temp * 10 + words[i].charAt(j) - 48;
                }
                numbers.add(temp);
            } else
                {
                    for (int j = 0; j < oneDigitNumber.length; j++) {
                        if (words[i].equals(oneDigitNumber[j])) {
                            numbers.add(j);
                            continue;
                        }
                    }
                }
        }

        //normal case
//        int temp = 0;
//        for (int i = 0; i < words.length; i++) {
//            if (words[i].charAt(0) < '0' || words[i].charAt(0) > '9') continue;
//            temp = 0;
//            for (int j = 0; j < words[i].length(); j++) {
//                temp = temp * 10 + words[i].charAt(j) - 48;
//            }
//            numbers.add(temp);
//        }

        if (numbers.size() <= oparatorsPosition.size()) return result;

        //testing
        for (int i = 0; i < numbers.size(); i++) {
            System.out.println(numbers.get(i));
        }

        //Calculate

        // Multiply and divide
        for (int i = 0; i < oparatorsPosition.size(); i++) {
            if (oparatorsValue.get(i) == NHAN || oparatorsValue.get(i) == CHIA) {
                if (oparatorsValue.get(i) == NHAN)
                    temp = numbers.get(i) * numbers.get(i + 1);
                if (oparatorsValue.get(i) == CHIA)
                    temp = numbers.get(i) / numbers.get(i + 1);
                numbers.set(i, 0);
                oparatorsValue.set(i, CONG);
                if (i > 0 && oparatorsValue.get(i - 1) == TRU) {
                    temp *= -1;
                }
                numbers.set(i + 1, temp);
            }
        }

        //Add and Sub
        int numberOfOparators = oparatorsPosition.size();
        result = numbers.get(0);
        for (int i = 0; i < numberOfOparators; i++) {
            if (oparatorsValue.get(i) == CONG) {
                result += numbers.get(i + 1);
            }
            if (oparatorsValue.get(i) == TRU) {
                result -= numbers.get(i + 1);
            }
        }
        return result;
    }

    public static String count(String s) {

        String result = "";

        // Separate String s to words
        String delims = "[ ]+";
        String[] words = s.split(delims);

        // testing
        System.out.println("words: ");
        for (int i = 0; i < words.length; i++) {
            System.out.print(words[i] + " ");
        }
        System.out.println();

        //check count expression
        if (!s.contains("từ") && !s.contains("đến") && !s.contains("đếm")) {
            return result;
        }
        ;

        //Converting number
        List<Integer> numbers = new ArrayList<Integer>();
        int temp = 0;
        for (int i = 0; i < words.length; i++) {
            if (words[i].charAt(0) < '0' || words[i].charAt(0) > '9') continue;
            temp = 0;
            for (int j = 0; j < words[i].length(); j++) {
                temp = temp * 10 + words[i].charAt(j) - 48;
            }
            numbers.add(temp);
        }

        if (numbers.size() != 2) return result;

        //testing
        System.out.println("Counting numbers");
        for (int i = 0; i < numbers.size(); i++) {
            System.out.println(numbers.get(i));
        }

        int first = numbers.get(0), second = numbers.get(1);
        if (numbers.get(0) > numbers.get(1)) {
            for (int i = first; i >= second; i--) {
                result += Integer.toString(i) + " . ";
            }
        } else {
            for (int i = first; i <= second; i++) {
                result += Integer.toString(i) + " . ";
            }
        }

        return result;
    }

    public static String getResult(String input) {
        String result = "";
        int tinh = fromStringToExpression(input);
        if ( tinh == -10000) {
            result = count(input);
        } else {
            if( tinh <0) {
                tinh *= -1;
                result += "âm ";
            }
            result += Integer.toString(tinh);
        }
        return result;
    }
}
