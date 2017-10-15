package shit;

public class FifoTest {

	public static void main(String[] args) {
		Fifo fifo = new Fifo("in.txt","out.txt",10);
		fifo.readMessage();
		fifo.wirteMessage(fifo.run());
	}
}
