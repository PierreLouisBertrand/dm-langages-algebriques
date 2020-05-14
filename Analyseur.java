import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Analyseur {
    
    private List<String> listeMotsCles;
    private Automate automateEntiers;
    private Automate automateIdentificateurs;

    public Analyseur() {
        this.listeMotsCles = Arrays.asList(
            "program",
            "begin",
            "end",
            "break",
            "while",
            "for",
            "do",
            "from",
            "to",
            "true",
            "false",
            "if",
            "then",
            "else",
            "and",
            "or",
            "not"
        );
        this.initialiserAutomateEntiers();
        this.initialiserAutomateIdentificateurs();
    }

    // Méthode qui initialise l'automate qui reconnaît les entiers valides
    private void initialiserAutomateEntiers() {
        // États
        Etat etatAEntiers = new Etat("A", false);
        Etat etatBEntiers = new Etat("B", true);
        Etat etatCEntiers = new Etat("C", false);
        Etat etatDEntiers = new Etat("D", true);

        // Transitions + ajout de ce qu'ils reconnaissent
        Transition transitionABEntiers = new Transition(etatAEntiers, etatBEntiers);
        transitionABEntiers.ajouterStringReconnue("0");
        
        Transition transitionACEntiers = new Transition(etatAEntiers, etatCEntiers);
        transitionACEntiers.ajouterStringReconnue("-");

        Transition transitionCDEntiers = new Transition(etatCEntiers, etatDEntiers);
        transitionCDEntiers.ajouterStringReconnue("1", "2", "3", "4", "5", "6", "7", "8", "9");

        Transition transitionADEntiers = new Transition(etatAEntiers, etatDEntiers);
        transitionADEntiers.ajouterStringReconnue("1", "2", "3", "4", "5", "6", "7", "8", "9");

        Transition transitionDDEntiers = new Transition(etatDEntiers, etatDEntiers);
        transitionDDEntiers.ajouterStringReconnue("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

        // Ajout des transitions aux états
        etatAEntiers.ajouterTransition(transitionABEntiers, transitionACEntiers, transitionADEntiers);
        etatCEntiers.ajouterTransition(transitionCDEntiers);
        etatDEntiers.ajouterTransition(transitionDDEntiers);

        // Création de l'automate, et assignation de l'état A en tant qu'état initial
        this.automateEntiers = new Automate(etatAEntiers);
    }

    // Méthode qui initialise l'automate qui reconnaît les identificateurs valides
    private void initialiserAutomateIdentificateurs() {
        // États
        Etat etatAIdentificateurs = new Etat("A", false);
        Etat etatBIdentificateurs = new Etat("B", true);

        // Transitions + ajout de ce qu'ils reconnaissent
        Transition transitionABIdentificateurs = new Transition(etatAIdentificateurs, etatBIdentificateurs);
        transitionABIdentificateurs.ajouterStringReconnue("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");

        Transition transitionBBIdentificateurs = new Transition(etatBIdentificateurs, etatBIdentificateurs);
        transitionBBIdentificateurs.ajouterStringReconnue("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

        // Ajout des transitions aux états
        etatAIdentificateurs.ajouterTransition(transitionABIdentificateurs);
        etatBIdentificateurs.ajouterTransition(transitionBBIdentificateurs);

        // Création de l'automate, et assignation de l'état A en tant qu'état initial
        this.automateIdentificateurs = new Automate(etatAIdentificateurs);
    }

    // Méthode qui va regarder si le mot donné en paramètre est dans la liste des mots-clés
    // retourne true si c'est le cas, false sinon
    public boolean testerMotCle(String mot) {
        if (this.listeMotsCles.contains(mot)) {
            return true;
        }
        else {
            return false;
        }
    }

    // Méthode qui prend en paramètre le chemin d'un fichier et retourne le contenu
    // de ce fichier sous la forme d'un String d'une ligne avec les identificateurs remplacés par "ident",
    // les entiers remplacés par "entier" et les espaces inutiles supprimés
    public String stringifierProgramme(String cheminFichier) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File fichierALG01 = new File(cheminFichier);
            Scanner lecteurFichier = new Scanner(fichierALG01);
            while (lecteurFichier.hasNextLine()) {
                String ligne = lecteurFichier.nextLine();
                stringBuilder.append(ligne);
                stringBuilder.append(" ");
            }
            lecteurFichier.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Le fichier spécifié n'a pas été trouvé");
            e.printStackTrace();
        }
        String programme = stringBuilder.toString();

        List<String> listeElementsNonNettoyee = Arrays.asList(programme.split(" "));
        List<String> listeElementsPropre = new ArrayList<>();
        
        StringBuilder res = new StringBuilder();

        // Nettoyage de la liste + remplacement des identificateurs par "ident"
        // et des entiers par "entier"
        for (String element : listeElementsNonNettoyee) {
            if (!element.equals(" ") && !element.equals("")) { // on enlève les espaces inutiles
                if (this.testerMotCle(element)) {
                    // si l'élément testé est un mot-clé
                    listeElementsPropre.add(element); // pas de remplacement : on le met tel quel
                }
                else if (this.automateIdentificateurs.tester(element)) { 
                    // si l'élément testé est un identificateur
                    listeElementsPropre.add("ident");
                }
                else if (this.automateEntiers.tester(element)) {
                    // si l'élement testé est un entier
                    listeElementsPropre.add("entier");
                }
                else {
                    // si ce n'est pas un mot-clé/identificateur/entier
                    listeElementsPropre.add(element);
                }
            }
        }

        // On met des espaces entre chaque mot du programme
        for (int i = 0 ; i < listeElementsPropre.size() ; i++) {
            String element = listeElementsPropre.get(i);
            res.append(element);
            if (i != listeElementsPropre.size()-1) {
                res.append(" ");
            }
        }

        String programmeEspaces = res.toString();

        // Suppression des espaces autour des points-virgules qui sont inutiles
        String resultat = programmeEspaces.replaceAll(" ; ", ";");

        return resultat;
    }
}