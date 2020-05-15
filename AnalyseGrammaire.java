import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class AnalyseGrammaire {

    private Grammaire grammaire;

    public AnalyseGrammaire() {}

    public void analyserFichier(String cheminFichier) {
        this.grammaire = new Grammaire();
        try {
            File fichier = new File(cheminFichier);
            Scanner lecteurFichier = new Scanner(fichier);
            String premiereLigne = lecteurFichier.nextLine();
            this.lireLigneAxiome(premiereLigne);

            String ligne;
            while (lecteurFichier.hasNextLine()) {
                ligne = lecteurFichier.nextLine();
                this.lireLigne(ligne);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Le fichier spécifié n'a pas été trouvé");
            e.printStackTrace();
        }

        System.out.println("Ensemble des non-terminaux :");
        System.out.println(this.grammaire.getEnsembleNonTerminaux());

        System.out.println("Ensemble des terminaux :");
        System.out.println(this.grammaire.getEnsembleTerminaux());

        this.grammaire.calculerPremiersEtSuivants();

        System.out.println("Ensemble Suivants : ");
        System.out.println(this.grammaire.getEnsembleSuivants());


    }

    public void afficherNonTerminaux() {
        Set<String> ensembleNonTerminaux = this.grammaire.getEnsembleNonTerminaux();
        System.out.println("Ensemble des non-terminaux : ");
        for (String nonTerminal : ensembleNonTerminaux) {
            System.out.println(nonTerminal);
        }
    }

    public void afficherTerminaux() {
        Set<String> ensembleTerminaux = this.grammaire.getEnsembleTerminaux();
        System.out.println("Ensemble des terminaux : ");
        for (String terminal : ensembleTerminaux) {
            System.out.println(terminal);
        }
    }

    public void afficherPremiers() {
        Map<String, Set<String>> ensemblePremiers = this.grammaire.getEnsemblePremiers();
        for (Map.Entry<String, Set<String>> entree : ensemblePremiers.entrySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(entree.getKey()).append(" : ");
            for (String premier : entree.getValue()) {
                stringBuilder.append(premier).append(" ");
            }
            System.out.println(stringBuilder.toString());
        }
    }

    public void lireLigneAxiome(String ligne) {
        String axiome = ligne.split("->")[0].trim();
        this.grammaire.setAxiome(axiome);
        this.lireLigne(ligne);
    }

    public void lireLigne(String ligne) {
        // le non-terminal est l'élément à gauche de la flèche "->"
        String nonTerminal = ligne.split("->")[0].trim();
        this.grammaire.ajouterNonTerminal(nonTerminal);

        // les productions sont les éléments à droite de la flèche "->", séparés par des "|"
        String[] productions = ligne.split("->")[1].split("\\|");

        for (String production : productions) {
            production = production.trim(); // on retire les caractères indésirables autour de la production
            String[] mots = production.split(" "); // on divise les mots qui sont séparés par des espaces

            for (String mot : mots) {
                if (Character.isUpperCase(mot.charAt(0))) { // si la première lettre du mot est une Majuscule...
                    System.out.println("première lettre Maj" + mot);
                    this.grammaire.ajouterNonTerminal(mot); // ... c'est un non-terminal
                }
                else {
                    System.out.println("autre cas" + mot);
                     this.grammaire.ajouterTerminal(mot); // sinon c'est un terminal
                }
            }

            // Puis on ajoute cette production à l'ensemble des règles de production de la grammaire
            this.grammaire.ajouterRegleProduction(nonTerminal, production);
        }
    }

}
