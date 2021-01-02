package uk.ac.brunel.cs1702;

import java.util.List;
import java.util.ArrayList;

public class GalaxyExplorer {
	
	public int[][] GalaxyArray;
	//Storing in a list of strings the enemy ships we've encountered to not have any duplicates results 
	public List<String> enemyShipsEncountered = new ArrayList<String>(); 
	//Current location of the ship
	public int locX = 0;
	public int locY = 0;
	public String facingDirection = "N";
	
	//Initializing the galaxy array
	public GalaxyExplorer(int x, int y, String enemyShips){ 
		GalaxyArray = new int[x][y];
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				GalaxyArray[i][j] = 0;
			}
		}
		//Bringing enemy ship in galaxy array if there are any
		if (enemyShips != "") {
			String[] enemyShipsLocations = enemyShips.split("\\((.*?)\\)*");
			for (int i = 1; i < enemyShipsLocations.length; i++) {
				String[] esLocation = enemyShipsLocations[i].replace("(", "").replace(")", "").split(",");
				GalaxyArray[Integer.parseInt(esLocation[0])][Integer.parseInt(esLocation[1])] = -1;
			}
		}
	}
	
	public String moveShip(String towards) {
		//Make local variables in order to decline the movement in case there's any enemy ship on the new location
		int newLocationX = this.locX;
		int newLocationY = this.locY;
		//Based on the direction specified, we will act accordingly
		switch (towards) {
			case "l":
				facingDirection = getCounterClockwiseMovement(this.facingDirection);
				break;
			case "r":
				facingDirection = getClockwiseMovement(this.facingDirection);
				break;
			case "f":
				int[] resultf =  getForwardMovement(this.facingDirection, this.locX, this.locY, GalaxyArray.length, GalaxyArray[0].length);
				newLocationX = resultf[0];
				newLocationY = resultf[1];
				break;
			case "b":
				int[] resultb =  getBackwardMovement(this.facingDirection, this.locX, this.locY, GalaxyArray.length, GalaxyArray[0].length);
				newLocationX = resultb[0];
				newLocationY = resultb[1];
				break;
		}
		//If there's an enemy ship at the new location, return the enemy ship's location and don't change the current position
		if (GalaxyArray[newLocationX][newLocationY] == -1) {
			return "(" + newLocationX + ";" + newLocationY + ")";
		}
		else {
			//Otherwise update the ship's location 
			this.locX = newLocationX;
			this.locY = newLocationY;
		}
		return null;
	}
	
	//CLOCKWISE MOVEMENT
	public String getClockwiseMovement(String facing) {
		switch (facing) {
			case "N":
				facing = "E";
				break;
			case "E":
				facing = "S";
				break;
			case "S":
				facing = "W";
				break;
			case "W":
				facing = "N";
				break;
		}
		return facing;
	}
	
	//COUNTER CLOCKWISE MOVEMENT
	public String getCounterClockwiseMovement(String facing) {
		switch (facing) {
			case "N":
				facing = "W";
				break;
			case "E":
				facing = "N";
				break;
			case "S":
				facing = "E";
				break;
			case "W":
				facing = "S";
				break;
		}
		return facing;
	}
	
	//FORWARD MOVEMENT
	public int[] getForwardMovement(String facing, int x, int y, int totalX, int totalY) {
		switch (facing) {
			case "S":
				if (y - 1 < 0) { y = totalY - 1; }
				else { y--; }
				break;
			case "E":
				if (x + 1 >= totalX) { x = 0; }
				else { x++; }
				break;
			case "N":
				if (y + 1 >= totalY) { y = 0; }
				else { y++; }
				break;
			case "W":
				if (x - 1 < 0) { x = totalX - 1; }
				else { x--; }
				break;
		}
		return new int[] { x, y };
	}
	
	//BACK MOVEMENT
	public int[] getBackwardMovement(String facing, int x, int y, int totalX, int totalY) {
		switch (facing) {
			case "N":
				if (y - 1 < 0) { y = totalY - 1; }
				else { y--; }
				break;
			case "W":
				if (x + 1 >= totalX) { x = 0; }
				else { x++; }
				break;
			case "S":
				if (y + 1 >= totalY) { y = 0; }
				else { y++; }
				break;
			case "E":
				if (x - 1 < 0) { x = totalX - 1; }
				else { x--; }
				break;
		}
		return new int[] { x, y };
	}
	
	public String executeCommand(String command){
		for (int i = 0; i < command.length(); i++) {
			String movementResult = moveShip(command.substring(i, i + 1));
			//If the ship has encountered any enemy ships on the way, it will return the enemy ship's location
			if (movementResult != null) {
				//Checking if the enemy ship has already encountered doesn't contain the current one
				if (!enemyShipsEncountered.contains(movementResult)) {
					enemyShipsEncountered.add(movementResult);
				}
			}
		}
		//Returning the current location and all the encountered enemy ships in full order
		return "(" + locX + ";" + locY + ";" + facingDirection + ")" + String.join("", enemyShipsEncountered);
	}
}