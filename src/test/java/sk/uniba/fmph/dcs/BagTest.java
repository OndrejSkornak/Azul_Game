package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BagTest {
    private BagInterface bag;
    private final int totalTilesCount = 100; // Adjust based on the actual setup for the game Azul

    @BeforeEach
    public void setUp() {
        bag = new Bag();
    }

    @Test
    public void testBagInitialization() {
        // Assume we expect an equal number of each Tile type initially.
        String state = bag.state();
        for (Tile tileType : Tile.values()) {
            assertTrue(state.contains(tileType.toString()));
        }
    }

    @Test
    public void testTakeNormalCase() {
        int numberOfTilesToTake = 5;
        List<Tile> tiles = Arrays.asList(bag.take(numberOfTilesToTake));
        assertEquals(numberOfTilesToTake, tiles.size());
    }

    @Test
    public void testTakeEmptyBag() {
        List<Tile> takenTiles = Arrays.asList(bag.take(totalTilesCount)); // Take all the tiles
        assertEquals(totalTilesCount, takenTiles.size()); // Check if the number of taken tiles is correct
        assertTrue(bag.isEmpty()); // Then check if the bag is empty

        List<Tile> tilesAfterEmptying = Arrays.asList(bag.take(1)); // Try taking more tiles
        assertTrue(tilesAfterEmptying.isEmpty()); // This should be empty
    }

    @Test
    public void testTakeMoreTilesThanAvailable() {
        List<Tile> tiles = Arrays.asList(bag.take(totalTilesCount + 1)); // Attempt to take more than the total number of tiles
        assertEquals(totalTilesCount, tiles.size()); // Only the total number should be returned
    }

    @Test
    public void testIsEmpty() {
        assertFalse(bag.isEmpty());
        bag.take(totalTilesCount); // This should empty the bag
        assertTrue(bag.isEmpty());
    }

    @Test
    public void testFillMethod() {
        // Empty the bag first
        List<Tile> allTiles = IntStream.range(0, totalTilesCount)
                .mapToObj(i -> Arrays.asList(bag.take(1)))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        assertTrue(bag.isEmpty());

        // Fill the bag with the tiles we just took
        bag.fill(allTiles);
        assertFalse(bag.isEmpty());

        // Check if the state contains the correct count for each type
        String state = bag.state();
        for (Tile tileType : Tile.values()) {
            assertTrue(state.contains(tileType.toString()));
        }
    }

    @Test
    public void testTakeNegativeNumberThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bag.take(-1);
        });
        String expectedMessage = "Cannot take a negative number of tiles";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
