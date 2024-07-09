package com.example;

import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.GrowingMapAlphabet;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class TestDriver {
    

    private boolean breadHeated = false;
    private boolean butterSpread = false;
    private boolean jamAdded = false;
    private boolean served = false;

    
    // input symbols
    private static final Character A = 'a';
    private static final Character B = 'b';

    // input alphabet used by learning algorithm
    public static final Alphabet<Character> SIGMA = new GrowingMapAlphabet<>();

    static {
        SIGMA.add(A);
        SIGMA.add(B);
    }

    public TestDriver() throws Exception {
        
    }

    public boolean executeSymbol(Character s) throws Exception {
        Oracle or = new Oracle();
        // Create an instance for the current state
        double[] vals = new double[or.trainingData.numAttributes()];
        vals[0] = breadHeated ? 1.0 : 0.0;
        vals[1] = butterSpread ? 1.0 : 0.0;
        vals[2] = jamAdded ? 1.0 : 0.0;
        vals[3] = served ? 1.0 : 0.0;

        Instance instance = new DenseInstance(1.0, vals);
        instance.setDataset(or.trainingData);

        // Predict the next step using the neural network
        double[] prediction = or.neuralNetwork.distributionForInstance(instance);
        int predictedClass = (int) Math.round(prediction[0]); // Assuming binary classification

        // Convert predicted class to corresponding next step (e.g., "A" or "B")
        String nextStep = or.trainingData.classAttribute().value(predictedClass);

        // Compare predicted next step with the provided symbol and update state if it matches
        if (nextStep.equals(s.toString())) {
            if (!breadHeated && s.equals(B)) {
                breadHeated = true;
            } else if (breadHeated && !butterSpread && s.equals(A)) {
                butterSpread = true;
            } else if (breadHeated && butterSpread && !jamAdded && s.equals(A)) {
                jamAdded = true;
            } else if (breadHeated && butterSpread && jamAdded && !served && s.equals(B)) {
                served = true;
            } else {
                return false; // Invalid state transition
            }
            return true; // Valid state transition
        } else {
            return false; // Prediction does not match the provided symbol
        }
    }

    public void reset() {
        breadHeated = butterSpread = jamAdded = served = false;
    }
}