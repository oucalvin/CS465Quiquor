package client.controller;
/**
 * User: Neal Eric
 * Date: 11/10/13
 */

import client.Main;
import client.readOnly.Drink;
import client.readOnly.Ingredient;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class GUIDrinkController extends AnchorPane {
	private static final int MAX_HEIGHT = 400;
	private static final int MAX_WIDTH = 400;
	private static final double CUP_BOTTOM_OFFSET = 40.0;
	private static final double CUP_LEFT_OFFSET = 96.0;
	List<String> ingredientList;
	private final VBox vBox = new VBox();
	boolean draggable;


	public GUIDrinkController(boolean draggable) {
		super();
		getStyleClass().add("cup");
		ingredientList = new ArrayList<>();
		setMinSize(MAX_WIDTH, MAX_HEIGHT);
		setMaxSize(MAX_WIDTH, MAX_HEIGHT);
		setBottomAnchor(vBox, CUP_BOTTOM_OFFSET);
		setLeftAnchor(vBox, CUP_LEFT_OFFSET);
		getChildren().add(vBox);
		vBox.setAlignment(Pos.BOTTOM_CENTER);
		this.draggable = draggable;
	}

	public void addDrink(Drink drink) {
		clear();
		for (Ingredient ingredient : drink.getIngredients()) {
			addIngredient(ingredient);
		}
	}

	public void addIngredient(Ingredient ingredient) {
		ingredientList.add(ingredient.getName());
		vBox.getChildren().add(new Cell(ingredient, draggable));
	}

	public void clear() {
		ingredientList.clear();
		vBox.getChildren().clear();
	}

	/**
	 * Each Cell is a (drink) row in the GUIDrinkController
	 */
	private static class Cell extends StackPane {

		private static final int MAX_WIDTH = 252;
		private static final double MIN_HEIGHT = 20;
		private static final int STARTING_HEIGHT = 50;
		private static final int HORIZONTAL_MARGIN = 7;
		private final boolean draggable;
		private double y;
		private boolean resizing;
		private boolean dragging;
		private Rectangle rectangle = new Rectangle(MAX_WIDTH, STARTING_HEIGHT);
		private Text text;

		private Cell(Ingredient ingredient, boolean draggable) {
			super();
			text = new Text(ingredient.getName());
			init(this);
			stylize(Main.pumpMap.get(ingredient));
			this.draggable = draggable;
		}

		private void stylize(int color) {
			//StackPane
			this.setAlignment(Pos.TOP_CENTER);
			this.setMaxWidth(MAX_WIDTH);

			//Rectangle
			rectangle.getStyleClass().add("drink-cell-" + color);
		}

		public void init(final Cell cell) {
			this.getChildren().addAll(rectangle, text);
			this.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					cell.mousePressed(event);
				}
			});
			this.setOnMouseReleased(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					cell.mouseReleased(event);
				}
			});
			this.setOnMouseMoved(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					cell.mouseOver(event);
				}
			});
			this.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					cell.mouseDragged(event);
				}
			});
		}

		private void mouseReleased(MouseEvent event) {
			resizing = false;
			this.setCursor(Cursor.DEFAULT);
			if (dragging) {
				double targetY = getBoundsInParent().getMinY();
				VBox vbox = (VBox) getParent();
				ObservableList<Node> children = vbox.getChildren();
				double[] minYOfCells = new double[children.size()];
				int i = 0;
				for (Node child : children) {
					minYOfCells[i] = child.getBoundsInParent().getMinY();
					i++;
				}
				i = 0;
				for (double minY : minYOfCells) {
					if (targetY <= minY) {
						Cell temp = this;
						children.remove(this);
						children.add(i, temp);
						break;
					}
					i++;
				}
			}
			dragging = false;
		}

		private void mouseOver(MouseEvent event) {
			if ((isInResizableMargin(event) || resizing) && draggable) {
				this.setCursor(Cursor.S_RESIZE);
			} else {
				this.setCursor(Cursor.DEFAULT);
			}
		}

		private boolean isInResizableMargin(MouseEvent event) {
			return event.getY() <= (HORIZONTAL_MARGIN);
		}

		private void mouseDragged(MouseEvent event) {
			if (resizing) {
				double mouseY = event.getSceneY();
				double newHeight = rectangle.getHeight() - mouseY + y;
				if (newHeight > MIN_HEIGHT) {
					rectangle.setHeight(newHeight);
					y = mouseY;
				}
			} else if (dragging) {
				double mouseY = event.getScreenY();
				double newPosition = mouseY - 500;
				setLayoutY(newPosition);
				y = mouseY;
			}
		}

		private void mousePressed(MouseEvent event) {
			if (draggable) {
				if (isInResizableMargin(event)) {
					resizing = true;
					y = event.getSceneY();
				} else {
					dragging = true;
					y = event.getSceneY();
					toFront();
				}
			}
		}
	}
}
