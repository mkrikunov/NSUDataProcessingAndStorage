import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Main {

  public static void main(String[] args) {
    if (args.length < 3) {
      return;
    }

    final int PORT_P = Integer.parseInt(args[0]); //порт, на котором сервер слушает
    final String SERVER = args[1]; //целевой сервер (IP-адрес), на который надо передавать данные
    final int PORT_P1 = Integer.parseInt(args[2]); //порт целевого сервера, куда надо передавать данные
    var hostname = "localhost";

    try {
      ServerSocketChannel channel = ServerSocketChannel.open();
      channel.bind(new InetSocketAddress(hostname, PORT_P));
      channel.configureBlocking(false);

      Selector selector = Selector.open();
      channel.register(selector, SelectionKey.OP_ACCEPT);

      ByteBuffer buffer = ByteBuffer.allocate(1024);

      Client client = new Client(PORT_P, hostname);
      Thread clientThread = new Thread(client);

      Server server = new Server(PORT_P1);
      Thread serverThread = new Thread(server);

      serverThread.start();
      clientThread.start();

      for (int i = 0; i < 4; i++) {
        selector.select();

        for (SelectionKey key : selector.selectedKeys()) {
          //Событие OP_ACCEPT, принимается входящее соединение от клиента, создается канал для этого соединения
          if (key.isAcceptable()) {
            SocketChannel clientChannel = channel.accept();
            if (clientChannel != null) {
              clientChannel.configureBlocking(false);
              //System.out.println("ПОДКЛЮЧИЛСЯ КЛИЕНТ");

              SocketChannel targetServerChannel = SocketChannel.open(
                  new InetSocketAddress(SERVER, PORT_P1));
              targetServerChannel.configureBlocking(false);
              //System.out.println("УСТАНОВЛЕНО СОЕДИНЕНИЕ С ЦЕЛЕВЫМ СЕРВЕРОМ");

              clientChannel.register(selector, SelectionKey.OP_READ, targetServerChannel);
              targetServerChannel.register(selector, SelectionKey.OP_READ, clientChannel);
            }
            //Оба канала регистрируются в селекторе для событий чтения OP_READ
          } else if (key.isReadable()) {
            SocketChannel channel1 = (SocketChannel) key.channel();
            SocketChannel channel2 = (SocketChannel) key.attachment();

            int bytesRead = channel1.read(buffer);
            if (bytesRead == -1) {
              channel1.close();
              channel2.close();
              key.cancel();
            } else if (bytesRead > 0) {
              //System.out.println("ПОЛУЧЕНЫ ДАННЫЕ");
              buffer.flip();
              while (buffer.hasRemaining()) {
                channel2.write(buffer);
              }
              //System.out.println("ДАННЫЕ ПЕРЕДАНЫ");
              buffer.clear();
            }
          }
        }
        selector.selectedKeys().clear();
      }
      channel.close();
      selector.close();
      clientThread.join();
      serverThread.join();
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
