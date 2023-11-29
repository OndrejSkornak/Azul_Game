package sk.uniba.fmph.dcs;
import  java.util.*;
public class Factory extends TyleSource{
    private List<Tile> tiles;
    private final int capacity; // The number of tiles the factory can hold
    private boolean wasAlreadyTaken = false;
    private BagInterface bag;
    private TyleSource tableCenter;

    //  Create a factory with a given capacity.
    public Factory(int capacity, BagInterface bag) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0.");
        }
        this.capacity = capacity;
        this.tiles = new ArrayList<>(capacity);
        this.bag = bag;
    }

    //  Draw a number idx tiles from the factory.
    @Override
    public Tile[] take(int idx) {
        if (wasAlreadyTaken) {
            return new Tile[0]; // Return an empty array if the factory was already taken from this round
        } else {
            if (idx == Tile.STARTING_PLAYER.ordinal()) {
                return new Tile[0]; // Return an empty array if the STARTING_PLAYER tile was selected
            }
            wasAlreadyTaken = true;
            if (idx < 0 || idx >= Tile.values().length) {
                return new Tile[0]; // Return an empty array if the index is out of bounds
            }

            Tile selectedTile = Tile.values()[idx];
            List<Tile> takenTiles = new ArrayList<>();
            List<Tile> remainingTiles = new ArrayList<>();

            // Iterate through the tiles and move the selected tiles to the taken list
            for (Tile tile : tiles) {
                if (tile == selectedTile) {
                    takenTiles.add(tile);
                } else {
                    remainingTiles.add(tile);
                }
            }

            // Replace the current tiles with the remaining tiles
            tiles = remainingTiles;
            return takenTiles.toArray(new Tile[0]); // Return the taken tiles
        }
    }

    //  Add the remaining tiles to the table center.
    public void addTilesToCenter() {
        if (wasAlreadyTaken) {
            for (Tile tile : tiles) {
                tableCenter.add(List.of(tile).toArray(new Tile[0]));
            }
            tiles.clear();
        }
    }

    //  Return true if the factory is empty.
    @Override
    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    //  Return the number of tiles in the factory.
    @Override
    public void startNewRound() {
        tiles.clear();
        wasAlreadyTaken = false;

        this.fillFactory();
    }

    //  Return the number of tiles in the factory.
    @Override
    public String state() {
        StringBuilder stateBuilder = new StringBuilder("Factory state: ");
        for (Tile tile : tiles) {
            stateBuilder.append(tile.toString()).append(" ");
        }
        return stateBuilder.toString().trim();
    }

    //  Fill the factory with tiles from the bag.
    public void fillFactory() {
        while (tiles.size() < capacity && !bag.isEmpty()) {
            Collections.addAll(tiles, bag.take(1)); // Assume that we take one tile at a time
        }
    }

    //  Return the number of tiles in the factory.
    public List<Tile> getRemainingTiles() {
        if (wasAlreadyTaken) {
            return new ArrayList<>(tiles);
        } else {
            return new ArrayList<>(); // Return an empty list if the factory was not taken from this round
        }
    }

    //  Return the number of tiles in the factory.
    public List<Tile> getAvailableTiles() {
        if (wasAlreadyTaken) {
            return new ArrayList<>(); // Return an empty list if the factory was already taken from this round
        } else {
            return new ArrayList<>(tiles); // Return a copy of the tiles
        }
    }

    //  Return the number of tiles that was taken in the factory.
    public boolean wasAlreadyTaken() {
        return wasAlreadyTaken;
    }

    //  Return the number of tiles in the factory.
    public void setTableCenter(TyleSource table){
        this.tableCenter = table;
    }

    //  Return the number of tiles in the factory.
    @Override
    public void add(Tile[] newTiles){

    }
}
