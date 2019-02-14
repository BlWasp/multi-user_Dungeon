package Tools;

import static Tools.Colors.green;
import static Tools.Colors.red;
import static Tools.Colors.yellow;

public class Text {
    public static void welcome(){
        System.out.println("████████▄  ███    █▄  ███▄▄▄▄      ▄██████▄     ▄████████  ▄██████▄  ███▄▄▄▄");
        System.out.println("███   ▀███ ███    ███ ███▀▀▀██▄   ███    ███   ███    ███ ███    ███ ███▀▀▀██▄ ");
        System.out.println("███    ███ ███    ███ ███   ███   ███    █▀    ███    █▀  ███    ███ ███   ███");
        System.out.println("███    ███ ███    ███ ███   ███  ▄███         ▄███▄▄▄     ███    ███ ███   ███");
        System.out.println("███    ███ ███    ███ ███   ███ ▀▀███ ████▄  ▀▀███▀▀▀     ███    ███ ███   ███");
        System.out.println("███    ███ ███    ███ ███   ███   ███    ███   ███    █▄  ███    ███ ███   ███");
        System.out.println("███   ▄███ ███    ███ ███   ███   ███    ███   ███    ███ ███    ███ ███   ███");
        System.out.println("████████▀  ████████▀   ▀█   █▀    ████████▀    ██████████  ▀██████▀   ▀█   █▀");
    }

    /**
     * renvoi le texte sous le format graphique choisi pour les instructions
     * @param text
     * @return
     */
    public static String instruction(String text){
        return Colors.bold.getValue()+Colors.blue.getValue()+text+Colors.reset.getValue();
    }

    /**
     * mets en forme un texte d'erreur
     * @param text
     * @return
     */
    public static String error(String text){
        return red(text);
    }

    /**
     * mets en forme un texte de succès
     * @param text
     * @return
     */
    public static String success(String text){
        return green(text);
    }

    /**
     * permet d'afficher un texte au format instruction
     * @param text
     */
    public static void printI(String text){
        System.out.println(instruction(text));
    }

    /**
     * Affiche un texte d'erreur au format d'erreur
     * @param text
     */
    public static void printE(String text){
        System.out.println(error(text));
    }

    /**
     * Affiche un texte au format "succès"
     * @param text
     */
    public static void printS(String text){
        System.out.println(success(text));
    }


}
