package da.developer.gamecaro;

import java.util.Random;

public class CaroBoard {
	
    static EvalBoard eBoard;
 //   List<Node> listUndo = new List<Node>();

    public static int kt= 0; // bien khoi tao
    public static int[][] BoardArr = new int[16][16]; //Nguoi 1 May 2 Chua 0
    static int playerFlag = 2; //Biến cờ xác định máy đi hay người đi.
    public static int _x; //Tọa độ nước cờ mà máy đi.
    public static int _y;

    public static int maxDepth = 20;
    public static int maxMove = 8;
    public static int depth = 0;

    public static boolean fWin = false;
    public static int fEnd = 1;

    public static int[] DScore = new int[] { 0, 1, 9, 81, 729 };
    
    //public int[] AScore = new int[5] { 0, 3, 24, 243, 2197 };
    public static int[] AScore = new int[] { 0, 2, 18, 162, 1458 };
    
    //public int[] AScore = new int[5] { 0, 1, 9, 81, 729 };


    static Node[] PCMove = new Node[maxMove+2];
    static Node[] HumanMove = new Node[maxMove+2];
    static Node[] WinMove = new Node[maxDepth+2];
    static Node[] LoseMove = new Node[maxDepth + 2];
    
    public static void InitCaroBoard()
    {
        for (int i = 0; i < 16 * 16; i++)
            BoardArr[i % 16][ i / 16] = 0;
        	eBoard = new EvalBoard();
    }
    
    // May di co
    public static void ComputerPlay()
    {
        for (int i = 0; i < maxMove; i++)
        {
            WinMove[i] = new Node();
            PCMove[i] = new Node();
            HumanMove[i] = new Node();
        }

        depth = 0;
        FindMove();
    	
        if (fWin)
        {
            _x = WinMove[0].Row;
            _y = WinMove[0].Column;
        }
        else
        {
            EvalChessBoard(2);
            Node temp = new Node(); 
            temp = eBoard.MaxPos();
            _x = temp.Row;
            _y = temp.Column;
        }
        BoardArr[_x][_y] = 2;
        
    }
    
    private static void FindMove()
    {
        if (depth > maxDepth) return;
        depth++;
        fWin = false;
        boolean fLose = false;
        Node pcMove = new Node();
        Node humanMove = new Node();
        int countMove = 0;

        EvalChessBoard(2);

        //Lay ra MaxMove buoc di co diem cao nhat
        Node temp = new Node();
        for (int i = 0; i < maxMove; i++)
        {
            temp = eBoard.MaxPos();
            PCMove[i] = temp;
            //eBoard.EBoard[temp.Row][temp.Column] = 0; ??
            EvalBoard.EBoard[temp.Row][temp.Column] = 0;
        }

        //Lay nuoc di trong PCMove[] ra danh thu
        countMove = 0;
        while (countMove < maxMove)
        {

            pcMove = PCMove[countMove++];
            BoardArr[pcMove.Row][pcMove.Column] = 2;
            //WinMove.SetValue(pcMove, depth - 1);
            WinMove[depth - 1] = pcMove;
            
            //Tim cac nuoc di toi uu cua nguoi
            eBoard.ResetBoard();
            EvalChessBoard(1);
            //Lay ra maxMove nuoc di co diem cao nhat cua nguoi
            for (int i = 0; i < maxMove; i++)
            {
                temp = eBoard.MaxPos();
                HumanMove[i] = temp;
                eBoard.EBoard[temp.Row][temp.Column] = 0;
            }
            //Danh thu cac nuoc di
            for (int i = 0; i < maxMove; i++)
            {
                humanMove = HumanMove[i];
                BoardArr[humanMove.Row][humanMove.Column] = 1;
                if (CaroModel.CheckEnd(humanMove.Row,humanMove.Column) == 2)
                {
                    fWin = true;
                    //MessageBox.Show("fwin" + fWin.ToString());
                }
                if (CaroModel.CheckEnd(humanMove.Row,humanMove.Column) == 1)
                {
                    fLose = true;
                    //MessageBox.Show("flose" + fLose.ToString());
                }
                if (fLose)
                {
                    BoardArr[pcMove.Row][ pcMove.Column] = 0;
                    BoardArr[humanMove.Row][ humanMove.Column] = 0;
                    break;
                }
                if (fWin)
                {
                    BoardArr[pcMove.Row][pcMove.Column] = 0;
                    BoardArr[humanMove.Row][humanMove.Column] = 0;
                    return;
                }
                FindMove();
                BoardArr[humanMove.Row][humanMove.Column] = 0;
            }
            BoardArr[pcMove.Row][pcMove.Column] = 0;

        }

    }
    
    //Ham tinh gia tri cho bang luong gia
    private static void EvalChessBoard(int player)
    {
        int rw, cl, ePC, eHuman;
        eBoard.ResetBoard();
        
        //Danh gia theo hang
        for (rw = 0; rw < 16; rw++)            
            for (cl = 0; cl < 16 - 4; cl++)
            {
                ePC = 0; eHuman = 0;
                for (int i = 0; i < 5; i++)
                {
                    if (BoardArr[rw][ cl + i] == 1) eHuman++;
                    if (BoardArr[rw][ cl + i] == 2) ePC++;
                }

                if (eHuman * ePC == 0 && eHuman != ePC)
                {
                    for (int i = 0; i < 5; i++)
                    {
                        if (BoardArr[rw][ cl + i] == 0) // Neu o chua duoc danh
                        {
                            if (eHuman == 0)
                                if (player == 1)
                                    eBoard.EBoard[rw][ cl + i] += DScore[ePC];
                                else eBoard.EBoard[rw][ cl + i] += AScore[ePC];
                            if (ePC == 0)
                                if (player == 2)
                                    eBoard.EBoard[rw][ cl + i] += DScore[eHuman];
                                else eBoard.EBoard[rw][ cl + i] += AScore[eHuman];
                            if (eHuman == 4 || ePC == 4)
                                eBoard.EBoard[rw][ cl + i] *= 2;
                        }
                    }
                    
                }                
             }

        //Danh gia theo cot
        for (cl = 0; cl < 16; cl++)
            for (rw = 0; rw < 16 - 4; rw++)
            {
                ePC = 0; eHuman = 0;
                for (int i = 0; i < 5; i++)
                {
                    if (BoardArr[rw + i][ cl] == 1) eHuman++;
                    if (BoardArr[rw + i][ cl] == 2) ePC++;
                }

                if (eHuman * ePC == 0 && eHuman != ePC)
                {
                    for (int i = 0; i < 5; i++)
                    {
                        if (BoardArr[rw + i][ cl] == 0) // Neu o chua duoc danh
                        {
                            if (eHuman == 0)
                                if (player == 1)
                                    eBoard.EBoard[rw + i][ cl] += DScore[ePC];
                                else eBoard.EBoard[rw + i][ cl] += AScore[ePC];
                            if (ePC == 0)
                                if (player == 2)
                                    eBoard.EBoard[rw + i][ cl] += DScore[eHuman];
                                else eBoard.EBoard[rw + i][ cl] += AScore[eHuman];
                            if (eHuman == 4 || ePC == 4)
                                eBoard.EBoard[rw + i][ cl] *= 2;
                        }
                    }

                }
            }

        //Danh gia duong cheo xuong
        for (cl = 0; cl < 16 - 4; cl++)
            for (rw = 0; rw < 16 - 4; rw++)
            {
                ePC = 0; eHuman = 0;
                for (int i = 0; i < 5; i++)
                {
                    if (BoardArr[rw + i][ cl + i] == 1) eHuman++;
                    if (BoardArr[rw + i][ cl + i] == 2) ePC++;
                }

                if (eHuman * ePC == 0 && eHuman != ePC)
                {
                    for (int i = 0; i < 5; i++)
                    {
                        if (BoardArr[rw + i][ cl + i] == 0) // Neu o chua duoc danh
                        {
                            if (eHuman == 0)
                                if (player == 1)
                                    eBoard.EBoard[rw + i][ cl + i] += DScore[ePC];
                                else eBoard.EBoard[rw + i][ cl + i] += AScore[ePC];
                            if (ePC == 0)
                                if (player == 2)
                                    eBoard.EBoard[rw + i][ cl + i] += DScore[eHuman];
                                else eBoard.EBoard[rw + i][ cl + i] += AScore[eHuman];
                            if (eHuman == 4 || ePC == 4)
                                eBoard.EBoard[rw + i][ cl + i] *= 2;
                        }
                    }

                }
            }

        //Danh gia duong cheo len
        for (rw = 4; rw < 16; rw++)
            for (cl = 0; cl < 16 - 4; cl++)
            {
                ePC = 0; eHuman = 0;
                for (int i = 0; i < 5; i++)
                {
                    if (BoardArr[rw - i][ cl + i] == 1) eHuman++;
                    if (BoardArr[rw - i][ cl + i] == 2) ePC++;
                }

                if (eHuman * ePC == 0 && eHuman != ePC)
                {
                    for (int i = 0; i < 5; i++)
                    {
                        if (BoardArr[rw - i][ cl + i] == 0) // Neu o chua duoc danh
                        {
                            if (eHuman == 0)
                                if (player == 1)
                                    eBoard.EBoard[rw - i][ cl + i] += DScore[ePC];
                                else eBoard.EBoard[rw - i][ cl + i] += AScore[ePC];
                            if (ePC == 0)
                                if (player == 2)
                                    eBoard.EBoard[rw - i][ cl + i] += DScore[eHuman];
                                else eBoard.EBoard[rw - i][ cl + i] += AScore[eHuman];
                            if (eHuman == 4 || ePC == 4)
                                eBoard.EBoard[rw - i][ cl + i] *= 2;
                        }
                    }

                }
            }
        
    }

    // Khoi tao game mơi
    public static void NewGame()
    {
	    for (int i = 0; i < 16 * 16; i++)
	        if (BoardArr[i % 16][ i / 16] != 0)
	        {
	            BoardArr[i % 16][ i / 16] = 0;
	        }
	    if (fEnd == 1)
	        playerFlag = 2;
	    else playerFlag = 1;
	    if (playerFlag == 2)
	    {
	        Random r = new Random();
	        _x = r.nextInt(3);
	        _y = r.nextInt(3);
	        BoardArr[_x + 7][ _y + 7] = 2;
	        playerFlag = 1;
	    }
	    fEnd = 0;
    }
}