package pt.trainings.maven.cars;

import java.util.ArrayList;

public class VehicleManager {

	ArrayList<IVehicle> vehicles;
	
	public VehicleManager() {
		vehicles = new ArrayList<IVehicle>();
	}
	
	public void addVehicle(IVehicle vehicle) {
		vehicles.add(vehicle);
	}
	
	public void removeVehicle(int index) {
		try {
		vehicles.remove(index);
		}
		catch(Exception e){
			System.out.println("Indeks poza zasiegiem!");
		}
	}
	
	public void changePrice(int index, int price) {
		try {
			vehicles.get(index).setPrice(price);
		}
		catch(Exception e){
			System.out.println("Indeks poza zasiegiem!");
		}
	}
	
	public void changeColor(int index, String color) {
		try {
			vehicles.get(index).setColor(color);
		}
		catch(Exception e){
			System.out.println("Indeks poza zasiegiem!");
		}
	}
	
	public boolean isIDcorrect(int id) {
		return (id >= 0 && id < vehicles.size());
	}
	
	public void printVehicles() {
		for(int i=0; i<vehicles.size(); i++) {
			System.out.println("Nr " + Integer.toString(i) + " " + vehicles.get(i).getDescription() );
		}
	}

	

}
