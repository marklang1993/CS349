package tetris;

import org.junit.Test;

import static org.junit.Assert.*;


public class ProgramArgsTest {
    @Test
    public void testParseArgsDefaults() throws Exception {
        ProgramArgs a = ProgramArgs.parseArgs(new String[]{});
        assertEquals(30, a.getFPS());
        assertEquals(7.0, a.getSpeed(), 0.0001);
        assertEquals(150, a.getSequence().length());
    }

    @Test
    public void testParseArgsParse() throws Exception {
        ProgramArgs a = ProgramArgs.parseArgs(new String[]{"-speed", "9.5", "-seq", "IJS", "-fps", "40"});
        assertEquals(40, a.getFPS());
        assertEquals(9.5, a.getSpeed(), 0.0001);
        assertEquals("IJS", a.getSequence());
    }

    @Test
    public void testParseArgsUnknown() {
        try {
            ProgramArgs a = ProgramArgs.parseArgs(new String[]{"-speed", "nine", "-seq", "IJA"});
            fail("ProgramArgs failed to catch a bad exception.");
        } catch(IllegalArgumentException e) {
            assertEquals("Parse error: For input string: \"nine\"\n" +
                    "Usage: TetrisMain [-fps n] [-speed -s.s] [-seq <ILJSZOT...>\n", e.getMessage());
        }

        try {
            ProgramArgs a = ProgramArgs.parseArgs(new String[]{"-speed", "9.0", "-seq", "IJA"});
            fail("ProgramArgs failed to catch a bad exception.");
        } catch(IllegalArgumentException e) {
            assertEquals("Invalid shape: A\n" +
                    "Usage: TetrisMain [-fps n] [-speed -s.s] [-seq <ILJSZOT...>\n", e.getMessage());
        }
    }
}