public class Main {
    public static void main(String [] args) {

        AnalyseGrammaire analyseGrammaire = new AnalyseGrammaire();
        analyseGrammaire.analyserFichier("grammaires/grammaireG'.txt");
        analyseGrammaire.afficherNonTerminaux();
        analyseGrammaire.afficherTerminaux();
        analyseGrammaire.afficherPremiers();

//        InterfaceAnalyseurSyntaxique interfaceAnalyseurSyntaxique = new InterfaceAnalyseurSyntaxique();
//        interfaceAnalyseurSyntaxique.start();

    }
}