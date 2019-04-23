import java.awt.*;

/**
 * klasa odpowiedzialna za wywoływanie konstruktorów figur
 */
public class FiguresFabric{
    /**
     * tworzy nową figurę danego typu i zwraca ją
     * @param name nazwa figury
     * @param p1 punkt pierwszego naroznika
     * @param p2 punkt drugiego naroznika
     * @return
     */
    public static FiguraProstokatna getFigure(String name, Point p1, Point p2){
//        System.out.println("FABRYKA TWORZY: "+name);
        FiguraProstokatna figure;
        switch (name) {
            case "Prostokat":
                figure = new Prostokat(p1, p2);
                break;
            case "Kolo":
                figure = new Kolo(p1, p2);
                break;
            case "Wielokat":
                figure = new Wielokat(null);
                break;
            default:
                figure = new Prostokat(p1, p2);
                break;
        }
        return figure;
    }
}
