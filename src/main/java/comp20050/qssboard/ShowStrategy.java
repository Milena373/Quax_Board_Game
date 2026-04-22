package comp20050.qssboard;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShowStrategy {
    private static final Color STRATEGY_ATTACK_COLOUR = Color.web("#2FB344");
    private static final Color STRATEGY_BLOCK_COLOUR = Color.web("#F76707");
    private static final Color STRATEGY_BOTH_COLOUR = Color.web("#7048E8");
    private static final Color STRATEGY_CHOSEN_COLOUR = Color.web("#FFD43B");
    private static final int STRATEGY_CANDIDATE_LIMIT = 6;

    private final Map<String, Polygon> cellMap;
    private final Button showStrategyButton;
    private final Button hideStrategyButton;
    private final Group strategyOverlay = new Group();

    private boolean visible = false;
    private BotPlayer.StrategyAnalysis currentAnalysis;

    public ShowStrategy(
            Group boardContainer,
            Map<String, Polygon> cellMap,
            Button showStrategyButton,
            Button hideStrategyButton
    ) {
        this.cellMap = cellMap;
        this.showStrategyButton = showStrategyButton;
        this.hideStrategyButton = hideStrategyButton;

        strategyOverlay.setManaged(false);
        strategyOverlay.setMouseTransparent(true);
        boardContainer.getChildren().add(strategyOverlay);

        updateButtons();
        clearOverlay();
    }

    public void show() {
        visible = true;
        updateButtons();
        refresh();
    }

    public void hide() {
        visible = false;
        updateButtons();
        clearOverlay();
    }

    public void setAnalysis(BotPlayer.StrategyAnalysis analysis) {
        currentAnalysis = analysis;
        refresh();
    }

    public void clearAnalysis() {
        currentAnalysis = null;
        refresh();
    }

    private void updateButtons() {
        showStrategyButton.setVisible(!visible);
        showStrategyButton.setDisable(visible);
        hideStrategyButton.setVisible(visible);
        hideStrategyButton.setDisable(!visible);
    }

    private void refresh() {
        clearOverlay();
        if (!visible || currentAnalysis == null || currentAnalysis.getSelectedMove() == null) {
            return;
        }

        BotPlayer.StrategyAnalysis analysis = currentAnalysis;

        strategyOverlay.getChildren().add(createStrategyLegend());
        strategyOverlay.getChildren().add(createStrategySummaryPanel(analysis));

        if (!analysis.getBotPath().isEmpty()) {
            strategyOverlay.getChildren().add(createPathOverlay(analysis.getBotPath(), STRATEGY_ATTACK_COLOUR, false));
        }

        if (!analysis.getHumanPath().isEmpty()) {
            strategyOverlay.getChildren().add(createPathOverlay(analysis.getHumanPath(), STRATEGY_BLOCK_COLOUR, true));
        }

        Set<String> markedCells = new HashSet<>();
        List<BotPlayer.CellEvaluation> evaluations = analysis.getCellEvaluations();
        int candidateCount = Math.min(STRATEGY_CANDIDATE_LIMIT, evaluations.size());
        for (int index = 0; index < candidateCount; index++) {
            BotPlayer.CellEvaluation evaluation = evaluations.get(index);
            strategyOverlay.getChildren().add(createCandidateMarker(evaluation, index + 1));
            markedCells.add(evaluation.getCell().getId());
        }

        BotPlayer.CellEvaluation selectedEvaluation = analysis.getSelectedEvaluation();
        if (selectedEvaluation != null && !markedCells.contains(selectedEvaluation.getCell().getId())) {
            strategyOverlay.getChildren().add(createCandidateRoleOverlay(selectedEvaluation));
        }

        strategyOverlay.getChildren().add(createChosenMoveOverlay(
                analysis.getSelectedMove(),
                analysis.isFallbackMove()
        ));
        strategyOverlay.getChildren().add(createSelectedMoveTag(
                analysis.getSelectedMove(),
                analysis.isFallbackMove() ? "BOT" : "PLAY"
        ));
    }

    private void clearOverlay() {
        strategyOverlay.getChildren().clear();
    }

    private Group createStrategySummaryPanel(BotPlayer.StrategyAnalysis analysis) {
        Group panel = new Group();
        panel.setId("strategy-summary");

        Rectangle background = new Rectangle(10, 326, 275, 215);
        background.setArcWidth(18);
        background.setArcHeight(18);
        background.setFill(Color.color(1.0, 1.0, 1.0, 0.92));
        background.setStroke(Color.color(0.0, 0.0, 0.0, 0.20));

        Text title = new Text(24, 352, "Why this move?");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));
        title.setFill(Color.color(0.15, 0.15, 0.15));

        Text moveText = new Text(24, 380, "Chosen: " + formatCellLabel(analysis.getSelectedMove().getId()));
        moveText.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        moveText.setFill(Color.color(0.15, 0.15, 0.15));

        Text routeText = new Text(24, 405, String.format(
                "Bot route: %s   Opponent route: %s",
                formatDistance(analysis.getBotMovesToWin()),
                formatDistance(analysis.getHumanMovesToWin())
        ));
        routeText.setFont(Font.font(13));
        routeText.setFill(Color.color(0.20, 0.20, 0.20));

        Text reasonText = new Text(24, 432, buildStrategyReasonText(analysis));
        reasonText.setFont(Font.font(13));
        reasonText.setWrappingWidth(245);
        reasonText.setFill(Color.color(0.18, 0.18, 0.18));

        Text instructionText = new Text(24, 518, "Showing the reasoning for the bot's last move.");
        instructionText.setFont(Font.font("System", FontWeight.SEMI_BOLD, 13));
        instructionText.setFill(Color.color(0.15, 0.15, 0.15));

        panel.getChildren().addAll(background, title, moveText, routeText, reasonText, instructionText);

        panel.setLayoutX(-150);
        panel.setLayoutY(638);

        return panel;
    }

    private String buildStrategyReasonText(BotPlayer.StrategyAnalysis analysis) {
        if (analysis.isFallbackMove()) {
            return "No useful path cell was available, so the bot fell back to a random legal move.";
        }

        BotPlayer.CellEvaluation selectedEvaluation = analysis.getSelectedEvaluation();
        if (selectedEvaluation == null) {
            return "The bot selected a legal move using its current path-based strategy.";
        }

        StringBuilder reason = new StringBuilder();
        if (selectedEvaluation.isBotUseful() && selectedEvaluation.isHumanUseful()) {
            if (analysis.getHumanMovesToWin() <= analysis.getBotMovesToWin()) {
                reason.append("This move blocks the opponent's most urgent route while still helping the bot's own route.");
            } else {
                reason.append("This move improves the bot's best route and still interferes with the opponent.");
            }
        } else if (selectedEvaluation.isHumanUseful()) {
            reason.append("This is mainly a defensive move because it lies on the opponent's shortest available route.");
        } else {
            reason.append("This is mainly an attacking move because it lies on the bot's shortest available route.");
        }

        int tiedBestMoves = countTiedBestMoves(analysis, selectedEvaluation);
        if (tiedBestMoves > 1) {
            reason.append(" ")
                    .append(tiedBestMoves)
                    .append(" moves shared the best score, so the bot randomly picked one of them.");
        }

        return reason.toString();
    }

    private int countTiedBestMoves(
            BotPlayer.StrategyAnalysis analysis,
            BotPlayer.CellEvaluation selectedEvaluation
    ) {
        if (selectedEvaluation == null) {
            return 0;
        }

        int tiedBestMoves = 0;
        long selectedScore = selectedEvaluation.getScore();
        for (BotPlayer.CellEvaluation evaluation : analysis.getCellEvaluations()) {
            if (evaluation.getScore() == selectedScore) {
                tiedBestMoves++;
            }
        }
        return tiedBestMoves;
    }

    private String formatDistance(int distance) {
        return distance >= QuaxController.MAX_DISTANCE
                ? "blocked"
                : distance + " move" + (distance == 1 ? "" : "s");
    }

    private String formatCellLabel(String cellId) {
        if (cellId == null) {
            return "unknown";
        }

        if (cellId.startsWith("OctCell")) {
            return "Oct " + cellId.replace("OctCell", "");
        }

        if (cellId.startsWith("RhoCell")) {
            return "Rho " + cellId.replace("RhoCell", "");
        }

        return cellId;
    }

    private Group createStrategyLegend() {
        Group legend = new Group();
        legend.setId("strategy-legend");

        Rectangle background = new Rectangle(10, 155, 208, 160);
        background.setArcWidth(18);
        background.setArcHeight(18);
        background.setFill(Color.color(1.0, 1.0, 1.0, 0.90));
        background.setStroke(Color.color(0.0, 0.0, 0.0, 0.20));

        Text title = new Text(24, 180, "Bot strategy");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));
        title.setFill(Color.color(0.15, 0.15, 0.15));

        Circle badgeKey = new Circle(35, 205, 11);
        badgeKey.setFill(Color.color(1.0, 1.0, 1.0, 0.96));
        badgeKey.setStroke(STRATEGY_BOTH_COLOUR);
        badgeKey.setStrokeWidth(3);

        Text badgeNumber = new Text("1");
        badgeNumber.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 14));
        badgeNumber.setFill(Color.color(0.15, 0.15, 0.15));
        Bounds badgeBounds = badgeNumber.getLayoutBounds();
        badgeNumber.setX(35 - (badgeBounds.getWidth() / 2.0));
        badgeNumber.setY(205 + (badgeBounds.getHeight() / 4.0));

        Text badgeText = new Text(54, 210, "ranked options");
        badgeText.setFont(Font.font(14));

        Line attackKey = new Line(26, 234, 44, 234);
        attackKey.setStroke(STRATEGY_ATTACK_COLOUR.deriveColor(0, 1, 1, 0.95));
        attackKey.setStrokeWidth(7);
        attackKey.setStrokeLineCap(StrokeLineCap.ROUND);

        Text attackText = new Text(54, 239, "bot route / attack");
        attackText.setFont(Font.font(14));

        Line blockKey = new Line(26, 262, 44, 262);
        blockKey.setStroke(STRATEGY_BLOCK_COLOUR.deriveColor(0, 1, 1, 0.95));
        blockKey.setStrokeWidth(6);
        blockKey.setStrokeLineCap(StrokeLineCap.ROUND);
        blockKey.getStrokeDashArray().addAll(10.0, 7.0);

        Text blockText = new Text(54, 267, "block / defend");
        blockText.setFont(Font.font(14));

        Rectangle chosenKey = new Rectangle(25, 276, 20, 20);
        chosenKey.setFill(Color.color(1.0, 0.83, 0.16, 0.20));
        chosenKey.setStroke(STRATEGY_CHOSEN_COLOUR);
        chosenKey.setStrokeWidth(3);

        Text chosenText = new Text(54, 291, "move the bot plays");
        chosenText.setFont(Font.font(14));

        legend.getChildren().addAll(
                background,
                title,
                badgeKey,
                badgeNumber,
                badgeText,
                attackKey,
                attackText,
                blockKey,
                blockText,
                chosenKey,
                chosenText
        );

        legend.setLayoutX(-150);
        legend.setLayoutY(600);

        return legend;
    }

    private Group createCandidateMarker(BotPlayer.CellEvaluation evaluation, int rank) {
        Group marker = createCandidateRoleOverlay(evaluation);
        marker.getChildren().add(createCandidateBadge(
                evaluation.getCell(),
                rank,
                getCandidateAccentColour(evaluation),
                rank <= 3
        ));
        return marker;
    }

    private Group createCandidateRoleOverlay(BotPlayer.CellEvaluation evaluation) {
        Group marker = new Group();
        Polygon cell = evaluation.getCell();

        if (evaluation.isBotUseful() && evaluation.isHumanUseful()) {
            marker.getChildren().add(createCellOutline(cell, STRATEGY_BLOCK_COLOUR, 4.6, true));
            marker.getChildren().add(createCellOutline(cell, STRATEGY_ATTACK_COLOUR, 2.8, false));
            return marker;
        }

        if (evaluation.isBotUseful()) {
            marker.getChildren().add(createCellOutline(cell, STRATEGY_ATTACK_COLOUR, 4.0, false));
            return marker;
        }

        marker.getChildren().add(createCellOutline(cell, STRATEGY_BLOCK_COLOUR, 4.0, true));
        return marker;
    }

    private Polygon createCellOutline(Polygon source, Color colour, double strokeWidth, boolean dashed) {
        Polygon outline = clonePolygon(source);
        outline.setFill(Color.TRANSPARENT);
        outline.setStroke(colour.deriveColor(0, 1, 1, 0.96));
        outline.setStrokeWidth(strokeWidth);
        if (dashed) {
            outline.getStrokeDashArray().addAll(12.0, 8.0);
        }
        return outline;
    }

    private Group createCandidateBadge(Polygon cell, int rank, Color accentColour, boolean emphasised) {
        Group badge = new Group();
        Bounds bounds = cell.getBoundsInParent();
        double radius = cell.getId().startsWith("RhoCell") ? 10.0 : 12.0;
        double centreX = bounds.getMinX() + (bounds.getWidth() * 0.76);
        double centreY = bounds.getMinY() + (bounds.getHeight() * 0.28);

        Circle shadow = new Circle(centreX + 1.0, centreY + 1.0, radius + 0.4);
        shadow.setFill(Color.color(0.0, 0.0, 0.0, 0.18));

        Circle bubble = new Circle(centreX, centreY, radius);
        bubble.setFill(Color.color(1.0, 1.0, 1.0, 0.96));
        bubble.setStroke(accentColour);
        bubble.setStrokeWidth(emphasised ? 3.3 : 2.5);

        Text number = new Text(Integer.toString(rank));
        number.setFont(Font.font(
                "System",
                FontWeight.EXTRA_BOLD,
                cell.getId().startsWith("RhoCell") ? 12 : 14
        ));
        number.setFill(Color.color(0.15, 0.15, 0.15));

        Bounds textBounds = number.getLayoutBounds();
        number.setX(centreX - (textBounds.getWidth() / 2.0));
        number.setY(centreY + (textBounds.getHeight() / 4.0));

        badge.getChildren().addAll(shadow, bubble, number);
        return badge;
    }

    private Color getCandidateAccentColour(BotPlayer.CellEvaluation evaluation) {
        if (evaluation.isBotUseful() && evaluation.isHumanUseful()) {
            return STRATEGY_BOTH_COLOUR;
        }
        return evaluation.isBotUseful() ? STRATEGY_ATTACK_COLOUR : STRATEGY_BLOCK_COLOUR;
    }

    private Group createPathOverlay(List<String> cellIds, Color colour, boolean dashed) {
        Group pathGroup = new Group();
        if (cellIds.size() < 2) {
            return pathGroup;
        }

        Polyline shadow = new Polyline();
        Polyline line = new Polyline();

        for (String cellId : cellIds) {
            Polygon cell = cellMap.get(cellId);
            if (cell == null) {
                continue;
            }
            Bounds bounds = cell.getBoundsInParent();
            double centreX = bounds.getMinX() + (bounds.getWidth() / 2.0);
            double centreY = bounds.getMinY() + (bounds.getHeight() / 2.0);
            shadow.getPoints().addAll(centreX, centreY);
            line.getPoints().addAll(centreX, centreY);
        }

        shadow.setFill(Color.TRANSPARENT);
        shadow.setStroke(Color.color(0.0, 0.0, 0.0, 0.28));
        shadow.setStrokeWidth(dashed ? 10 : 11);
        shadow.setStrokeLineCap(StrokeLineCap.ROUND);
        if (dashed) {
            shadow.getStrokeDashArray().addAll(18.0, 10.0);
        }

        line.setFill(Color.TRANSPARENT);
        line.setStroke(colour.deriveColor(0, 1, 1, 0.90));
        line.setStrokeWidth(dashed ? 6 : 7);
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        if (dashed) {
            line.getStrokeDashArray().addAll(18.0, 10.0);
        }

        pathGroup.getChildren().addAll(shadow, line);
        return pathGroup;
    }

    private Node createChosenMoveOverlay(Polygon chosenMove, boolean fallbackMove) {
        Polygon overlay = clonePolygon(chosenMove);
        overlay.setFill(Color.color(1.0, 0.83, 0.16, 0.18));
        overlay.setStroke(STRATEGY_CHOSEN_COLOUR);
        overlay.setStrokeWidth(5.5);
        if (fallbackMove) {
            overlay.getStrokeDashArray().addAll(14.0, 8.0);
        }
        return overlay;
    }

    private Text createSelectedMoveTag(Polygon selectedMove, String textValue) {
        Bounds bounds = selectedMove.getBoundsInParent();
        double centreX = bounds.getMinX() + (bounds.getWidth() / 2.0);
        double centreY = bounds.getMinY() + (bounds.getHeight() / 2.0);

        Text tag = new Text(textValue);
        tag.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 18));
        tag.setFill(Color.BLACK);
        tag.setStroke(Color.WHITE);
        tag.setStrokeWidth(1.2);

        Bounds textBounds = tag.getLayoutBounds();
        tag.setX(centreX - (textBounds.getWidth() / 2.0));
        tag.setY(centreY + (textBounds.getHeight() / 4.0));
        return tag;
    }

    private Polygon clonePolygon(Polygon source) {
        Polygon copy = new Polygon();
        copy.getPoints().addAll(source.getPoints());
        copy.setLayoutX(source.getLayoutX());
        copy.setLayoutY(source.getLayoutY());
        copy.setScaleX(source.getScaleX());
        copy.setScaleY(source.getScaleY());
        copy.setScaleZ(source.getScaleZ());
        copy.setTranslateX(source.getTranslateX());
        copy.setTranslateY(source.getTranslateY());
        copy.setRotate(source.getRotate());
        copy.setStrokeType(source.getStrokeType());
        copy.setMouseTransparent(true);
        return copy;
    }
}
