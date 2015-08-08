package com.eu.skyblue.iaasdocumenter;

import com.eu.skyblue.iaasdocumenter.ui.CommandLineInterface;


/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 13/06/15
 * Time: 09:59
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("*** IaaSDocumenter! ***");
        CommandLineInterface cli = new CommandLineInterface();
        cli.parseArgs(args);
    }
}
