package colors.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.LogManager;

import colors.interfaces.Factory;

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
	
	public static String readFile(final File file) {
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

	/**
	 * Returns a Factory<T> that only ever returns <code>object</code>.
	 */
	public static <T> Factory<T> staticFactory(final T object) {
		return new Factory<T>(){
			@Override
			public T instantiate() {
				return object;
			}
		};
	}
	
	public static abstract class SmartStaticFactory<T> implements Factory<T> {
		private int instantiationRequests = 0;
		private T obj = null;
		@Override
		public T instantiate() {
			System.out.println("Instance requested for " + this.getClass().getName());
			instantiationRequests++;
			if(obj == null)
				obj = instantiate_();
			return obj;
		}
		
		protected abstract T instantiate_();
		
		public int instantiationRequests() {
			return instantiationRequests;
		}
	}

	public static void initLogging() {
		try {
			LogManager.getLogManager().readConfiguration(new FileInputStream("logging.properties"));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
