package tetris;

import java.util.Random;

/**
 * Parse the program's arguments.
 */
class ProgramArgs {
    private static String shapes = "ILJSZOT";

    // Initialize with defaults
    private int fps = 30;
    private double speed = 7.0;
    private String sequence = randomSequence(150);

    public int getFPS() {
        return this.fps;
    }

    public double getSpeed() {
        return this.speed;
    }

    public String getSequence() {
        return this.sequence;
    }

    static String randomSequence(int length) {
        Random rand = new Random();
        StringBuffer seq = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            int r = rand.nextInt(shapes.length());
            seq.append(shapes.charAt(r));
        }
        return seq.toString();
    }

    static ProgramArgs parseArgs(String[] args) {
        boolean valid = true;
        ProgramArgs a = new ProgramArgs();    // defaults
        StringBuffer errors = new StringBuffer();
        String usage = "Usage: TetrisMain [-fps n] [-speed -s.s] [-seq <ILJSZOT...>\n";

        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i] == "-fps") {
                    a.fps = Integer.parseInt(args[i + 1]);
                    i++;
                } else if (args[i] == "-speed") {
                    a.speed = Double.parseDouble(args[i + 1]);
                    i++;
                } else if (args[i] == "-seq") {
                    String s = args[i + 1];
                    for (char c : s.toCharArray()) {
                        if (ProgramArgs.shapes.indexOf(c) < 0) {
                            errors.append("Invalid shape: " + c + "\n");
                            valid = false;
                        }
                    }
                    a.sequence = s;
                    i++;
                } else {
                    errors.append("Invalid argument: " + args[i] + "\n");
                    valid = false;
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Parse error: " + e.getMessage() + "\n" + usage);
        }
        if (!valid) {
            errors.append(usage);
            throw new IllegalArgumentException(errors.toString());
        }
        return a;
    }
}
