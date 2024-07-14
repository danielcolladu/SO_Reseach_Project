package com.example;

import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.api.query.Query;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

import java.util.Collection;

public class Oracle implements MembershipOracle.DFAMembershipOracle<Character> {

    private TestDriver tD;

    public Oracle() throws Exception {
        tD = new TestDriver("src\\main\\java\\com\\example\\training_data.arff");
    }

    @Override
    public void processQueries(Collection<? extends Query<Character, Boolean>> queries) {
        for (Query<Character, Boolean> query : queries) {
            StringBuilder cadena = new StringBuilder();
            for (Character c : query.getInput()) {
                cadena.append(c);
            }
            boolean result = tD.executeSymbol(cadena.toString());
            query.answer(result);
        }
    }
}
