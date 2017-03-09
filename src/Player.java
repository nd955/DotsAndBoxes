public interface Player
{
    public void makeBestMove(BoardGame game, int level, double alpha, double beta, int opponents);
    
    public double maxValue(BoardGame game, int level, double alpha, double beta, int opponents);
    
    public double minValue(BoardGame game, int level, double alpha, double beta, int opponents);
    
    public int getScore();
    
    public char getColor();
    
    public void incrementScore();
    
    public void decrementScore();
    
    public Player getNextPlayer(BoardGame board);
    
    public Player getLastPlayer(BoardGame board);
    
    public void setOrder(int position);
    
    public void resetScore();
    
    public String toString();
}
