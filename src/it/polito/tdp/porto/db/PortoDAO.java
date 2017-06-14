package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.AuthorIdMap;
import it.polito.tdp.porto.model.Paper;
import it.polito.tdp.porto.model.PaperIdMap;

public class PortoDAO {
	
	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public List<Author> getAllAutori(AuthorIdMap authorIdMap) {

		String sql = "SELECT id, lastname, firstname " + "FROM author " + "ORDER BY lastname ASC ";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			List<Author> autori = new ArrayList<Author>();
			
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				Author a = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				a = authorIdMap.put(a);
				autori.add(a) ;
			}
			st.close();
			conn.close();
			
			return autori;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public List<Paper> getAllArticoli(PaperIdMap paperIdMap) {

		String sql = "SELECT eprintid, title, issn,  publication,  type,  types " + "FROM paper " + "ORDER BY title ASC ";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			List<Paper> articoli = new ArrayList<Paper>();
			
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				Paper p = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				p = paperIdMap.put(p);
				articoli.add(p) ;
			}
			st.close();
			conn.close();
			
			return articoli;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public List<Paper> getArticoliPerAutore(Author autore, PaperIdMap paperIdMap) {

		final String sql = "SELECT p.eprintid, p.title, p.issn, p.publication, p.`type`, p.types FROM paper p, creator c WHERE p.eprintid=c.eprintid AND authorid = ? ";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, autore.getId());
			
			List<Paper> articoli = new ArrayList<Paper>();

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Paper p = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				p = paperIdMap.put(p);
				articoli.add(p) ;
			}
			st.close();
			conn.close();
			
			return articoli;
			
		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
		
	public List<Author> getAutoriPerArticolo(Paper paper, AuthorIdMap authorIdMap) {

		final String sql = "SELECT a.id, a.lastname, a.firstname FROM author a, creator c WHERE a.id=c.authorid AND eprintid = ? ";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, paper.getEprintid());

			List<Author> autori = new ArrayList<Author>();
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Author a = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				a = authorIdMap.put(a);
				autori.add(a) ;
			}
			st.close();
			conn.close();

			return autori;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}		
	}
	
}