package is.gestionevideo.database;

import java.time.LocalDate;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import is.gestionevideo.entity.Giornalista;
import is.gestionevideo.entity.Sport;
import is.gestionevideo.entity.Video;
import is.gestionevideo.entity.VideoApprofondimento;
import is.gestionevideo.entity.VideoEvento;

public class VideoDAO {
	
	public static int create(Video v) throws SQLException {
		
		Connection conn = DBManager.getConnection();
		
		int id_video = -1;
		
		String query = "INSERT INTO VIDEO VALUES(NULL,?,?,?,?);";
		
		try(PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.setString(1, v.getNome());
			stmt.setDate(2, Date.valueOf(v.getData()));
			stmt.setString(3, v.getSport().toString());
			
			if(v instanceof VideoEvento ) {
				stmt.setString(4,"EVENTO");
			}
			else if (v instanceof VideoApprofondimento) {
				stmt.setString(4, "APPROFONDIMENTO");
			}
			else
				stmt.setString(4, "");
			
			stmt.executeUpdate();
			
			try(ResultSet r = stmt.getGeneratedKeys()){
				
				if(r.next())
					id_video = r.getInt(1);
			}
				
			}
		
		v.setId(id_video);
		
		return id_video;
		
	}
	
	public static Video createVideo(String nome, LocalDate data, Sport sport) throws SQLException{
		
		Video video = new Video(nome,data,sport);
		create(video);
		return video;
	}
	
	public static VideoEvento createVideoEvento(String nome, LocalDate data, Sport sport) throws SQLException{
		
		VideoEvento v = new VideoEvento(nome,data,sport);
		create(v);
		return v;
	}
	
	public static VideoApprofondimento createVideoApprofondimento(String nome, LocalDate data, Sport sport) throws SQLException{
		
		VideoApprofondimento v = new VideoApprofondimento(nome,data,sport);
		create(v);
		return v;
		
	}
	
	public static Video read(int id) throws SQLException{
		Video v = null;
		
		Connection conn = DBManager.getConnection();
		
		String query = "SELECT NOME,DATA,SPORT,TIPO FROM VIDEO WHERE ID=?;";
		
		try(PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.setInt(1, id);
			
			try(ResultSet r = stmt.executeQuery()){
				if(r.next()) {
					String nome = r.getString(1);
					LocalDate data = r.getDate(2).toLocalDate();
					Sport sport = Sport.valueOf(r.getString(3));
					String tipo = r.getString(4);
					
					if(tipo.equals("EVENTO"))
							v = new VideoApprofondimento(id,nome,data,sport);
					
					else if(tipo.equals("APPROFONDIMENTO")) {
						v = new VideoApprofondimento(id,nome,data,sport);
						
						String query2 = "SELECT NOME,COGNOME FROM GIORNALISTI WHERE ID_VIDEO=?;";
						
						try(PreparedStatement stmt2 = conn.prepareStatement(query2)){
							stmt2.setInt(1,id);
							
							try(ResultSet r2 = stmt2.executeQuery()){
								while(r2.next()){
									String nome_g = r2.getString(1);
									String cognome_g = r2.getString(2);
									
									Giornalista g = new Giornalista(nome_g,cognome_g);
									((VideoApprofondimento)v).addGiornalista(g);
								}
							}
						}
					}
					else
						v = new Video(id,nome,data,sport);
				
					
			}
			
			
			
	}
	
	
}
		return v;
}
	
	public static ArrayList<Video> readAll() throws SQLException{
		
		ArrayList<Video> list = new ArrayList<Video>();
		Connection conn = DBManager.getConnection();
		String query = "SELECT ID,NOME,DATA,SPORT,TIPO FROM VIDEO;";
		
		try(PreparedStatement stmt = conn.prepareStatement(query)){
			try(ResultSet r = stmt.executeQuery()){
				while(r.next()) {
					int id = r.getInt(1);
					String nome = r.getString(2);
					LocalDate data = r.getDate(3).toLocalDate();
					Sport sport = Sport.valueOf(r.getString(4));
					String tipo = r.getString(5);
					
					Video v = null;
					
					if(tipo.equals("EVENTO"))
						v = new VideoEvento(id,nome,data,sport);
					
					else if(tipo.equals("APPROFONDIMENTO"))
						v = new VideoApprofondimento(id,nome,data,sport);
					
					else
						v = new Video(id,nome,data,sport);
					
					list.add(v);
				}
			}
			
		}
		return list;
	}
	
	public static void update(Video v) throws SQLException{
		Connection conn = DBManager.getConnection();
		int id_video = v.getId();
		String query = "UPDATE VIDEO SET NOME=?, DATA=?, SPORT=?, TIPO=? WHERE ID=?;";
		
		try(PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setString(1,v.getNome());
			stmt.setDate(2,Date.valueOf(v.getData()));
			stmt.setString(3,v.getSport().toString());
			
			if(v instanceof VideoEvento)
				stmt.setString(4,"EVENTO");
			
			else if(v instanceof VideoApprofondimento)
				stmt.setString(4,"APPROFONDIMENTO");
			
			else stmt.setString(4,"");
			
			stmt.setInt(5,id_video);
			
			stmt.executeUpdate();
			
		}
		
		if(v instanceof VideoApprofondimento) {
			
			
			
			String query2 = "DELETE FROM GIORNALISTI WHERE ID_VIDEO=?;";
			
			try(PreparedStatement stmt2 = conn.prepareStatement(query2)) {
				
				stmt2.setInt(1, id_video);
				
				stmt2.executeUpdate();
			}
			
			
			
			String query3 = "INSERT INTO GIORNALISTI VALUES(?,?,?);";
			
			try(PreparedStatement stmt3 = conn.prepareStatement(query3)) {
				
				ArrayList<Giornalista> lista_giornalisti = ((VideoApprofondimento) v).getGiornalisti();
				
				for(Giornalista g : lista_giornalisti) {
					
					String nome_g = g.getNome();
					String cognome_g = g.getCognome();
				
					stmt3.setInt(1, id_video);
					stmt3.setString(2, nome_g);
					stmt3.setString(3, cognome_g);
				
					stmt3.executeUpdate();
				}
			}
			
		}
	}
	
	public static void delete(Video v) throws SQLException{
		Connection conn = DBManager.getConnection();
		
		int id_video = v.getId();
		
		String query = "DELETE FROM VIDEO WHERE ID=?;";
		
		try(PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setInt(1,id_video);
			stmt.executeUpdate();
			
		}
		
		if(v instanceof VideoApprofondimento) {
			String query2 = "DELETE FROM GIORNALISTI WHERE ID_VIDEO=?;";
			try(PreparedStatement stmt2 = conn.prepareStatement(query2)){
				stmt2.setInt(1,id_video);
				stmt2.executeUpdate();
			}
		}
		
	}
	
}
	
	


