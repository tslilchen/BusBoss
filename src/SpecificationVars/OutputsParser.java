package SpecificationVars;

import CityComponents.Bus;
import CityComponents.City;
import Lines.BusMover;
import Lines.LineName;

import java.util.HashMap;
import java.util.Map;

public class OutputsParser {
	//TODO: fields should not be public! its just for the test!!!

	public Map<String, String> sysValues;
	public City city;
	int NUM_BUSSES;
	int NUM_RESERVE_BUSSES;
	int NUM_LINES;
	
	public OutputsParser(City city) {
		this.sysValues = new HashMap<>();
		this.city = city;
		this.NUM_BUSSES = city.getNumBusses();
		this.NUM_RESERVE_BUSSES = city.getNumReserveBusses();
		this.NUM_LINES = city.getNumLines();
	}

	private boolean[] parseToArrayOfBooleans(String sysVarName, int numElements) {
		boolean[] result = new boolean[numElements];
    	for(int i=0; i<numElements; i++) {
    		String name = String.format("%s[%d]", sysVarName, i);
    		result[i] = Boolean.parseBoolean(sysValues.get(name));
    	}
    	return result;	
	}
	
	private int[] parseToArrayOfInts(String sysVarName, int numElements) {
		int[] result = new int[numElements];
    	for(int i=0; i<numElements; i++) {
    		String name = String.format("%s[%d]", sysVarName, i);
    		result[i] = Integer.parseInt(sysValues.get(name));
    	}
    	return result;	
	}
	
//	private void updateLineOfReserveBus() {
//		int[] values = parseToArrayOfInts("lineOfReserveBus", NUM_RESERVE_BUSSES);
//		for(int i=0; i<NUM_RESERVE_BUSSES; i++) {
//			Bus bus = city.getBusses().get(i + NUM_RESERVE_BUSSES);
//			if(values[i] == 0) {
//				bus.setLine(city.getLines().get(0));
//			}
//
//			else {
//				bus.setLine(city.getLines().get(1));
//			}
//		}
//	}
	
	private void updateShouldGoToGasStation() {
		boolean[] values = parseToArrayOfBooleans("shouldGoToGasStation", NUM_BUSSES);
		for(int i=0; i<NUM_BUSSES; i++) {
			Bus bus = city.getBusses().get(i);
			bus.setShouldGoToGasStation(values[i]);
			if(bus.getId() == 2 || bus.getId() == 3){
				BusMover busMover = city.getBusMover();
				System.out.println("bus "+ bus.getId() + " should go: " + bus.isShouldGoToGasStation());
				System.out.println("bus "+ bus.getId() + "  at main: " + busMover.isAtMainStationEntrance(bus));
			}
		}
	}
	
	private void updateWaiting() {
		boolean resultA = Boolean.parseBoolean(sysValues.get("waitingA"));
		boolean resultB = Boolean.parseBoolean(sysValues.get("waitingB"));
		city.getLines().get(0).setWaiting(resultA);
		city.getLines().get(1).setWaiting(resultB);
	}
	
	
	
	private void updateExtraBusSentLine() {
		boolean[] values = parseToArrayOfBooleans("extraBusSentLine", city.getNumLines());
		int[] lines = parseToArrayOfInts("lineOfReserveBus", city.getNumReserveBusses());
		boolean sendForA = values[0];
		boolean sendForB = values[1];
		int newLineFor2 = lines[0];
		int newLineFor3 = lines[1];
		HashMap<Integer, Bus> busses = city.getBusses();
		BusMover busMover = city.getBusMover();

		if(sendForA){
			if(newLineFor2==0 && busMover.isAtPrivateParking(busses.get(2))){
				busses.get(2).setLine(city.getLines().get(0));
				busses.get(2).setInUse(true);

			}
			else if(newLineFor3==0 && busMover.isAtPrivateParking(busses.get(3))){
				busses.get(3).setLine(city.getLines().get(0));
				busses.get(3).setInUse(true);
			}
		}

		if(sendForB){
			if(newLineFor2==1 && busMover.isAtPrivateParking(busses.get(2))){
				busses.get(2).setLine(city.getLines().get(1));
				busses.get(2).setInUse(true);

			}
			else if(newLineFor3==1 && busMover.isAtPrivateParking(busses.get(3))){
				busses.get(3).setLine(city.getLines().get(1));
				busses.get(3).setInUse(true);
			}
		}

		System.out.println("sent for A: "+ values[0]);
		System.out.println("sent for B: "+ values[1]);

	}
	
	public void updateStopAtNextStation() {
		boolean[] values = parseToArrayOfBooleans("stopAtNextStation", NUM_BUSSES);
		for(int i=0; i<NUM_BUSSES; i++) {
			Bus bus = city.getBusses().get(i);
			bus.setStopAtNextStation(values[i]);
		}
	}
	
//	private void updateInUse() {
//		boolean[] values = parseToArrayOfBooleans("inUse", NUM_RESERVE_BUSSES);
//		for(int i=0; i<NUM_RESERVE_BUSSES; i++) {
//			Bus bus = city.getBusses().get(i + NUM_RESERVE_BUSSES);
//			bus.setInUse(values[i]);
//		}
//
//		System.out.println("inUse0: "+ values[0]);
//		System.out.println("inUse1: "+ values[1]);
//	}

	private void updateUnstoppedStationsLine(){
		String nameA = "unstoppedStationsLineA";
		int valueA = Integer.parseInt(sysValues.get(nameA));
		System.out.println("unstoppedStationsLineA" + valueA);

		String nameB = "unstoppedStationsLineB";
		int valueB = Integer.parseInt(sysValues.get(nameB));
		System.out.println("unstoppedStationsLineB" + valueB);

	}

	public void parseSysValues(Map<String, String> sysValues) {
		this.sysValues = sysValues;
		updateShouldGoToGasStation();
		updateWaiting();
		updateStopAtNextStation();
		updateExtraBusSentLine();
		updateUnstoppedStationsLine();
		//updateNumOfStopsPassedBus();
	}
		
	

//				lineOfReserveBus
//				shouldGoToGasStation
//				needExtraBusForLine[0]=false, //only need to visualize this
//				extraBusSentLine[1]=true, //do nothing with this
//				stopAtNextStation[0]=false, //visualize + gui should stop when arriving destinationStation
//				inUse[1]=true, lineOfReserveBus[1]=1, // if not in main station, go there. otherwise do nothing
//				unstoppedStationsLineA=0, // only need to visualize this
//				numOfStopsPassedBus2=1, // only need to visualize this


		
	
}
