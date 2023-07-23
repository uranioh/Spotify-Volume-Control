package authorization.authorization_code;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

public class WebServer {
    public static CompletableFuture<String> start() {
        int port = 8888;
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create HttpServer", e);
        }

        CompletableFuture<String> authorizationCodeFuture = new CompletableFuture<>();

        server.createContext("/callback", exchange -> {
            // Extract the authorization code from the query parameters
            String query = exchange.getRequestURI().getQuery();
            String[] params = query.split("&");
            String authorizationCode = null;

            for (String param : params) {
                String[] keyValue = param.split("=");
                if ("code".equals(keyValue[0])) {
                    authorizationCode = keyValue[1];
                    break;
                }
            }

            if (authorizationCode != null) {
                // Complete the CompletableFuture with the authorization code
                authorizationCodeFuture.complete(authorizationCode);

                // Respond to the user with a simple success message (optional)
                String response = "Authorization successful! You can close this window.";
                exchange.sendResponseHeaders(200, response.length());

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                // Respond to the user with an error message (optional)
                String response = "Authorization failed! Please try again.";
                exchange.sendResponseHeaders(400, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        });

        server.setExecutor(null);
        server.start();

        return authorizationCodeFuture;
    }
}
