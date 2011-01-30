package colors;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Util {
	public static String readFile(final String filename) {
		final StringBuilder file = new StringBuilder();
		try {
			final BufferedReader r = new BufferedReader(new FileReader(filename));
			String tmp = null;
			while( (tmp=r.readLine()) != null) {
				file.append(tmp);
				file.append("\n");
			}
			r.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.toString();
	}
	
	public static void serialize(final Object obj, final String filename) {
		try {
			FileOutputStream f_out = new FileOutputStream(filename);
			ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
			obj_out.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
