package tetris;

/**
 * Created by bwbecker on 2016-09-19.
 */
public class TetrisMain {

    public static void main(String[] args) {
        System.out.println("Hello, Tetris!");
        try {
            ProgramArgs a = ProgramArgs.parseArgs(args);
            //Tetris tetris = new Tetris(a.getFPS(), a.getSpeed(), a.getSequence());
            Tetris tetris = new Tetris(a.getFPS(), 1.0, a.getSequence());

        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }
}


