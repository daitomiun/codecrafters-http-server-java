import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

      String validPath = "/";

      String[] checkRequest = out.readLine().split(" ");

      if (checkRequest[0].equals("GET") && checkRequest[1].equals(validPath)) {
        client.getOutputStream().write(("HTTP/1.1 200 OK\r\n\r\n").getBytes());
      } else {
        client.getOutputStream().write(("HTTP/1.1 404 Not Found\r\n\r\n").getBytes());
      }

      System.out.println("accepted new connection");
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
