import java.util.Arrays;
import java.util.List;

public class Automate {
    
    private Etat etatInitial;
    
    public Automate(Etat etatInitial) {
        this.etatInitial = etatInitial;
    }

    // Méthode qui va tester le mot avec l'automate
    // Renvoie true si le mot est reconnu par l'automate, false sinon
    public boolean tester(String mot) {

        // Pour tester le mot, on le divise en une liste de différents strings de longueur 1
        // ex: "1234" devient ["1", "2", "3", "4']
        String[] listeCaracteres = mot.split("");

        Etat etatActuel = etatInitial;

        // à chaque élement analysé, cette variable est "initialisée" à "false".
        // Si une transition partant de l'état actuel reconnaît l'élément,
        // cette variable passe à true
        boolean elementReconnu;

        for (String caractere : listeCaracteres) { // on boucle sur la liste des caractères du mot
            elementReconnu = false; // pour l'instant, l'élément n'est pas reconnu
            for (Transition transition : etatActuel.getListeTransitions()) { // on boucle sur les différentes transition qui "partent" de l'état actuel
                if (transition.isElementAccepte(caractere)) { // Dans le cas où la transition reconnait l'élément actuel...
                    elementReconnu = true; // on note que l'élément est reconnu
                    etatActuel = transition.getEtatFin(); // on change l'état actuel à l'état relié par la transition
                    break; // et on stoppe la boucle pour éviter de tester les autres transitions
                }
            }
            if (!elementReconnu) { // après avoir testé les transitions, si aucune ne reconnaît l'élement actuel...
                return false; // ...on retourne false car le mot n'est pas reconnu par l'automate
            }
        }

        if (etatActuel.estFinal()) { // si, après avoir fini la boucle sur les éléments du mot, l'état actuel est final...
            return true; // ...on retourne "true"
        }
        else {
            return false; // sinon "false"
        }
    }
}