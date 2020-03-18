package net.etfbl.lab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.etfbl.lab.dto.Korisnik;

public class KorisnikDAO {

	private static final String LOGIN = "SELECT * FROM korisnik WHERE username=? AND password=?";

	public static Korisnik login(String username, String password) {
		Korisnik user = null;
		Connection conn = null;
		try {
			conn = ConnectionPool.getConnectionPool().checkOut();
			PreparedStatement stmt = conn.prepareStatement(LOGIN);
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				user = new Korisnik(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
			}
			rs.close();
			stmt.close();
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		} finally {
			ConnectionPool.getConnectionPool().checkIn(conn);
		}
		return user;
	}
}
