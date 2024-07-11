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
        // -> Para calentar el pan: Se utiliza una arista B para pasar al siguiente estado (Poner mantequilla), si se quiere calentar otro pan, utilizar la arista A 
        // 2º Poner Mantequilla 
        // -> Para poner mantequilla debe haber al menos un pan. Usar la arista A para pasar al siguiente estado (Poner Jamón). Si se desea un sandwich sin jamón usar la arista B
        // 3º Poner Jamón
        // -> Debe haber antes un pan untado con mantequilla. Se utiliza la arista B para pasar al siguiente estado (Montar Sandwich), o se utiliza la arista A para poner más jamón
        // 4º Montar Sandwich
        // -> Para que se pueda hacer el sandwich (Es decir, para que la cadena esté aceptada),
        // debe haberse calentado un pan (B), puesto la mantequilla (A), y poner o no Jamón o extra de Jamón 
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