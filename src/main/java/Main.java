import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    // Uncomment this block to pass the first stage
    try {
      ServerSocket serverSocket = new ServerSocket(4221);

      // Since the tester restarts your program quite often, setting SO_REUSEADDR
      // ensures that we don't run into 'Address already in use' errors
      serverSocket.setReuseAddress(true);

      Socket client = serverSocket.accept(); // Wait for connection from client.
      // After accepting the client, we output the stream and write a simple HTTP 1.1 server
      // If the path doesn't have anything then accept the request
      BufferedReader out = new BufferedReader(new InputStreamReader(client.getInputStream()));

      String[] validPaths = {"/", "/echo/", "/user-agent"};

      String[] checkRequest = out.readLine().split(" ");

     for (String line: checkRequest) {
        System.out.println("line --> " + line);
      }

      OutputStream clientOutputStream = client.getOutputStream();

      String incomingPath = checkRequest[1];
      String basePath = incomingPath.substring(0, incomingPath.lastIndexOf("/") + 1);
      String pathValue = incomingPath.substring(incomingPath.lastIndexOf("/") + 1);

      if (checkRequest[0].equals("GET")) {
        if (basePath.equals("/") && pathValue.isEmpty() || basePath.equals(validPaths[0]) || basePath.equals(validPaths[1])) {
            clientOutputStream.write(("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + pathValue.length() + "\r\n\r\n" + pathValue).getBytes());
        }
        if (incomingPath.equals("/user-agent")) {
          // get headers a return User agent header value from the arguments
          out.readLine();
          String userAgent = out.readLine().split("\\s+")[1];
          Integer conentLength = userAgent.length();
          System.out.println("user agent: " + conentLength);

          String reply = String.format("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + userAgent.length() + "\r\n\r\n" + userAgent);
          clientOutputStream.write(reply.getBytes());
        }
      } else {
        System.out.println("Invalid path: " + incomingPath);
        clientOutputStream.write(("HTTP/1.1 404 Not Found\r\n\r\n").getBytes());
      }

      System.out.println("accepted new connection");
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
