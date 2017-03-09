import java.util.Scanner;


public class HumanPlayer implements Player
{
    private char color;
    private int score;
    private int order;
    
    public HumanPlayer(char playerColor)
    {
        color = playerColor;
        score = 0;
    }
    
    public void makeBestMove(BoardGame game, int level, double alpha, double beta, int opponents)
    {
        System.out.println("Select a move.");
        int choice = getInput();
        while(!game.validMove(choice))
        {
            System.out.println("Not a valid move, select again.");
            choice = getInput();
        }
        game.makeMove(choice);
    }
    public int getInput()
    { 
        @SuppressWarnings("resource")
        Scanner scan = new Scanner(System.in);
        int result;
        while(!scan.hasNextInt())
        {
            System.out.println("Not an integer, select again.");
            scan.next();
        }
        result = scan.nextInt();
        return result;
    }
    public double maxValue(BoardGame game, int level, double alpha, double beta, int opponents)
    {
        // Unnecessary for human input
        return 0;
    }
    public double minValue(BoardGame game, int level, double alpha, double beta, int opponents)
    {
        // Unnecessary for human input
        return 0;
    }
    public int getScore()
    {
        return score;
    }
    public void incrementScore()
    {
        score++;
    }

    public void decrementScore()
    {
        score--;
    }
    public Player getNextPlayer(BoardGame game)
    {
        if(order == game.getPlayerList().length - 1)
        {
            return game.getPlayerList()[0];
        }
        else 
        {
            return game.getPlayerList()[order + 1];
        }
    }
    public Player getLastPlayer(BoardGame game)
    {
        if(order == 0)
        {
            return game.getPlayerList()[game.getPlayerList().length - 1];
        }
        else 
        {
            return game.getPlayerList()[order - 1];
        }
    }
    public void setOrder(int position)
    {
        order = position;
    }
    public void resetScore()
    {
        score = 0;
    }
    public char getColor()
    {
        return color;
    }
    public String toString()
    {
        return Character.toString(color);
    }

}
