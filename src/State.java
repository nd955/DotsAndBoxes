public interface State
{
    public boolean isWon();
    
    public void updateIsWon();
    
    public double getValue();
    
    public String getSymbol();
    
    public int getID();
}
