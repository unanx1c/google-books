package google.books;

public enum BookOption {
    SEARCH_FOR_BOOKS(1, "Find books", BookOperation.SEARCH_FOR_BOOKS, "Query", "query", "Field", "field"),
    RETRIEVE_VOLUME_BY_ID(2, "Get a specific volume", BookOperation.RETRIEVE_VOLUME_BY_ID, "Volume id", "volumeId"),
    FETCH_PUBLIC_BOOKSHELVES(3, "Get a user's open bookshelves", BookOperation.FETCH_PUBLIC_BOOKSHELVES, "User id", "userId"),
    FETCH_BOOKSHELF_BY_ID(4, "Get public bookshelf using its id", BookOperation.FETCH_BOOKSHELF_BY_ID, "Bookshelf id", "bookshelfId"),
    GET_BOOKSHELF_VOLUMES(5, "List out the volumes from a public shelf", BookOperation.GET_BOOKSHELF_VOLUMES, "Bookshelf id", "bookshelfId"),
    EXIT(6, "Close the program");

    final int code;
    final String description;
    final BookOperation operation;
    final String[] prompts;
    final String[] paramKeys;

    BookOption(int code, String description, BookOperation operation, String... inputs) {
        this.code = code;
        this.description = description;
        this.operation = operation;
        this.prompts = new String[inputs.length / 2];
        this.paramKeys = new String[inputs.length / 2];
        for (int i = 0; i < inputs.length; i += 2) {
            this.prompts[i / 2] = inputs[i];
            this.paramKeys[i / 2] = inputs[i + 1];
        }
    }

    BookOption(int code, String description) {
        this.code = code;
        this.description = description;
        this.operation = null; // No corresponding operation
        this.prompts = new String[0];
        this.paramKeys = new String[0];
    }

    public static BookOption getByCode(int code) {
        for (BookOption option : values()) {
            if (option.code == code) return option;
        }
        return null;
    }


    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public BookOperation getOperation() {
        return operation;
    }

    public String[] getPrompts() {
        return prompts;
    }

    public String[] getParamKeys() {
        return paramKeys;
    }
}