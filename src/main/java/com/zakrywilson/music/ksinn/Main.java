package com.zakrywilson.music.ksinn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Runs the training of the neural network.
 *
 * @author Zach Wilson
 * @since 01/14/16
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * Main: runs program by training neural network over a specified iterations.
     *
     * @param args - command line arguments
     */
    public static void main(String[] args) {
        try {
            run(args);
        } catch (Exception e) {
            LOGGER.error("A fatal error has occurred:", e);
        }
    }

    /**
     * Runs the program after validating command line arguments.
     *
     * @param args - command line arguments
     */
    private static void run(final String[] args) {
        CLI cli = new CLI();
        cli.parseCommandLine(args);

        while (true) {
            NeuralNet nn = new NeuralNet(cli.getInputLayerSize(), cli.getHiddenLayerSize(), cli.getOutputLayerSize(), cli.getLearningRate());

            if (cli.requiresHelp()) {
                cli.printHelp();
                return;
            }

            if (cli.runVerbose()) {
                verboseRun(nn, cli.getMaxIterations());
                return;
            }

            normalRun(nn, cli.getMaxIterations(), cli.getResetRate());
        }
    }

    /**
     * Tests neural network with verbose output.
     *
     * @param net - neural network
     * @param maxIt - specified training iterations
     */
    private static void verboseRun(NeuralNet net, int maxIt) {
        for (int iterations = 0; iterations < maxIt; ++iterations) {
            Song song = new Song();
            NeuralNetOutput output = net.run(song);
            output.setCorrectAnswer(song.getKeyOfSong());
            displayVerbose(output);
        }
        System.out.println("Performing final test...");
        testNeuralNetwork(net);
    }

    /**
     * Test neural network with smaller subsets of the output.
     *
     * @param net - neural network
     * @param maxIt - specified training iterations
     * @param resetRate - rate at which the data is displayed
     */
    private static void normalRun(NeuralNet net, int maxIt, int resetRate) {
        // Train neural network for x iterations
        for (int iterations = 0; iterations < maxIt; ++iterations) {
            Song song = new Song();
            NeuralNetOutput output = net.run(song);
            output.setCorrectAnswer(song.getKeyOfSong());
            if ((iterations % resetRate) == 0) {
                displayPercentages(output);
                output.resetCounters();
            }
        }
        System.out.println("\nPerforming final test...");
        testNeuralNetwork(net);
        NeuralNetOutput.resetAll();
    }

    private static void testNeuralNetwork(NeuralNet net) {
        NeuralNetOutput output = null;
        for (int key = 0; key < 12; key++) {
            Song song = new Song(key);
            output = net.run(song);
            output.setCorrectAnswer(song.getKeyOfSong());
            displayResults(output);
        }
        if (output.getPercentCorrect() >= 90) {
            File file = new File(String.format("neural-network-%.2f-percent-correctness-%s.bin", output.getPercentCorrect(), getTimeStamp()));
            try {
                if (!file.createNewFile()) {
                    LOGGER.error("Failed to create file: {} -- neural network will not be saved");
                    return;
                }
            } catch (IOException e) {
                LOGGER.error("Failed to create file: {} -- neural network will not be saved", e);
                return;
            }
            try {
                writeNeuralNetToFile(net, file);
            } catch (Exception e) {
                LOGGER.error("Failed to write neural network out to file -- neural network will not be saved", e);
            }
        }
    }

    private static void writeNeuralNetToFile(NeuralNet net, File file) throws Exception {
        try (FileOutputStream out = new FileOutputStream(file)) {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(out)) {
                objectOutputStream.writeObject(net);
            } catch (FileNotFoundException e) {
                throw new Exception("File does not exist: " + file);
            }
        } catch (IOException e) {
            throw new Exception("Encountered I/O error while attempting to write to file: " + file);
        }
    }

    private static String getTimeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * Displays results: percent correct, how iteration number, etc.
     */
    private static void displayPercentages(final NeuralNetOutput output) {
        System.out.print("\r");
        System.out.printf("Iterations: %,d     Correct: %2.2f%%     Error: %8.5f%%",
                          output.getAccumulativeIterations(),
                          output.getPercentCorrect(),
                          output.getError());
    }

    /**
     * Displays results: key of the song, neural network's guess, and its error.
     *
     * @param output - output results from the neural net
     */
    private static void displayVerbose(final NeuralNetOutput output) {
        System.out.println("------------------------------------------------------------------------");
        System.out.printf("answer:\t%s%n", Conversions.numbersToLetters(output.getCorrectAnswer()));
        System.out.printf("\t|\tnet's guess:\t%s%n", Conversions.numbersToLetters(output.getGuess()));
        System.out.printf("\t|\terr:\t%8.5f  %s\n", output.getError(), (output.isCorrect()) ? "+" : " ");
    }

    /**
     * Displays results: key of the song, neural network's guess, and its error.
     *
     * @param output - output results from the neural net
     */
    private static void displayResults(final NeuralNetOutput output) {
        System.out.printf("answer: %s\tnet's guess: %s %s%n",
                          Conversions.numbersToLetters(output.getCorrectAnswer()),
                          Conversions.numbersToLetters(output.getGuess()),
                          (output.isCorrect()) ? "+" : " ");
    }

}