package sk.uniba.fmph.dcs;

import java.util.*;

public class Game implements GameInterface{
    private int currentPlayerId;
    private int startingPlayerId;
    private boolean isGameOver;
    private ArrayList<BoardInterface> boards;
    private TableAreaInterface table;
    private Bag bag;
    private GameObserverInterface gameObserver;

    //  Create a game with a given number of players.
    public Game(ArrayList<BoardInterface> boards, TableAreaInterface table, Bag bag, GameObserverInterface gameObserver) {
        this.currentPlayerId = 0;
        this.startingPlayerId = 0;
        this.isGameOver = false;
        this.boards = boards;
        this.table = table;
        this.bag = bag;
        this.gameObserver = gameObserver;
    }

    //  Move a number of tiles from the factory, tableCenter to a player's board.
    @Override
    public boolean take(int playerId, int sourceId, int idx, int destinationIdx) {
        if (isGameOver) return false;
        if (playerId != currentPlayerId) return false;
        if (idx < 1 || idx > 5) return false;
        if (destinationIdx < 0 || destinationIdx > 4) return false;
        Tile[] tiles = table.take(sourceId, idx);
        if (tiles == null) return false;
        boards.get(playerId).put(destinationIdx, tiles);
        currentPlayerId = (currentPlayerId + 1) % boards.size();

        if (table.isRoundEnd()){
            for (BoardInterface board : boards) {
                if (board.finishRound() == FinishRoundResult.GAME_FINISHED) {
                    isGameOver = true;
                    gameObserver.notifyEverybody("Game finished");
                    return true;
                }
            }

            if (isGameOver){
                for (BoardInterface board : boards) {
                    board.endGame();
                }
            }else {
                table.startNewRound();
            }
        }
        return true;
    }


    public void notifyAllObservers() {
        gameObserver.notifyEverybody(getState());
    }


    public void registerObserver(GameObserverInterface observer) {
        this.gameObserver = observer;
    }

    public void cancelObserver(GameObserverInterface observer) {
        this.gameObserver = null;
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public boolean getisGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public GameObserverInterface getGameObserver() {
        return gameObserver;
    }

    public String getState() {
        StringBuilder stateBuilder = new StringBuilder();
        stateBuilder.append("Table area:\n");
        stateBuilder.append(table.state());
        stateBuilder.append("\n");
        stateBuilder.append("Players:\n");
        for (BoardInterface board : boards) {
            stateBuilder.append(board.state());
            stateBuilder.append("\n");
        }
        return stateBuilder.toString();
    }
}
