package com.example.ajax.myapplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Util {

    private Matcher matcher;

    Boolean match(String input, String pattern) {
        init(input, pattern);
        return matcher.matches();
    }

    private void init(String input, String pattern) {
        Pattern compile = Pattern.compile(pattern);
        matcher = compile.matcher(input);
    }

    int matchCount(String input, String pattern) {
        init(input, pattern);
        int counter = 0;
        while (matcher.find()) counter++;
        return counter;
    }
}
