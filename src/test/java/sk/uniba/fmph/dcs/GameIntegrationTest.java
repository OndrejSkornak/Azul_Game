package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameIntegrationTest {
    @Test
    @DisplayName("Game should be played correctly")
    void testWholeGameFromBeginingToEnd() {
        //Setup
        ArrayList<BoardInterface> boards = new ArrayList<>();
        boards.add(GameInitialization.createWholeBoard());
        boards.add(GameInitialization.createWholeBoard());

        for (BoardInterface board : boards) {
            assertEquals("Points [value=0]\n" +
                    "P1: Capacity: 1 | Current state: 0 | Color: EMPTY\n" +
                    "W1: -----\n" +
                    "P2: Capacity: 2 | Current state: 0 | Color: EMPTY\n" +
                    "W2: -----\n" +
                    "P3: Capacity: 3 | Current state: 0 | Color: EMPTY\n" +
                    "W3: -----\n" +
                    "P4: Capacity: 4 | Current state: 0 | Color: EMPTY\n" +
                    "W4: -----\n" +
                    "P5: Capacity: 5 | Current state: 0 | Color: EMPTY\n" +
                    "W5: -----\n" +
                    "Floor -> ", board.state());
        }

        ArrayList<Object> tableAndBag = GameInitialization.createTableAreaFullBag(2, true);

        TableAreaInterface table = (TableAreaInterface) tableAndBag.get(0);
        Bag bag = (Bag) tableAndBag.get(1);
        assertEquals("Bag contains 80 tiles: \n" +
                "S: 0\n" +
                "R: 16\n" +
                "G: 16\n" +
                "I: 16\n" +
                "B: 16\n" +
                "L: 16\n", bag.state());

        assertEquals("TableCenter state: S\n" +
            "Factory state: L B I G\n" +
            "Factory state: R L B I\n" +
            "Factory state: G R L B\n" +
            "Factory state: I G R L\n" +
            "Factory state: B I G R\n", table.state());

        GameObserverInterface gameObserver = new GameObserver();
        //GAME START
        Game game = new Game(boards, table, bag, gameObserver);
        assertEquals(0, game.getCurrentPlayerId());
        game.cancelObserver(gameObserver);
        assertNull(game.getGameObserver());
        game.registerObserver(gameObserver);
        assertNotNull(game.getGameObserver());
        assertEquals(game.getGameObserver(), gameObserver);

        //Test
        assertTrue(game.take(0, 1, 2, 0)); table.addTilesToCenter(); //BLUE
        assertTrue(game.take(1, 2, 1, 0)); table.addTilesToCenter(); //RED
        assertTrue(game.take(0, 4, 3, 1)); table.addTilesToCenter(); //YELLOW
        assertTrue(game.take(1, 3, 5, 1)); table.addTilesToCenter(); //GREEN
        assertTrue(game.take(0, 5, 4, 2)); table.addTilesToCenter(); //BLACK

        assertEquals("Table area:\n" +
        "TableCenter state: SLBILBIGRLGRBIGR\n" +
        "Factory state:\n" +
        "Factory state:\n" +
        "Factory state:\n" +
        "Factory state:\n" +
        "Factory state:\n" +
        "\n" +
        "Players:\n" +
        "Points [value=0]\n" +
        "P1: Capacity: 0 | Current state: 1 | Color: G\n" +
        "W1: -----\n" +
        "P2: Capacity: 1 | Current state: 1 | Color: I\n" +
        "W2: -----\n" +
        "P3: Capacity: 2 | Current state: 1 | Color: B\n" +
        "W3: -----\n" +
        "P4: Capacity: 4 | Current state: 0 | Color: EMPTY\n" +
        "W4: -----\n" +
        "P5: Capacity: 5 | Current state: 0 | Color: EMPTY\n" +
        "W5: -----\n" +
        "Floor -> \n" +
        "Points [value=0]\n" +
        "P1: Capacity: 0 | Current state: 1 | Color: R\n" +
        "W1: -----\n" +
        "P2: Capacity: 1 | Current state: 1 | Color: L\n" +
        "W2: -----\n" +
        "P3: Capacity: 3 | Current state: 0 | Color: EMPTY\n" +
        "W3: -----\n" +
        "P4: Capacity: 4 | Current state: 0 | Color: EMPTY\n" +
        "W4: -----\n" +
        "P5: Capacity: 5 | Current state: 0 | Color: EMPTY\n" +
        "W5: -----\n" +
        "Floor -> \n", game.getState());

        assertTrue(game.take(1, 0, 4, 2)); //BLUE
        assertTrue(game.take(0, 0, 3, 1)); //YELLOW
        assertTrue(game.take(1, 0, 1, 3)); //RED
        assertTrue(game.take(0, 0, 2, 4)); //GREEN
        assertTrue(game.take(1, 0, 5, 4)); //BLACK

        assertEquals("Table area:\n" +
                "TableCenter state: S\n" +
                "Factory state: L B I G\n" +
                "Factory state: R L B I\n" +
                "Factory state: G R L B\n" +
                "Factory state: I G R L\n" +
                "Factory state: B I G R\n" +
                "\n" +
                "Players:\n" +
                "Points [value=1]\n" +
                "P1: Capacity: 1 | Current state: 0 | Color: EMPTY\n" +
                "W1: -G---\n" +
                "P2: Capacity: 2 | Current state: 0 | Color: EMPTY\n" +
                "W2: -I---\n" +
                "P3: Capacity: 2 | Current state: 1 | Color: B\n" +
                "W3: -----\n" +
                "P4: Capacity: 4 | Current state: 0 | Color: EMPTY\n" +
                "W4: -----\n" +
                "P5: Capacity: 2 | Current state: 3 | Color: G\n" +
                "W5: -----\n" +
                "Floor -> \n" +
                "Points [value=2]\n" +
                "P1: Capacity: 1 | Current state: 0 | Color: EMPTY\n" +
                "W1: R----\n" +
                "P2: Capacity: 1 | Current state: 1 | Color: L\n" +
                "W2: -----\n" +
                "P3: Capacity: 3 | Current state: 0 | Color: EMPTY\n" +
                "W3: -B---\n" +
                "P4: Capacity: 1 | Current state: 3 | Color: R\n" +
                "W4: -----\n" +
                "P5: Capacity: 2 | Current state: 3 | Color: L\n" +
                "W5: -----\n" +
                "Floor -> \n", game.getState());
        //Round 2
        assertTrue(game.take(0, 1, 5, 0)); table.addTilesToCenter(); //BLACK
        assertTrue(game.take(1, 2, 5, 0)); table.addTilesToCenter(); //BLACK
        assertTrue(game.take(0, 3, 3, 1)); table.addTilesToCenter(); //YELLOW
        assertTrue(game.take(1, 4, 5, 1)); table.addTilesToCenter(); //GREEN
        assertTrue(game.take(0, 5, 4, 4)); table.addTilesToCenter(); //BLACK

        assertTrue(game.take(1, 0, 4, 2));
        assertTrue(game.take(0, 0, 1, 1));
        assertTrue(game.take(1, 0, 3, 3));
        assertTrue(game.take(0, 0, 2, 4));
        assertTrue(game.take(1, 0, 5, 4));
        //Round 3
        assertTrue(game.take(0, 1, 3, 0)); table.addTilesToCenter(); //BLACK
        assertTrue(game.take(1, 2, 3, 0)); table.addTilesToCenter(); //BLACK
        assertTrue(game.take(0, 3, 1, 1)); table.addTilesToCenter(); //YELLOW
        assertTrue(game.take(1, 4, 1, 1)); table.addTilesToCenter(); //GREEN
        assertTrue(game.take(0, 5, 4, 4)); table.addTilesToCenter(); //BLACK

        assertTrue(game.take(1, 0, 4, 2));
        assertTrue(game.take(0, 0, 1, 1));
        assertTrue(game.take(1, 0, 3, 3));
        assertTrue(game.take(0, 0, 2, 4));
        assertTrue(game.take(1, 0, 5, 4));
        //Round 4
        assertTrue(game.take(0, 1, 4, 0)); table.addTilesToCenter(); //BLACK
        assertTrue(game.take(1, 2, 4, 0)); table.addTilesToCenter(); //BLACK
        assertTrue(game.take(0, 3, 3, 1)); table.addTilesToCenter(); //YELLOW
        assertTrue(game.take(1, 4, 5, 1)); table.addTilesToCenter(); //GREEN
        assertTrue(game.take(0, 5, 4, 4)); table.addTilesToCenter(); //BLACK

        assertTrue(game.take(1, 0, 4, 2));
        assertTrue(game.take(0, 0, 1, 1));
        assertTrue(game.take(1, 0, 3, 3));
        assertTrue(game.take(0, 0, 2, 4));
        assertTrue(game.take(1, 0, 5, 4));

        //Round 5
        assertTrue(game.take(0, 1, 5, 1)); table.addTilesToCenter(); //BLACK
        assertTrue(game.take(1, 2, 5, 1)); table.addTilesToCenter(); //BLACK
        assertTrue(game.take(0, 3, 3, 1)); table.addTilesToCenter(); //YELLOW
        assertTrue(game.take(1, 4, 5, 1)); table.addTilesToCenter(); //GREEN
        assertTrue(game.take(0, 5, 4, 4)); table.addTilesToCenter(); //BLACK

        assertTrue(game.take(1, 0, 2, 0));
        assertTrue(game.take(0, 0, 1, 0));
        assertTrue(game.take(1, 0, 3, 3));
        assertTrue(game.take(0, 0, 4, 4));
        assertTrue(game.take(1, 0, 5, 4));

        System.out.println(game.getState());
        assertEquals("Table area:\n" +
                "TableCenter state: \n" +
                "Factory state:\n" +
                "Factory state:\n" +
                "Factory state:\n" +
                "Factory state:\n" +
                "Factory state:\n" +
                "\n" +
                "Players:\n" +
                "Points [value=-24]\n" +
                "P1: Capacity: 1 | Current state: 0 | Color: EMPTY\n" +
                "W1: RGIBL\n" +
                "P2: Capacity: 1 | Current state: 1 | Color: L\n" +
                "W2: -I--R\n" +
                "P3: Capacity: 2 | Current state: 1 | Color: B\n" +
                "W3: -----\n" +
                "P4: Capacity: 4 | Current state: 0 | Color: EMPTY\n" +
                "W4: -----\n" +
                "P5: Capacity: 5 | Current state: 0 | Color: EMPTY\n" +
                "W5: --G-B\n" +
                "Floor -> \n" +
                "Points [value=-39]\n" +
                "P1: Capacity: 0 | Current state: 1 | Color: G\n" +
                "W1: R-IBL\n" +
                "P2: Capacity: 1 | Current state: 1 | Color: R\n" +
                "W2: ---L-\n" +
                "P3: Capacity: 3 | Current state: 0 | Color: EMPTY\n" +
                "W3: -B---\n" +
                "P4: Capacity: 1 | Current state: 3 | Color: R\n" +
                "W4: -----\n" +
                "P5: Capacity: 5 | Current state: 0 | Color: EMPTY\n" +
                "W5: L----\n" +
                "Floor -> LLGGGIIIIL\n", game.getState());

        //Check if end of game
        assertEquals(FinishRoundResult.GAME_FINISHED, boards.get(0).finishRound());
        assertEquals(FinishRoundResult.GAME_FINISHED, boards.get(1).finishRound());
    }

}
