import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;


public class MyFileFilter extends FileFilter {
    public final static String properExtension = "guru";

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
            if (extension.equals(properExtension)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Pliki .guru";
    }
}