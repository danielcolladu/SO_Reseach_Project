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



public class Main 
{

    public static List<Character> toCharacterArray(String s)
    {
        List<Character> out = new ArrayList<>();
        for(char c : s.toCharArray())
        {
            out.add(c);
        }
        return out;
    }
    public static void main( String[] args ) throws Exception
    {
        
        //Se crea e inicializa el oraculo de membresia 'om'

        MembershipOracle.DFAMembershipOracle<Character> om = new Oracle();
        //  Por parametros: alfabeto 'a', 'lstar'

        //Se crea e inicializa el oraculo de equivalencia 'oe':

        EquivalenceOracle.DFAEquivalenceOracle<Character> oe = new DFAWMethodEQOracle<>( om,4);
        //  Por parametros: 'om' y la profundidad de busqueda

        //Se crea e inicializa el L* 'lstar'(se puede crear antes del oraculo de equivalencia)

         ClassicLStarDFA<Character> lstar = new ClassicLStarDFABuilder<Character>()
                .withAlphabet(TestDriver.SIGMA)
                .withOracle(om)
                .create();
        //Cretae the experiment with the L* and the equivalence oracle
        Experiment.DFAExperiment<Character> ex = new Experiment.DFAExperiment<>( lstar,oe,TestDriver.SIGMA);
    
        // Time proffilling
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

        // model statistics
        System.out.println("States: " + res.size() + "\n\nSigma: " + TestDriver.SIGMA.size());
        System.out.println("Model: ");


        GraphDOT.write(res, TestDriver.SIGMA, System.out); 

        //Visualization.visualize(res, TestDriver.SIGMA);


        System.out.println("****************************TESTS****************************");

        GraphDOT.write(res, TestDriver.SIGMA, System.out);
            
        System.out.println("FAILED TEST:");
        System.out.println(res.accepts(toCharacterArray("aa")));
        System.out.println(res.accepts(toCharacterArray("abbb")));
        System.out.println(res.accepts(toCharacterArray("baa")));
        System.out.println(res.accepts(toCharacterArray("")));
        System.out.println(res.accepts(toCharacterArray("abbbba")));

        System.out.println("SUCCESS TEST:");
        System.out.println(res.accepts(toCharacterArray("abba")));

            
        System.out.println("*************************************************************");
        

    }
}
