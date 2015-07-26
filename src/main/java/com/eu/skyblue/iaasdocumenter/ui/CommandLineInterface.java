package com.eu.skyblue.iaasdocumenter.ui;

import com.amazonaws.regions.Regions;
import com.eu.skyblue.iaasdocumenter.documenter.IaasDocumenter;
import com.eu.skyblue.iaasdocumenter.documenter.aws.DocumenterFactory;
import com.eu.skyblue.iaasdocumenter.renderer.GraphRenderer;
import com.eu.skyblue.iaasdocumenter.renderer.RendererFactory;
import com.eu.skyblue.iaasdocumenter.utils.Logger;
import org.apache.commons.cli.*;
import org.graphstream.graph.Graph;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 26/07/15
 * Time: 01:20
 * To change this template use File | Settings | File Templates.
 */
public class CommandLineInterface {
    private Options options;
    private CommandLineParser commandLineParser;
    private static IaasDocumenter iaasDocumenter;

    public CommandLineInterface() {
        this.commandLineParser = new DefaultParser();

        this.options = new Options();
        this.createOptions();
    }

    private void createOptions() {
        options.addOption("h", "help", false, "Show help");

        options.addOption("l", "list-regions", false, "List AWS regions");

        options.addOption(Option.builder("f")
                .longOpt("display-format")
                .required(false)
                .hasArg()
                .desc("Display format [XMI|SVG|PDF|RAW_GRAPH]")
                .build());

        options.addOption(Option.builder("r")
                .longOpt("aws-region")
                .required(false)
                .hasArg()
                .desc("AWS region (use -l or --list-regions to get valid values for this option)")
                .build());

        options.addOption(Option.builder("o")
                .longOpt("output-folder")
                .required(false)
                .hasArg()
                .desc("Output folder")
                .build());

        options.addOption("d", "debug", false, "Display debug information");
    }

    public void parseArgs(String[] args) {
        try {
            CommandLine line = commandLineParser.parse(options, args);

            // Check for debug flag
            Boolean debug = Boolean.FALSE;
            if (line.hasOption("debug")) {
                debug = Boolean.TRUE;
            }
            Logger logger = new Logger(debug);

            if (line.hasOption("help")) {
                displayHelp();
            } else if (line.hasOption("list-regions")) {
                listRegions();
            } else if (line.hasOption("display-format") && line.hasOption("aws-region")) {
                String outputFolder = "./";
                if (line.hasOption("output-folder")) {
                    outputFolder = line.getOptionValue("output-folder");
                }
                renderGraphs(logger, line.getOptionValue("display-format"), outputFolder, Regions.US_WEST_2);
            } else {
                displayHelp();
            }
        } catch (ParseException p) {
            System.err.println("Unexpected exception:" + p.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void displayHelp() {
        HelpFormatter formatter = new HelpFormatter();
        String header = "Create UML deployment diagrams for specified AWS region\n\n";
        String footer = "\n";
        formatter.printHelp("IaaSDocumenter.jar", header, options, footer, true);
    }

    private void listRegions() {
        System.out.println("AWS region list");
    }

    private void renderGraphs(Logger logger, String displayFormat, String outputFolder, Regions region) throws Exception {
        iaasDocumenter = DocumenterFactory.createIaasDocumenter(region);
        iaasDocumenter.createInventory();

        for (Graph graph : iaasDocumenter.getGraphs()) {
            GraphRenderer renderer = RendererFactory.createRenderer(displayFormat, logger);
            renderer.render(graph, outputFolder + graph.getId());
        }
    }
}
