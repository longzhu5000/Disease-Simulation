package disease;



public class Board {
  private int width;
  private int height;
  private int population;
  private Tile[][] theBoard;
  
  Board() {
    this.width = 1;
    this.height = 1;
    // Allocate space and place tiles on all spots
    theBoard = new Tile[this.width][this.height];
    for(int x = 0 ; x < this.width ; x++) {
      for(int y = 0 ; y < this.height ; y++) {
        this.theBoard[x][y] = new Tile();
      }
    }
  }
  
  // Constructor by width/height/hosts
  Board(int w, int h, int hosts) {
    this.width      = w;
    this.height     = h;
    this.population = hosts;
    // In case more hosts than fit on the board is specified...
    if(this.population > (width*height)) {
      this.population = (width * height);
    }
    // Allocate space and place tiles on all spots
    theBoard = new Tile[this.width][this.height];
    for(int x = 0 ; x < this.width ; x++) {
      for(int y = 0 ; y < this.height ; y++) {
        this.theBoard[x][y] = new Tile();
      }
    }
    // Place all hosts
    for(int i = 0 ; i < this.population ; i++) {
      placeRandomHost();
    }
    // Infect one at random to initialize the board
    randomInfect();
  }

  public void advanceTimeslice() {
    // Start of turn - set all Hosts to not having moved yet
    for(int x = 0 ; x < this.width ; x++) {
      for(int y = 0 ; y < this.height ; y++) {
        // If there's a person at this address...
        if(theBoard[x][y].isOccupied) {
          // Set them to not having yet moved
          theBoard[x][y].getOccupant().hasMoved = false;
        }
      }
    } // Closing start-of-turn behavior
    
    // Turn in progress - Move all hosts
    for(int x = 0 ; x < this.width ; x++) {
      for(int y = 0 ; y < this.height ; y++) {
        // If there's a person at this address...
        if(theBoard[x][y].isOccupied) {
          Host currHost = theBoard[x][y].getOccupant();
          // Attempt to move them (might fail and host remains stationary)
          int tgtX = x - currHost.getSpeed() + ((int)(Math.random() * currHost.getSpeed())) + 2;
          if(tgtX < 0) {
            tgtX = 0;
          }
          else if(tgtX >= this.width) {
            tgtX = this.width - 1;
          }
          int tgtY = y - currHost.getSpeed() + ((int)(Math.random() * currHost.getSpeed())) + 2;
          if(tgtY < 0) {
            tgtY = 0;
          }
          else if(tgtY >= this.height) {
            tgtY = this.height - 1;
          }
          if( isLegalMove(tgtX, tgtY )){
            theBoard[x][y].sendHost();
            theBoard[tgtX][tgtY].acceptHost( currHost );
            currHost.hasMoved = false;
          }
        }
      }
    } // Closing mid-turn behavior
    
    // End of turn
    for(int x = 0 ; x < this.width ; x++) {
      for(int y = 0 ; y < this.height ; y++) {
        infectAdjacent(x, y);
        theBoard[x][y].endTurn();
      }
    } // Closing end-of-turn behavior
  } // Closing advanceTimeslice()
  
  private void infectAdjacent(int x, int y) {
    Host currHost = theBoard[x][y].getOccupant();
    if(theBoard[x][y].isOccupied && currHost.getSIR() == 'I') {
      for(int row = x-1 ; row < x+2 ; row++) {
        for(int col = y-1 ; col < y+2 ; col++) {
          // Bounds check
          if(isInBounds(row, col)) {
            if(theBoard[row][col].isOccupied) {
              Host tgtHost = theBoard[row][col].getOccupant();
              currHost.infect(tgtHost);
            }
          }
        }
      } // Closing for - All adjacencies processed
    }
  } // Closing infectAdjacent()

  private boolean isInBounds(int x, int y) {
    // Bounds-checking
    if(x < 0 || y < 0) {
      return false;
    }
    if(x >= this.width || y >= this.height) {
      return false;
    }
    return true;
  }

  private boolean isLegalMove(int x, int y) {
    if(!isInBounds( x, y )) {
      return false;
    }
    // Is another host at that location?
    if(theBoard[x][y].isOccupied) {
      return false;
    }
    // All legality checks complete, ergo...
    return true;
  }

  // Randomly select a tile and attempt to place a host on it
  private void placeRandomHost() {
    boolean isPlaced = false;
    while(!isPlaced) {
      int xTgt = (int)(Math.random() * this.width);
      int yTgt = (int)(Math.random() * this.height);
      if(theBoard[xTgt][yTgt].isOccupied) {
        // Tile occupied, try again
      }
      else {
        // Place the host
        Host newHost = new Host();
        theBoard[xTgt][yTgt].acceptHost(newHost);
        isPlaced = true;
      }
    }
  } // Closing placeRandomHost()

  private void randomInfect() {
    boolean hasInfected = false;
    while(!hasInfected) {
      int xTgt = (int)(Math.random() * this.width);
      int yTgt = (int)(Math.random() * this.height);
      if(theBoard[xTgt][yTgt].isOccupied) {
        // Tile occupied, set to infected
        theBoard[xTgt][yTgt].getOccupant().gotInfected();
        hasInfected = true;
      }
    }
  }
  
  // Scan for infected, tile-by-tile
  public boolean hasInfected() {
    for(int x = 0 ; x < this.width ; x++) {
      for(int y = 0 ; y < this.height ; y++) {
        // If there's a person at this address...
        if(theBoard[x][y].isOccupied) {
          // Test for disease
          if(theBoard[x][y].getOccupant().getSIR() == 'I') {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  public void renderBoard() {
    // Print top border
    printBorderRow();
    for(int row = 0 ; row < this.height ; row++) {
      // Left edge
      System.out.print("|");
      for(int col = 0 ; col < this.width ; col++) {
        System.out.print(" " + theBoard[row][col].toString());
      }
      // Right edge
      System.out.println("|");
    }
    // Print bottom border
    printBorderRow();
  }
  
  public void printBorderRow() {
    System.out.print("+");
    for(int i = 0 ; i < this.width ; i++) {
      System.out.print("---");
    }
    System.out.println("+");
  }
}



