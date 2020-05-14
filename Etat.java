import java.util.ArrayList;

public class Etat {

    private String nom;
    private boolean estFinal;
    private ArrayList<Transition> listeTransitions;

    public Etat(String nom, boolean estFinal) {
        this.nom = nom;
        this.estFinal = estFinal;
        this.listeTransitions = new ArrayList<>();
    }

    public void ajouterTransition(Transition... transitions) {
        for (Transition transition : transitions) {
            this.listeTransitions.add(transition);
        }
    }

    public boolean estFinal() {
        return this.estFinal;
    }

    public ArrayList<Transition> getListeTransitions() {
        return this.listeTransitions;
    }

    @Override
    public String toString() {
        return this.nom;
    }

}