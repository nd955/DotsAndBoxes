public interface BoardGame 
{
	
    public void setup();
    
    public void startNewGame();
    
    public void resetGame();
    
    public Player[] randomizeOrder(Player[] players);
    
	public boolean makeMove(int position);
	
	public void undoMove(int position); 
    
	public Player whoseMove(); 
    
	public boolean gameOver();  

	public Player getWinner();

    public double getBoardValue(Player player);
    
    public int getNumRows();

    public int getNumCols(); 
    
    public boolean validMove(int position); 
    
    public Player[] getPlayerList();
    
    public Move[] getMoveList();
    
    public State[] getStateList();

}