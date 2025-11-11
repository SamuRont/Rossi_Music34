public class Canzone {
    public int idCanzone;
    public String titoloBrano;
    public int durataSecondi;
    public int annoUscita;
    public Artista artistaProprietario;

    public String toString() {
        return idCanzone + "\t" + titoloBrano + "\t" + durataSecondi + "\t" + annoUscita + "\n";
    }

    public Canzone(int id, String titolo, int durata, int anno, Artista artista) {
        this.idCanzone = id;
        this.titoloBrano = titolo;
        this.durataSecondi = durata;
        this.annoUscita = anno;
        this.artistaProprietario = artista;
    }

    public Canzone(){}
}