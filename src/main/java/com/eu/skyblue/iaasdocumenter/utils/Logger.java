package com.eu.skyblue.iaasdocumenter.utils;

/**
 * Displays information on program activity.
 */
public class Logger {

    /** Debug flag */
    private Boolean debug;

    /**
     * Construct a new <code>Logger</code> object.
     *
     * @param debug Debug flag
     */
    public Logger(Boolean debug) {
        this.debug = debug;
    }

    /**
     * Displays the specified debug output.
     *
     * @param format  Format string
     * @param args    The string to be displayed.
     */
    public void out(String format, Object... args) {
        if (this.debug) {
            System.out.printf(format, args);
            if (!format.endsWith("%n")) {
                System.out.println();
            }
        }
    }

    /**
     * Displays the specified error output.
     *
     * @param format  Format string
     * @param args    The string to be displayed.
     */
    public void err(String format, Object... args) {
        System.err.printf(format, args);
        if (!format.endsWith("%n")) {
            System.err.println();
        }
    }
}
