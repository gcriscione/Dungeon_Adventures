package server_package;
import java.net.*;
import java.util.concurrent.*;

/**
 * Reti e laboratorio III - A.A. 2022/2023
 * Assignment_6_Dungeon_Adventures
 * 
 * @author Giovanni Criscione
 *
 */
public class Server {
	public static void main(String[] args) throws Exception {
		final int PORT = 10163;                       			    //numero porta del server
		final int terminationDelay = 5000;             				//tempo massimo che aspetta per la terminazione del thread-pool
		ExecutorService pool = Executors.newFixedThreadPool(20);    //thread-pool con 20 threads
		

		//si mette in ascolto...
		try(ServerSocket listener = new ServerSocket(PORT)){                 
			System.out.println("\tAssignment_6_Dungeon_Adventures Server\nServer is running...");
			
			int game = 1;     	//id partita
			while (true) {
				pool.execute(new Game(listener.accept(), game));             //affida la connessione ad un thread del threadpool
				game++;
			}
			
		}
		catch(BindException ex){
			System.out.println(PORT + " occupata!");                 //eccezione porta occupata
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//chiusura del thread-pool
		finally {
			pool.shutdown();
			try {
				if (!pool.awaitTermination(terminationDelay, TimeUnit.MILLISECONDS))
					pool.shutdownNow();
			}
			catch (InterruptedException e){
				e.printStackTrace();
				pool.shutdownNow();
			}
			System.out.println("\n\n\t<--  Server Terminated  -->");
		}
	}
}


