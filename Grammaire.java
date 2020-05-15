import java.util.*;

public class Grammaire {

    private Set<String> ensembleTerminaux; // un terminal est en minuscule
    private Set<String> ensembleNonTerminaux; // un non terminal commence par une Majuscule
    private String axiome;
    private Map<String, List<String>> ensembleReglesProduction; // clé : le non terminal ; valeur(s) : les différentes productions de ce non terminal
    private Map<String, Set<String>> ensemblePremiers; // contient (après calcul) les premiers de la grammaire
    private Map<String, Set<String>> ensembleSuivants; // contient (après calcul) les suivants de la grammaire


    public Grammaire() {
        // initialisation des listes
        this.ensembleTerminaux = new HashSet<>();
        this.ensembleNonTerminaux = new HashSet<>();
        this.ensembleReglesProduction = new HashMap<>();
        this.ensemblePremiers = new HashMap<>();
        this.ensembleSuivants = new HashMap<>();
    }

    public void setAxiome(String axiome) {
        this.axiome = axiome;
    }

    public void ajouterTerminal(String terminal) {
        this.ensembleTerminaux.add(terminal);
    }

    public void ajouterNonTerminal(String nonTerminal) {
        this.ensembleNonTerminaux.add(nonTerminal);
    }

    public void ajouterRegleProduction(String nonTerminal, String production) {
        List<String> listeProductions = this.ensembleReglesProduction.get(nonTerminal); // on récupère la liste des productions de ce non-terminal
        if (listeProductions == null) { // si il n'y a aucune liste associée au non-terminal...
            listeProductions = new ArrayList<>(); // ...on en initialise une...
        }
        listeProductions.add(production); // dans tous les cas (liste associée au non-terminal ou non) on ajoute la production à la liste
        this.ensembleReglesProduction.put(nonTerminal, listeProductions); // et on ajoute cette modification au HashSet
    }

    public void calculerPremiersEtSuivants() {
        // on boucle sur les règles de production
        for (Map.Entry<String, List<String>> entree : this.ensembleReglesProduction.entrySet()) {
            // variables pour que ce soit plus clair
            String nonTerminal = entree.getKey();
            List<String> reglesProduction = entree.getValue();

            // calcul des premiers
            Set<String> premiersDuNonTerminal = this.calculerPremiers(nonTerminal, reglesProduction);
            this.setEnsemblePremiersDunNonTerminal(nonTerminal, premiersDuNonTerminal);
        }

        System.out.println("Ensemble des Premiers calculé");

        for (Map.Entry<String, List<String>> entree : this.ensembleReglesProduction.entrySet()) {
            // variables pour que ce soit plus clair
            String nonTerminal = entree.getKey();
            List<String> reglesProduction = entree.getValue();

            // calcul des suivants
            Set<String> suivantsDuNonTerminal = this.calculerSuivants(nonTerminal, reglesProduction);
            this.setEnsembleSuivantsDunNonTerminal(nonTerminal, suivantsDuNonTerminal);
        }

        System.out.println("Ensemble des Suivants calculé");
    }

    public Set<String> calculerPremiers(String nonTerminal, List<String> listeProductions) {
        String premierElement;
        Set<String> ensemblePremiersPourCeNonTerminal = this.ensemblePremiers.get(nonTerminal);
        if (ensemblePremiersPourCeNonTerminal == null) {
            ensemblePremiersPourCeNonTerminal = new HashSet<>();
        }
        for (String production : listeProductions) {
            String[] elements = production.split(" ");
            String element = elements[0]; // on commence par le premier elément
            int indiceElementActuel = 0; // on note l'indice de l'élement
            int nombreElements = elements.length; // la limite à ne pas dépasser est nombreElements - 1
            if (this.ensembleTerminaux.contains(element)) { // si le premier élément est terminal
                ensemblePremiersPourCeNonTerminal.add(element);
            }
            else { // sinon, si le premier élément est un non-terminal
                // if faut récupérer les premiers de ce non-terminal puis les ajouter à l'ensemble des premiers du non-terminal actuel
                if (!nonTerminal.equals(element)) { // si le premier n'est pas le non-terminal dont on cherche les premiers
                    // s'il n'y a pas de récursion gauche
                    Set<String> ensemblePremiersDuNonTerminal = this.calculerPremiers(element, this.getListeProductions(element));
                    if (ensemblePremiersDuNonTerminal.contains("ε") && indiceElementActuel < nombreElements - 1) {
                        // si le dernier ensemble calculé contient epsilon, on va chercher les Premiers de l'élément suivant
                        indiceElementActuel++;
                        element = elements[indiceElementActuel];
                        if (this.ensembleTerminaux.contains(element)) {
                            // si c'est un terminal, on l'ajoute et puis c'est bon
                            ensemblePremiersPourCeNonTerminal.add(element);
                        }
                        else {
                            // c'est un non-terminal, et on va donc chercher ses Premiers pour les ajouter à ceux du non-terminal actuel
                            ensemblePremiersPourCeNonTerminal.addAll(ensemblePremiersDuNonTerminal);
                            ensemblePremiersPourCeNonTerminal.addAll(calculerPremiers(element, this.getListeProductions(element)));
                        }
                    }
                    else {
                        ensemblePremiersPourCeNonTerminal.addAll(ensemblePremiersDuNonTerminal);
                    }
                }
                else {
                    // sinon on ignore la récursion gauche
                }
            }
        }
        return ensemblePremiersPourCeNonTerminal;
    }

    public Set<String> calculerSuivants(String nonTerminal, List<String> listeProductions) {
        Set<String> ensembleSuivantsPourCeNonTerminal = this.ensembleSuivants.get(nonTerminal);
        if (ensembleSuivantsPourCeNonTerminal == null) {
            ensembleSuivantsPourCeNonTerminal = new HashSet<>();
        }
        if (nonTerminal == this.axiome) { // axiome : cas particulier, on lui ajoute $
            ensembleSuivantsPourCeNonTerminal.add("$");
        }
        for (String production : listeProductions) {
            String[] listeElementsProduction = production.trim().split(" ");
            boolean ilFautSinteresserALElementSuivant = false;
            for (int i = 0 ; i < listeElementsProduction.length ; i++) {
                String element = listeElementsProduction[i];
                if (element == nonTerminal) { // dans ce cas là, l'élément suivant est intéressant !!!
                    ilFautSinteresserALElementSuivant = true;
                }
                else if (ilFautSinteresserALElementSuivant) {
                    // on est donc sur un élément qui suit le nonTerminal
                    if (this.ensembleNonTerminaux.contains(element)) {
                        ensembleSuivantsPourCeNonTerminal.addAll(this.getPremiers(element));
                    }
                }
            }
        }
        return ensembleSuivantsPourCeNonTerminal;
    }

    public Set<String> getPremiers(String nonTerminal) {
        return this.getEnsemblePremiers().get(nonTerminal);
    }

    public Map<String, Set<String>> getEnsemblePremiers() {
        return ensemblePremiers;
    }

    public List<String> getListeProductions(String nonTerminal) {
        return this.ensembleReglesProduction.get(nonTerminal);
    }

    public void setEnsemblePremiersDunNonTerminal(String nonTerminal, Set<String> ensemblePremiers) {
        this.ensemblePremiers.put(nonTerminal, ensemblePremiers);
    }

    public void setEnsembleSuivantsDunNonTerminal(String nonTerminal, Set<String> ensembleSuivants) {
        this.ensembleSuivants.put(nonTerminal, ensembleSuivants);
    }

    public void calculerEnsembleSuivants() {

    }

    public Set<String> getEnsembleTerminaux() {
        return ensembleTerminaux;
    }

    public Set<String> getEnsembleNonTerminaux() {
        return ensembleNonTerminaux;
    }

    public Map<String, Set<String>> getEnsembleSuivants() {
        return ensembleSuivants;
    }
}
