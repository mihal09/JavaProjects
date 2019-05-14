import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

/**
 * Klasa odpowiedzialna za zapis i odczyt figur z pliku
 */
public class FigureManager {
    private static JSONArray figuresToJson(Figura[] figures){
        if(figures==null)
            return new JSONArray();
        Gson gson = new Gson();
        JSONArray jArrayFigures = new JSONArray();
        for(Figura figure : figures){
            JSONObject jObjectFigure = new JSONObject();
            String json = gson.toJson(figure);
            JSONObject jObjectData = new JSONObject(json);

            jObjectFigure.put("name", figure.getClass().getName());
            jObjectFigure.put("data", jObjectData);

            jArrayFigures.put(jObjectFigure);
        }
        return jArrayFigures;
    }
    private static Figura[] figuresFromJson(JSONArray jArrayFigures){
        Figura[] figures = new Figura[jArrayFigures.length()];
        Gson gson = new Gson();
        for( int i=0; i<jArrayFigures.length(); i++){
            try{
            JSONObject jsonObjectFigure = jArrayFigures.getJSONObject(i);
            String name = jsonObjectFigure.getString("name");
            figures[i] = gson.fromJson(jsonObjectFigure.get("data").toString(), FiguresFabric.getFigure(name, null, null).getClass());
            ((FiguraProstokatna)figures[i]).addVertices();
//            System.out.println(i+" -"+name+" : "+figures[i]);
            }
            catch(Exception ignored){}
        }
        return figures;
    }

    /**
     * zapisuje listę figur do pliku
     * @param file plik do którego zostaną zapisane figury
     * @param figures lista figur
     */
    public static void save(File file, Figura[] figures){
        String extension;
        String fileName = file.getName();
        int i = fileName.lastIndexOf('.');
        if (i > 0 && i < fileName.length() - 1) {
            extension = fileName.substring(i + 1).toLowerCase();
            if(!extension.equals(MyFileFilter.properExtension)){
                fileName = fileName.substring(0,i) + "." + MyFileFilter.properExtension;
            }
        }
        else {
            fileName = fileName + "." + MyFileFilter.properExtension;
        }
        file = new File(file.getParentFile(), fileName);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            String fileContent = figuresToJson(figures).toString();
            bw.write(fileContent);
            bw.close();
        }
        catch(Exception e){
            System.out.println("Blad podczas zapisu");
        }
    }

    /**
     * odczytuje zapisane figury z pliku
     * @param file plik z którego wczytywane są dane
     * @return lista figur
     */
    public static Figura[] load(File file){
        StringBuilder fileContent = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null)
                fileContent.append(st);
            JSONArray jArrayFigures = new JSONArray(fileContent.toString());
            return figuresFromJson(jArrayFigures);
        }
        catch(Exception e){
            System.out.println("Blad podczas wczytywania");
            return null;
        }
    }
}
