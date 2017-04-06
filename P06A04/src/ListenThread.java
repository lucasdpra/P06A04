import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ListenThread implements Runnable {

	private static DatagramSocket s = Coordenador.unicastSocket;

	@Override
	public void run() {
		while (true) {
			byte[] b = new byte[256];
			DatagramPacket p = new DatagramPacket(b, b.length);
			try {
				s.receive(p);
				String response = new String(p.getData(), p.getOffset(), p.getLength(), "UTF-8");
				
				Coordenador.pendingMessages.add(response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
