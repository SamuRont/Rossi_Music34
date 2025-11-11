import java.util.List;

public class Artista {
    public int idArtista;
    public String nomeDArte;
    public String paeseOrigine;
    public String genereMusicale;
    public List<Canzone> brani;

    public Artista(int id, String nome, String paese, String genere, List<Canzone> listaBrani) {
        this.idArtista = id;
        this.nomeDArte = nome;
        this.paeseOrigine = paese;
        this.genereMusicale = genere;
        this.brani = listaBrani;
    }

    public Artista(String nome, String paese, String genere) {
        this.nomeDArte = nome;
        this.paeseOrigine = paese;
        this.genereMusicale = genere;
    }

    public Artista(){}

    public String toString(){
        String info = "";
        for (Canzone branoSingolo : brani)
            info += branoSingolo.toString();

        return info;
    }
}