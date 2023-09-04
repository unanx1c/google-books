package servlets;



import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class BookshelfVolumesServlet extends HttpServlet {

    private static final String GOOGLE_BOOKS_ENDPOINT = "https://www.googleapis.com/books/v1/users/%s/bookshelves/%s/volumes";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] pathInfo = req.getPathInfo().split("/");
        String userId = pathInfo[1];       // Extract user_id from the URL
        String bookshelfId = pathInfo[3];  // Extract bookshelfId from the URL

        String apiKey = "AIzaSyCHj5SSH9cDBUNj_19AQUhbLS8r1b-B2ZI";  // Consider hiding or encrypting this in real-world applications
        String apiUrl = String.format(GOOGLE_BOOKS_ENDPOINT + "?key=" + apiKey, userId, bookshelfId);

        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                resp.getWriter().print(response.toString());
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("Failed to fetch data from Google Books API");
        }
    }
}
