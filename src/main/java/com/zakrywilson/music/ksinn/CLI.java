package com.zakrywilson.music.ksinn;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * {@code CLI} (Command Line Interface) for parsing command line input.
 *
 * @author Zach Wilson
 */
class CLI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CLI.class);

    private static final int SHORT_NAME = 0;
    private static final int LONG_NAME = 1;
    private static final int DESCRIPTION = 2;
    private static final int ARG_NAME = 3;

    private static final int WIDTH = 200;
    private static final String APPLICATION_NAME = "Key Signature Identifying Neural Network by Zach Wilson";

    private static final String[] help = {"h", "help", "Print instructions about how to run application"};
    private static final String[] verbose = {"v", "verbose", "Run with verbose output"}; // TODO Do we want to use a logging level instead of "verbose"?
    private static final String[] resetRate = {"r", "reset-rate", "Reset rate of the neural net", "rate"};
    private static final String[] maxIterations = {"m", "max-iterations", "Maximum number of iterations for training the neural net", "max"};
    private static final String[] layers = {"n", "neural-net-layers", "Number of the neural network's input, hidden, and output nodes, respectfully", "size"};
    private static final String[] learningRate = {"l", "learning-rate", "Rate at which the neural network will learn", "rate"};

    private static final Options options = new Options();
    private static final HelpFormatter helpFormatter = new HelpFormatter();

    private boolean helpValue = false;
    private boolean verboseValue = false;
    private int resetRateValue = 10_000;
    private int maxIterationsValue = 10_000_000;
    private short[] layersValues = {12, 12, 12};
    private double learningRateValue = 0.18;

    CLI() {
        createOptions();
    }

    void parseCommandLine(String... args) {
        LOGGER.trace("Receive command line args: {}", Arrays.toString(args));

        //
        // Parse the command line args
        //
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, args);
            LOGGER.trace("Successfully parsed command line args");
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse command line arguments: " + Arrays.toString(args), e);
        }

        //
        // Extract args into member variables for retrieval
        //

        // Help option
        if (commandLine.hasOption(help[SHORT_NAME])) {
            helpValue = true;
            LOGGER.debug("Received 'help' option");
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Received no 'help' option");
        }

        // Verbose option
        if (commandLine.hasOption(verbose[SHORT_NAME])) {
            verboseValue = true;
            LOGGER.debug("Received 'verbose' option");
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Received no 'verbose' option");
        }

        // Reset rate option
        if (commandLine.hasOption(resetRate[SHORT_NAME])) {
            resetRateValue = Integer.parseInt(commandLine.getOptionValue(resetRate[SHORT_NAME]));
            LOGGER.debug("Received 'reset rate' option of {}", resetRateValue);
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Received no 'reset rate' option -- defaulting to {}", resetRateValue);
        }

        // Max iterations option
        if (commandLine.hasOption(maxIterations[SHORT_NAME])) {
            maxIterationsValue = Integer.parseInt(commandLine.getOptionValue(maxIterations[SHORT_NAME]));
            LOGGER.debug("Received 'max iterations' option of {}", maxIterationsValue);
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Received no 'max iterations' option -- defaulting to {}", maxIterationsValue);
        }

        // Neural net layers option
        if (commandLine.hasOption(layers[SHORT_NAME])) { // parse each neural net option and store it
            String[] layersOption = commandLine.getOptionValues(layers[SHORT_NAME]);
            for (int i = 0; i < layersOption.length; i++) {
                layersValues[i] = Short.parseShort(layersOption[i]);
            }
            LOGGER.debug("Received 'layers' option of {}", Arrays.toString(layersValues));
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Received no 'layers' option -- defaulting to {}", Arrays.toString(layersValues));
        }

        // Learning rate option
        if (commandLine.hasOption(learningRate[SHORT_NAME])) {
            learningRateValue = Double.parseDouble(learningRate[SHORT_NAME]);
            LOGGER.debug("Received 'learning rate' option of {}", learningRateValue);
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Received no 'learning rate' option -- defaulting to {}", learningRateValue);
        }
    }

    void printHelp() {
        System.out.printf("%n%s%n%n", APPLICATION_NAME);
        helpFormatter.printHelp(WIDTH, "\njava -jar [file]", "\nArguments:", options, "", true);
    }

    boolean requiresHelp() {
        return helpValue;
    }

    boolean runVerbose() {
        return verboseValue;
    }

    int getResetRate() {
        return resetRateValue;
    }

    int getMaxIterations() {
        return maxIterationsValue;
    }

    short[] getLayerSizes() {
        return layersValues;
    }

    short getInputLayerSize() {
        return layersValues[0];
    }

    short getHiddenLayerSize() {
        return layersValues[1];
    }

    short getOutputLayerSize() {
        return layersValues[2];
    }

    double getLearningRate() {
        return learningRateValue;
    }

    private void createOptions() {
        Option helpOption = new Option(help[SHORT_NAME], help[LONG_NAME], false, help[DESCRIPTION]);
        helpOption.setRequired(false);
        options.addOption(helpOption);

        Option verboseOption = new Option(verbose[SHORT_NAME], verbose[LONG_NAME], false, verbose[DESCRIPTION]);
        verboseOption.setRequired(false);
        options.addOption(verboseOption);

        Option resetRateOption = new Option(String.valueOf(resetRate[SHORT_NAME]), resetRate[LONG_NAME], true, resetRate[DESCRIPTION]);
        resetRateOption.setRequired(false);
        resetRateOption.setArgName(resetRate[ARG_NAME]);
        options.addOption(resetRateOption);

        Option maxIterationsOption = new Option(String.valueOf(maxIterations[SHORT_NAME]), maxIterations[LONG_NAME], true, maxIterations[DESCRIPTION]);
        maxIterationsOption.setRequired(false);
        maxIterationsOption.setArgName(maxIterations[ARG_NAME]);
        options.addOption(maxIterationsOption);

        Option layersOption = new Option(String.valueOf(layers[SHORT_NAME]), layers[LONG_NAME], true, layers[DESCRIPTION]);
        layersOption.setRequired(false);
        layersOption.setArgName(layers[ARG_NAME]);
        layersOption.setArgs(3);
        options.addOption(layersOption);

        Option learningRateOption = new Option(learningRate[SHORT_NAME], learningRate[LONG_NAME], true, learningRate[DESCRIPTION]);
        learningRateOption.setRequired(false);
        learningRateOption.setArgName(learningRate[ARG_NAME]);
        options.addOption(learningRateOption);
    }

}
