package disease;

public class Host {
	  public  boolean hasMoved; // Marker if the host has tried moving this turn
	  private int  healthPoints;
	  private int  maxSpeed;
	  private char sirState; // 'S', 'I', 'R' for susceptible, infectious, recovered
	  private int  infectTime;
	// Default constructor
	  Host() {
	    this.hasMoved     = false;
	    this.healthPoints = 100  ;
	    this.maxSpeed     = 3    ;
	    this.sirState     = 'S'  ;
	    this.infectTime   = 0    ;
	  }
	  
	  // Constructor by all parameters
	  Host(int hp, int spd, char status, int ctr) {
	    this.hasMoved     = false ;
	    this.healthPoints = hp    ;
	    this.maxSpeed     = spd   ;
	    this.sirState     = status;
	    this.infectTime   = ctr   ;
	  }
	// Getters
	  public int getHP() {
	    return this.healthPoints;
	  }
	  public int getSpeed() {
	    return this.maxSpeed;
	  }
	  public char getSIR() {
	    return this.sirState;
	  }    
	  public int getInfectTime() {
	    return this.infectTime;
	  }
	// Setters
	  public void setHP(int points) {
	    this.healthPoints = points;
	  }
	  public void setSpeed(int spd) {
	    this.maxSpeed = spd;
	  }
	  public void setSIRState(char state) {
	    this.sirState = state;
	  }
	  public void setInfectTime(int duration) {
	    this.infectTime = duration;
	  }
	  public String toString() {
		    if(this.sirState == 'S') {
		      return("S");
		    }
		    else if( this.sirState == 'I' ) {
		      return("I");
		    }
		    else {
		      return("R");
		    }
		  }
	// infect() - Attempts to infect a target Host
	  public void infect(Host tgtHost) {
	    if(tgtHost.getSIR() == 'S') {
	      // Attempt to infect
	      int damage = (int)(Math.random()*50);
	      tgtHost.setHP(tgtHost.getHP() - damage);
	      if(tgtHost.getHP() <= 0) {
	        // Infected!
	        tgtHost.gotInfected();
	      }
	    }
	    else {
	      // Nothing happens, target is already infected or immune
	    }
	  } // Closing infect()

	  // gotInfected() - Comprehensive setter simulating infection
	  public void gotInfected() {
	    this.healthPoints = 0  ;
	    this.sirState     = 'I';
	    this.infectTime   = 14 ;
	  } // Closing gotInfected()





	}


