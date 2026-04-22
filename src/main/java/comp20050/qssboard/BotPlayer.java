package comp20050.qssboard;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class BotPlayer {
    private static final int INF = QuaxController.MAX_DISTANCE;

    private final Random random = new Random();


    public static final class CellEvaluation {
        private final Polygon cell;
        private final int botForward;
        private final int botBackward;
        private final int humanForward;
        private final int humanBackward;
        private final boolean botUseful;
        private final boolean humanUseful;
        private final long score;

        private CellEvaluation(
                Polygon cell,
                int botForward,
                int botBackward,
                int humanForward,
                int humanBackward,
                boolean botUseful,
                boolean humanUseful,
                long score
        ) {
            this.cell = cell;
            this.botForward = botForward;
            this.botBackward = botBackward;
            this.humanForward = humanForward;
            this.humanBackward = humanBackward;
            this.botUseful = botUseful;
            this.humanUseful = humanUseful;
            this.score = score;
        }

        public Polygon getCell() {
            return cell;
        }

        public int getBotForward() {
            return botForward;
        }

        public int getBotBackward() {
            return botBackward;
        }

        public int getHumanForward() {
            return humanForward;
        }

        public int getHumanBackward() {
            return humanBackward;
        }

        public boolean isBotUseful() {
            return botUseful;
        }

        public boolean isHumanUseful() {
            return humanUseful;
        }

        public long getScore() {
            return score;
        }
    }

    public static final class StrategyAnalysis {
        private final Color botColour;
        private final Color humanColour;
        private final int botMovesToWin;
        private final int humanMovesToWin;
        private final List<CellEvaluation> cellEvaluations;
        private final Polygon selectedMove;
        private final List<String> botPath;
        private final List<String> humanPath;
        private final boolean fallbackMove;

        private StrategyAnalysis(
                Color botColour,
                Color humanColour,
                int botMovesToWin,
                int humanMovesToWin,
                List<CellEvaluation> cellEvaluations,
                Polygon selectedMove,
                List<String> botPath,
                List<String> humanPath,
                boolean fallbackMove
        ) {
            this.botColour = botColour;
            this.humanColour = humanColour;
            this.botMovesToWin = botMovesToWin;
            this.humanMovesToWin = humanMovesToWin;
            this.cellEvaluations = List.copyOf(cellEvaluations);
            this.selectedMove = selectedMove;
            this.botPath = List.copyOf(botPath);
            this.humanPath = List.copyOf(humanPath);
            this.fallbackMove = fallbackMove;
        }

        public Color getBotColour() {
            return botColour;
        }

        public Color getHumanColour() {
            return humanColour;
        }

        public int getBotMovesToWin() {
            return botMovesToWin;
        }

        public int getHumanMovesToWin() {
            return humanMovesToWin;
        }

        public List<CellEvaluation> getCellEvaluations() {
            return cellEvaluations;
        }

        public Polygon getSelectedMove() {
            return selectedMove;
        }

        public List<String> getBotPath() {
            return botPath;
        }

        public List<String> getHumanPath() {
            return humanPath;
        }

        public boolean isFallbackMove() {
            return fallbackMove;
        }

        public CellEvaluation getSelectedEvaluation() {
            if (selectedMove == null) {
                return null;
            }

            String selectedId = selectedMove.getId();
            for (CellEvaluation evaluation : cellEvaluations) {
                if (evaluation.getCell().getId().equals(selectedId)) {
                    return evaluation;
                }
            }
            return null;
        }
    }

    private static final class CandidateScore {
        private final Polygon cell;
        private final int botForward;
        private final int botBackward;
        private final int humanForward;
        private final int humanBackward;
        private final boolean botUseful;
        private final boolean humanUseful;
        private final long score;

        private CandidateScore(
                Polygon cell,
                int botForward,
                int botBackward,
                int humanForward,
                int humanBackward,
                boolean botUseful,
                boolean humanUseful,
                long score
        ) {
            this.cell = cell;
            this.botForward = botForward;
            this.botBackward = botBackward;
            this.humanForward = humanForward;
            this.humanBackward = humanBackward;
            this.botUseful = botUseful;
            this.humanUseful = humanUseful;
            this.score = score;
        }
    }

    public Polygon chooseMove(List<Polygon> cells, Color botColour, QuaxController controller) {
        StrategyAnalysis analysis = analyseMove(cells, botColour, controller);
        return analysis == null ? null : analysis.getSelectedMove();
    }

    public StrategyAnalysis analyseMove(List<Polygon> cells, Color botColour, QuaxController controller) {
        Color humanColour = (botColour == Color.BLACK) ? Color.WHITE : Color.BLACK;

        QuaxController.PathSearchResult botForwardSearch = controller.runDijkstraSearch(botColour, true);
        QuaxController.PathSearchResult botBackwardSearch = controller.runDijkstraSearch(botColour, false);
        QuaxController.PathSearchResult humanForwardSearch = controller.runDijkstraSearch(humanColour, true);
        QuaxController.PathSearchResult humanBackwardSearch = controller.runDijkstraSearch(humanColour, false);

        int[][] botForward = botForwardSearch.dist;
        int[][] botBackward = botBackwardSearch.dist;
        int[][] humanForward = humanForwardSearch.dist;
        int[][] humanBackward = humanBackwardSearch.dist;

        int botMovesToWin = findShortestWinDistance(botColour, botForward, controller);
        int humanMovesToWin = findShortestWinDistance(humanColour, humanForward, controller);

        List<CandidateScore> candidates = new ArrayList<>();
        long bestScore = Long.MAX_VALUE;

        for (Polygon cell : cells) {
            if (controller.cellIsOccupied(cell)) {
                continue;
            }

            int[] coordinates = controller.getCoordinateFromId(cell.getId());
            int row = coordinates[0];
            int col = coordinates[1];

            int botForwardCost = botForward[row][col];
            int botBackwardCost = botBackward[row][col];
            int humanForwardCost = humanForward[row][col];
            int humanBackwardCost = humanBackward[row][col];

            boolean botUseful = botForwardCost < INF && botBackwardCost < INF;
            boolean humanUseful = humanForwardCost < INF && humanBackwardCost < INF;
            if (!botUseful && !humanUseful) {
                continue;
            }

            long score;
            if (botUseful && humanUseful) {
                int botPathCost = botForwardCost + botBackwardCost - 1;
                int humanPathCost = humanForwardCost + humanBackwardCost - 1;
                score = (long) humanMovesToWin * humanPathCost
                        + (long) botMovesToWin * botPathCost;
            } else if (botUseful) {
                score = (long) botMovesToWin * (botForwardCost + botBackwardCost - 1);
            } else {
                score = (long) humanMovesToWin * (humanForwardCost + humanBackwardCost - 1);
            }

            candidates.add(new CandidateScore(
                    cell,
                    botForwardCost,
                    botBackwardCost,
                    humanForwardCost,
                    humanBackwardCost,
                    botUseful,
                    humanUseful,
                    score
            ));
            bestScore = Math.min(bestScore, score);
        }

        if (candidates.isEmpty()) {
            List<Polygon> fallbackMoves = new ArrayList<>();
            for (Polygon cell : cells) {
                if (!controller.cellIsOccupied(cell)) {
                    fallbackMoves.add(cell);
                }
            }

            Polygon selectedMove = fallbackMoves.isEmpty()
                    ? null
                    : fallbackMoves.get(random.nextInt(fallbackMoves.size()));

            return new StrategyAnalysis(
                    botColour,
                    humanColour,
                    botMovesToWin,
                    humanMovesToWin,
                    Collections.emptyList(),
                    selectedMove,
                    Collections.emptyList(),
                    Collections.emptyList(),
                    true
            );
        }

        List<Polygon> bestMoves = new ArrayList<>();
        List<CellEvaluation> evaluations = new ArrayList<>();

        for (CandidateScore candidate : candidates) {
            if (candidate.score == bestScore) {
                bestMoves.add(candidate.cell);
            }

            evaluations.add(new CellEvaluation(
                    candidate.cell,
                    candidate.botForward,
                    candidate.botBackward,
                    candidate.humanForward,
                    candidate.humanBackward,
                    candidate.botUseful,
                    candidate.humanUseful,
                    candidate.score
            ));
        }

        evaluations.sort(Comparator
                .comparingLong(CellEvaluation::getScore)
                .thenComparing(evaluation -> evaluation.getCell().getId()));

        Polygon selectedMove = bestMoves.get(random.nextInt(bestMoves.size()));
        CellEvaluation selectedEvaluation = null;
        for (CellEvaluation evaluation : evaluations) {
            if (evaluation.getCell().getId().equals(selectedMove.getId())) {
                selectedEvaluation = evaluation;
                break;
            }
        }

        List<String> botPath = Collections.emptyList();
        List<String> humanPath = Collections.emptyList();
        if (selectedEvaluation != null) {
            if (selectedEvaluation.isBotUseful()) {
                botPath = controller.buildPathFromSearches(
                        selectedMove.getId(),
                        botForwardSearch,
                        botBackwardSearch
                );
            }
            if (selectedEvaluation.isHumanUseful()) {
                humanPath = controller.buildPathFromSearches(
                        selectedMove.getId(),
                        humanForwardSearch,
                        humanBackwardSearch
                );
            }
        }

        return new StrategyAnalysis(
                botColour,
                humanColour,
                botMovesToWin,
                humanMovesToWin,
                evaluations,
                selectedMove,
                botPath,
                humanPath,
                false
        );
    }

    private int findShortestWinDistance(Color colour, int[][] dist, QuaxController controller) {
        int bestDistance = INF;
        for (int i = 0; i < QuaxController.BOARD_SIZE; i++) {
            int row = (colour == Color.BLACK) ? QuaxController.MAX_COORD : i * 2;
            int col = (colour == Color.BLACK) ? i * 2 : QuaxController.MAX_COORD;
            bestDistance = Math.min(bestDistance, dist[row][col]);
        }
        return bestDistance;
    }
}
