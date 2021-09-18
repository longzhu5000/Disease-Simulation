package disease;

public class Tile {
	  public  boolean isOccupied;
	  private Host    occupant;
	  private int     contamination;
	// Default constructor
	  Tile() {
	    this.isOccupied    = false;
	    this.contamination = 0;
	    this.occupant      = null;
	  }
	// Getters
	  public Host getOccupant() {
	    return this.occupant;
	  }
	  public int getContamination() {
	    return this.contamination;
	  }
	// Setters
	  public void setOccupant(Host guest) {
	    this.occupant = guest;
	  }
	  public void setContamination(int contaminate) {
	    this.contamination = contaminate;
	  }
	// acceptHost() - If a host is moved to this tile
	  public void acceptHost(Host guest) {
	    this.isOccupied = true;
	    this.occupant   = guest;
	  }
	  
	  // sendHost() - If a host leaves this tile
	  public void sendHost() {
	    this.isOccupied = false;
	    this.occupant   = null;
	  }

	  // toString() - Renders the tile
	  public String toString() {
	    if(this.isOccupied) {
	      return this.occupant.toString();
	    }
	    return " ";
	  }
	// endTurn() - What the tile does at the end of the turn
	  public void endTurn() {
	    if(!isOccupied) {
	      // Contamination decreases
	      this.contamination -= 25;
	      if( this.contamination < 0 ) {
	        this.contamination = 0;
	      }
	      return;
	    }

	    // If the occupant is susceptible
	    if(this.occupant.getSIR() == 'S') {
	      // Attempt to infect
	      this.occupant.setHP((this.occupant.getHP() - this.contamination));
	      if(this.occupant.getHP() <= 0) {
	        // Infected!
	        this.occupant.gotInfected();
	      }
	    }
	    // If the occupant is infected (could be same turn)
	    if(this.occupant.getSIR() == 'I') {
	      this.contamination += 25;
	      this.occupant.setInfectTime(this.occupant.getInfectTime() - 1);
	      if(this.occupant.getInfectTime() <= 0) {
	        // Recovered!
	        this.occupant.setSIRState('R');
	      }
	    }
	    else if(this.occupant.getSIR() == 'R') {
	      // Contamination decreases
	      this.contamination -= 25;
	      if(this.contamination < 0) {
	        this.contamination = 0;
	      }
	    }
	  }





	}

