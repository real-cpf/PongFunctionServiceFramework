package server.instance;

import jdk.net.UnixDomainPrincipal;

import java.io.IOException;
import java.net.ProtocolFamily;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UdsServerSocket {

  private final static ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();

  private final static Selector selector;

  static {
    try {
      selector = Selector.open();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public UdsServerSocket(){

  }
  public void run(){
    try {
      Path path = Path.of("/tmp/server.sock");
      if (Files.exists(path)) {
        Files.deleteIfExists(path);
      }
      ServerSocketChannel channel = ServerSocketChannel.open(StandardProtocolFamily.UNIX);
      channel.bind(UnixDomainSocketAddress.of(path));
      channel.configureBlocking(false);
      channel.register(selector, SelectionKey.OP_ACCEPT);
      start(selector);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  private void start(Selector selector) {
    while (true){
      try {
        selector.select();
        Iterator<SelectionKey> it = selector.selectedKeys().iterator();
        while (it.hasNext()){
          SelectionKey key = it.next();
          it.remove();
          if (key.isValid()){
            if (key.isAcceptable()) {
              accept(key);
            }else if(key.isReadable()) {
              read(key);
            }else if(key.isWritable()){
              System.out.println("write key "+ key.interestOps());
              write(key);
            }else {
              System.out.println(key);
            }
          }
        }

      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
  private void accept(SelectionKey key){
    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
    SocketChannel socketChannel = null;
    while (true) {
      try {
        if (!(null != (socketChannel = serverSocketChannel.accept()))) break;
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_READ);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

    }
  }
  private void write(SelectionKey key) {
    SocketChannel socketChannel = (SocketChannel) key.channel();
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    buffer.put("hello worlds\n".getBytes(StandardCharsets.UTF_8));
    buffer.flip();
    try {
      if (!socketChannel.isConnected()) {
        System.out.println("close");
        return;
      }
      socketChannel.write(buffer);
      socketChannel.configureBlocking(false);
      socketChannel.register(selector,SelectionKey.OP_READ);
    } catch (IOException e) {
      try {
        socketChannel.close();
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
      e.printStackTrace();
    }
  }
  private void read(SelectionKey key) throws IOException {
    SocketChannel socketChannel = (SocketChannel) key.channel();
    ByteBuffer buffer = ByteBuffer.allocate(10240);
    while (true){
      int c = socketChannel.read(buffer);
      if (c < 1){
        break;
      }
      System.out.println(buffer.position());
      buffer.position(0);
    }
    socketChannel.configureBlocking(false);
    socketChannel.register(selector,SelectionKey.OP_WRITE);
  }
}
