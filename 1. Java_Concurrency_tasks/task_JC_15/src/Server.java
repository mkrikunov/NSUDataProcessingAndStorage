import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

  private final int PORT_P1;

  Server(int PORT_P1) {
    //Порт, на котором сервер будет слушать
    this.PORT_P1 = PORT_P1;
  }

  @Override
  public void run() {
    try {
      ServerSocket serverSocket = new ServerSocket(PORT_P1);
      System.out.println("СЕРВЕР | Запустился. Ожидание подключений...");

      Socket clientSocket = serverSocket.accept();
      System.out.println("СЕРВЕР | Подключился клиент");

      //Входной и выходной потоки для работы с клиентом
      InputStream clientInput = clientSocket.getInputStream();
      OutputStream clientOutput = clientSocket.getOutputStream();

      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = clientInput.read(buffer)) != -1) {
        System.out.println("СЕРВЕР | Данные получил");
        clientOutput.write(buffer, 0, bytesRead);
        System.out.println("СЕРВЕР | Данные отправил");
      }

      clientInput.close();
      clientOutput.close();
      clientSocket.close();
      serverSocket.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    System.out.println("СЕРВЕР | Закончил");
  }
}
