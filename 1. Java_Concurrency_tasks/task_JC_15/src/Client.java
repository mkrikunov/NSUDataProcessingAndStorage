import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client implements Runnable {

  private final int PORT_P;
  private final String SERVER_HOST;

  Client(int PORT_P, String SERVER_HOST) {
    //Порт и хост сервера
    this.PORT_P = PORT_P;
    this.SERVER_HOST = SERVER_HOST;
  }

  @Override
  public void run() {
    Socket socket;
    try {
      socket = new Socket(SERVER_HOST, PORT_P);
      System.out.println("КЛИЕНТ | Подключился к серверу ");

      //Входной и выходной потоки для работы с клиентом
      InputStream input = socket.getInputStream();
      OutputStream output = socket.getOutputStream();

      //Сообщение серверу
      var message = "Ержан, вставай, заколебал, на работу пора";
      output.write(message.getBytes());
      System.out.println("КЛИЕНТ | Сообщение отправил");

      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = input.read(buffer)) != -1) {
        String response = new String(buffer, 0, bytesRead);
        System.out.println("КЛИЕНТ | Получил от сервера: " + response);
      }


      input.close();
      output.close();
      socket.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    System.out.println("КЛИЕНТ | закончил");
  }
}
