package com.vinfast.rental_service.enums;

public enum DateFormatPattern {
    DAILY("%Y-%m-%d"),
    MONTHLY("%Y-%m"),
    YEARLY("%Y"),
    AUTO("%Y-%m-%d %H:%i:%S");

    private final String pattern;

    DateFormatPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
