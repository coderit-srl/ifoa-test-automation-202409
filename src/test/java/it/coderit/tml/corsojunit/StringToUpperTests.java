package it.coderit.tml.corsojunit;

import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StringToUpperTests {


    @Test
    void testConStringaVuota() {

        // Given
        String s = "Straße";

        // When
        String risultato = s.toLowerCase();

        // Then
        Assertions.assertEquals("straße", risultato);
    }

}
