import java.util.ArrayList;

public class Etat {

    private String nom;
    private boolean estInitial;
    private boolean estFinal;
    private ArrayList<Transition> listeTransitions;

    public Etat(String nom, boolean estInitial, boolean estFinal) {
        this.nom = nom;
        this.estInitial = estInitial;
        this.estFinal = estFinal;
        this.listeTransitions = new ArrayList<>();
    }

    public void ajouterTransition(Transition... transitions) {
        for (Transition transition : transitions) {
            this.listeTransitions.add(transition);
        }
    }

    public boolean estInitial() {
        return this.estInitial;
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