import java.io.File;
import javax.swing.filechooser.FileFilter;


/**
 * klasa odpowiedzialna za filtrację plików
 */
public class MyFileFilter extends FileFilter {
    final static String properExtension = "guru";

    /**
     * zwraca, czy dany plik ma odpowiednie rozszerzenie
     * @param f plik
     * @return True, jeśli plik ma odpowiednie rozszerzenie
     * False, w przeciwnym wypadku
     */
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            extension = s.substring(i + 1).toLowerCase();
        }

        if (extension != null) {
            return extension.equals(properExtension);
        }
        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Pliki .guru";
    }
}