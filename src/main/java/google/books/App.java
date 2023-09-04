package google.books;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;


public class App 
{
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8085), 0);
        server.createContext("/books", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();


        Scanner scanner = new Scanner(System.in);
        BooksAPI booksService = new BooksAPI();

        while (true) {
            Map<String, String> params = getUserInput(scanner);
            if (params == null) {
                continue;
            }
            BookOption chosenOption = BookOption.getByCode(Integer.parseInt(params.get("code")));
            assert chosenOption != null;
            params.remove("code");
            try { 
            	System.out.println(booksService.fetchGoogleBookData(chosenOption.getOperation(), params));
            } catch (Exception e) {
            	System.out.println();
            }
        }
    }


    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            BooksAPI booksService = new BooksAPI();
            Map<String, String> params = new HashMap<>();
            params.put("query", "flowers");
            params.put("field", "keyes");

            String response = booksService.fetchGoogleBookData(BookOperation.SEARCH_FOR_BOOKS, params).toString();

            t.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    public static Map<String, String> getUserInput(Scanner scanner) {
        System.out.println("Choose an option:\n" +
                Arrays.stream(BookOption.values())
                        .map(opt -> opt.getCode() + ": " + opt.getDescription())
                        .collect(Collectors.joining("\n")));

        int inputCode;
        try {
            inputCode = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return null;
        }

        BookOption chosenOption = BookOption.getByCode(inputCode);

        if (chosenOption == null) {
            System.out.println("Invalid input. Try again.");
            return null;
        } else if (chosenOption == BookOption.EXIT) {
            System.out.println("Exiting...");
            scanner.close();
            System.exit(0);
        }

        Map<String, String> params = IntStream.range(0, chosenOption.getPrompts().length)
                .boxed()
                .collect(Collectors.toMap(
                        i -> chosenOption.getParamKeys()[i],
                        i -> {
                            System.out.println(chosenOption.getPrompts()[i]);
                            return scanner.nextLine();
                        }));

        // Add the chosen code to the map
        params.put("code", String.valueOf(inputCode));

        return params;
    }

}
