package net.etfbl.lab.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import net.etfbl.lab.dto.Obaveza;

public class ObavezeDAO {

	private static final String INSERT = "INSERT INTO planer_obaveza.obaveza (opis, datum, kategorija, adresaKorisnika) VALUES (?, ?, ?, ?)";
	private static final String SELECT = "SELECT * FROM planer_obaveza.obaveza WHERE datum>? order by datum";
	private static final String DELETE = "DELETE FROM planer_obaveza.obaveza WHERE id=?";

	public static boolean unesiObavezu(Obaveza obaveza) {
		Connection conn = null;
		try {
			conn = ConnectionPool.getConnectionPool().checkOut();
			PreparedStatement stmt = conn.prepareStatement(INSERT);
			stmt.setString(1, obaveza.getOpis());
			stmt.setDate(2, new Date(obaveza.getDatum().getTime()));
			stmt.setString(3, obaveza.getKategorija());
			stmt.setString(4, obaveza.getAdresa());
			stmt.executeUpdate();
			stmt.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			return false;
		} finally {
			ConnectionPool.getConnectionPool().checkIn(conn);
		}
	}

	public static ArrayList<Obaveza> ocitajObavezePoDatumu(String datum) {
		ArrayList<Obaveza> obaveze = new ArrayList<>();
		Connection conn = null;
		try {
			conn = ConnectionPool.getConnectionPool().checkOut();
			PreparedStatement stmt = conn.prepareStatement(SELECT);
			stmt.setObject(1, datum);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				obaveze.add(
						new Obaveza(rs.getInt(1), rs.getString(2), rs.getDate(3), rs.getString(4), rs.getString(5)));
			}
			rs.close();
			stmt.close();
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		} finally {
			ConnectionPool.getConnectionPool().checkIn(conn);
		}
		return obaveze;
	}

	public static void obrisiObavezu(int id) {
		Connection conn = null;
		try {
			conn = ConnectionPool.getConnectionPool().checkOut();
			PreparedStatement stmt = conn.prepareStatement(DELETE);
			stmt.setInt(1, id);
			stmt.executeUpdate();
			stmt.close();
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		} finally {
			ConnectionPool.getConnectionPool().checkIn(conn);
		}
	}
}
