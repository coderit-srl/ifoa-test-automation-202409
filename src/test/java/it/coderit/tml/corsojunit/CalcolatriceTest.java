package it.coderit.tml.corsojunit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalcolatriceTest {

    public int esegui(String operazione) {
        String[] tokens = operazione.split(" ");
        int a = Integer.parseInt(tokens[0]);
        int b = Integer.parseInt(tokens[2]);
        String operatore = tokens[1].trim();
        if (operatore.equals("+")) {
            return a + b;
        } else if (operatore.equals("-")) {
            return a - b;
        } else if (operatore.equals("*")) {
            return a * b;
        } else if (operatore.equals("/")) {
            return a / b;
        } else {
            throw new IllegalArgumentException("Operatore sconosciuto " + operatore);
        }
    }

    @Test
    public void laFunzioneEseguiNonFunzionaSeUsoUnOperatoreNonStandard() {
        try {
            esegui("3 ^ 5");
            // Qui non ci devo passare
            Assertions.fail("Mi aspettavo una IllegalArgumentException e invece non c'è stata");
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            Assertions.assertNotNull(message);
        }
    }


    @Test
    public void laFunzioneEseguiNonFunzionaSeUsoUnOperatoreNonStandardV2() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            esegui("3 ^ 5");
        });
    }

    @Test
    public void testInutile() {
        int risultato = 3 + 3;
    }

}
