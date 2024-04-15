import processing.core.PApplet;

public class Sketch extends PApplet {
	
	
  // Creating variables

  int intGridOffsetX, intGridOffsetY;
  int intRowNum, intColumnNum;
  int intRoadWidth;

  public void settings() {
	  // user can freely change size of screen 
    size(1000, 500);
  }

  public void setup() {
    // background colour
    background(210, 255, 173);

    // This is where the user can input row and column units for the house grid
    intRowNum = 20;
    intColumnNum = 8;
  }

  public void draw() {
    // draws house grid 
    houseGrid(intRowNum, intColumnNum);

    // draws road grid
    drawRoadGrid(intRowNum, intColumnNum, 2);
  }

  /**
   * Draws a grid of houses based on the input of # of rows and columns
   * Scales houses so that all of them fit in the screen size limitations
   * Position of houses are all equal and fit grid shape
   * 
   * @param intRows The number of rows in house grid
   * @param intColumns The number of columns in house grid
   */
  private void houseGrid(int intRows, int intColumns){

    // Calculates horizontal space between houses
    intGridOffsetX = width / (intColumns + 1);

    //Calculates vertical space between houses
    intGridOffsetY = height / (intRows + 1);

    // Creating house grid
    for (int x = 0; x < intColumns; x++){
      for (int y = 0; y < intRows; y++){
        drawHouse((x + 1) * intGridOffsetX, (y + 1) * intGridOffsetY, (intColumns + intRows) / 2);
      }
    }
  }

  /**
   * Draws default house in a centered fashion based on X and Y position inputs
   * Scales house size based on intScalar value
   * 
   * @param intXPos The x position of the entire house
   * @param intYPos The y position of the entire house
   * @param intScaler The scaling factor of the entire house
   */
  private void drawHouse(int intXPos, int intYPos, int intScaler){

    // High scaler means smaller house
    intScaler /= 0.9;

    // Creating gray square house base (with input location)
    fill(150, 150, 150);
    rect(intXPos - (100 / intScaler), intYPos - (100 / intScaler) , 200 / intScaler, 200 / intScaler);

    // Creating white triangle roof (with input location)
    strokeWeight(2);
    fill(0);
    triangle(intXPos - (100 / intScaler), intYPos - (100 / intScaler), intXPos, intYPos - (150 / intScaler), intXPos + (100 / intScaler), intYPos - (100 / intScaler));
    strokeWeight(1);

    // Creating door for the house (with input location)
    fill(100, 100, 100);
    rect(intXPos - (20 / intScaler), intYPos + (40 / intScaler), (40 / intScaler), (60 / intScaler));

    // Creating windows for the house (with input location)
    fill(50, 50, 150);
    rect(intXPos + (25 / intScaler), intYPos - (60 / intScaler), (50 / intScaler), (50 / intScaler));
    rect(intXPos - (75 / intScaler), intYPos - (60 / intScaler), (50 / intScaler), (50 / intScaler));
  }
  
  /**
   * Draws the road and 10 yellow dotted lines in the center of the road. 
   * The colour of the road will get lighter as more roads are created
   * Road and yellow lines scale based on size of screen, and space between the house grid
   * 
   * @param intPos The X or Y position of the road
   * @param intDir Determines whether intPos represents the X or Y value (whether a horizontal or vertical road)
   * @param intRoadNum The number of roads that have been placed so far (inclusive)
   * @param intTotalRoadNum The total number of roads to be placed
   */
  private void drawRoad(int intPos, int intDir, double intRoadNum, double intTotalRoadNum){

    // Determines shade of black of road based on the number of roads created so far
    fill((int) (1 + (intRoadNum / intTotalRoadNum) * 255.0));
    
    // Horizontal road
    if (intDir == 1){
      intRoadWidth = intGridOffsetY / 8;
      rect(0, intPos - (intRoadWidth / 2), width, intRoadWidth);
      
      // Creating 10 Yellow lines for road, following direction of road
      for (int i = 0; i < 10; i++){
        fill(255, 255, 0);
        rect((int) ((i + 0.25) * (width / 10)), intPos - (intRoadWidth / 10), width / 20, intRoadWidth / 5);
      }
    }

    // Vertical road
    else if (intDir == 2){
      intRoadWidth = intGridOffsetX / 6;
      rect(intPos - (intRoadWidth / 2), 0, intRoadWidth, height);

      // Creating 10 Yellow lines for road, following direction of road
      for (int i = 0; i < 10; i++){
        fill(255, 255, 0);
        rect(intPos - (intRoadWidth / 10), (int) ((i + 0.25) * (height / 10)), intRoadWidth / 5, height / 20);
      }
    }

    else{}
  }

  /**
   * This code creates the roads such that each house has access to at least 2 roads
   * The roads are also spaced equally between houses, and go as long as the screen size permits
   * Road size and position scale based on space between houses in house grid
   * 
   * @param intRowNum The number of rows of houses
   * @param intColumnNum The number of columns of houses
   * @param intDir The desired direction that the roads will be created in
   */
  private void drawRoadGrid(int intRowNum, int intColumnNum, int intDir){
    // Vertical roads
    if (roadBuildCheck(intDir) == 2){

      // Builds 1 more road than the number of columns in the house grid
      for (int x = 0; x < intColumnNum + 1; x++){
        drawRoad((int) ((x + 0.5) * intGridOffsetX), 2, x, intColumnNum + 1);
      }
    }

    // Horizontal roads
    else if (roadBuildCheck(intDir) == 1){
      
      // Builds 1 more road than the number of rows in the house grid
      for (int y = 0; y < intRowNum + 1; y++){
        drawRoad((int) ((y + 0.5) * intGridOffsetY), 1, y, intRowNum + 1);
      }
    }
    else{}
  }

  /**
   * The user will input a desired direction for the roads to be built, and the program will decide
   * whether the road to be built is viable (based on the width of the road and the horizontal/vertical distance 
   * between houses). If not, it will check if another direction is viable. 
   * If neither horizontal or vertical roads are viable, no roads will be made
   * For example, if the horizontal gap between houses is too small but the user wants to make vertical lines, 
   * this program will dismiss the user input and try horizontal roads based on the vertical gap between houses.
   * If neither work, no roads will be made
   * 
   * @param intDir The desired direction of the roads (from user)
   * @return The direction the roads will be built
   */
  public int roadBuildCheck(int intDir){
    // when intDir = 0(no road); 1(horizontal); 2(vertical)

    if (intDir == 1 && intGridOffsetY < 80){
      // cannot build horizontal
      intDir = 2;

      if (intDir == 2 && intGridOffsetX < 50){
        // cannot build vertical 
        intDir = 0;
      }
    }

    else if (intDir == 2 && intGridOffsetX < 50){
      // cannot build vertical
      intDir = 1;
      
      if (intDir == 1 && intGridOffsetY < 80){
        // cannot build horizontal
        intDir = 0;
      }
    }
    // returns final intDir integer
    return intDir;
  }
}