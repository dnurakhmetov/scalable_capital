package capital.scalable.webcrawler.model;

/**
 * A class of object that represents parsed Javascript library's name and hash.
 * <p>The hash is build using SHA256.</p>
 *
 */
public class ParsedData {
    private final String name;
    private final String hash;

    public ParsedData(String name, String hash) {
        this.name = name;
        this.hash = hash;
    }

    public String getName() {
        return name;
    }

    public String getHash() {
        return hash;
    }
}
