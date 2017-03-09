public class AlphaBeta implements Player
{
	private Move idealMove;
	private char color;
	private int score;
	private int order;
	
	public AlphaBeta(char playerColor)
	{
		color = playerColor;
		score = 0;
	}

	public double maxValue(BoardGame board, int level, double alpha, double beta, int opponents)
	{
	    Move bestMove = null;
	    BoardGame game = board;

	    if(game.gameOver() || level == 0)
	    {
	        return game.getBoardValue(this);
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
	        return game.getBoardValue(this);
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
			maxValue(game, level, alpha, beta, opponents);
			idealMove.setPlayer(this);
			game.makeMove(idealMove.getID());
		}
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
