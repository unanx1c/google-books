package servlets;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class VolumeDetailServlet extends HttpServlet {

    private static final String GOOGLE_BOOKS_ENDPOINT = "https://www.googleapis.com/books/v1/volumes/%s?key=AIzaSyCHj5SSH9cDBUNj_19AQUhbLS8r1b-B2ZI";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String volumeId = req.getPathInfo().substring(1); // This will extract the volumeId from the URL
        String apiUrl = String.format(GOOGLE_BOOKS_ENDPOINT, volumeId);

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


