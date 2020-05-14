import java.util.ArrayList;

public class Transition {

    private Etat etatDepart;
    private Etat etatFin;
    private ArrayList<String> stringsReconnus;

    public Transition(Etat etatDepart, Etat etatFin) {
        this.etatDepart = etatDepart;
        this.etatFin = etatFin;
        this.stringsReconnus = new ArrayList<>();
    }


    public void ajouterStringReconnue(String... strings) {
        for (String string : strings) {
            this.stringsReconnus.add(string);
        }
    }

    // Méthode qui vérifie si le caractère donné en paramètre est "accepté" par la transition
    // Cette méthode est utilisée pour savoir si le caractère actuellement utilisé
    public boolean isElementAccepte(String element) {
        if (this.stringsReconnus.contains(element)) {
            return true;
        } else {
            return false;
        }
    }

    public Etat getEtatDepart() {
        return this.etatDepart;
    }

    public Etat getEtatFin() {
        return this.etatFin;
    }

    public ArrayList<String> getCaracteresReconnus() {
        return this.stringsReconnus;
    }

    @Override
    public String toString() {
        return this.etatDepart.toString() + " à " + this.etatFin.toString();
    }
}