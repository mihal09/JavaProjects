package pt.trainings.maven.cars;

public class SportCar extends Car {

	public SportCar(String name, int price, String color) {
		super(name, price, color);
		System.out.println("samochod sportowy");
	}
	
	@Override
	public String getDescription() {
		return "Samochod sportowy "+name+", cena: "+ Integer.toString(price)+ "z³, kolor: " + getColor();
	}

}
