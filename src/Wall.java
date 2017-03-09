public class Wall implements Move
{
    private boolean isHorizontal;       
    private boolean movePlayed;         
    private int id;                     
    private char symbol;                
    private Player player;                 
    private BoardGame game;
    private Box[] boxList;
    private Box upLeftBox;
    private Box downRightBox;
    
    public Wall(int numID, BoardGame board)
    {
        id = numID;
        game = board;
        player = null;
        boxList = (Box[]) game.getStateList();
    }
    
    public boolean isHorizontal()
    {
        return isHorizontal;
    }
    public int getID()
    {
        return id;
    }
    public void setHorizontal()
    {
        isHorizontal = true;
        symbol = '_';
        
        for(Box b : boxList)
        {
            if(b.getBottom() == this)
            {
                upLeftBox = b;
            }
            if(b.getTop() == this)
            {
                downRightBox = b;
            }
        }
    }
    public void setVertical()
    {
        isHorizontal = false;
        symbol = '|';
        
        for(Box b : boxList)
        {
            if(b.getRight() == this)
            {
                upLeftBox = b;
            }
            if(b.getLeft() == this)
            {
                downRightBox = b;
            }
        }
    }
    public char getSymbol()
    {
        return symbol;
    }
    public boolean movePlayed()
    {
        return movePlayed;
    }
    public void setMovePlayed(boolean played)
    {
        movePlayed = played;
    }
    public String toString()
    {
        if(movePlayed)
        {
            return Character.toString(symbol);
        }
        else 
        {
            return Integer.toString(id);
        }
    }
    public BoardGame getBoardGame()
    {
        return game;
    }
    public Player getPlayer()
    {
        return player;
    }
    public void setPlayer(Player play)
    {
        player = play;
    }
    public Box getUpLeft()
    {
        return upLeftBox;
    }
    public Box getDownRight()
    {
        return downRightBox;
    }
}
