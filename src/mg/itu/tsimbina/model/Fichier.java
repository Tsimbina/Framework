package mg.itu.tsimbina.model;

import jakarta.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Fichier {
    String format;
    byte[] donnee;

    public Fichier(Part part) throws IOException {
        this.setDonnee(part.getInputStream().readAllBytes());
        this.setFormat(part.getContentType());
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public byte[] getDonnee() {
        return donnee;
    }

    public void setDonnee(byte[] donnee) {
        this.donnee = donnee;
    }

    public void enregistrer(String dossier, String nomFichier) throws IOException {
        Files.createDirectories(Paths.get(dossier));
        String extension = getFormat().substring(getFormat().indexOf('/') + 1);
        Files.write(Paths.get(dossier, nomFichier + "." + extension), donnee);
    }
}
