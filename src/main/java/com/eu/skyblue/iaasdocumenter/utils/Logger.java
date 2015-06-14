package com.eu.skyblue.iaasdocumenter.utils;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 13/06/15
 * Time: 10:57
 * To change this template use File | Settings | File Templates.
 */
public class Logger {
    private Boolean debug;

    public Logger(Boolean debug) {
        this.debug = debug;
    }

    public void out(String format, Object... args) {
        if (this.debug) {
            System.out.printf(format, args);
            if (!format.endsWith("%n")) {
                System.out.println();
            }
        }
    }

    public void err(String format, Object... args) {
        System.err.printf(format, args);
        if (!format.endsWith("%n")) {
            System.err.println();
        }
    }
}
