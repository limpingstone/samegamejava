import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;

import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import javax.swing.JOptionPane; 

/**
 * The main class body of the game. The game is simplified for easier design. 
 * For more information, visit <a href="https://en.wikipedia.org/wiki/SameGame">Wikipedia: SameGame</a> 
 * @author <em>Steven Chen Hao Nyeo</em>
 */
public class SameGame extends Application implements EventHandler<ActionEvent> {
  
  /* The field for storing the primary stage for the game program */
  private Stage primaryStage;
  
  /* The field for storing grid object in the program */
  private static GridPane gridPane = new GridPane();
  
  /* Default size of game is 12 x 12 grid */
  private static int col = 12; 
  private static int row = 12;
  
  /* Default number of colors in the game is 3 colors */
  private static int numColors = 3;
  
  /* Initalizes the array for storing the color block objects */
  private static ColorBlock[][] colorBlockGrid;
  
  /* The field that stores the color block when the undo button is clicked */
  private static ColorBlock[][] savedColorBlockGrid;
  
  /**
   * Inherited from javafx.application.Application. Setup of the stage and its elements. 
   */
  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    Group root = new Group();
    
    createBlockGrid(); 
    initColorBlocks(); 

    gridPane.add(getMenuBar(), 0, 0, col, 1);
    
    root.getChildren().add(gridPane);
    Scene scene = new Scene(root);
    
    primaryStage.setTitle("SameGame (" + col + "x" + row + ")");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  
  
  /**
   * The method that constructs and returns a menu bar.
   * @return an object of type MenuBar that includes menus and functions 
   */
  public MenuBar getMenuBar() {
    
    // Main menus in the main program. 
    Menu fileMenu = new Menu("File");
    Menu optionsMenu = new Menu("Options");
    Menu helpMenu = new Menu("Help");
    
    // Items in the 'File' menu 
    MenuItem newGameButton = new MenuItem("New Game");
    newGameButton.setOnAction(e -> {
      createBlockGrid();
      initColorBlocks();
    });
    
    MenuItem quitButton = new MenuItem("Quit");
    quitButton.setOnAction(e -> {
      System.exit(0);
    });
    
    fileMenu.getItems().add(newGameButton);
    
    fileMenu.getItems().add(quitButton);
    
    // Items in the 'Options' menu 
    MenuItem undoButton = new MenuItem("Undo");
    undoButton.setOnAction(e -> {
      if (savedColorBlockGrid != null) {
        for (int i = 0; i < col; i++) {
          for (int j = 0; j < row; j++) {
            colorBlockGrid[i][j].setColor(savedColorBlockGrid[i][j].getColorIndex());
          }
        }
      }
    });
    
    MenuItem resetButton = new MenuItem("Reset");
    resetButton.setOnAction(e -> {
      createBlockGrid();
      initColorBlocks();
    });
    
    MenuItem viewScoreButton = new MenuItem("View Score");
    viewScoreButton.setOnAction(e -> {
      int numGrayBlocks = 0;
      for (int i = 0; i < col; i++) {
        for (int j = 0; j < row; j++) {
          if (colorBlockGrid[i][j].getColorIndex() == 10)
            numGrayBlocks++;
        }
      }
      JOptionPane.showMessageDialog(null, "Your current score: " + numGrayBlocks + "\nMaximum score: " + col * row);
    });
    
    optionsMenu.getItems().add(undoButton);
    optionsMenu.getItems().add(resetButton);
    optionsMenu.getItems().add(viewScoreButton);
    
    // Items in the 'Help' menu 
    MenuItem aboutSameGameButton = new MenuItem("About SameGame");
    aboutSameGameButton.setOnAction(e -> {
      JOptionPane.showMessageDialog(null, "                               -- The Same Game -- \n\nClick on groups of two or more blocks of the same type \nthat touch horizontally or vertically to make them disappear, \nbut pay attention! The more blocks in a group, \nthe higher your score when you click! \n\nFor more information, visit Wikipedia's page: \nhttps://en.wikipedia.org/wiki/SameGame \n\nAuthor: \nSteven Chen Hao Nyeo <cxn152@case.edu>\n ");
    });
    
    helpMenu.getItems().add(aboutSameGameButton);
    
    MenuBar menuBar = new MenuBar();
    menuBar.getMenus().addAll(fileMenu, optionsMenu, helpMenu); 
    return menuBar;
  }
  
  /**
   * Inherited from import javafx.event.EventHandler. The method that detects clicks on buttons and set actions. 
   * @param e an object of type ActionEvent 
   */
  @Override
  public void handle(ActionEvent e) {
    ColorBlock clickedBlock = (ColorBlock)e.getSource();
    
    /* Check if the surrounding buttons have the same color as the clicked button */
    if (checkVertical(clickedBlock) || checkHorizontal(clickedBlock)) {
      
      saveColorBlockGrid();
      
      /* Remove the rows of the row and the column. 
       * The removeSameColorRow method must be executed before the removeSameColorColumn method. */
      removeSameColorRow(clickedBlock);
      removeSameColorColumn(clickedBlock);
      
      /* Check for empty columns and shift the columns. */
      for (int i = 0; i < getNumEmptyColumns(); i++)
        shiftEmptyLeft();
    }
  }
  
  
  /**
   * The method that saves the previous color block grid before click. 
   */
  public void saveColorBlockGrid() {
    savedColorBlockGrid = new ColorBlock[col][row];
    
    for (int i = 0; i < col; i++) {
      for (int j = 0; j < row; j++) {
        savedColorBlockGrid[i][j] = new ColorBlock(colorBlockGrid[i][j].getColorIndex()); 
        savedColorBlockGrid[i][j].setCol(i);
        savedColorBlockGrid[i][j].setRow(j);
        savedColorBlockGrid[i][j].setOnAction(this);
      }
    }
  }
  
  
  /**
   * The method that checks if the adjacent blocks in the vertical direction have the same color. 
   * @return true if either the block above or below is the same color as the clicked block
   */
  public boolean checkVertical(ColorBlock block) {
    if (block.getRow() == 0) 
      return block.getColorIndex() == colorBlockGrid[block.getCol()][block.getRow() + 1].getColorIndex();
    else if (block.getRow() == this.row - 1)
      return block.getColorIndex() == colorBlockGrid[block.getCol()][block.getRow() - 1].getColorIndex();
    return block.getColorIndex() == colorBlockGrid[block.getCol()][block.getRow() + 1].getColorIndex() || 
           block.getColorIndex() == colorBlockGrid[block.getCol()][block.getRow() - 1].getColorIndex();
  }
  
  
  /**
   * The method that checks if the adjacent blocks in the horizontal direction have the same color. 
   * @return true if either the block left or right is the same color as the clicked block
   */
  public boolean checkHorizontal(ColorBlock block) {
    if (block.getCol() == 0) 
      return block.getColorIndex() == colorBlockGrid[block.getCol() + 1][block.getRow()].getColorIndex();
    else if (block.getCol() == this.col - 1)
      return block.getColorIndex() == colorBlockGrid[block.getCol() - 1][block.getRow()].getColorIndex();
    return block.getColorIndex() == colorBlockGrid[block.getCol() + 1][block.getRow()].getColorIndex() || 
           block.getColorIndex() == colorBlockGrid[block.getCol() - 1][block.getRow()].getColorIndex();
  }
  
  
  /**
   * Removes the input ColorBlock and drops down.
   * @param block an object of type ColorBlock that is to be removed 
   */
  public void removeBlock(ColorBlock block) {
    
    // Sets the color on the top to gray
    if (block.getRow() == 0) 
      block.setColor(10);
    else {
      
      // Recursive method that copies the color to the previous block
      block.setColor(colorBlockGrid[block.getCol()][block.getRow() - 1].getColorIndex());
      removeBlock(colorBlockGrid[block.getCol()][block.getRow() - 1]);
    }
  }
  
  
  /**
   * The method that removes the group of blocks with the same color vertical to each other.  
   */
  public void removeSameColorColumn(ColorBlock block) {
    
    // To prevent infinite loop when clicking on gray buttons 
    if (block.getColorIndex() < 10) {
      
      // Set the color to remove 
      int colorToRemove = block.getColorIndex();
      int row = block.getRow(); 
      
      // Set row to the lowest index with the same color to remove
      while (row < this.row - 1 && colorBlockGrid[block.getCol()][row + 1].getColorIndex() == colorToRemove)
        row++;
      
      // call the remove block till the colors except the clicked block are all removed
      while (row > 0 && colorBlockGrid[block.getCol()][row - 1].getColorIndex() == colorToRemove) { 
        removeBlock(colorBlockGrid[block.getCol()][row]);
      }

      // removes the clicked block
      removeBlock(colorBlockGrid[block.getCol()][row]);
    }

    
  }
  
  
  /**
   * The method that removes the group of blocks with the same color horizontal to each other.  
   */
  public void removeSameColorRow(ColorBlock block) {
    
    // To prevent infinite loop when clicking on gray buttons 
    if (block.getColorIndex() < 10) {
      
      // Set the color to remove 
      int colorToRemove = block.getColorIndex();
      
      // Remove the left with the same colors marked to be removed
      int colLeft = block.getCol();
      while (colLeft > 0 && colorBlockGrid[colLeft - 1][block.getRow()].getColorIndex() == colorToRemove) 
        removeBlock(colorBlockGrid[colLeft-- - 1][block.getRow()]);
      
      // Remove the right with the same colors marked to be removed
      int colRight = block.getCol();
      while (colRight < this.col - 1 && colorBlockGrid[colRight + 1][block.getRow()].getColorIndex() == colorToRemove) 
        removeBlock(colorBlockGrid[colRight++ + 1][block.getRow()]);
    }
  }
  
  /**
   * The method that counts and returns the number of columns that only have gray buttons. 
   * @return a value of type int representing the number of empty columns
   */
  public int getNumEmptyColumns() {
    int numEmptyColumns = 0;
    for (int i = 0; i < this.col; i++) {
      if (colorBlockGrid[i][this.row - 1].getColorIndex() == 10) 
        numEmptyColumns++;
    }
    
    return numEmptyColumns;
  }
  
  
  /**
   * The method that shifts the colors to the left and replaces the last column with gray buttons. 
   * 
   */
  public void shiftEmptyLeft() {
    
    for (int i = 0; i < this.col - 1; i++) {
      
      // Check if the bottom button is empty, which indicates an empty column. 
      if (colorBlockGrid[i][this.row - 1].getColorIndex() == 10) {
        for (int j = 0; j < this.row; j++) {
          
          // Exchange the colors of the buttons between a filled and an empty button
          colorBlockGrid[i][j].setColor(colorBlockGrid[i + 1][j].getColorIndex());
          colorBlockGrid[i + 1][j].setColor(10);
        }
      }
    }
    
  }
  
  
  /**
   * The method that creates the grid of blocks. 
   */
  public void createBlockGrid() {
    
    /* Set the column and row numbers if parameters are available. */ 
    if (this.getParameters().getRaw().size() == 3) {
      col = Integer.parseInt(this.getParameters().getRaw().get(0));
      row = Integer.parseInt(this.getParameters().getRaw().get(1));
    }
    colorBlockGrid = new ColorBlock[col][row];
  }
  
  
  /**
   * The method that assigns the objects of type ColorBlock to the grid. 
   */
  public void initColorBlocks() {    
    for (int i = 0; i < col; i++) {
      for (int j = 0; j < row; j++) {
        colorBlockGrid[i][j] = new ColorBlock(randomColor());
        gridPane.add(colorBlockGrid[i][j], i, j + 1);
        
        // Stores the row and column indices to each ColorBlock object 
        colorBlockGrid[i][j].setCol(i);
        colorBlockGrid[i][j].setRow(j);
        
        colorBlockGrid[i][j].setOnAction(this);
      }
    }
  }
  
  
  /**
   * The method that randomly returns the integer that represents a color. 
   * @return an index of the randomly assigned color
   */
  public int randomColor() {
    
    /* Set the number of colors if parameters are available. */
    if (this.getParameters().getRaw().size() == 3)
      numColors = Integer.parseInt(this.getParameters().getRaw().get(2));
    
    return (int)(Math.random() * numColors);
  }
  
  
  /**
   * The main method. Launch the application and pass in the arguments. 
   * @param args[0] Number of columns in the grid
   * @param args[1] Number of rows in the grid
   * @param args[2] Number of colors in the grid
   */
  public static void main(String[] args) {
    Thread thread = new Thread() {
      
      @Override         
      public void run() {
        launchApp(args);
      }
    };
    
    thread.start();
  }
  
  /**
   * The static method that launches the Application launch method. 
   * @param args The arguments of type String from the interactions pane prompt 
   */
  public static void launchApp(String[] args) {
      Application.launch(args);
  }
}

