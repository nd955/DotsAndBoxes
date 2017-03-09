import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Random;


public class DotsAndBoxes implements BoardGame
{
    final int NUM_ROWS;
    final int NUM_COLS;
    private Wall[] wallPositions;
    private Box[] boxList;
    private Player[] players;
    private char neutral;
    private Player currPlayer;
    
    public DotsAndBoxes(int row, int col, Player[] allPlayers)
    {
        NUM_ROWS = row;
        NUM_COLS = col;
        neutral = ' ';
        boxList = new Box[NUM_ROWS * NUM_COLS];
        wallPositions = new Wall[NUM_COLS * (NUM_ROWS + 1) + (NUM_COLS + 1) * NUM_ROWS];
        players = randomizeOrder(allPlayers);
        for(int i = 0; i < players.length; i++)
        {
            players[i].setOrder(i);
        }
        currPlayer = players[0];
        setup();
        
    }
    
    public void setup()
    {
        initializeBoxes();
        initializeWalls();
        linkWallsAndBoxes();
    }
    private void initializeBoxes()
    {
        int idBox = 0;
        
        //Initializes boxes and adds them to the list
        for(int i = 0; i < NUM_COLS; i++)
        {
            for(int j = 0; j < NUM_ROWS; j++)
            {
                Box box = new Box(idBox);
                boxList[(i * NUM_COLS) + j] = box;
                idBox++;
            }
        }
    }
    private void initializeWalls()
    {
        int idWall = 0;

        //Navigates through the board
        for(int i = 0; i < NUM_ROWS; i++)
        {
                //Initializes horizontal walls on the board
                for(int j = 0; j < NUM_COLS; j++)
                {
                    Wall wall = new Wall(idWall, this);
                    wall.setHorizontal();
                    wallPositions[(i * (2 * NUM_COLS + 1) + j)] = wall;
                    idWall++;
                }
                
                //Initializes vertical walls on the board
                for(int j = 0; j <= NUM_COLS; j++)
                {
                    Wall wall = new Wall(idWall, this);
                    wall.setVertical();
                    wallPositions[(i * (2 * NUM_COLS + 1) + j + NUM_COLS)] = wall;
                    idWall++;
                }
            }
        
        //Initializes bottom row of the board
        for(int i = 0; i < NUM_COLS; i++)
        {
            Wall wall = new Wall(idWall, this);
            wall.setHorizontal();
            wallPositions[(NUM_ROWS * (2 * NUM_COLS + 1) + i)] = wall;
            idWall++;
        }
    }
    private void linkWallsAndBoxes()
    {
        int id = 0;
        for(int i = 0; i < NUM_ROWS; i++)
        {
            for(int j = 0; j < NUM_COLS; j++)
            {
                boxList[id].setTop(wallPositions[(i * (2 * NUM_COLS + 1) + j)]);
                boxList[id].setLeft(wallPositions[(i * (2 * NUM_COLS + 1) + j + NUM_COLS)]);
                boxList[id].setRight(wallPositions[(i * (2 * NUM_COLS + 1) + j + NUM_COLS + 1)]);
                boxList[id].setBottom(wallPositions[(i * (2 * NUM_COLS + 1) + j + 2 * NUM_COLS + 1)]);
                id++;
            }
        }
    }
    public void startNewGame()
    {
        //players = randomizeOrder(players);
        resetGame();
        currPlayer = players[0];
    }
    public void resetGame()
    {
        for(Wall wall : wallPositions)
        {
            wall.setMovePlayed(false);
            wall.setPlayer(null);
        }
        for(Box box : boxList)
        {
            box.updateIsWon();
            box.setBoxWinner(null);
        }
        for(Player player : players)
        {
            player.resetScore();
        }
    }
    public Player[] randomizeOrder(Player[] unshuffledPlayers)
    {
        Random rand = new Random();
        Player[] shuffledPlayers = new Player[unshuffledPlayers.length];
        
        for(int i = 0; i < unshuffledPlayers.length; i++)
        {
            int randInt = rand.nextInt(unshuffledPlayers.length);
            while(shuffledPlayers[randInt] != null)
            {
                randInt = rand.nextInt(unshuffledPlayers.length);
            }
            shuffledPlayers[randInt] = unshuffledPlayers[i];
        }
        
        return shuffledPlayers;
    }
    public boolean makeMove(int position)
    {
        if(this.validMove(position))
        {
            //Makes move
            Wall wall = wallPositions[position];
            wall.setPlayer(currPlayer);
            wall.setMovePlayed(true);

            //Checks if any boxes were won with that move
            for(Box box : boxList)
            {
                if(!box.isWon())
                {
                    box.updateIsWon();
                    if(box.isWon())
                    {
                        box.setBoxWinner(currPlayer);
                        currPlayer.incrementScore();
                    }
                }
            }

            //Changes the player's move
            currPlayer = currPlayer.getNextPlayer(this);

            return wall.movePlayed();
        }
        else
        {
            //Returns false if move is invalid
            return false;
        }
    }
    public void undoMove(int position)
    {
        //Undo the move
        Wall wall = wallPositions[position];
        wall.setMovePlayed(false);
        
        //Changes the player's move
        currPlayer = currPlayer.getLastPlayer(this);
        
        //Checks if any boxes were lost with that move
        for(Box box : boxList)
        {
            if(box.isWon())
            {
                box.updateIsWon();
                if(!box.isWon())
                {
                    currPlayer.decrementScore();
                    box.setColor(neutral);
                }
            }
        }
    }
    public Player whoseMove()
    {
        return currPlayer;
    }
    public boolean gameOver()
    {
        boolean result = true;
        for(Wall wall : wallPositions)
        {
            if(!wall.movePlayed())
            {
                result = false;
            }
        }
        
        return result;
    }
    public Player getWinner()
    {
        int max = 0;
        Player winner = null;
        for(Player player : players)
        {
            if(player.getScore() > max)
            {
                max = player.getScore();
                winner = player;
            }
        }
        
        return winner;
    }
    public double getBoardValue(Player player)
    {
        double playerScore = player.getScore();
        double opponentScore = 0;
        for(Player eachPlayer : players)
        {
            if(eachPlayer != player)
            {
                opponentScore += eachPlayer.getScore();
            }
        }
        return (playerScore - opponentScore);
    }
    public int getNumRows()
    {
        return NUM_ROWS;
    }
    public int getNumCols()
    {
        return NUM_COLS;
    }
    public boolean validMove(int position)
    {
        boolean valid = true;
        if(position >= wallPositions.length || position < 0)
        {
            valid = false;
        }
        else if(wallPositions[position].movePlayed())
        {
            valid = false;
        }
        return valid;
    }
    public Player[] getPlayerList()
    {
        return players;
    }
    public Move[] getMoveList()
    {
        return wallPositions;
    }
    public State[] getStateList()
    {
        return boxList;
    }
    public String toString()
    {
        String result = "";
        int id = 0;
        
        //Runs through the array of wall positions
        for(int i = 0; i < NUM_COLS; i++)
        {
            
            //Horizontal row
            for(int j = 0; j < NUM_COLS; j++)
            {
                result += "." + "\t";
                result += wallPositions[id] + "\t";
                id++;
            }
            
            //Extra formatting
            result += ".";
            result += "\r";
            
            //Vertical row
            for(int j = 0; j <= NUM_COLS; j++)
            {
                boolean isBox = false;
                Box copyBox = new Box(0);
                for(Box box : boxList)
                {
                    if(box.isWon() && box.getLeft() == wallPositions[id]){
                        isBox = true;
                        copyBox = box;
                    }
                }
                if(isBox)
                {
                    result += (wallPositions[id] + "\t" + Character.toString(copyBox.getColor()) + "\t");
                }
                else
                {
                    result += (wallPositions[id] + "\t" + "\t");
                }
                id++;
            }
            result += "\r";
        }
        
        //Final horizontal row
        for(int i = 0; i < NUM_COLS; i++)
        {
            result += "." + "\t";
            result += wallPositions[id] + "\t";
            id++;
        }
        result += ".";
        
        //List the scores
        result += "\r";
        
        for(Player player : players)
        {
            result += "Player " + player + "'s score is " + player.getScore() + ".\r";
        }
        
        return result;
    }
}
