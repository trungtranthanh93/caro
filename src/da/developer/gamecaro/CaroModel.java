package da.developer.gamecaro;

enum Piece {X, O , _ };
//enum Player {None, Human, Machine }; None = -1, Human = 1; Machine = 2;
/**
 * Thực hiện các logic liên quan đến bàn cờ
 * @author H2O
 *
 */
public class CaroModel {

	public static int GRID_WIDTH = 16;
	public static int GRID_HEIGHT = 16;

	private int currenPlayer = 0;
	
    
	public CaroModel()
	{
		reset();
	}
	/**
	 * Thực hiện reset bàn cờ
	 */
	public void reset()
	{
		for(int i = 0; i < GRID_WIDTH; i++)
			for(int j = 0; j < GRID_HEIGHT; j++)
				CaroBoard.BoardArr[i][j] = 0;
	}
	
	/**
	 * Tìm player hiện tại
	 * @return int player hiện tại
	 */
	public int getCurrentPlayer(){
		return currenPlayer;
	}
	
	public void setValueX(int row, int col)
	{
		CaroBoard.BoardArr[row][col] = 1;
		currenPlayer = 1;
	}
	
	public void setValueO(int row, int col)
	{
		CaroBoard.BoardArr[row][col] = 2;
		currenPlayer = 2;
	}
	
	public int getValue(int row, int col){
		return CaroBoard.BoardArr[row][col];
	}
	
	// Ham kiem tra ket thuc
	 public static int CheckEnd(int cl, int rw)
     {
		 int iRet = 0;
         int r =0, c = 0;
         int i;
         boolean human, pc;
         //Check hàng ngang
         while (c < GRID_HEIGHT - 5)
         {
             human = true; pc = true;
             for (i = 0; i < 5; i++)
             {
                 if (CaroBoard.BoardArr[cl][ c + i] !=1 )
                     human = false;
                 if (CaroBoard.BoardArr[cl][ c + i] != 2)
                     pc = false;
             }
             if (human)  iRet = 1;
             if (pc)  iRet = 2;
             c++;
         }
         
         //Check hàng dọc
         while (r < GRID_WIDTH - 5)
         {
             human = true; pc = true;
             for (i = 0; i < 5; i++)
             {
                 if (CaroBoard.BoardArr[r + i][rw] != 1)
                     human = false;
                 if (CaroBoard.BoardArr[r + i][ rw] != 2)
                     pc = false;
             }
             if (human)  iRet = 1;
             if (pc)  iRet = 2;
             r++;
         }
         
         //Check duong cheo xuong
         r = rw; c = cl;
         while (r > 0 && c > 0) { r--; c--; }
         while (r <= GRID_WIDTH - 5 && c <= GRID_HEIGHT - 5)
         {
             human = true; pc = true;
             for (i = 0; i < 5; i++)
             {
                 if (CaroBoard.BoardArr[c + i][ r + i] != 1)
                     human = false;
                 if (CaroBoard.BoardArr[c + i][ r + i] != 2)
                     pc = false;
             }
             if (human)  iRet = 1;
             if (pc)  iRet = 2;
             r++; c++;
         }
         
         //Check duong cheo len
         r = rw; c = cl;
         while (r < GRID_WIDTH - 1 && c > 0) { r++; c--; }
         while (r >= 4 && c <= GRID_HEIGHT - 5)
         {
             human = true; pc = true;
             for (i = 0; i < 5; i++)
             {
                 if (CaroBoard.BoardArr[r - i][ c + i] != 1)
                     human = false;
                 if (CaroBoard.BoardArr[r - i][c + i] != 2)
                     pc = false;
             }
             if (human)  iRet = 1;
             if (pc)  iRet = 2;
             r--; c++;
         }
         
         
         return iRet;
     }

     
}
