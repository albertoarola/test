package is.gestionevideo.control;

import java.sql.SQLException;
import java.util.ArrayList;

import is.gestionevideo.database.VideoDAO;
import is.gestionevideo.entity.Sport;
import is.gestionevideo.entity.Video;

public class GestioneVideo {
	ArrayList<Video> lista_video;
	
	
	public GestioneVideo() throws SQLException {
		
		lista_video = VideoDAO.readAll();
	}
	
	public ArrayList<Video> ricercaVideo(Sport s, String testo){
		ArrayList<Video> lista_risultati = new ArrayList<Video>();
		
		for(Video v : lista_video) {
			
			if(v.getSport().equals(s) && testo == null)
				lista_risultati.add(v);
			else
			if(s == null && testo == null)
				lista_risultati.add(v);
			else
			if(s == null && v.getNome().contains(testo))
				lista_risultati.add(v);
			else
			if(v.getSport().equals(s) && v.getNome().contains(testo))
				lista_risultati.add(v);
			
		
		}
		
		return lista_risultati;
	}
	
	public void caricaVideo(Video v) throws SQLException {
		
		lista_video.add(v);
		
		int id = VideoDAO.create(v);
		
		v.setId(id);
	}
	
	
	public void rimuoviVideo(Video v) throws SQLException {
		
		lista_video.remove(v);
		
		VideoDAO.delete(v);
	}	
	
	public void visualizzaVideo(Video v) {
		
	}
	
	public void attivaNotifiche() {
		
	}
	
	public int consultaVisualizzazioni(Video v) {
		return 0;
	}
	
	
	
}
