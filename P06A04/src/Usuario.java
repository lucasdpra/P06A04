import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Usuario {

	private static MulticastSocket multiCastSocket;

	public static void main(String[] args) {

		try {
			InetAddress group = InetAddress.getByName("227.0.0.2");
			InetAddress server = InetAddress.getLocalHost();

			multiCastSocket = new MulticastSocket(4446);
			multiCastSocket.joinGroup(group);

			while (true) {
				byte[] b = new byte[256];
				DatagramPacket response = new DatagramPacket(b, b.length);
				multiCastSocket.receive(response);
				String serverPort = new String(response.getData(), response.getOffset(), response.getLength(), "UTF-8");

				byte[] msg = new String(ManagementFactory.getRuntimeMXBean().getName()).getBytes();
				
				DatagramPacket message = new DatagramPacket(msg, msg.length, server, Integer.parseInt(serverPort));
				multiCastSocket.send(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
