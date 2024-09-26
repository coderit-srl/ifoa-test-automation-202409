package it.coderit.tml.corsojunit;

import org.junit.jupiter.api.*;

class CorsoJunitApplicationTests {

    int i;
    int j;
    boolean tuttoBene;

    @BeforeAll
    static void beforeAll() {
        String s = "TEST";
        System.out.println("CIAO DA BEFORE ALL");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("CIAO DA AFTER ALL");
    }

    @BeforeEach
    void setUp() {
        System.out.println("CIAO DA SETUP");
        i = 2;
        j = 4;
        tuttoBene = false;
    }

    @AfterEach
    void tearDown() {
        System.out.println("CIAO DA TEARDOWN. E' fallito qualcosa? " + tuttoBene);
    }


    @Test
    void testaSomma() {
        int risultato = 7;
        float a = 0.3f;
        Assertions.assertEquals(0.6f, a + a, 0.00001);
        Assertions.assertEquals(7, risultato);
        tuttoBene = true;
    }

    @Test
    void testaProdotto() {
        int prodotto = i * j;
        Assertions.assertEquals(8, prodotto);
        tuttoBene = true;
    }


}
