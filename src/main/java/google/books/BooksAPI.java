package google.books;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import google.books.data.BookshelfList;
import google.books.data.PublicBookshelfs;
import google.books.data.Volume;
import google.books.data.VolumeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class BooksAPI {
    private final String apiKey = "AIzaSyCHj5SSH9cDBUNj_19AQUhbLS8r1b-B2ZI";
    private final String userId = "102701940585560677579";
    private final String BOOK_SELVES = "/bookshelves";
    private static final String BASE_URL = "https://www.googleapis.com/books/v1";

    private String sendHttpRequest(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        return handleResponse(responseCode, connection);
    }

    private static String handleResponse(int responseCode, HttpURLConnection connection) throws IOException {
        if (responseCode == 200) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                String line;
                StringBuilder errorResponse = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    errorResponse.append(line);
                }
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(errorResponse.toString());
                if (rootNode.has("error") && rootNode.get("error").has("message")) {
                    String errorMessage = rootNode.get("error").get("message").asText();
                    System.out.println("API Error Message: " + errorMessage);
                } else {
                    System.out.println("Error response: " + errorResponse);
                }
            }

            throw new RuntimeException("Failed HTTP request with response code: " + responseCode);
        }
    }

    public Object  fetchGoogleBookData(BookOperation operation, Map<String, String> params) {
        String apiUrl = constructUrl(operation, params);
        String response = fetchDataFromGoogleBooksApi(apiUrl);
        return extractDataFromResponse(operation, response);
    }

    private static Object extractDataFromResponse(BookOperation operation, String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            if (operation == BookOperation.SEARCH_FOR_BOOKS || operation == BookOperation.GET_BOOKSHELF_VOLUMES) {
                VolumeList volumeList = objectMapper.readValue(response, VolumeList.class);
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(volumeList);
            } else if (operation == BookOperation.RETRIEVE_VOLUME_BY_ID) {
                Volume volume = objectMapper.readValue(response, Volume.class);
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(volume);
            } else if (operation == BookOperation.FETCH_PUBLIC_BOOKSHELVES) {
                BookshelfList bookshelfList = objectMapper.readValue(response, BookshelfList.class);
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bookshelfList);
            } else if (operation == BookOperation.FETCH_BOOKSHELF_BY_ID) {
                PublicBookshelfs publicBookshelfContents = objectMapper.readValue(response, PublicBookshelfs.class);
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(publicBookshelfContents);
            }
            try {
                return objectMapper.readValue(response, Map.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to parse API response to a map.", e);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String constructUrl(BookOperation operation, Map<String, String> params) {
        String apiUrl;
        switch (operation) {
            case SEARCH_FOR_BOOKS -> {
                StringBuilder queryParams = new StringBuilder();
                params.forEach((k, v) -> queryParams.append("+" + k + ":" + v));
                String finalQuery = params.get("query") + queryParams.toString();
                apiUrl = BASE_URL + "/volumes?q=" + finalQuery + "&key=" + apiKey;
            }
            case RETRIEVE_VOLUME_BY_ID -> apiUrl = String.format(BASE_URL + "/volumes/%s?key=%s", params.get("volumeId"), apiKey);
            case FETCH_PUBLIC_BOOKSHELVES -> apiUrl = BASE_URL + "/users" + "/" + userId + BOOK_SELVES;
            case FETCH_BOOKSHELF_BY_ID -> apiUrl = String.format(BASE_URL + "/users/%s/bookshelves/%s/volumes", userId, params.get("bookshelfId"));
            case GET_BOOKSHELF_VOLUMES -> apiUrl = String.format("https://www.googleapis.com/books/v1/users/%s/bookshelves/%s/volumes?key=%s", userId, params.get("bookshelfId"), apiKey);
            default -> throw new IllegalArgumentException("Unsupported operation: " + operation);
        }
        return apiUrl;
    }

    public String fetchDataFromGoogleBooksApi(String url) {
        try {
            String response = sendHttpRequest(url);
            System.out.println("Response from API: " + response);  // Debugging line
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }





}

