import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class InterfaceAnalyseurSyntaxique {

    public InterfaceAnalyseurSyntaxique() {}

    public void start() {
        // Affichage pour guider l'utilisateur
        System.out.println("Analyseur syntaxique pour le langage ALG01");
        System.out.println("Veuillez choisir une option : ");
        System.out.println("1 - Générer la table d'analyse d'une grammaire");
        System.out.println("2 - Analyser un programme ALG01");

        // Récupération du choix de l'utilisateur
        Scanner scanner = new Scanner(System.in);
        System.out.print("Votre choix (1 ou 2) : ");
        String choix = scanner.nextLine();

        if (choix.equals("1")) {
            boolean cheminValide = false;
            do {
                System.out.print("Chemin relatif de la grammaire à analyser : ");
                String chemin = scanner.nextLine();

                File fichierGrammaire = new File(chemin);

                try {
                    AnalyseGrammaire analyseGrammaire = new AnalyseGrammaire();
                    analyseGrammaire.analyserFichier(fichierGrammaire);
                    cheminValide = true;
                }
                catch (FileNotFoundException e) {
                    System.out.println("Le chemin que vous avez entré ne mène pas à un fichier (FileNotFoundException)");
                    System.out.println("Veuillez entrer un chemin valide");
                }
            } while (!cheminValide);
        }

        else if (choix.equals("2")) {
            boolean cheminValide = false;
            do {
                System.out.print("Chemin relatif du fichier contenant le programme ALG01 à analyser : ");
                String chemin = scanner.nextLine();

                File fichierALG01 = new File(chemin);

                try {
                    AnalyseALG01 analyseur = new AnalyseALG01();
                    String resultat = analyseur.stringifierProgramme(fichierALG01);
                    cheminValide = true;
                    System.out.println(resultat);
                }
                catch (FileNotFoundException e) {
                    System.out.println("Le chemin que vous avez entré ne mène pas à un fichier (FileNotFoundException)");
                    System.out.println("Veuillez entrer un chemin valide");
                }
            } while (!cheminValide);
        }

        scanner.close();
    }
}
