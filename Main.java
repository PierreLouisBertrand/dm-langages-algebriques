import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String [] args) {

        // Récupération auprès de l'utilisateur du mot à analyser, via le terminal
        Scanner scanner = new Scanner(System.in);
        System.out.print("Chemin relatif du programme ALG01 à analyser : ");
        String chemin = scanner.nextLine();
        scanner.close();

        Analyseur analyseur = new Analyseur();
        String bidule = analyseur.stringifierProgramme(chemin);
        
    }
}