package com.eu.skyblue.iaasdocumenter.ui;

import com.amazonaws.regions.RegionUtils;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import com.eu.skyblue.iaasdocumenter.documenter.IaasDocumenter;
import com.eu.skyblue.iaasdocumenter.documenter.DocumenterFactory;
import com.eu.skyblue.iaasdocumenter.renderer.GraphRenderer;
import com.eu.skyblue.iaasdocumenter.renderer.RendererFactory;
import com.eu.skyblue.iaasdocumenter.utils.Logger;

import org.apache.commons.cli.*;

import org.graphstream.graph.Graph;

import java.io.File;
import java.util.List;

/**
 * Parses command line options and calls relevant functionality.
 */
public class CommandLineInterface {
    public static final String DEBUG = "debug";
    public static final String HELP = "help";
    public static final String LIST_REGIONS = "list-regions";
    public static final String DISPLAY_FORMAT = "display-format";
    public static final String AWS_REGION = "aws-region";
    public static final String OUTPUT_FOLDER = "output-folder";

    private Options options;
    private CommandLineParser commandLineParser;
    private static IaasDocumenter iaasDocumenter;
    private static String CURRENT_WORKING_PATH = ".";

    /**
     * Constructs a new <code>CommandLineInterface</code> object.
     */
    public CommandLineInterface() {
        this.commandLineParser = new DefaultParser();

        this.options = new Options();
        this.createOptions();
    }

    private void createOptions() {
        options.addOption("h", HELP, false, "Show help");

        options.addOption("l", LIST_REGIONS, false, "List AWS regions");

        options.addOption(Option.builder("f")
                .longOpt(DISPLAY_FORMAT)
                .required(false)
                .hasArg()
                .desc("Display format [XMI|SVG|PDF]")
                .build());

        options.addOption(Option.builder("r")
                .longOpt(AWS_REGION)
                .required(false)
                .hasArg()
                .desc("AWS region (use -l or --list-regions to get valid values for this option)")
                .build());

        options.addOption(Option.builder("o")
                .longOpt(OUTPUT_FOLDER)
                .required(false)
                .hasArg()
                .desc("Output folder")
                .build());

        options.addOption("d", DEBUG, false, "Display debug information");
    }

    /**
     * Parses the command line options and calls the appropriate functionality.
     *
     * @param args        Supplied command line options.
     */
    public void parseArgs(String[] args) {
        try {
            CommandLine line = commandLineParser.parse(options, args);

            // Check for debug flag
            Boolean debug = Boolean.FALSE;
            if (line.hasOption(DEBUG)) {
                debug = Boolean.TRUE;
            }
            Logger logger = new Logger(debug);

            if (line.hasOption(HELP)) {
                displayHelp();
            } else if (line.hasOption(LIST_REGIONS)) {
                listRegions();
            } else if (line.hasOption(DISPLAY_FORMAT) && line.hasOption(AWS_REGION)) {
                String outputFolder = "";
                if (line.hasOption(OUTPUT_FOLDER)) {
                    outputFolder = fixFilePath(line.getOptionValue(OUTPUT_FOLDER));
                }

                try {
                    Regions region = getRegion(line.getOptionValue(AWS_REGION));
                    renderGraphs(logger, line.getOptionValue(DISPLAY_FORMAT), outputFolder, region); //Regions.US_WEST_2
                } catch(IllegalArgumentException iae) {
                    logger.err("Invalid Argument Error: " + iae.getMessage());
                }
            } else {
                displayHelp();
            }
        } catch (ParseException p) {
            System.err.println("Unexpected exception:" + p.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private String fixFilePath(String filePath) {
        if (filePath.length() == 0) {
            return CURRENT_WORKING_PATH + File.separator;
        }

        if (filePath.charAt(filePath.length() - 1) != File.separatorChar) {
            return filePath + File.separator;
        } else {
            return filePath;
        }
    }

    private void displayHelp() {
        HelpFormatter formatter = new HelpFormatter();
        String header = "Create UML deployment diagrams for specified AWS region\n\n";
        String footer = "\n";
        formatter.printHelp("IaaSDocumenter.jar", header, options, footer, true);
    }

    private Regions getRegion(String regionName) throws IllegalArgumentException {
        return Regions.fromName(regionName);
    }

    private void listRegions() {
        System.out.println("AWS region list:");

        List<Region> regionList = RegionUtils.getRegions();
        for (Region region : regionList) {
            System.out.println(region.getName());
        }
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
