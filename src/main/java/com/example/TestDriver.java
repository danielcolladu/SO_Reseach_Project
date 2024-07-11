package com.example;

import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.GrowingMapAlphabet;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import java.util.ArrayList;

public class TestDriver {

    // input symbols
    private static final Character A = 'a';
    private static final Character B = 'b';

    // input alphabet used by learning algorithm
    public static final Alphabet<Character> SIGMA = new GrowingMapAlphabet<>();

    static {
        SIGMA.add(A);
        SIGMA.add(B);
    }

    private MultilayerPerceptron nn;
    private Instances dataStructure;

    public TestDriver(String arffFile) throws Exception {
        // Cargar los datos y entrenar el modelo
        DataSource source = new DataSource(arffFile);
        Instances data = source.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);

        nn = new MultilayerPerceptron();
        nn.buildClassifier(data);

        // Crear estructura de datos para predicción de instancia única
        ArrayList<Attribute> attributes = new ArrayList<>(data.numAttributes());
        for (int i = 0; i < data.numAttributes() - 1; i++) {
            ArrayList<String> values = new ArrayList<>(3);
            values.add("a");
            values.add("b");
            values.add("_");
            attributes.add(new Attribute("pos" + (i + 1), values));
        }
        ArrayList<String> classValues = new ArrayList<>(2);
        classValues.add("true");
        classValues.add("false");
        attributes.add(new Attribute("class", classValues));

        dataStructure = new Instances("TestInstances", attributes, 0);
        dataStructure.setClassIndex(dataStructure.numAttributes() - 1);
    }

    public boolean executeSymbol(String cadena) {
        try {
            int maxLength = dataStructure.numAttributes() - 1; // -1 porque la última es la clase
            DenseInstance instance = new DenseInstance(maxLength + 1); // +1 para la clase
            for (int i = 0; i < maxLength; i++) {
                if (i < cadena.length()) {
                    instance.setValue(dataStructure.attribute("pos" + (i + 1)), String.valueOf(cadena.charAt(i)));
                } else {
                    instance.setValue(dataStructure.attribute("pos" + (i + 1)), "_");
                }
            }
            instance.setDataset(dataStructure);

            double prediction = nn.classifyInstance(instance);
            return prediction == dataStructure.classAttribute().indexOfValue("true");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
