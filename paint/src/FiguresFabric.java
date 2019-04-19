import java.awt.*;

public class FiguresFabric{
    static FiguraProstokatna getFigure(String name, Point p1, Point p2){
        if(name == "Prostokat")
            return new Prostokat(p1, p2);
        else if(name == "Kolo")
            return new Kolo(p1, p2);
        else if(name == "Wielokat")
            return new Wielokat(null);
;        return new Prostokat(p1, p2);
    }
}
