public class Box implements State
{
    private boolean isWon;   
    private Player boxWinner;
    private char color;         
    private int id;            
    private Wall top;           
    private Wall left;         
    private Wall right;         
    private Wall bottom;        
    private String symbol;
    
    public Box(int numID)
    {
        color = 'n';
        id = numID;
        isWon = false;
        boxWinner = null;
        symbol = "\t";
    }
    
    public boolean isWon()
    {
        return isWon;
    }
    public Player getBoxWinner()
    {
        return boxWinner;
    }
    public void setBoxWinner(Player winner)
    {
        boxWinner = winner;
        if(boxWinner != null)
            color = boxWinner.getColor();
        else
            color = 'n';
    }
    public char getColor()
    {
        return color;
    }
    public void setColor(char newColor)
    {
        color = newColor;
    }
    public Wall getTop()
    {
        return top;
    }
    public Wall getLeft()
    {
        return left;
    }
    public Wall getRight()
    {
        return right;
    }
    public Wall getBottom()
    {
        return bottom;
    }
    public void setTop(Wall wall)
    {
        top = wall;
    }
    public void setLeft(Wall wall)
    {
        left = wall;
    }
    public void setRight(Wall wall)
    {
        right = wall;
    }
    public void setBottom(Wall wall)
    {
        bottom = wall;
    }
    public void updateIsWon()
    {
        if(top.movePlayed() && left.movePlayed() && right.movePlayed() && bottom.movePlayed())
        {
            isWon = true;
        }
        else
        {
            isWon = false;
        }
    }
    public int getNumSides()
    {
        int wallCount = 0;
        if(top.movePlayed())
            wallCount++;
        if(left.movePlayed())
            wallCount++;
        if(right.movePlayed())
            wallCount++;
        if(bottom.movePlayed())
            wallCount++;
        return wallCount;
    }
    public String getSymbol()
    {
        return symbol;
    }
    public int getID()
    {
        return id;
    }
    public double getValue()
    {
        return getNumSides();
    }
}
