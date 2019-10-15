package pt.trainings.maven.cars;

public class Car implements IVehicle {

	int price;
	String color;
	String name;
	
	public Car(String name, int price, String color) {
		this.name = name;
		this.price = price;
		this.color = color;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
		System.out.println("Zmieniono cenê pojazdu na "+ Integer.toString(price) + " z³");
	}

	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}


	public String getDescription() {
		return "Samochod "+name+", cena: "+ Integer.toString(price)+ "z³, kolor: " + getColor();
	}

}
