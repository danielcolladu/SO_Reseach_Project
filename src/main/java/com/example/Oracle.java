package com.example;

import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.api.query.Query;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.util.Collection;

public class Oracle implements MembershipOracle.DFAMembershipOracle<Character> {

    
    private TestDriver tD = new TestDriver();
    public MultilayerPerceptron neuralNetwork;
    public Instances trainingData;

    public Oracle() throws Exception {
        // Add your exception handling logic here
        // Load training data
        DataSource source = new DataSource("C:\\Users\\danel\\Documents\\GitHub\\SO_Reseach_Project_1\\src\\main\\java\\com\\example\\training_data.csv");
        trainingData = source.getDataSet();

         // Set the class index (the last attribute)
        if (trainingData.classIndex() == -1)
            trainingData.setClassIndex(trainingData.numAttributes() - 1);

        // Train the neural network
        neuralNetwork = new MultilayerPerceptron();
        neuralNetwork.buildClassifier(trainingData);

        // Optionally, evaluate the model (for validation purposes)
        Evaluation eval = new Evaluation(trainingData);
        eval.evaluateModel(neuralNetwork, trainingData);
        System.out.println(eval.toSummaryString());
    }

    @Override
    public void processQueries(Collection<? extends Query<Character, Boolean>> queries) {

        for(Query<Character, Boolean> query : queries)
        {
            tD.reset();
            boolean out = true;
            for (Character input : query.getInput()) {
                try {
                    out = tD.executeSymbol(input);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            query.answer(out);
        }
    }
}