package tetris;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by LangChen on 2016/9/26.
 */


public class TetrisModel
{
    // Size of the play area (unit: Block)
    private static final Size PlayArea = new Size(10, 24);
    private static final Size BoundaryPlayArea = new Size(PlayArea.Width + 2, PlayArea.Height + 2); // 2 => boundary

    // Status of Block
    public enum BlockStatus {
        EMPTY,      // Nothing to shown
        BLOCK,      // Fixed pieces
        WALL,       // Boundary
        ACTIVE      // Pieces (NOT VALID FOR playArea)
    }

    // Status of Game
    public static final int STATUS_SPLASH = 0;
    public static final int STATUS_MAINMENU = 1;
    public static final int STATUS_DIFFSELECT = 2;
    public static final int STATUS_PLAYING = 3;
    public static final int STATUS_PAUSE = 4;
    public static final int STATUS_GAMEOVER = 5;

    // Definition of pieces
    private Vector< Vector<BlockStatus[][]> > _piecesData; // Types => Rotation => Data Matrix

    // Play area related
    private BlockStatus _playAreaMatrix[][];    // main play area
    private int _playAreaMaskMatrix[][];        // used for differentiate the type of pieces, only valid for BLOCK
    // Basic parameters
    private String _sequence;

    // Runtime parameters
    private Point _position;                    // position of current moving pieces
    private int _indexMovingPiece;              // current index of moving piece: 0 ~ _sequence.size() - 1
    private int _indexRotation;                 // current index of moving piece after rotation
    private BlockStatus[][] _movingPiece;       // current moving piece
    private boolean _pieceSelected;             // current piece selected flag
    private int _score;                         // total score

    // View & Controller
    private TetrisView _tetrisView;

    // Game status
    /**
     * 0 SPLASH
     * 1 MAIN MENU
     * 2 DIFFICULTY SELECT
     * 3 PLAYING
     * 4 PAUSE
     * 5 GAME OVER
     */
    private AtomicInteger _gameStatus;

    // Constructor
    public TetrisModel(String sequence) {
        _gameStatus = new AtomicInteger();
        // initialize basic parameters
        _sequence = TetrisMath.ParsePiecesType(sequence);

        //Start from SPLASH
        _gameStatus.set(STATUS_SPLASH);
    }
    // Internal newGame action processing
    private void newGame() {
        // initialize game parameters
        _playAreaMatrix = new BlockStatus[BoundaryPlayArea.Width][BoundaryPlayArea.Height];
        _playAreaMaskMatrix = new int[BoundaryPlayArea.Width][BoundaryPlayArea.Height];
        // init. playAreaMatrix
        TetrisMath.InitPlayAreaMatrix(_playAreaMatrix, BoundaryPlayArea);
        // init. walls
        TetrisMath.AddWalls(_playAreaMatrix, BoundaryPlayArea);
        // init. pieces data
        _piecesData = TetrisMath.GeneratePieces();

        // init. runtime parameters
        _position = TetrisMath.GetInitPosMoving(BoundaryPlayArea);
        _indexMovingPiece = 0;
        _indexRotation = 0;
        int indexMovingPieceType = TetrisMath.GetCurrentPieceType(_sequence, _indexMovingPiece);
        _movingPiece = TetrisMath.GetCurrentPiece(_piecesData, indexMovingPieceType, _indexRotation);
        _score = 0;

        // init. View
        _draw();

        // init. GameStatus
        _gameStatus.set(STATUS_PLAYING);
    }

    // Update reference of View
    public void SetView(TetrisView tetrisView) {
        _tetrisView = tetrisView;
    }

    // Accessor
    public Size GetDrawArea(){
        return PlayArea;
    }
    public int GetStatus() { return _gameStatus.get();}

    // Common Actions
    public void NewGame(){
        newGame();
    }
    public void GameOver()
    {
        _gameStatus.set(STATUS_GAMEOVER);
    }
    public void Splash() {_gameStatus.set(STATUS_SPLASH);}

    // KeyBoard Actions
    public boolean MoveLeft() {
        boolean result =_changePosition(_position.Subtract(new Point(1, 0)));
        _draw();
        return result;
    }
    public boolean MoveRight() {
        boolean result = _changePosition(_position.Add(new Point(1, 0)));
        _draw();
        return result;
    }
    public boolean Drop() {
        boolean result = _changePosition(_position.Add(new Point(0, 2)));
        if(!result)
        {
            result = _changePosition(_position.Add(new Point(0, 1)));
        }
        _draw();
        return result;
    }
    public boolean RotateLeft() {
        int countRotation = _piecesData.elementAt(TetrisMath.GetCurrentPieceType(_sequence, _indexMovingPiece)).size();
        int newIndexRotation = (_indexRotation + 1) % countRotation;
        boolean result = _changeRotation(newIndexRotation);

        if(result)
        {
            // Update indexRotation
            _indexRotation = newIndexRotation;
        }

        _draw();
        return result;
    }
    public boolean RotateRight() {
        int countRotation = _piecesData.elementAt(TetrisMath.GetCurrentPieceType(_sequence, _indexMovingPiece)).size();
        int newIndexRotation = (countRotation + _indexRotation - 1) % countRotation;
        boolean result = _changeRotation(newIndexRotation);

        if(result)
        {
            // Update indexRotation
            _indexRotation = newIndexRotation;
        }

        _draw();
        return result;
    }
    public boolean ResizePieceSize() {
        if(_gameStatus.get() == STATUS_PAUSE)
        {
            _tetrisView.ResizePieceSize();
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean Pause() {
        if(_gameStatus.get() == STATUS_PLAYING)
        {
            _gameStatus.set(STATUS_PAUSE);
            return true;
        }
        else if (_gameStatus.get() == STATUS_PAUSE)
        {
            _gameStatus.set(STATUS_PLAYING);
            return true;
        }
        return false;
    }

    // Mouse Actions
    public boolean MouseClickPiece(Point mousePos) {
        if(!_pieceSelected)
        {
            // Select
            _pieceSelected = TetrisMath.CheckMouseSelected(mousePos, _position, _movingPiece);
            return _pieceSelected;
        }
        else
        {
            // Drop
            _pieceSelected = false; // clear flag to prevent processing event of MouseMotion
            while(Drop()) { ; }     // drop until the bottom?
            return true;
        }
    }
    public void MouseMovePiece(Point mousePos) {
        // Move only if the current piece is selected
        if(_pieceSelected)
        {
            int mouseStartPos = mousePos.X - 2; // -2: transfer from mid position to start position w.r.t moving pieces
            int pieceStartPos = _position.X;
            int diff = mouseStartPos - pieceStartPos;
            int moveTimes = Math.abs(diff);
            // Direction Determine
            for(int i = 0; i < moveTimes; i++)
            {
                if(diff > 0)
                {
                    // move right
                    if(!MoveRight())
                    {
                        break;
                    }
                }
                else if (diff < 0)
                {
                    // move left
                    if(!MoveLeft())
                    {
                        break;
                    }
                }
            }
        }

    }

    // Called only no other actions after one time period (i.e. after $speed seconds)
    public void NextStep() {
        /**
         * 1. Check collision at current position
         * 2. If (!collision) => Drop(), Return; Else => 3.
         * 3. FixMovingPieces();
         * 4. CheckFinishedRow();
         * 5. Generate new moving pieces
         * 6. Init. some runtime parameters
         * 7. CheckGameOver();
         * 8. Transfer the right of control to user
         */

        boolean hitBottom = TetrisMath.CheckHitBottom(_movingPiece, _playAreaMatrix, _position);
        if(!hitBottom)
        {
            _position = _position.Add(new Point(0, 1));
        }
        else
        {
            TetrisMath.FixMovingPieces(
                    _movingPiece,
                    _playAreaMatrix,
                    TetrisMath.GetCurrentPieceType(_sequence, _indexMovingPiece),
                    _playAreaMaskMatrix,
                    _position
            );

            _score += TetrisMath.CheckFinishedRow(
                    _playAreaMatrix,
                    _playAreaMaskMatrix,
                    BoundaryPlayArea
            );

            // Generate new moving pieces
            _indexMovingPiece = TetrisMath.GetNextPieceIndex(_sequence, _indexMovingPiece);
            _indexRotation = 0;
            _position = TetrisMath.GetInitPosMoving(BoundaryPlayArea);
            int indexMovingPieceType = TetrisMath.GetCurrentPieceType(_sequence, _indexMovingPiece);
            _movingPiece = TetrisMath.GetCurrentPiece(_piecesData, indexMovingPieceType, _indexRotation);
            _pieceSelected = false;     // clear selected flag

            // Check Game Over
            if(TetrisMath.CheckGameOver(_movingPiece, _playAreaMatrix, BoundaryPlayArea))
            {
                GameOver();
            }
        }

        // Update View
        _draw();
    }

    // Helper function for MoveLeft, MoveRight, Drop
    private boolean _changePosition(Point newPosition) {
        boolean collision = TetrisMath.CheckCollision(_movingPiece, _playAreaMatrix, newPosition);
        if(!collision)
        {
            _position = newPosition;
            return true;
        }
        return false;
    }
    // Helper function for RotateLeft, RotateRight
    private boolean _changeRotation(int newIndexRotation) {
        TetrisModel.BlockStatus[][] newPiece = TetrisMath.GetCurrentPiece(
                _piecesData,
                TetrisMath.GetCurrentPieceType(_sequence, _indexMovingPiece),
                newIndexRotation);
        boolean collision = TetrisMath.CheckCollision(newPiece, _playAreaMatrix, _position);
        if(!collision)
        {
            _movingPiece = newPiece;
            return true;
        }
        return false;
    }

    // Update View
    private void _draw() {
        int[][] displayMatrix = new int[PlayArea.Width][PlayArea.Height];

        // Processing PlayArea
        for(int i = 1; i < BoundaryPlayArea.Width - 1; i++)
        {
            for(int j = 1; j < BoundaryPlayArea.Height - 1; j++)
            {
                if(_playAreaMatrix[i][j] == BlockStatus.BLOCK)
                {
                    displayMatrix[i - 1][j - 1] = _playAreaMaskMatrix[i][j];
                }
                else
                {
                    displayMatrix[i - 1][j - 1] = - 1;  // -1 : Nothing to display
                }
            }
        }

        // Processing Moving Piece
        // Assert : Should not go out of boundary, Overlap with PlayArea should not occur
        for(int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                Point absPosition = _position.Add(new Point(i - 1, j - 1));
                if(_movingPiece[i][j] != BlockStatus.EMPTY)
                {
                    displayMatrix[absPosition.X][absPosition.Y] = TetrisMath.GetCurrentPieceType(_sequence, _indexMovingPiece);
                }
            }
        }

        // Pass the processed Matrix to View
        _tetrisView.DataOpeartion(displayMatrix, false);
        // Pass the current score to View
        _tetrisView.ScoreOperation(_score, false);
    }
}
