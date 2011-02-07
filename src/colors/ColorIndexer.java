package colors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class ColorIndexer {
	private static String readFile(final File file) {
		StringBuilder text = new StringBuilder();
		try {
			BufferedReader r = new BufferedReader(new FileReader(file));
			String tmp = null;
			while( (tmp=r.readLine()) != null) {
				text.append(tmp);
				text.append("\n");
			}
			r.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text.toString();
	}
	
	public static void main(String[] args) {
		final String colorsDir = "/home/josh/school/cs673/data/colors";
		final ColorDB db = new ColorDB("/home/josh/school/cs673/colors.db");
		
		for(File subdir : new File(colorsDir).listFiles()) {
			for(File jsonFile : subdir.listFiles()) {
				try {
					final JSONObject color = new JSONObject(readFile(jsonFile));
					db.indexColor(color);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
//		try {
//			db.conn.commit();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
	}
}
