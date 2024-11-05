package it.coderit.tml.corsojunit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PrimoTest {

    String daTestare;

    @BeforeEach
    public void setUp() {
        System.out.println("eseguo setUp");
        daTestare = "CIAO";
    }

    @AfterEach
    public void cleanUp() {
        System.out.println("eseguo cleanUp");
    }

    @Test
    /* Questo test fa XYZ */
    public void verficaLunghezzaStringa() {
        // Setup (Given)
        String daTestare2 = "CIAO";
        System.out.println("eseguo verficaLunghezzaStringa");

        // Azioni (When)
        int lunghezza = daTestare.length();
        

        // Verificano (Then)
        Assertions.assertEquals(4, lunghezza);
    }

    @Test
    public void verificaCheIniziaPerC() {
        daTestare = "ZZZZZZ";
        System.out.println("eseguo verificaCheIniziaPerC");
        char inizo = daTestare.charAt(0);
        Assertions.assertEquals('C', inizo);
    }

}
