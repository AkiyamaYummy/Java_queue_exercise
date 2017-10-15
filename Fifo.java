package shit;

import java.io.*;

public class Fifo {
	public static String splitStr = " ";
	private MyQueue<Event> orderLine;
	private MyQueue<Event> workLine;
	private Simulator sim;
	private String fileOut,fileIn; 
	public Fifo(String fileIn,String fileOut,int sec){
		orderLine = new MyQueue<Event>();
		sim = new Simulator(sec);
		workLine = sim.getQueue();
		this.fileOut = fileOut;
		this.fileIn = fileIn;
	}
	public void readMessage(){
		String strin = null;
		String[] messagein = null;
		String user; int pages,time;
		try {
			BufferedReader reader = new BufferedReader(
								new FileReader(fileIn));
			while((strin = reader.readLine()) != null){
				//System.out.println(strin);
				messagein = strin.split(splitStr);
				//System.out.println(messagein[0]);
				time = Integer.parseInt(messagein[0]);
				user = messagein[1];
				pages = Integer.parseInt(messagein[2]);
				Event eventin = new Event(new Job(user,pages),time); 
				orderLine.push(eventin);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void wirteMessage(String message){
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(
					new FileWriter(fileOut));
			writer.write(message);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String run(){
		StringBuffer sb = new StringBuffer();
		int timeToUse = -1;
		for(int time = 0;!(orderLine.empty()&&workLine.empty());time++){
			if(!workLine.empty()){
				if(timeToUse == 0){
					sb.append("Time : "+time+"\r\n");
					sb.append("Complete event : "+workLine.front()+"\r\n");
					workLine.pop();
					timeToUse = -1;
				}
			}
			while(!orderLine.empty()&&time==orderLine.front().getTime()){
				sb.append("Time : "+time+"\r\n");
				sb.append("Get event : "+orderLine.front().toString()+"\r\n");
				workLine.push(orderLine.front());
				orderLine.pop();
			}
			if(!workLine.empty()){
				if(timeToUse == -1){
					while(!workLine.empty()){
						timeToUse = workLine.front().getJob().getNumOfPages()*sim.getSec();
						if(timeToUse == 0){
							//System.out.println(time);
							sb.append("Time : "+time+"\r\n");
							sb.append("Get an empty event : "+workLine.front()+"\r\n");
							workLine.pop();
						}else {
							sb.append("Time : "+time+"\r\n");
							sb.append("Start event : "+workLine.front()+"\r\n");
							break;
						}
					}
				}
				timeToUse = workLine.empty()?-1:timeToUse-1;
			}
		}
		//System.out.print(sb.toString());
		return sb.toString();
	}
}
