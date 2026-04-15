package comp20050.qssboard;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import java.util.*;

public class BotPlayer {
    private static final int INF = QuaxController.MAX_DISTANCE;

    public Polygon chooseMove(List<Polygon> cells, Color botColour, QuaxController controller) {
        Color humanColour = (botColour == Color.BLACK) ? Color.WHITE : Color.BLACK;

        // Run Dijkstra for both players from both sides
        // Fwd: distance from start edge to tile
        // Bwd: distance from tile to goal edge
        int[][] botFwd = controller.runDijkstra(botColour, true);
        int[][] botBwd = controller.runDijkstra(botColour, false);
        int[][] humanFwd = controller.runDijkstra(humanColour, true);
        int[][] humanBwd = controller.runDijkstra(humanColour, false);

        // Calculate overall shortest path for each player
        int botMovesToWin = INF;
        int humanMovesToWin = INF;

        for (int i = 0; i < controller.BOARD_SIZE; i++) {
            // Bot's goal edge
            int br = (botColour == Color.BLACK) ? controller.MAX_COORD : i * 2;
            int bc = (botColour == Color.BLACK) ? i * 2 : controller.MAX_COORD;
            botMovesToWin = Math.min(botMovesToWin, botFwd[br][bc]);

            // Human's goal edge
            int hr = (humanColour == Color.BLACK) ? 20 : i * 2;
            int hc = (humanColour == Color.BLACK) ? i * 2 : 20;
            humanMovesToWin = Math.min(humanMovesToWin, humanFwd[hr][hc]);
        }

        // Print shortest path
        if (botMovesToWin >= INF) {
            System.out.println("Bot shortest path: BLOCKED");
        } else {
            System.out.println("Bot shortest path: " + botMovesToWin);
        }

        // Evaluate all available moves
        List<Polygon> bestTiles = new ArrayList<>();
        long bestScore = Long.MAX_VALUE;

        for (Polygon cell : cells) {
            // Skip already occupied tiles
            if (controller.cellIsOccupied(cell)) continue;

            int[] coords = controller.getCoordinateFromId(cell.getId());
            int r = coords[0];
            int c = coords[1];

            // Costs for specific tiles
            int bf = botFwd[r][c];   // Bot distance from start to here
            int bb = botBwd[r][c];   // Bot distance from here to goal
            int hf = humanFwd[r][c]; // Human distance from start to here
            int hb = humanBwd[r][c]; // Human distance from here to goal

            // Tile is useful if it's on a valid path between edges
            boolean botUseful = (bf < INF && bb < INF);
            boolean humanUseful = (hf < INF && hb < INF);
            if (!botUseful && !humanUseful) {
                continue;
            }

            long score;

            // Calculate how many moves are needed to complete a path through this tile
            // Lower scores are better, this prioritizes blocking when human is close to winning
            if (botUseful && humanUseful) {
                int botPathCost = bf + bb - 1;
                int humanPathCost = hf + hb - 1;
                score = (long) humanMovesToWin * humanPathCost + (long) botMovesToWin * botPathCost;
            } else if (botUseful) {
                // Only bot can use this tile
                score = (long) botMovesToWin * (bf + bb - 1);
            } else {
                // Only human can use this tile (defensive blocking move)
                score = (long) humanMovesToWin * (hf + hb - 1);
            }

            // Update list of best moves
            if (score < bestScore) {
                bestScore = score;
                bestTiles.clear();
                bestTiles.add(cell);
            } else if (score == bestScore) {
                bestTiles.add(cell);
            }
        }

        if (bestTiles.isEmpty()) {
            System.out.println("Strategy found no useful moves. Picking random move.");
            for (Polygon cell : cells) {
                if (!controller.cellIsOccupied(cell)) {
                    bestTiles.add(cell);
                }
            }
        }

        // Pick randomly among best tiles to avoid predictability
        return bestTiles.get(new Random().nextInt(bestTiles.size()));
    }
}