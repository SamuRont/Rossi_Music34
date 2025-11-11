import com.google.gson.Gson;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClientHttp client = new ClientHttp();
        GestoreDB database = GestoreDB.avvia();

        System.out.println(client.controllaStato());

        String continua;
        do{
            System.out.println("MENU ");
            System.out.println(
                    "\t Ottieni tutti gli artisti\n" +
                            "\t Ottieni un artista specifico\n" +
                            "\t Ottieni canzoni di un artista\n" +
                            "\t Ottieni tutte le canzoni\n" +
                            "\t Ottieni una canzone specifica\n" +
                            "\t Inserisci un nuovo artista\n" +
                            "\t Aggiorna dati artista\n" +
                            "\t Elimina un artista\n" +
                            "\t Salva artista nei preferiti (DB Locale)\n" +
                            "\t Rimuovi artista dai preferiti (DB Locale)\n" +
                            "\t Mostra artisti preferiti (DB Locale)"
            );

            int scelta = scanner.nextInt();
            if(scelta < 1 || scelta > 11)
                System.exit(1);

            switch (scelta) {
                case 1:
                    List<Artista> listaArtisti = client.ottieniArtisti();
                    for(Artista a: listaArtisti)
                        System.out.println(a.toString());
                    break;
                case 2:
                    gestisciArtistaSingolo(scanner, client);
                    break;
                case 3:
                    gestisciCanzoniPerArtista(scanner, client);
                    break;
                case 4:
                    List<Canzone> listaCanzoni = client.ottieniTutteCanzoni();
                    String output = "";
                    for(Canzone c: listaCanzoni)
                        output += c.toString();
                    System.out.println(output);
                    break;
                case 5:
                    gestisciCanzoneSingola(scanner, client);
                    break;
                case 6:
                    gestisciCreazioneArtista(scanner, client);
                    break;
                case 7:
                    gestisciAggiornamentoArtista(scanner, client);
                    break;
                case 8:
                    gestisciEliminazioneArtista(scanner, client);
                    break;
                case 9:
                    gestisciSalvataggioPreferito(scanner, client, database);
                    break;
                case 10:
                    gestisciRimozionePreferito(scanner, database);
                    break;
                case 11:
                    System.out.println(database.leggiArtistiSalvati());
                    break;
            }

            scanner.nextLine();
            System.out.println("Eseguire altra operazione? [si/no]");
            continua = scanner.nextLine();
        }while(continua.equalsIgnoreCase("si"));

        scanner.close();
    }

    private static void gestisciArtistaSingolo(Scanner scanner, ClientHttp client) {
        System.out.println("Inserisci ID artista:");
        int id = scanner.nextInt();
        System.out.println(client.ottieniArtistaPerId(id).toString());
    }

    private static void gestisciCanzoniPerArtista(Scanner scanner, ClientHttp client) {
        System.out.println("Inserisci ID artista:");
        int id = scanner.nextInt();

        List<Canzone> canzoni = client.ottieniCanzoniPerArtista(id);
        String output = "";

        for(Canzone c: canzoni)
            output += c.toString();

        System.out.println(output);
    }

    private static void gestisciCanzoneSingola(Scanner scanner, ClientHttp client) {
        System.out.println("Inserisci ID canzone:");
        int id = scanner.nextInt();

        System.out.println(client.ottieniCanzonePerId(id).toString());
    }

    private static void gestisciCreazioneArtista(Scanner scanner, ClientHttp client) {
        scanner.nextLine();

        System.out.println("Nome artista:");
        String nome = scanner.nextLine();

        System.out.println("Genere musicale:");
        String genere = scanner.nextLine();

        System.out.println("Paese di origine:");
        String paese = scanner.nextLine();

        Artista nuovo = new Artista(nome, genere, paese);

        Gson gson = new Gson();
        String jsonPayload = gson.toJson(nuovo);

        String risposta = client.inserisciNuovoArtista(jsonPayload);
        System.out.println("Risposta API: " + risposta);
    }

    private static void gestisciAggiornamentoArtista(Scanner scanner, ClientHttp client) {
        System.out.println("ID artista da aggiornare:");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Nuovo nome:");
        String nome = scanner.nextLine();

        System.out.println("Nuovo genere:");
        String genere = scanner.nextLine();

        System.out.println("Nuovo paese:");
        String paese = scanner.nextLine();

        Artista aggiornato = new Artista(nome, genere, paese);
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(aggiornato);

        String risposta = client.aggiornaArtistaEsistente(id, jsonPayload);
        System.out.println("Risposta API: " + risposta);
    }

    private static void gestisciEliminazioneArtista(Scanner scanner, ClientHttp client) {
        System.out.println("ID artista da eliminare:");
        int id = scanner.nextInt();

        String risposta = client.rimuoviArtista(id);
        System.out.println("Risposta API: " + risposta);
    }

    private static void gestisciSalvataggioPreferito(Scanner scanner, ClientHttp client, GestoreDB database) {
        System.out.println("ID artista da salvare:");
        int id = scanner.nextInt();

        Artista daSalvare = client.ottieniArtistaPerId(id);
        database.salvaArtista(daSalvare.idArtista, daSalvare.nomeDArte, daSalvare.paeseOrigine, daSalvare.genereMusicale);

        for(Canzone c: daSalvare.brani)
            database.salvaCanzone(c.idCanzone, c.titoloBrano, c.durataSecondi, c.annoUscita, daSalvare.idArtista);

        System.out.println("Artista salvato nei preferiti.");
    }

    private static void gestisciRimozionePreferito(Scanner scanner, GestoreDB database) {
        System.out.println("ID artista da rimuovere dai preferiti:");
        int id = scanner.nextInt();

        database.eliminaCanzoni(id);
        database.eliminaArtista(id);

        System.out.println("Artista rimosso dai preferiti.");
    }
}