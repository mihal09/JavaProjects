import java.awt.*;

public class FiguresFabric{
    static FiguraProstokatna getFigure(String name, Point p1, Point p2){
        System.out.println("FABRYKA TWORZY: "+name);
        FiguraProstokatna figure;
        if(name.equals("Prostokat")){
            figure = new Prostokat(p1, p2);
        }
        else if(name.equals("Kolo")) {
            figure = new Kolo(p1, p2);
        }
        else if(name.equals("Wielokat")) {
            figure = new Wielokat(null);
        }
        else
            figure = new Prostokat(p1, p2);
        return figure;
    }
}
