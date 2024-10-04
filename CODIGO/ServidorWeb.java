import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.nio.file.Files;
import java.net.InetSocketAddress;

public class ServidorWeb {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new MeuManipulador());
        server.setExecutor(null); 
        server.start();
        
        System.out.println("Servidor rodando na porta 8080...");
    }

    static class MeuManipulador implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath(); 
            if (path.equals("/")) {
                path = "home.html";  
            }
            else if (path.equals("/curiosidades")) {
                path = "curiosidades.html";  
            }
            else if (path.equals("/sobre")) {
                path = "sobre.html";  
            }

            File file = new File("./pages/" + path);

            if (file.exists() && !file.isDirectory()) {
                byte[] content = Files.readAllBytes(file.toPath());
                exchange.sendResponseHeaders(200, content.length); 
                OutputStream os = exchange.getResponseBody();
                os.write(content);
                os.close();
            } 
            else {
                String response = "404 (Not Found)\n";
                exchange.sendResponseHeaders(404, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}
