package com.example;

import java.util.ArrayList;
import java.util.List;

import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFA;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFABuilder;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.datastructure.observationtable.writer.ObservationTableASCIIWriter;
import de.learnlib.oracle.equivalence.DFAWMethodEQOracle;
import de.learnlib.util.Experiment;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.visualization.Visualization;

public class Main {

    public static List<Character> toCharacterArray(String s) {
        List<Character> out = new ArrayList<>();
        for (char c : s.toCharArray()) {
            out.add(c);
        }
        return out;
    }

    public static void main(String[] args) throws Exception {

        // Initialize membership oracle
        MembershipOracle.DFAMembershipOracle<Character> om = new Oracle();

        // Initialize equivalence oracle
        EquivalenceOracle.DFAEquivalenceOracle<Character> oe = new DFAWMethodEQOracle<>(om, 4);

        // Initialize L* algorithm
        ClassicLStarDFA<Character> lstar = new ClassicLStarDFABuilder<Character>()
                .withAlphabet(TestDriver.SIGMA)
                .withOracle(om)
                .create();

        // Create the experiment
        Experiment.DFAExperiment<Character> ex = new Experiment.DFAExperiment<>(lstar, oe, TestDriver.SIGMA);

        // Time profiling
        ex.setProfile(true);

        // Logging
        ex.setLogModels(true);

        // Run Experiment
        ex.run();

        // Results of the experiment
        DFA<?, Character> res = ex.getFinalHypothesis();

        System.out.println("***************************RESULTS***************************");

        System.out.println("\n\n Learning statistics: \n" + ex.getRounds().getSummary());

        new ObservationTableASCIIWriter<>().write(lstar.getObservationTable(), System.out);

        // Model statistics
        System.out.println("States: " + res.size() + "\n\nSigma: " + TestDriver.SIGMA.size());
        System.out.println("Model: ");

        GraphDOT.write(res, TestDriver.SIGMA, System.out);

        Visualization.visualize(res, TestDriver.SIGMA);

        System.out.println("****************************TESTS****************************");

        GraphDOT.write(res, TestDriver.SIGMA, System.out);

        System.out.println("FAILED TEST:");
        System.out.println(res.accepts(toCharacterArray("aa"))); // Expected: false
        System.out.println(res.accepts(toCharacterArray("abbb"))); // Expected: false
        System.out.println(res.accepts(toCharacterArray("baa"))); // Expected: false
        System.out.println(res.accepts(toCharacterArray(""))); // Expected: false
        System.out.println(res.accepts(toCharacterArray("abbbba"))); // Expected: false

        System.out.println("SUCCESS TEST:");
        System.out.println(res.accepts(toCharacterArray("b"))); // Expected: false
        System.out.println(res.accepts(toCharacterArray("ba"))); // Expected: true
        System.out.println(res.accepts(toCharacterArray("bab"))); // Expected: true
        System.out.println(res.accepts(toCharacterArray("baa"))); // Expected: true
        System.out.println(res.accepts(toCharacterArray("baab"))); // Expected: true
        System.out.println(res.accepts(toCharacterArray("baabb"))); // Expected: true
        System.out.println(res.accepts(toCharacterArray("baabba"))); // Expected: true
        System.out.println(res.accepts(toCharacterArray("bba"))); // Expected: false
        System.out.println(res.accepts(toCharacterArray("bb"))); // Expected: false

        System.out.println("*************************************************************");

        // Metrics calculation
        int truePositives = 0, trueNegatives = 0, falsePositives = 0, falseNegatives = 0;

        // Test data should be added here to calculate the metrics
        // For example:
        String[] testSequences = { "ba", "bab", "baa", "baab", "baabb", "baabba", "aa", "abbb", "baa", "", "abbbba" };
        boolean[] groundTruth = { true, true, true, true, true, true, false, false, false, false, false }; // Example
                                                                                                           // ground
                                                                                                           // truth

        for (int i = 0; i < testSequences.length; i++) {
            boolean prediction = res.accepts(toCharacterArray(testSequences[i]));
            if (prediction && groundTruth[i])
                truePositives++;
            else if (!prediction && !groundTruth[i])
                trueNegatives++;
            else if (prediction && !groundTruth[i])
                falsePositives++;
            else if (!prediction && groundTruth[i])
                falseNegatives++;
        }

        double accuracy = (truePositives + trueNegatives) / (double) testSequences.length;
        double precision = truePositives / (double) (truePositives + falsePositives);
        double recall = truePositives / (double) (truePositives + falseNegatives);

        System.out.println("Accuracy: " + accuracy);
        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall);

        System.out.println("States in the learned DFA: " + res.size());
    }
}
