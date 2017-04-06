import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;

public class Coordenador {

	private static Scanner sc = new Scanner(System.in);
	public static DatagramSocket unicastSocket;
	public static ArrayList<String> pendingMessages = new ArrayList<>();
	private static byte[] b;
	private static DatagramPacket p;

	public static void main(String[] args) {
		sc.useDelimiter("\n");

		try {
			unicastSocket = new DatagramSocket();
			// Mensagem de sinal com a porta de envio para clientes

			InetAddress group = InetAddress.getByName("227.0.0.2");

			while (true) {
				b = new String(Integer.toString(unicastSocket.getLocalPort())).getBytes();
				System.out.println("Pressione qualquer tecla para descobrir quantos membros o grupo "
						+ group.getHostName() + " tem.");
				sc.next();

				p = new DatagramPacket(b, b.length, group, 4446);

				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							unicastSocket.send(p);
							b = new byte[256];
							p = new DatagramPacket(b, b.length);

							unicastSocket.receive(p);

							String response = new String(p.getData(), p.getOffset(), p.getLength(), "UTF-8");
							pendingMessages.add(response);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				
				t.start();
				t.join(10000);
				
				if(pendingMessages.size() == 0){
					System.out.println("Nenhuma mensagem recebida em 10000ms, cancelando.");
				} else {
					System.out.println("Primeira mensagem recebida...");
					startListenThread();
					System.out.println("Concluida execução do carregamento de mais mensagens durante 5000ms.");
					printMessages();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void startListenThread() throws InterruptedException {
		Thread t = new Thread(new ListenThread());
		t.start();
		t.join(5000);
	}
	
	public static void printMessages(){
		for(String i : pendingMessages){
			System.out.println(i);
		}
		pendingMessages.clear();
	}
}
