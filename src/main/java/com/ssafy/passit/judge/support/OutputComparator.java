package com.ssafy.passit.judge.support;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OutputComparator {

    public boolean isMatch(String expected, String actual) {
        return normalize(expected).equals(normalize(actual));
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }

        String normalized = value.replace("\r\n", "\n").replace('\r', '\n');

        return Arrays.stream(normalized.split("\n", -1))
            .map(line -> line.replaceAll("\\s+$", ""))
            .collect(Collectors.joining("\n"))
            .replaceAll("\n+$", "");
    }
}
