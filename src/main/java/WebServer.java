import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    public static final int PORT = 8092;

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT);

        while (true){
            Socket clientSocket = serverSocket.accept();
            String resource = getResource(clientSocket.getInputStream());
            System.out.println("Browser trazi: " + resource);
            if (resource==null){
                resource = "index.html";

            }
            sendRespounse(resource, clientSocket.getOutputStream());

        }
    }
    private static String getResource(InputStream inputStream) throws Exception{
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String [] params = in.readLine().split(" ");
        String resource = params[1];
        if (resource.isEmpty()){
            return "index.html";
        }
        return resource;
    }

    private static void sendRespounse(String resource, OutputStream outputStream) throws IOException {
        PrintStream out = new PrintStream(outputStream);
        InputStream htmlAsInput = WebServer.class.getResourceAsStream(resource);
        out.println("HTTP/1.1 200 OK");
        out.println("Content-type: text/html");
        out.println("\r\n");
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(htmlAsInput));
        String line;
        while ((line=fileReader.readLine())!=null){
            out.println(line);
        }
        fileReader.close();
        out.close();
        out.flush();
    }
}
