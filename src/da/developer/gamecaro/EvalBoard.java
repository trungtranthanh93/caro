package da.developer.gamecaro;

public class EvalBoard {

	public static int[][] EBoard;
	public EvalBoard()
	{
		EBoard = new int[16 + 2][ 16 + 2];
        ResetBoard();
	}
    public static void ResetBoard()
    {
        for (int r = 0; r < 16 ; r++)
            for (int c = 0; c < 16 ; c++)
            	EBoard[r][ c] = 0;
    }
    public Node MaxPos()
    {
        int r, c, MaxValue = 0;
        Node n = new Node();

        for (r = 1; r <= 16; r++)
            for (c = 1; c <= 16; c++)
                if (EBoard[r][ c] > MaxValue)
                {
                    n.Row = r; n.Column = c;
                    MaxValue = EBoard[r][ c];
                }
        return n;
    }
}
