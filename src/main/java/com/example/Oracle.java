package com.example;

import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.api.query.Query;

import java.util.Collection;

public class Oracle implements MembershipOracle.DFAMembershipOracle<Character> {

    
    TestDriver tD;

    public Oracle() throws Exception {
        // Add your exception handling logic here
        tD= new TestDriver();
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