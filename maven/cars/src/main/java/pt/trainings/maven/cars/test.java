package pt.trainings.maven.cars;

public class test {
	static Logic logic;
	public static void main(String[] args) {
		logic = new Logic();
		while(true) {
			logic.printInstructions();
		}
//		VehicleManager manager = new VehicleManager();
//		Car car = new Car("Fiat 126P", 500, "zielony");
//		car.setPrice(300);
//		car.setColor("niebieski");
//		manager.addVehicle(car);
//		manager.addVehicle(car);
//		System.out.println(car.getDescription());
//		manager.printVehicles();
	}

}
