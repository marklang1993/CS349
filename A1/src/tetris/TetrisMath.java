package tetris;

import javafx.geometry.Pos;

import java.util.Vector;

/**
 * Created by LangChen on 2016/9/26.
 */
public class TetrisMath {

    public static final Size MinPieceSize = new Size(16, 16);  // Fixed, Minimum size
    public static Size PieceSize;

    // Generate the vector of all pieces
    public static Vector< Vector<TetrisModel.BlockStatus[][]> >GeneratePieces()
    {
        // total count of types of pieces: 7
        // counts of all shapes for each type of pieces: 2, 4, 4, 4, 1, 2, 2
        Vector< Vector<TetrisModel.BlockStatus[][]> > piecesDefinition = new Vector< Vector<TetrisModel.BlockStatus[][]> >();

        // Define the pieces
        // 0. I
        TetrisModel.BlockStatus[][] matrix_I0 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY}
                };
        TetrisModel.BlockStatus[][] matrix_I1 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY}
                };
        TetrisModel.BlockStatus[][] matrix_I2 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY}
                };
        TetrisModel.BlockStatus[][] matrix_I3 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY}
                };
        Vector<TetrisModel.BlockStatus[][]> vector_I = new Vector<TetrisModel.BlockStatus[][]>();
        vector_I.add(matrix_I0);
        vector_I.add(matrix_I1);
        vector_I.add(matrix_I2);
        vector_I.add(matrix_I3);
        piecesDefinition.add(vector_I);

        // 1. L
        TetrisModel.BlockStatus[][] matrix_L0 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY}
                };
        TetrisModel.BlockStatus[][] matrix_L1 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY}
                };
        TetrisModel.BlockStatus[][] matrix_L2 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY}
                };
        TetrisModel.BlockStatus[][] matrix_L3 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY}
                };
        Vector<TetrisModel.BlockStatus[][]> vector_L = new Vector<TetrisModel.BlockStatus[][]>();
        vector_L.add(matrix_L0);
        vector_L.add(matrix_L1);
        vector_L.add(matrix_L2);
        vector_L.add(matrix_L3);
        piecesDefinition.add(vector_L);

        // 2. J
        TetrisModel.BlockStatus[][] matrix_J0 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY}
                };
        TetrisModel.BlockStatus[][] matrix_J1 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY}
                };
        TetrisModel.BlockStatus[][] matrix_J2 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY}
                };
        TetrisModel.BlockStatus[][] matrix_J3 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY}
                };
        Vector<TetrisModel.BlockStatus[][]> vector_J = new Vector<TetrisModel.BlockStatus[][]>();
        vector_J.add(matrix_J0);
        vector_J.add(matrix_J1);
        vector_J.add(matrix_J2);
        vector_J.add(matrix_J3);
        piecesDefinition.add(vector_J);

        // 3. T
        TetrisModel.BlockStatus[][] matrix_T0 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY}
                };
        TetrisModel.BlockStatus[][] matrix_T1 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY}
                };
        TetrisModel.BlockStatus[][] matrix_T2 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY}
                };
        TetrisModel.BlockStatus[][] matrix_T3 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY}
                };
        Vector<TetrisModel.BlockStatus[][]> vector_T = new Vector<TetrisModel.BlockStatus[][]>();
        vector_T.add(matrix_T0);
        vector_T.add(matrix_T1);
        vector_T.add(matrix_T2);
        vector_T.add(matrix_T3);
        piecesDefinition.add(vector_T);

        // 4. O
        TetrisModel.BlockStatus[][] matrix_O0 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY}
                };
        Vector<TetrisModel.BlockStatus[][]> vector_O = new Vector<TetrisModel.BlockStatus[][]>();
        vector_O.add(matrix_O0);
        piecesDefinition.add(vector_O);

        // 5. S
        TetrisModel.BlockStatus[][] matrix_S0 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY}
                };
        TetrisModel.BlockStatus[][] matrix_S1 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY}
                };
        Vector<TetrisModel.BlockStatus[][]> vector_S = new Vector<TetrisModel.BlockStatus[][]>();
        vector_S.add(matrix_S0);
        vector_S.add(matrix_S1);
        piecesDefinition.add(vector_S);

        // 6. Z
        TetrisModel.BlockStatus[][] matrix_Z0 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY}
                };
        TetrisModel.BlockStatus[][] matrix_Z1 = new TetrisModel.BlockStatus[][]
                {
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY},
                        {TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.ACTIVE, TetrisModel.BlockStatus.EMPTY, TetrisModel.BlockStatus.EMPTY}
                };
        Vector<TetrisModel.BlockStatus[][]> vector_Z = new Vector<TetrisModel.BlockStatus[][]>();
        vector_Z.add(matrix_Z0);
        vector_Z.add(matrix_Z1);
        piecesDefinition.add(vector_Z);

        return piecesDefinition;
    }

    // Convert pieces type from string data to numeric data
    public static String ParsePiecesType(String inStr)
    {
        /**
         * I = 0
         * L = 1
         * J = 2
         * T = 3
         * O = 4
         * S = 5
         * Z = 6
         */
        StringBuilder outStr = new StringBuilder(inStr.length());

        for(int i = 0; i < inStr.length(); i++)
        {
            char ch = '0';
            switch (inStr.charAt(i))
            {
                case 'I':
                    ch = '0';
                    break;
                case 'L':
                    ch = '1';
                    break;
                case 'J':
                    ch = '2';
                    break;
                case 'T':
                    ch = '3';
                    break;
                case 'O':
                    ch = '4';
                    break;
                case 'S':
                    ch = '5';
                    break;
                case 'Z':
                    ch = '6';
                    break;
            } // switch
            outStr.append(ch);
        } // for

        return outStr.toString();
    }

    // Get type of current piece
    public static int GetCurrentPieceType(String inStr, int currentIndex)
    {
        String num = inStr.substring(currentIndex, currentIndex + 1);
        return Integer.parseInt(num);
    }

    // Get data of current piece
    public static TetrisModel.BlockStatus[][] GetCurrentPiece(
            Vector< Vector<TetrisModel.BlockStatus[][]> > piecesData,
            int type, int rotation)
    {
        return piecesData.elementAt(type).elementAt(rotation);
    }

    // Get initial position of moving piece
    public static Point GetInitPosMoving(Size boundaryPlayAreaSize)
    {
        return new Point(boundaryPlayAreaSize.Width / 2 - 2, 1); // "-2" since the size of moving pieces is 4*4
    }

    // Init. PlayArea Matrix
    public static void InitPlayAreaMatrix(TetrisModel.BlockStatus[][] playArea, Size boundaryPlayArea)
    {
        for(int i = 0; i< boundaryPlayArea.Width; i++)
        {
            for(int j = 0; j < boundaryPlayArea.Height; j++)
            {
                playArea[i][j] = TetrisModel.BlockStatus.EMPTY;
            }
        }
    }

    // Add walls
    public static void AddWalls(TetrisModel.BlockStatus[][] playArea, Size boundaryPlayArea)
    {
        // init. walls
        for (int i = 0; i < boundaryPlayArea.Width; i++)
        {
            playArea[i][0] = TetrisModel.BlockStatus.WALL;
            playArea[i][boundaryPlayArea.Height - 1] = TetrisModel.BlockStatus.WALL;
        }
        for (int i = 0; i< boundaryPlayArea.Height; i++)
        {
            playArea[0][i] = TetrisModel.BlockStatus.WALL;
            playArea[boundaryPlayArea.Width - 1][i] = TetrisModel.BlockStatus.WALL;
        }
    }

    // Get the index of next piece
    public static int GetNextPieceIndex(String inStr, int currentIndex)
    {
        currentIndex = (currentIndex + 1) % inStr.length();
        return currentIndex;
    }


    // Check the collision of moving pieces and walls & blocks
    // if collide => true, otherwise => false
    public static boolean CheckCollision(TetrisModel.BlockStatus[][] movingPieces,
                                                   TetrisModel.BlockStatus[][] playArea,
                                                   Point position)
    {
        // Check the boundary
        Point lowerBound = position.Add(0);
        Point upperBound = position.Add(4);
        try
        {
            for(int i = lowerBound.X; i < upperBound.X; i++)
            {
                for(int j = lowerBound.Y; j < upperBound.Y; j++)
                {
                    Point relativePos = (new Point(i, j)).Subtract(lowerBound); // for iterating movingPieces
                    TetrisModel.BlockStatus movingPiecesElement = movingPieces[relativePos.X][relativePos.Y];
                    if(movingPiecesElement == TetrisModel.BlockStatus.ACTIVE)
                    {
                        // if the element of moving piece is ACTIVE, then check the corresponding element of play area.
                        TetrisModel.BlockStatus playAreaElement = playArea[i][j];
                        if(playAreaElement != TetrisModel.BlockStatus.EMPTY)
                        {
                            return true;
                        }
                    }
                }
            }
        }
        catch (IndexOutOfBoundsException ex)
        {
            // Out of boundary
            return true;
        }
        return false;
    }

    // Check the collision of moving pieces with the bottom
    // if collide => true, otherwise => false
    public static boolean CheckHitBottom(TetrisModel.BlockStatus[][] movingPieces,
                                         TetrisModel.BlockStatus[][] playArea,
                                         Point position)
    {
        // Check the boundary
        Point lowerBound = position.Add(0);
        Point upperBound = position.Add(4);
        try
        {
            for(int i = lowerBound.X; i < upperBound.X; i++)
            {
                for(int j = lowerBound.Y; j < upperBound.Y; j++)
                {
                    Point relativePos = (new Point(i, j)).Subtract(lowerBound); // for iterating movingPieces
                    TetrisModel.BlockStatus movingPiecesElement = movingPieces[relativePos.X][relativePos.Y];
                    if(movingPiecesElement == TetrisModel.BlockStatus.ACTIVE)
                    {
                        // if the element of moving piece is ACTIVE, then check the surrounding elements of play area.
                        try
                        {
                            /**
                             * X X X
                             * X A X
                             * X O X
                             *
                             * A: ACTIVE
                             * X: Does not check
                             * W: Only check wall
                             * O: Check all
                             */
                            // May be out of boundary
                            TetrisModel.BlockStatus playAreaElementDown = playArea[i][j + 1];
                            if(playAreaElementDown != TetrisModel.BlockStatus.EMPTY)
                            {
                                return true;
                            }
                        }
                        catch (IndexOutOfBoundsException ex)
                        {

                        }
                    }
                }
            }
        }
        catch (IndexOutOfBoundsException ex)
        {
            // Out of boundary
            return true;
        }
        return false;
    }

    // Make the moving pieces become fixed blocks
    public static void FixMovingPieces(TetrisModel.BlockStatus[][] movingPieces,
                                       TetrisModel.BlockStatus[][] playArea,
                                       int typePieces,
                                       int[][] playAreaMask,
                                       Point position)
    {
        // Get the boundary
        Point lowerBound = position.Add(0);
        Point upperBound = position.Add(4);
        for(int i = lowerBound.X; i < upperBound.X; i++)
        {
            for(int j = lowerBound.Y; j < upperBound.Y; j++)
            {
                Point relativePos = (new Point(i, j)).Subtract(lowerBound); // for iterating movingPieces
                TetrisModel.BlockStatus movingPiecesElement = movingPieces[relativePos.X][relativePos.Y];
                if(movingPiecesElement == TetrisModel.BlockStatus.ACTIVE)
                {
                    // if the element of moving piece is ACTIVE, then make the corresponding element become BLOCK
                    playArea[i][j] = TetrisModel.BlockStatus.BLOCK;
                    playAreaMask[i][j] = typePieces;
                }
            }
        }
    }

    // Check finished row
    // return value: score
    public static int CheckFinishedRow(TetrisModel.BlockStatus[][] playArea,
                                       int[][] playAreaMask,
                                       Size boundaryPlayAreaSize)
    {
        int score = 0;
        // Be careful with WALL
        for(int rowIndex = boundaryPlayAreaSize.Height - 2; rowIndex > 0; rowIndex--) // from (boundaryPlayAreaSize.Height - 2) to 1
        {
            // Check current row
            boolean finished = true;
            for(int i = 1; i < boundaryPlayAreaSize.Width - 1; i++)
            {
                finished = finished && (playArea[i][rowIndex] == TetrisModel.BlockStatus.BLOCK);
            }
            if(finished) {
                // Clear rowIndex-th row , and move other rows down
                _clearRow(playArea, playAreaMask, boundaryPlayAreaSize, rowIndex);
                // Reset the rowIndex
                rowIndex++;
                // Add score
                score += 10;
            }
        }
        return score;
    }

    private static void _clearRow(TetrisModel.BlockStatus[][] playArea,
                                  int[][] playAreaMask,
                                  Size boundaryPlayAreaSize,
                                  int rowIndex)
    {
        TetrisModel.BlockStatus[][] newPlayArea = new TetrisModel.BlockStatus[boundaryPlayAreaSize.Width][boundaryPlayAreaSize.Height];
        InitPlayAreaMatrix(newPlayArea, boundaryPlayAreaSize);

        int[][] newPlayAreaMask = new int[boundaryPlayAreaSize.Width][boundaryPlayAreaSize.Height];

        for(int n = boundaryPlayAreaSize.Height - 2; n > 0 ; n--) // from the bottom to the top
        {
            for(int m = 1; m < boundaryPlayAreaSize.Width - 1; m++)
            {
                if(rowIndex < n)
                {
                    // below the removed row, just copy
                    newPlayArea[m][n] = playArea[m][n];
                    newPlayAreaMask[m][n] = playAreaMask[m][n];
                }
                else if (rowIndex > n)
                {
                    // above the removed row => move all rows down
                    newPlayArea[m][n + 1] = playArea[m][n];
                    newPlayAreaMask[m][n + 1] = playAreaMask[m][n];
                }
            }
        }
        // Update
        for(int i = 1; i < boundaryPlayAreaSize.Width - 1; i++)
        {
            for(int j = 1; j < boundaryPlayAreaSize.Height - 1; j++)
            {
                playArea[i][j] = newPlayArea[i][j];
                playAreaMask[i][j] = newPlayAreaMask[i][j];
            }
        }
    }

    // Check game over
    public static boolean CheckGameOver(TetrisModel.BlockStatus[][] movingPieces,
                                        TetrisModel.BlockStatus[][] playArea,
                                        Size boundaryPlayAreaSize)
    {
        return CheckCollision(movingPieces, playArea, GetInitPosMoving(boundaryPlayAreaSize));
    }

    // Mouse related
    public static boolean CheckMouseSelected(Point mousePos,
                                             Point position,
                                             TetrisModel.BlockStatus[][] movingPieces)
    {
        Point mousePos_Wall = mousePos.Add(1); // Add the offset of Wall

        Point lowerBound = new Point(position);
        Point upperBound = lowerBound.Add(4);

        for(int i = lowerBound.X; i < upperBound.X; i++)
        {
            for(int j = lowerBound.Y; j < upperBound.Y; j++)
            {
                Point relativePos = (new Point(i, j)).Subtract(lowerBound);
                if(movingPieces[relativePos.X][relativePos.Y] == TetrisModel.BlockStatus.ACTIVE)
                {
                    if(mousePos_Wall.X == i && mousePos_Wall.Y == j)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
