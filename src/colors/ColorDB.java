package colors;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import colors.artefacts.NamedColor;

public class ColorDB {
	private static final String createUsers = "create table users("
		+ "id integer primary key autoincrement,"
		+ "username varchar(128) not null);";
	private static final String createColors = "create table colors("
		+ "id integer primary key autoincrement,"
		+ "user_id integer not null references users(id),"
		+ "r integer not null,"
		+ "g integer not null,"
		+ "b integer not null,"
		+ "name varchar(256) not null);";
	private final Connection conn;
	public ColorDB(final String dbname) {
		Connection c = null;
		final boolean mustInit = !(new File(dbname).exists());
	    try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + dbname);
//				c.setAutoCommit(false);
			Statement s = c.createStatement();
			if(mustInit) {
				s.executeUpdate(createUsers);
				s.executeUpdate(createColors);
				s.executeUpdate("create index username_idx on users(username);");
				s.executeUpdate("create index r_idx on colors(r);");
				s.executeUpdate("create index g_idx on colors(g);");
				s.executeUpdate("create index b_idx on colors(b);");
				s.executeUpdate("create index name_idx on colors(name);");
//					c.commit();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.conn = c;
		
	}
	
	public Iterator<Integer> getUserColorIDs(final int user_id) {
//			select hex from colors,users where colors.user_id=users.id and users.username="Nice Autumn";
		Iterator<Integer> result = null;
		try {
			Statement s = conn.createStatement();
			final String sql = "select colors.id as color_id from colors,users where colors.user_id=users.id and users.id=" +  user_id + " order by random();";
			final ResultSet rs = s.executeQuery(sql);
			result = new Iterator<Integer>() {
				@Override
				public boolean hasNext() {
					try {
						return rs.next();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return false;
				}

				@Override
				public Integer next() {
					try {
						return rs.getInt("color_id");
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return null;
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public NamedColor getColor(final int colorID) {
		NamedColor color = null;
		try {
			Statement s = conn.createStatement();
			final String sql = "select r, g, b, title from colors where colors.id=" + colorID + ";";
			ResultSet rs = s.executeQuery(sql);
			if(rs.next()) {
				final int r = rs.getInt("r");
				final int g = rs.getInt("g");
				final int b = rs.getInt("b");
				final String name = rs.getString("title");
				color = new NamedColor(r, g, b, name);
			} else throw new IllegalArgumentException();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return color;
	}
	
	private boolean userExists(final String userName) {
		try {
			Statement s = conn.createStatement();
			final String sql = "select * from users where username=\"" + userName + "\";";
//				System.out.println(sql);
			ResultSet rs = s.executeQuery(sql);
			return rs.next();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void addUser(final String userName) {
		try {
			Statement s = conn.createStatement();
			final String sql = "insert into \"users\" (\"username\") values(\"" + userName + "\");";
			System.out.println(sql);
			s.executeUpdate(sql);
//				s.close();
//				this.conn.commit();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int userID(final String userName) {
		System.out.println(userName);
		if(!userExists(userName))
			addUser(userName);
		
		try {
			Statement s = conn.createStatement();
			final String sql = "select * from users where username=\"" + userName + "\";";
//				System.out.println(sql);
			ResultSet rs = s.executeQuery(sql);
			return rs.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException("Problem");
	}
	
	public boolean colorExists(final String name) {
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("select * from colors where name=\"" + name + "\";");
			return rs.next();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private int i = 0;
	public void addColor(final int creator_id, final int r, final int g, final int b, final String title) {
		try {
			Statement s = conn.createStatement();
			final String sql = "insert into colors(user_id,r,g,b,name) values(" + creator_id + "," + r + "," + g + "," + b + ",\"" + title + "\");";
			System.out.println(sql);
			s.executeUpdate(sql);
			i++;
			if(i % 1000 == 0 && i > 1000)
				conn.commit();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void indexColor(final JSONObject color) {
		try {
			final String userName = color.getString("userName");
			final JSONObject rgb = color.getJSONObject("rgb");
			final int r = rgb.getInt("red");
			final int g = rgb.getInt("green");
			final int b = rgb.getInt("blue");
			final String title = color.getString("title");
			System.out.print(userName + ":" + title+ " ");
//				final String description = color.getString("description");
			
			if(!colorExists(title)) {
				final int userID = userID(userName);
				addColor(userID, r, g, b, title);
			}
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}
	
	public Connection connection() {
		return conn;
	}
}