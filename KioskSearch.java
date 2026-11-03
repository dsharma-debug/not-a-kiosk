public class KioskSearch {
    public static boolean matches(Product p, String query) {
        if (query.length() == 0) {
            return true;
        }
        String searchableText = getSearchableText(p);
        if (searchableText.contains(query)) {
            return true;
        }
        String[] words = query.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (words[i].length() > 0 && searchableText.indexOf(words[i]) >= 0) {
                return true;
            }
        }
        return false;
    }
    private static String getSearchableText(Product p) {
        String description = p.getDescription().toLowerCase();
        String text = p.getName() + " " + p.getDescription();
        if (description.contains("oily")) {
            text += " grease greasy oil shine shiny scalp cooking food hair skin soap";
        }
        if (description.contains("vegan")) {
            text += " plant based animal free dairy free leather alternative";
        }
        if (description.contains("cheap")) {
            text += " budget affordable low price inexpensive value";
        }
        if (description.contains("dry")) {
            text += " moisturizing hydration hydrate repair soft sensitive";
        }
        if (description.contains("spicy")) {
            text += " hot heat pepper bold flavor food";
        }
        return text.toLowerCase();
    }
}
