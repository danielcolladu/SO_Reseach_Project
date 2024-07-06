package com.example;

import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.GrowingMapAlphabet;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class TestDriver {
    private J48 tree;
    private Instances trainingData;

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
        // Load training data


        DataSource source = new DataSource("/path/to/training_data.csv");
        trainingData = source.getDataSet();

        // Set the class index (the last attribute)
        if (trainingData.classIndex() == -1)
            trainingData.setClassIndex(trainingData.numAttributes() - 1);

        // Train the model
        tree = new J48();
        tree.buildClassifier(trainingData);
    }

    public boolean executeSymbol(Character s) throws Exception {
        // Create an instance for the current state
        double[] vals = new double[trainingData.numAttributes()];
        vals[0] = breadHeated ? 1.0 : 0.0;
        vals[1] = butterSpread ? 1.0 : 0.0;
        vals[2] = jamAdded ? 1.0 : 0.0;
        vals[3] = served ? 1.0 : 0.0;

        Instance instance = new DenseInstance(1.0, vals);
        instance.setDataset(trainingData);

        // Predict the next step
        double predictedClass = tree.classifyInstance(instance);
        String nextStep = trainingData.classAttribute().value((int) predictedClass);

        // Update the state based on the symbol
        if (!breadHeated && s.equals('B') && nextStep.equals("B")) {
            breadHeated = true;
        } else if (breadHeated && !butterSpread && s.equals('A') && nextStep.equals("A")) {
            butterSpread = true;
        } else if (breadHeated && butterSpread && !jamAdded && s.equals('A') && nextStep.equals("A")) {
            jamAdded = true;
        } else if (breadHeated && butterSpread && jamAdded && !served && s.equals('B') && nextStep.equals("B")) {
            served = true;
        } else {
            return false; // Invalid symbol for the current state
        }
        return true;
    }

    public void reset() {
        breadHeated = butterSpread = jamAdded = served = false;
    }
}
