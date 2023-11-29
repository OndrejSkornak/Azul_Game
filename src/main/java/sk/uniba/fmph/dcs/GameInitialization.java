package sk.uniba.fmph.dcs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameInitialization {
    private static int colorCount = Tile.values().length - 1; // The number of colors in the game, excluding the STARTING_PLAYER tile
    private static int tileCount = 20; // The number of tiles of each color in the game
    private static UsedTiles usedTiles = new UsedTiles();

    // Create a board.
    public static BoardInterface createWholeBoard() {
        BoardInterface board;
        ArrayList<Points> pointsPattern = new ArrayList<>();
        Collections.addAll(pointsPattern,
                new Points(1), new Points(1),
                new Points(2), new Points(2), new Points(2),
                new Points(3), new Points(3), new Points(3), new Points(3));

        Floor floor = new Floor(usedTiles, pointsPattern);

        // Create the wall lines
        WallLine[] wallLines = new WallLine[colorCount];
        for (int i = 0; i < colorCount; i++) {
            Tile[] tiles = new Tile[colorCount];
            for (int j = 0; j < colorCount; j++) {
                tiles[j] = Tile.values()[(i + j) % colorCount + 1];
            }
            wallLines[i] = new WallLine(tiles, null, null);
        }

        // Link the wall lines together
        for (int i = 0; i < colorCount; i++) {
            wallLines[i].setUp(wallLines[(i + 1) % colorCount]);
            wallLines[i].setDown(wallLines[(i + colorCount - 1) % colorCount]);
        }

        // Create the pattern lines
        PatternLine[] patternLines = new PatternLine[colorCount];
        for (int i = 0; i < colorCount; i++) {
            patternLines[i] = new PatternLine(i+1, usedTiles, wallLines[i], floor);
        }
        board = new Board(patternLines, wallLines, floor);
        return board;
    }

    // Create a game with a given number of players.
    public static TableAreaInterface createTableArea(int numberOfPlayers, boolean FixSetUp){
        int factoryCount = numberOfPlayers*2 + 1;

        TableCenter tableCenter = new TableCenter();
        Bag bag = new Bag();
        if (FixSetUp) {
            bag.fillWithoutShuffle(List.of( new Tile[]
                    {Tile.BLACK, Tile.BLACK, Tile.YELLOW, Tile.RED,
                    Tile.RED, Tile.RED, Tile.RED, Tile.RED,
                    Tile.BLUE, Tile.BLUE, Tile.YELLOW, Tile.RED,
                    Tile.RED, Tile.RED, Tile.RED, Tile.RED,
                    Tile.YELLOW, Tile.YELLOW, Tile.YELLOW, Tile.YELLOW,
                    Tile.BLACK, Tile.BLACK, Tile.BLACK, Tile.BLACK,
                    Tile.BLUE, Tile.BLUE, Tile.GREEN, Tile.BLUE,
                    Tile.RED, Tile.RED, Tile.RED, Tile.RED,
                    Tile.YELLOW, Tile.YELLOW, Tile.YELLOW, Tile.YELLOW,
                    Tile.BLACK, Tile.BLACK, Tile.BLACK, Tile.BLACK,
                    }));
        }

        ArrayList<TyleSource> tileSources = new ArrayList<>();
        tileSources.add(tableCenter);

        for(int i = 0; i < factoryCount; i++){
            tileSources.add(new Factory(4, bag));
        }

        for (TyleSource t : tileSources) {
            t.startNewRound();
        }

        return new TableArea(tileSources);
    }

    // Create a game with a given number of players and FullBag.
    public static ArrayList<Object> createTableAreaFullBag(int numberOfPlayers, boolean FixSetUp){
        int factoryCount = numberOfPlayers*2 + 1;
        Bag bagFullBag = new Bag();
        bagFullBag.take(100);
        TableCenter tableCenter = new TableCenter();
        if (FixSetUp) {
            ArrayList<Tile> bagTiles = new ArrayList<>();
            Tile[] order = {Tile.RED, Tile.GREEN, Tile.YELLOW, Tile.BLUE, Tile.BLACK};
            for(int i = 0; i < 20; i++){
                bagTiles.addAll(Arrays.asList(order));
            }
            bagFullBag.fillWithoutShuffle(bagTiles);
        }

        ArrayList<TyleSource> tileSources = new ArrayList<>();
        tileSources.add(tableCenter);

        for(int i = 0; i < factoryCount; i++){
            Factory f = new Factory(4, bagFullBag);
            f.setTableCenter(tableCenter);
            tileSources.add(f);
        }

        for (TyleSource t : tileSources) {
            t.startNewRound();
        }
        ArrayList<Object> result = new ArrayList<>();
        result.add(new TableArea(tileSources));
        result.add(bagFullBag);
        return result;
    }
}
