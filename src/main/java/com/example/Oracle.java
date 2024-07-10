package com.example;

import de.learnlib.api.oracle.MembershipOracle;
import java.util.ArrayList;
import de.learnlib.api.query.Query;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.converters.ConverterUtils.DataSource;

import java.util.Collection;

public class Oracle implements MembershipOracle.DFAMembershipOracle<Character> {

    
    private TestDriver tD = new TestDriver();
    public MultilayerPerceptron neuralNetwork;
    public Instances trainingData;

    public Oracle() throws Exception {
        // Add your exception handling logic here
        // Load training data

        // 1º Calentar Pan
        // -> Se pueden calentar muchos panes seguidos (b b b b ...)
        // 2º Poner Mantequilla 
        // -> Para poner mantequilla debe haber al menos un pan (b -> a)
        // 3º Poner Jamón
        // -> Debe haber antes un pan untado con mantequilla (ba .. a), y se puede poner extra (ba ..aaa)
        // 3º Montar Sandwich
        // -> Para que se pueda hacer el sandwich, debe haberse calentado un pan, puesto la mantequilla, y (opcional) poner jamón, pan entre medias (ba b, ba aaaa b)


        DataSource source = new DataSource("C:\\Users\\danel\\Documents\\GitHub\\SO_Reseach_Project_1\\src\\main\\java\\com\\example\\training_data.csv");
        trainingData = source.getDataSet();

         // Set the class index (the last attribute)
        if (trainingData.classIndex() == -1)
            trainingData.setClassIndex(trainingData.numAttributes() - 1);

        // Train the neural network
        neuralNetwork = new MultilayerPerceptron();
        neuralNetwork.buildClassifier(trainingData);



        // Optionally, evaluate the model (for validation purposes)
        //Evaluation eval = new Evaluation(trainingData);
        //eval.evaluateModel(neuralNetwork, trainingData);
        //System.out.println(eval.toSummaryString());
    }

    public boolean predict(String inputString) throws Exception {
        // Crear una instancia para la predicción
        Instance instance = new DenseInstance(1.0, new double[]{});
        
        // Crear un ArrayList para almacenar los atributos
        ArrayList<String> attributeValues = new ArrayList<>();
        attributeValues.add(inputString); // Añadir el valor de la cadena de entrada
        
        // Crear un conjunto de datos temporal
        Instances dataset = new Instances("Test-dataset", new Attribute(attributeValues), 0);
        dataset.setClassIndex(dataset.numAttributes() - 1);

        // Establecer la instancia con el valor de la cadena de entrada
        instance.setDataset(dataset);
        instance.setValue(dataset.attribute("input"), inputString);
        
        // Obtener la predicción
        double[] prediction = neuralNetwork.distributionForInstance(instance);
        
        // Interpretar la salida como booleana (asumiendo un problema binario)
        return prediction[0] > 0.5; 
    }

    @Override
    public void processQueries(Collection<? extends Query<Character, Boolean>> queries) {

        for(Query<Character, Boolean> query : queries)
        {

            boolean out = true;


            // for (Character input : query.getInput()) {
            //     try {
            //         out = tD.executeSymbol(input);
            //     } catch (Exception e) {
            //         e.printStackTrace();
            //     }
            // }

            query.answer(out);
        }
    }
}