import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * The class for initializing each color block object. 
 * @author <em>Steven Chen Hao Nyeo</em>
 */
public class ColorBlock extends javafx.scene.control.Button {
  
  /** Stores the index of the color */
  private int colorIndex;
  
  /** Stores the Circle object */ 
  private Circle circle;
  
  /** Stores the position of the button in rows and columns */
  private int col;
  private int row;
  
  
  /**
   * The constructor that stores the index of the color and initializes the colored circle onto the block. 
   * @param colorIndex an integer that represents the index of a certain color
   */
  public ColorBlock(int colorIndex) {
    this.colorIndex = colorIndex;
    circle = new Circle(8, getColor(colorIndex));
    this.setGraphic(circle);
  }
  
  
  /** 
   * The static method that translates the index into the color fields under javafx.scene.paint.Color 
   * @param colorIndex an integer index that represents a certain color
   */
  public static Color getColor(int colorIndex) {
    switch(colorIndex) {
      case 0:
        return Color.RED;
      case 1:
        return Color.YELLOW;
      case 2:
        return Color.BLUE;
      case 3:
        return Color.GREEN;
      case 4:
        return Color.ORANGE;
      case 5:
        return Color.PURPLE;
      case 6:
        return Color.BLACK;
      case 7:
        return Color.WHITE;
      case 8:
        return Color.CYAN;
      case 9:
        return Color.PINK;
      default:
        return Color.LIGHTGRAY;
    }
  }
  
  
  /**
   * The method for the game to call for changing the colors of the button. 
   * @param colorIndex an integer that represents the index of a certain color
   */
  public void setColor(int colorIndex) {
    setColorIndex(colorIndex);
    setCircle(new Circle(8, getColor(colorIndex)));
    this.setGraphic(getCircle());
  }
  
  
  /**
   * The getter method that returns the index of the block color. 
   * @return an interger that points to a certain color
   */
  public int getColorIndex() {
    return colorIndex;
  }
  
  
  /**
   * The setter method that sets the index of the block color.  
   * @param colorIndex an integer that represents the index of a certain color
   */
  public void setColorIndex(int colorIndex) {
    this.colorIndex = colorIndex;
  }
  
  
  /**
   * The method that returns the column index of button. 
   * @returns an int that represents the column index of button
   */
  public int getCol() {
    return col;
  }
  
  
  /**
   * The method that initializes the column index of button. 
   * @param col an int that sets the column index of button
   */
  public void setCol(int col) {
    this.col = col;
  }
  
  
  /**
   * The method that returns the row index of button. 
   * @returns an int that represents the row index of button
   */
  public int getRow() {
    return row;
  }
  
  
  /**
   * The method that initializes the row index of button. 
   * @param col an int that sets the row index of button
   */
  public void setRow(int row) {
    this.row = row;
  }

  
  /**
   * The getter method that returns the stored circle object. 
   * @return an object of type Circle stored in the field
   */
  public Circle getCircle() {
    return circle;
  }
  
  
  /**
   * The setter method that sets a new circle object to the field. 
   * @param circle an object of type Circle
   */
  public void setCircle(Circle circle) {
    this.circle = circle;
  }
 
}