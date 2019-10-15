package pt.trainings.maven.cars;


public class Logic {

	VehicleManager manager;
	
	public Logic() {
		manager = new VehicleManager();
		manager.addVehicle(new Car("citroen c4", 3000, "srebrny"));
		manager.addVehicle(new SportCar("bmw g01", 5000, "czarny"));
		manager.addVehicle(new Car("fiat 126P", 2500, "bia³y"));
	}
	
	public boolean printInstructions() {
		try {
			System.out.println();
			System.out.println();
			System.out.println("Wybierz jedn¹ z opcji: ");
			System.out.println("1: Dodaj pojazd");
			System.out.println("2: Usun pojazd");
			System.out.println("3: Edytuj pojazd");
			System.out.println("4: Wypisz pojazdy");
			int choice = Integer.parseInt(System.console().readLine());
			if(choice == 1)
				return addVehicle();
			else if(choice ==2)
				return removeVehicle();
			else if(choice ==3)
				return editVehicle();
			else if(choice ==4) {
				manager.printVehicles();
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {return false;}
	}
	
	private boolean editVehicle() {
		try {
			manager.printVehicles();
			System.out.println("Podaj indeks pojazdu do edycji: ");
			int index = Integer.parseInt(System.console().readLine());
			if(!manager.isIDcorrect(index)) {
				System.out.println("Niepoprawny indeks pojazdu");
				return false;
			}
			System.out.println("Wybierz które pole chcesz edytowac: ");
			System.out.println("1: kolor");
			System.out.println("2: cena");
			int choice = Integer.parseInt(System.console().readLine());
			if(choice == 1) {
				System.out.println("Podaj nowy kolor:");
				String color = System.console().readLine();
				manager.changeColor(index, color);
			}
			else if(choice == 2) {
				System.out.println("Podaj now¹ cenê:");
				int price = Integer.parseInt(System.console().readLine());
				manager.changePrice(index, price);
			}
			else
				return false;
			
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean removeVehicle() {
		manager.printVehicles();
		System.out.println("Podaj indeks pojazdu do usuniêcia: ");
		int index = Integer.parseInt(System.console().readLine());
		if(!manager.isIDcorrect(index)) {
			System.out.println("Niepoprawny indeks pojazdu");
			return false;
		}
		manager.removeVehicle(index);
		return true;
	}

	public boolean addVehicle() {
		try {
		IVehicle vehicle;
		
		System.out.println("Podaj markê pojazdu: ");
		String name = System.console().readLine();
		
		System.out.println("Podaj cenê pojazdu: ");
		int price = Integer.parseInt(System.console().readLine());
		
		System.out.println("Podaj kolor pojazdu: ");
		String color = System.console().readLine();
		
		System.out.println("Czy samochód jest sportowy? wpisz tak/nie ");
		String response = System.console().readLine();
		if(response.equalsIgnoreCase("tak")) {
			vehicle = new SportCar(name, price, color);
		}
		else
			vehicle = new Car(name, price, color);
		
		manager.addVehicle(vehicle);
		return true;
		}
		catch(Exception e) {
			return false;
		}
	}

}
