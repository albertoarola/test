package is.gestionevideo.test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import is.gestionevideo.control.GestioneVideo;
import is.gestionevideo.database.DBManager;
import is.gestionevideo.entity.Sport;
import is.gestionevideo.entity.Video;
import is.gestionevideo.entity.VideoApprofondimento;
import is.gestionevideo.entity.VideoEvento;

public class tester {
	
	GestioneVideo gestione;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		try {
			
			Connection conn = DBManager.getConnection();
			
			String query;
			
			query = "CREATE TABLE VIDEO("
					+" ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
					+" NOME VARCHAR(30),"
					+" DATA DATE,"
					+" SPORT VARCHAR(30),"
					+" TIPO VARCHAR(30)"
					+");";
			
			try(PreparedStatement stmt = conn.prepareStatement(query)) {
				
				stmt.executeUpdate();
			}
			
			
			query = "CREATE TABLE GIORNALISTI("
					+" ID_VIDEO INT NOT NULL,"
					+" NOME VARCHAR(30) NOT NULL,"
					+" COGNOME VARCHAR(30) NOT NULL,"
					+" PRIMARY KEY(ID_VIDEO,NOME,COGNOME)"
					+");";
			
			
			try(PreparedStatement stmt = conn.prepareStatement(query)) {
				
				stmt.executeUpdate();
			}
			
			
			
			System.out.println("Inizializzazione DB completata.");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
		
		try {
			
			Connection conn = DBManager.getConnection();
			
			String query;
			
			query = "DROP TABLE GIORNALISTI; DROP TABLE VIDEO;";

			
			try(PreparedStatement stmt = conn.prepareStatement(query)) {
				
				stmt.executeUpdate();
			}
			
			
			System.out.println("Rimozione DB completata.");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Before
	public void setUp() throws Exception {
		
		gestione = new GestioneVideo();
	}

	@After
	public void tearDown() throws Exception {
		
		gestione = null;
		
		
		Connection conn = DBManager.getConnection();
		
		
		String query = "DELETE FROM VIDEO;";
		
		try(PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.executeUpdate();
		}
		
				
		String query2 = "DELETE FROM GIORNALISTI;";

		try(PreparedStatement stmt2 = conn.prepareStatement(query2)) {
			
			stmt2.executeUpdate();
		}
	}

	@Test
	public void test01UnVideoPerNomeNonTrovato() throws SQLException {
		
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",
										LocalDate.of(2019, Month.AUGUST, 24),
										Sport.CALCIO);
		
		gestione.caricaVideo(v1);
		
		
		ArrayList<Video> v_ricerca = gestione.ricercaVideo(null,"Samp");
		
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v + "/n");
		}
		
		assertEquals(0, v_ricerca.size());
		
		gestione.rimuoviVideo(v1);
		
	}
	
	@Test
	public void test02UnVideoPerSportNonTrovato() throws SQLException {
		
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",
										LocalDate.of(2019, Month.AUGUST, 24),
										Sport.CALCIO);
		
		gestione.caricaVideo(v1);
		
		
		ArrayList<Video> v_ricerca = gestione.ricercaVideo(Sport.FORMULA1, null);
		
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v + "/n");
		}
		
		assertEquals(0, v_ricerca.size());
		
		gestione.rimuoviVideo(v1);
		
	}
	
	@Test
	public void test03UnVideoPerSportENomeNonTrovato() throws SQLException {
		
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",
										LocalDate.of(2019, Month.AUGUST, 24),
										Sport.CALCIO);
		
		gestione.caricaVideo(v1);
		
		
		ArrayList<Video> v_ricerca = gestione.ricercaVideo(Sport.CALCIO,"Samp");
		
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v + "/n");
		}
		
		assertEquals(0, v_ricerca.size());
		
		gestione.rimuoviVideo(v1);
	}
	
	@Test
	public void test04UnVideoPerNomeUnoTrovato() throws SQLException {
		
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",
										LocalDate.of(2019, Month.AUGUST, 24),
										Sport.CALCIO);
		
		gestione.caricaVideo(v1);
		
		
		ArrayList<Video> v_ricerca = gestione.ricercaVideo(null,"Nap");
		
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v + "/n");
		}
		
		assertEquals(1, v_ricerca.size());
		
		gestione.rimuoviVideo(v1);
	}
	
	@Test
	public void test05UnVideoPerSportUnoTrovato() throws SQLException {
		
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",
										LocalDate.of(2019, Month.AUGUST, 24),
										Sport.CALCIO);
		
		gestione.caricaVideo(v1);
		
		
		ArrayList<Video> v_ricerca = gestione.ricercaVideo(Sport.CALCIO, null);
		
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v + "/n");
		}
		
		assertEquals(1, v_ricerca.size());
		
		gestione.rimuoviVideo(v1);
		
	}
	
	@Test
	public void test06UnVideoPerNomeESportUnoTrovato() throws SQLException {
		
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli",
										LocalDate.of(2019, Month.AUGUST, 24),
										Sport.CALCIO);
		
		gestione.caricaVideo(v1);
		
		
		ArrayList<Video> v_ricerca = gestione.ricercaVideo(Sport.CALCIO, "Nap");
		
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v + "/n");
		}
		
		assertEquals(1, v_ricerca.size());
		
		gestione.rimuoviVideo(v1);
		
	}
	
	@Test
	public void test07PiuVideoPerNomeNonTrovato() throws SQLException {
		
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										LocalDate.of(2019, Month.AUGUST, 24), 
										Sport.CALCIO);
		
		VideoEvento v2 = new VideoEvento("Partita Milan-Sampdoria", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.CALCIO);
		
		VideoEvento v3 = new VideoEvento("Partita Juve-Napoli", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.CALCIO);
		
		gestione.caricaVideo(v1);
		gestione.caricaVideo(v2);
		gestione.caricaVideo(v3);
		
		ArrayList<Video> v_ricerca = gestione.ricercaVideo(null, "Inter");
		
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v + "/n");
		}
		
		assertEquals(0, v_ricerca.size());
		
		gestione.rimuoviVideo(v1);
		gestione.rimuoviVideo(v2);
		gestione.rimuoviVideo(v3);
		
		
	}
	
	@Test
	public void test08PiuVideoPerSportNonTrovato() throws SQLException {
		
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										LocalDate.of(2019, Month.AUGUST, 24), 
										Sport.CALCIO);
		
		VideoEvento v2 = new VideoEvento("Partita Milan-Sampdoria", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.CALCIO);
		
		VideoEvento v3 = new VideoEvento("Partita Juve-Napoli", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.CALCIO);
		
		gestione.caricaVideo(v1);
		gestione.caricaVideo(v2);
		gestione.caricaVideo(v3);
		
		ArrayList<Video> v_ricerca = gestione.ricercaVideo(Sport.FORMULA1, null);
		
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v + "/n");
		}
		
		assertEquals(0, v_ricerca.size());
		
		gestione.rimuoviVideo(v1);
		gestione.rimuoviVideo(v2);
		gestione.rimuoviVideo(v3);
	}
	
	@Test
	public void test09PiuVideoPerNomeESportNonTrovato() throws SQLException {
		
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										LocalDate.of(2019, Month.AUGUST, 24), 
										Sport.CALCIO);
		
		VideoEvento v2 = new VideoEvento("Partita Milan-Sampdoria", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.CALCIO);
		
		VideoEvento v3 = new VideoEvento("Partita Juve-Napoli", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.CALCIO);
		
		gestione.caricaVideo(v1);
		gestione.caricaVideo(v2);
		gestione.caricaVideo(v3);
		
		ArrayList<Video> v_ricerca = gestione.ricercaVideo(Sport.FORMULA1, "Inter");
		
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v + "/n");
		}
		
		assertEquals(0, v_ricerca.size());
		
		gestione.rimuoviVideo(v1);
		gestione.rimuoviVideo(v2);
		gestione.rimuoviVideo(v3);
	}
	
	@Test
	public void test10PiuVideoPerNomeUnoTrovato() throws SQLException {
		
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										LocalDate.of(2019, Month.AUGUST, 24), 
										Sport.CALCIO);
		
		VideoEvento v2 = new VideoEvento("Partita Milan-Sampdoria", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.CALCIO);
		
		VideoEvento v3 = new VideoEvento("Partita Juve-Napoli", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.CALCIO);
		
		gestione.caricaVideo(v1);
		gestione.caricaVideo(v2);
		gestione.caricaVideo(v3);
		
		ArrayList<Video> v_ricerca = gestione.ricercaVideo(null, "Samp");
		
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v + "/n");
		}
		
		assertEquals(1, v_ricerca.size());
		
		gestione.rimuoviVideo(v1);
		gestione.rimuoviVideo(v2);
		gestione.rimuoviVideo(v3);
	}
	
	@Test
	public void test11PiuVideoPerSportUnoTrovato() throws SQLException {
		
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										LocalDate.of(2019, Month.AUGUST, 24), 
										Sport.CALCIO);
		
		VideoEvento v2 = new VideoEvento("GP Montecarlo", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.FORMULA1);
		
		VideoEvento v3 = new VideoEvento("GP Italia", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.FORMULA1);
		
		gestione.caricaVideo(v1);
		gestione.caricaVideo(v2);
		gestione.caricaVideo(v3);
		
		ArrayList<Video> v_ricerca = gestione.ricercaVideo(Sport.CALCIO, null);
		
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v + "/n");
		}
		
		assertEquals(1, v_ricerca.size());
		
		gestione.rimuoviVideo(v1);
		gestione.rimuoviVideo(v2);
		gestione.rimuoviVideo(v3);
	}
	
	@Test
	public void test12PiuVideoPerNomeESportUnoTrovato() throws SQLException {
		
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										LocalDate.of(2019, Month.AUGUST, 24), 
										Sport.CALCIO);
		
		VideoEvento v2 = new VideoEvento("GP Montecarlo", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.FORMULA1);
		
		VideoEvento v3 = new VideoEvento("GP Italia", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.FORMULA1);
		
		gestione.caricaVideo(v1);
		gestione.caricaVideo(v2);
		gestione.caricaVideo(v3);
		
		ArrayList<Video> v_ricerca = gestione.ricercaVideo(Sport.CALCIO, "Nap");
		
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v + "/n");
		}
		
		assertEquals(1, v_ricerca.size());
		
		gestione.rimuoviVideo(v1);
		gestione.rimuoviVideo(v2);
		gestione.rimuoviVideo(v3);
	}
	
	@Test
	public void test13PiuVideoPerNomePiuRisultati() throws SQLException {
		
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										LocalDate.of(2019, Month.AUGUST, 24), 
										Sport.CALCIO);
		
		VideoEvento v2 = new VideoEvento("Partita Napoli-Sampdoria", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.CALCIO);
		
		VideoEvento v3 = new VideoEvento("Partita Juve-Napoli", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.CALCIO);
		
		gestione.caricaVideo(v1);
		gestione.caricaVideo(v2);
		gestione.caricaVideo(v3);
		
		ArrayList<Video> v_ricerca = gestione.ricercaVideo(null, "Nap");
		
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v + "/n");
		}
		
		assertEquals(3, v_ricerca.size());
		
		gestione.rimuoviVideo(v1);
		gestione.rimuoviVideo(v2);
		gestione.rimuoviVideo(v3);
	}
	
	@Test
	public void test14PiuVideoApprofondimentoPerNomePiuRisultati() throws SQLException {
		
		VideoApprofondimento v1 = new VideoApprofondimento("Commento Fiorentina-Napoli", 
										LocalDate.of(2019, Month.AUGUST, 24), 
										Sport.CALCIO);
		
		VideoApprofondimento v2 = new VideoApprofondimento("Commento Napoli-Sampdoria", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.CALCIO);
		
		VideoApprofondimento v3 = new VideoApprofondimento("Commento Juve-Napoli", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.CALCIO);
		
		gestione.caricaVideo(v1);
		gestione.caricaVideo(v2);
		gestione.caricaVideo(v3);
		
		ArrayList<Video> v_ricerca = gestione.ricercaVideo(null, "Nap");
		
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v + "/n");
		}
		
		assertEquals(3, v_ricerca.size());
		
		gestione.rimuoviVideo(v1);
		gestione.rimuoviVideo(v2);
		gestione.rimuoviVideo(v3);
	}
	
	@Test
	public void test15PiuVideoMistiPerNomePiuRisultati() throws SQLException {
		
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										LocalDate.of(2019, Month.AUGUST, 24), 
										Sport.CALCIO);
		
		VideoApprofondimento v2 = new VideoApprofondimento("Commento Napoli-Sampdoria", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.CALCIO);
		
		VideoEvento v3 = new VideoEvento("Partita Juve-Napoli", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.CALCIO);
		
		gestione.caricaVideo(v1);
		gestione.caricaVideo(v2);
		gestione.caricaVideo(v3);
		
		ArrayList<Video> v_ricerca = gestione.ricercaVideo(null, "Nap");
		
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v + "/n");
		}
		
		assertEquals(3, v_ricerca.size());
		
		gestione.rimuoviVideo(v1);
		gestione.rimuoviVideo(v2);
		gestione.rimuoviVideo(v3);
	}
	
	@Test
	public void test16NessunVideoPerNome() throws SQLException {
		
		ArrayList<Video> v_ricerca = gestione.ricercaVideo(null, "Nap");
		
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v + "/n");
		}
		
		assertEquals(0, v_ricerca.size());
	}
	
	@Test
	public void test17UnVideoUnRisultato() throws SQLException {
		
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
				LocalDate.of(2019, Month.AUGUST, 24), 
				Sport.CALCIO);
		
		gestione.caricaVideo(v1);
		
		ArrayList<Video> v_ricerca = gestione.ricercaVideo(null, null);
		
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v + "/n");
		}
		
		assertEquals(1, v_ricerca.size());
		
		
	}
	

}
