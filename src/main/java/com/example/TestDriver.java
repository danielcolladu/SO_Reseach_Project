package com.example;

import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.GrowingMapAlphabet;


public class TestDriver {

    // Símbolos de entrada
    private static final Character A = 'a';
    private static final Character B = 'b'; 

    private boolean breadHeated = false;    // Indica si el pan ha sido calentado.
    private boolean butterSpread = false;   // Indica si la mantequilla ha sido untada
    private boolean jamAdded = false;       // Indica si la mermelada ha sido agregada
    private boolean served = false;         // Indica si el desayuno ha sido servido.

    public static final Alphabet<Character> SIGMA = new GrowingMapAlphabet<>();

    static {
        SIGMA.add(A);
        SIGMA.add(B);
    }

    /*
        Calentar Pan (B): El primer paso es calentar una rebanada de pan.
        Untar Mantequilla (A): Después de calentar el pan, se unta mantequilla sobre él.
        Agregar Mermelada (A): Luego de untar la mantequilla, se agrega mermelada.
        Servir en Plato (B): Finalmente, el desayuno está listo para ser servido en un plato.
    */
    public boolean executeSymbol(Character s) {
        if (!breadHeated) {
            if (s.equals(B)) {
                breadHeated = true; // Pan calentado
            }
            return true;
        } else if (breadHeated && !butterSpread) {
            if (s.equals(A)) {
                butterSpread = true; // Mantequilla untada
            }
            return butterSpread;
        } else if (breadHeated && butterSpread && !jamAdded) {
            if (s.equals(A)) {
                jamAdded = true; // Mermelada agregada
            }
            return jamAdded;
        } else if (breadHeated && butterSpread && jamAdded && !served) {
            if (s.equals(B)) {
                served = true; // Desayuno servido
            }
            return served && s.equals(B);
        }
        return false;
    }

    // Método para resetear el estado
    public void reset() {
        breadHeated = butterSpread = jamAdded = served = false;
    }
}

