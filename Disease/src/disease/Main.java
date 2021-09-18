package disease;

import java.util.concurrent.TimeUnit;

public class Main {
  public static void main(String[] args) {
    Board mainBoard = new Board(20, 20, 50);
    mainBoard.renderBoard();
    
    while(mainBoard.hasInfected()) {
      // Pause for 0.5 seconds
      try {
        java.util.concurrent.TimeUnit.MILLISECONDS.sleep(500);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
      
      // Then advance simulation one timeslice
      mainBoard.advanceTimeslice();
      // And check results
      mainBoard.renderBoard();

    }
    System.out.println("Simulation complete");
  }
}
