package com.example;

import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.api.query.Query;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;


import java.util.Collection;

public class Oracle implements MembershipOracle.DFAMembershipOracle<Character> {

    
    private TestDriver tD;
    public MultilayerPerceptron neuralNetwork;
    public Instances trainingData;

    public Oracle() throws Exception {
        // 1º Calentar Pan
        // -> Se pueden calentar muchos panes seguidos (b b b b ...)
        // 2º Poner Mantequilla 
        // -> Para poner mantequilla debe haber al menos un pan (b -> a)
        // 3º Poner Jamón
        // -> Debe haber antes un pan untado con mantequilla (ba .. a), y se puede poner extra (ba ..aaa)
        // 3º Montar Sandwich
        // -> Para que se pueda hacer el sandwich, debe haberse calentado un pan, puesto la mantequilla, y (opcional) poner jamón, pan entre medias (ba b, ba aaaa b)
        tD = new TestDriver("SO_Research_Project/src/main/java/com/example/training_data.arff");
    }

    @Override
    public void processQueries(Collection<? extends Query<Character, Boolean>> queries) {

        for (Query<Character, Boolean> query : queries) {
    
            StringBuilder cadena = new StringBuilder();
            for (Character c : query.getInput()) {
                cadena.append(c);
            }
            query.answer(tD.executeSymbol(cadena.toString()));
        }
    }
}