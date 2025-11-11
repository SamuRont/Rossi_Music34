import java.sql.*;

public class GestoreDB {
    private static GestoreDB istanza;
    private Connection connessione;

    private GestoreDB() {
        try{
            String urlDb = "jdbc:sqlite:Database/artisti.db";
            connessione = DriverManager.getConnection(urlDb);
        } catch (SQLException e) {
        }
    }

    public static GestoreDB avvia() {
        if(istanza == null)
            istanza = new GestoreDB();
        return istanza;
    }

    private boolean controllaConnessione() {
        try{
            if(connessione == null || !connessione.isValid(5)){
                return false;
            }
        }catch(SQLException e){
            return false;
        }
        return true;
    }

    public String leggiArtistiSalvati(){
        String querySql = "SELECT * FROM artisti";
        controllaConnessione();

        try{
            PreparedStatement comando = connessione.prepareStatement(querySql);
            ResultSet risultati = comando.executeQuery();

            String testoRisultato = "";
            while(risultati.next()){
                testoRisultato += risultati.getString(1) + ".\tNome: ";
                testoRisultato += risultati.getString(2) + ",\tPaese: ";
                testoRisultato += risultati.getString(3) + ",\tGenere: ";
                testoRisultato += risultati.getString(4) + "\n";
            }
            return testoRisultato;
        }catch(SQLException e){
            return null;
        }
    }

    public Boolean salvaArtista(int id, String nome, String paese, String genere){
        String querySql = "INSERT INTO artisti (id, nome, paese, genere) VALUES(?, ?, ?, ?)";

        if(!controllaConnessione())
            return false;

        try{
            PreparedStatement comando = connessione.prepareStatement(querySql);
            comando.setInt(1, id);
            comando.setString(2, nome);
            comando.setString(3, paese);
            comando.setString(4, genere);

            comando.executeUpdate();

            return true;
        }catch(SQLException e){
            return false;
        }
    }

    public boolean salvaCanzone(int id, String titolo, int durata, int anno, int idArtista){
        String querySql = "INSERT INTO canzoni (id, titolo, durata, annoPubblicazione, idArtista) VALUES(?, ?, ?, ?, ?)";

        if(!controllaConnessione())
            return false;

        try{
            PreparedStatement comando = connessione.prepareStatement(querySql);
            comando.setInt(1, id);
            comando.setString(2, titolo);
            comando.setInt(3, durata);
            comando.setInt(4, anno);
            comando.setInt(5, idArtista);

            comando.executeUpdate();
            return true;
        }catch(SQLException e){
            return false;
        }
    }

    public boolean eliminaArtista(int id){
        String querySql = "DELETE FROM artisti WHERE id = ?";

        if(!controllaConnessione())
            return false;

        try{
            PreparedStatement comando = connessione.prepareStatement(querySql);
            comando.setInt(1, id);

            comando.executeUpdate();
            return true;
        }catch(SQLException e){
            return false;
        }
    }

    public boolean eliminaCanzoni(int idArtista){
        String querySql = "DELETE FROM canzoni WHERE idArtista = ?";

        if(!controllaConnessione())
            return false;

        try{
            PreparedStatement comando = connessione.prepareStatement(querySql);
            comando.setInt(1, idArtista);

            comando.executeUpdate();
            return true;
        }catch(SQLException e){
            return false;
        }
    }
}