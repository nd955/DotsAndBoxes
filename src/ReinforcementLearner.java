import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;


public class ReinforcementLearner implements Player 
{
	private Move idealMove;
	private Double[] featureList;
	private Double[] weightList;
	private double learningFactor;
	private double epsilon;
	private Random rand;
	private char color;
	private double reward;
	private boolean rewardEarned;
	private int score;
	private int order;
	
	public ReinforcementLearner(char playerColor)
	{
		featureList = new Double[3];
		weightList = new Double[3];
		learningFactor = 0.1;
		epsilon = 0.1;
		color = playerColor;
		rand = new Random();
		reward = 1;
		rewardEarned = false;
		score = 0;
	}
	
	public double maxValue(BoardGame board, int level, double alpha, double beta, int opponents)
	{
	    Move bestMove = null;
        BoardGame game = board;

        if(game.gameOver() || level == 0)
        {
            return getLearnedValue();
        }
        else
        {
            for(Move move : game.getMoveList())
            {
                if(game.validMove(move.getID()))
                {
                    double val;
                    game.makeMove(move.getID());
                    val = this.minValue(game, level - 1, alpha, beta, opponents - 1);
                    game.undoMove(move.getID());
                    if(val > alpha)
                    {
                        alpha = val;
                        bestMove = move;
                    }
                    if(alpha >= beta)
                    {
                        return beta;
                    }
                }
            }
        }
        idealMove = bestMove;
        return alpha;
	}
	public double minValue(BoardGame board, int level, double alpha, double beta, int opponents)
	{
	    Move bestMove = null;
        BoardGame game = board;

        if(game.gameOver() || level == 0)
        {
            return getLearnedValue();
        }
        else
        {
            for(Move move : game.getMoveList())
            {
                if(game.validMove(move.getID()))
                {
                    double val;
                    game.makeMove(move.getID());
                    if(opponents > 0)
                    {
                        val = this.minValue(game, level - 1, alpha, beta, opponents - 1);
                    }
                    else
                    {
                        val = this.maxValue(game, level - 1, alpha, beta, game.getPlayerList().length - 1);
                    }
                    game.undoMove(move.getID());
                    if(val < beta)
                    {
                        beta = val;
                        bestMove = move;
                    }
                    if(beta <= alpha)
                    {
                        return alpha;
                    }
                }
            }
        }
        idealMove = bestMove;
        return beta;
	}
	public void makeBestMove(BoardGame game, int level, double alpha, double beta, int opponents)
	{
		if(!game.gameOver())
		{
			if(rand.nextDouble() < epsilon)
			{
				int randomMove = rand.nextInt(game.getMoveList().length);
				while(!game.validMove(randomMove))
				{
					randomMove = rand.nextInt(game.getMoveList().length);
				}
				game.makeMove(randomMove);
			}
			else
			{
				maxValue(game, level, alpha, beta, opponents);
				idealMove.setPlayer(this);
				game.makeMove(idealMove.getID());
			}
		}
		updateFeatures(game);
		updateWeights(game);
	}
	public void updateFeatures(BoardGame game)
	{
		/*
		 * Feature 0 = % of boxes with 3 walls
		 * Feature 1 = % of boxes with 2 walls
		 * Feature 2 = % of boxes with 1 walls
		 */
	    
		double feat0 = 0;
		double feat1 = 0;
		double feat2 = 0;
		
		for(State s : game.getStateList())
		{
			if(s.getValue() == 3)
				feat0++;
			if(s.getValue() == 2)
				feat1++;
			if(s.getValue() == 1)
				feat2++;
		}
		
		feat0 = feat0 / game.getStateList().length;
		feat1 = feat1 / game.getStateList().length;
		feat2 = feat2 / game.getStateList().length;

		if(game.gameOver() && game.getWinner().equals(this))
			rewardEarned = true;
		else
			rewardEarned = false;

		featureList[0] = feat0;
		featureList[1] = feat1;
		featureList[2] = feat2;
	}
	public void updateWeights(BoardGame game)
	{
		for(int i =  0; i < featureList.length; i++)
		{
			double oldWeight = weightList[i];
			double newWeight;
			if(rewardEarned)
				newWeight = oldWeight + learningFactor * (reward - getLearnedValue()) * featureList[i];
			else
				newWeight = oldWeight + learningFactor * (getNextMax(game) - getLearnedValue()) * featureList[i];
			weightList[i] = newWeight;
			System.out.println();
		}
	}
	public void initializeWeights(File file) throws IOException
	{
	    if(file.length() > 0)
	    {
	        Scanner scan = new Scanner(file);
	        for(int i = 0; i < featureList.length; i++)
	        {
	            double weight = scan.nextDouble();
	            weightList[i] = weight;
	        }
	        scan.close();
	    }
	    else
	    {
	        for(int i = 0; i < featureList.length; i++)
	        {
	            weightList[i] = 1.0;
	        }
	        writeWeights(file);
	    }
	}
	public void writeWeights(File file) throws IOException
	{
	    FileWriter writer = new FileWriter(file);
        writer.write(getWeights());
        writer.close();
	}
	public String getWeights()
	{
		String result = "";
		for(int i =  0; i < weightList.length; i++)
		{
			result += (weightList[i] + " ");
		}
		return result;
	}
	public double getLearnedValue()
	{
		double value = 0;
		for(int i =  0; i < featureList.length; i++)
		{
			value += (featureList[i] * weightList[i]);
		}
		return value;
	}
	public double getNextMax(BoardGame game)
	{
	    double max = 0;
	    for(Move move : game.getMoveList())
	    {
	        if(game.validMove(move.getID()))
	        {
	            double value;
	            game.makeMove(move.getID());
	            updateFeatures(game);
	            value = getLearnedValue();
	            if(max < value)
	                max = value;
	            game.undoMove(move.getID());
	            updateFeatures(game);
	        }
	    }
	    return max;
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
