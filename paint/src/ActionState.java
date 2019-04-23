/**
 * enum możliwych typów akcji
 */
public enum ActionState {
    /**
     * brak żadnej akcji
     */
    NOTHING,
    /**
     * czeka na tworzenie nowej figury
     */
    CREATING,
    /**
     * rysuje nową figurę (użytkownik przeciąga myszka)
     */
    PAINTING,
    /**
     * zaznacza punkty (np. do wielokąta)
     */
    SELECTING_POINTS,
    /**
     * czeka na edycję figury
     */
    EDITING,
    /**
     * rozszerza figurę
     */
    RESIZING,
    /**
     * przesuwa figurę
     */
    MOVING
}
