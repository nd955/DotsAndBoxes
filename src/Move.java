public interface Move 
{
    public int getID();
    
    public BoardGame getBoardGame();
    
    public char getSymbol();
    
    public Player getPlayer();
    
    public void setPlayer(Player play);
    
    public boolean movePlayed();
    
    public void setMovePlayed(boolean played);
}
