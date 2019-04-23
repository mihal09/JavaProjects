import java.awt.*;

/**
 * interfejs figury
 */
interface Figura {
    /**
     * przesuwa figurę
     * @param dx zmiana wspolrzednej x
     * @param dy zmiana wspolrzednej y
     */
    void move(int dx, int dy);

    /**
     * przesuwa wierzcholek o indeksie index
     * @param x nowa wspolrzedna x
     * @param y nowa wspolrzedna y
     * @param index numer indeksu wierzchołka
     */
    void moveVertice(int x, int y, int index);

    /**
     * rysuje figure
     * @param g objekt graphics
     */
    void draw(Graphics g);

    /**
     * ustawia kolor figury
     * @param c nowy kolor
     */
    void setColor(Color c);

    /**
     * zwraca, czy punkt o wspolrzędnych (x,y) jest w środku figury
     * @param x
     * @param y
     * @return true, jeśli punkt o wspolrzędnych (x,y) jest w środku figury
     */
    boolean isPointInside(int x, int y);

    /**
     * zwraca indeks najblizszego wierzcholka od punktu (x,y) w okreslonym promieniu
     * @param x wspolrzedna x
     * @param y wspolrzedna y
     * @param radius promien
     * @return indeks wierzcholka jesli jakis jest w promieniu, -1 w przeciwnym wypadku
     */
    int getNearestVertice(int x, int y, int radius);

    /**
     * zwraca typ rysowania figury
     * @return typ rysowania danej figury
     */
    DrawingType getDrawingType();
}
