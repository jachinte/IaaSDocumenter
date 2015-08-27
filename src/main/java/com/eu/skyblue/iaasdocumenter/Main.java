package com.eu.skyblue.iaasdocumenter;

import com.eu.skyblue.iaasdocumenter.ui.CommandLineInterface;

/**
 * Entry point for IaaSDocumenter.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("*** IaaSDocumenter! ***");
        CommandLineInterface cli = new CommandLineInterface();
        cli.parseArgs(args);
    }
}
