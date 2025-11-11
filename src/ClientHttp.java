import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ClientHttp {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String urlRadice = "http://localhost:4567/api";
    private final Gson parserJson = new Gson();

    private HttpResponse<String> eseguiRichiesta(HttpRequest richiesta){
        HttpResponse<String> risposta;
        try{
            risposta = client.send(richiesta, HttpResponse.BodyHandlers.ofString());
        }catch(IOException | InterruptedException e){
            throw new RuntimeException(e);
        }
        return risposta;
    }

    public String controllaStato() {
        String urlCompleta = urlRadice + "/health";

        HttpRequest richiestaGet = preparaGet(urlCompleta);
        HttpResponse<String> risposta = eseguiRichiesta(richiestaGet);

        return risposta.body();
    }

    public List<Artista> ottieniArtisti(){
        String urlCompleta = urlRadice + "/artisti";

        HttpRequest richiestaGet = preparaGet(urlCompleta);
        HttpResponse<String> risposta = eseguiRichiesta(richiestaGet);

        return parserJson.fromJson(risposta.body(), new TypeToken<List<Artista>>(){}.getType());
    }

    public Artista ottieniArtistaPerId(int id){
        String urlCompleta = urlRadice + "/artisti/"+id;

        HttpRequest richiestaGet = preparaGet(urlCompleta);
        HttpResponse<String> risposta = eseguiRichiesta(richiestaGet);

        return parserJson.fromJson(risposta.body(), Artista.class);
    }

    public List<Canzone> ottieniCanzoniPerArtista(int idArtista){
        String urlCompleta = urlRadice + "/artisti/" + idArtista + "/canzoni";

        HttpRequest richiestaGet = preparaGet(urlCompleta);
        HttpResponse<String> risposta = eseguiRichiesta(richiestaGet);

        return parserJson.fromJson(risposta.body(), new TypeToken<List<Canzone>>(){}.getType());
    }

    public List<Canzone> ottieniTutteCanzoni(){
        String urlCompleta = urlRadice + "/canzoni";

        HttpRequest richiestaGet = preparaGet(urlCompleta);
        HttpResponse<String> risposta = eseguiRichiesta(richiestaGet);

        return parserJson.fromJson(risposta.body(), new TypeToken<List<Canzone>>(){}.getType());
    }

    public Canzone ottieniCanzonePerId(int idCanzone){
        String urlCompleta = urlRadice + "/canzoni/"+idCanzone;

        HttpRequest richiestaGet = preparaGet(urlCompleta);
        HttpResponse<String> risposta = eseguiRichiesta(richiestaGet);
        return parserJson.fromJson(risposta.body(), Canzone.class);
    }

    private HttpRequest preparaGet(String url) {
        return HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(java.net.URI.create(url))
                .GET()
                .build();
    }

    public String inserisciNuovoArtista(String datiJson){
        String urlCompleta = urlRadice + "/artisti";

        HttpRequest richiestaPost = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(java.net.URI.create(urlCompleta))
                .POST(HttpRequest.BodyPublishers.ofString(datiJson))
                .build();

        HttpResponse<String> risposta = eseguiRichiesta(richiestaPost);

        return risposta.body();
    }

    public String aggiornaArtistaEsistente(int id, String datiJson){
        String urlCompleta = urlRadice + "/artisti/"+id;

        HttpRequest richiestaPut = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(java.net.URI.create(urlCompleta))
                .PUT(HttpRequest.BodyPublishers.ofString(datiJson))
                .build();

        HttpResponse<String> risposta = eseguiRichiesta(richiestaPut);

        return risposta.body();
    }

    public String rimuoviArtista(int id){
        String urlCompleta = urlRadice + "/artisti/"+id;

        HttpRequest richiestaDelete = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(java.net.URI.create(urlCompleta))
                .DELETE()
                .build();

        HttpResponse<String> risposta = eseguiRichiesta(richiestaDelete);
        return risposta.body();
    }
}