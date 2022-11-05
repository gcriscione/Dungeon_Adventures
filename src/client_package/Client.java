package client_package;
import java.io.*;
import java.net.*;


/**
 * Reti e laboratorio III - A.A. 2022/2023
 * Assignment_6_Dungeon_Adventures
 * 
 * @author Giovanni Criscione
 *
 */
public class Client {
	
	public static void main(String[] args) throws Exception {
		final int PORT = 10163; 
		final String ADDRESS = "127.0.0.1";
		
		//Dichiarazione variabili
		Socket socket;
		BufferedReader input_term = null;               //stream per la lettura da terminale
		BufferedReader input_socket = null;             //stream per la lettura dal socket
        PrintWriter output_socket = null;               //stream per la scrittura nel socket
    	String in_socket = "";                          //stringa dove vengono salvati i dati letti dal socket
    	String in_term = "";                            //stringa dove vengono salvati i dati letti dal terminale
    	
    	Boolean play = true;                            //true --> rinizia il gioco
		Boolean alive = true;                           //true --> continua a giocare (round)
        int round = 0;                                  //round attualmente giocato
        int choice = 0;                                 //contiene la scelta dell'utente per ogni round
		GameVariable game = new GameVariable();         //Classe contente i informazioni dei personaggi di gioco
		//-----------------------------------------------------------------------------------------------------------


        try{    
			//Inizio partita
        	while(play) {
				//crea una connessione con il server
				socket = new Socket(ADDRESS, PORT);

				//Inizializzazione stream di comunicazione
				input_term = new BufferedReader(new InputStreamReader(System.in));
				input_socket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				output_socket = new PrintWriter(socket.getOutputStream(),true);

				
				//Pulisce la console
				ClearConsole();
				System.out.println("\n\tAssignment_6_Dungeon_Adventures Client");
				System.out.println("Connesso con il server [ip: "+socket.getRemoteSocketAddress()+"], avvio gioco:\n");
        		round = 1;

			
            	//Inizio comunicazione con il server  -----------
            	output_socket.println("start");                                //invio richiesta al server per avviare il gioco
            	in_socket = input_socket.readLine();                             //ricezione IdGame
        		introduction(Integer.valueOf(in_socket));                        //stampa introduzione
        		
        		in_socket = input_socket.readLine();                           //ricezione dati personaggi
				game.setAll(in_socket);
	        	poit(game);

	        	
	        	//Inizio turni di gioco
	        	while(alive){
	        		System.out.println("\n\tRound "+round+":");
	        		
	        		System.out.print(" 1) Fight\n 2) Use the potion\n 3) Exit\n -->");
	        		choice = Integer.valueOf(input_term.readLine());          //legge la scelta utente da terminale
					output_socket.println(choice);                            //invia il valore al server

	        		//riceve la risposta del server
	        		switch(input_socket.readLine().trim()){

	        			case "1":
	        				battle();      //stampa animazione battaglia
	        			break;

	        			case "2":
	        				potion();      //stampa animazione pozione
	        			break;

	        			case "3":
							gameover();
	        				alive = false;   //interrompe gioco
							play = false;
	        			continue;
	        		
						case "invalid":
							System.out.println("\t<---!  Valore inserito non corretto  !--->");
						break;

	        			default:
	        				System.out.println("\n\tError! Wrong value received from the server\n");
							socket.close();
						return ;
	        		}
	        		System.out.println("_______________________________________________________________________\n\n");

	        		in_socket = input_socket.readLine();          //ricezione dati personaggi
	        		game.setAll(in_socket);
		        	poit(game);
	        		round++;
	        		System.out.print("\n\n\n");


					//riceve la risposta del server
					switch(input_socket.readLine().trim()){
						case "continue":
							;
						break;

						case "win":
							win();
							System.out.println("\n\t<--!!! Vittoria !!!--");
							alive = false;
						break;

						case "pareggio":
							pareggio();
							System.out.println("\n\t<--! Pareggio !-->");
							alive = false;
						break;

						case "gameover":
							gameover();
							alive = false;
							play = false;
						continue;

						default:
							System.out.println("\n\tError! Wrong value received from the server\n");
							socket.close();
						break;
					}
	        	}
				socket.close();        //chiude la connessione

	        	if(play){
					//Opzione per rigiocare la partita
					System.out.println("\n\nPrimi 1 se vuoi rigiocare:");
					in_term = input_term.readLine();
					
					if(in_term.compareTo("1")==0) {
						play = true;
						alive = true;
						System.out.println("\n\n\n\n\n\n\n\n\n");
					}else{
						play = false;
					}
				}
        	}
        	
        	
        	System.out.println("\n\nFine Gioco\n");  
            
        }
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
        	if(input_term!=null) {  input_term.close(); }
        	if(input_socket!=null) {  input_socket.close(); }
        }
    }
	


    static void ClearConsole(){
        try{
        	//Controlla il sistema operativo del dispositivo
            String operatingSystem = System.getProperty("os.name"); 
              
            if(operatingSystem.contains("Windows")){        
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } 
        }catch(Exception e){
            System.out.println(e);
        }
    }
	
	
	static void introduction(int IdGame) {
		System.out.println("\t        ,     \\    /      ,        \r\n"
						+ "\t       / \\    )\\__/(     / \\       \r\n"
						+ "\t      /   \\  (_\\  /_)   /   \\      \r\n"
						+ "\t ____/_____\\__\\@  @/___/_____\\____ \r\n"
						+ "\t|             |\\../|              |\r\n"
						+ "\t|              \\VV/               |\r\n"
						+ "\t|       Dungeon_Adventures        |\r\n"
  					    + String.format("\t|            Game %02d             |\r\n",IdGame)
						+ "\t|_________________________________|\r\n"
						+ "\t |    /\\ /      \\\\       \\ /\\    | \r\n"
						+ "\t |  /   V        ))       V   \\  | \r\n"
						+ "\t |/     `       //        '     \\| \r\n"
						+ "\t `              V                '\n");
		
	}
	
	
	static void poit(GameVariable g) {

		String s = "\n   ________________________________________\n"+
				   	 "  |       Player       |       Monster     |\n";
		if(g.dec_player>=0){
			s += String.format("  |  hp:     %02d  (+%02d) |  hp:    %02d        |\n",g.player_hp,g.dec_player,g.monster_hp,g.dec_monster);
		}else{
			s += String.format("  |  hp:     %02d  (-%02d) |  hp:    %02d  (-%02d) |\n",g.player_hp,Math.abs(g.dec_player),g.monster_hp,Math.abs(g.dec_monster));
		}

		s += String.format("  |  potion: %02d        |                   |\n",g.player_potion)+
			"  |____________________|___________________|\n";

		System.out.printf(s);
							
	}
	
	
	static void battle() {
		System.out.println("\t\t\t<<<    Fight    >>>\n\n"
				+ "                                       |\\             //\r\n"
				+ "                                        \\\\           _!_\r\n"
				+ "                                         \\\\         /___\\\r\n"
				+ "                                          \\\\        [+++] \r\n"
				+ "                                           \\\\    _ _\\^^^/_ _\r\n"
				+ "               ,a_a                         \\\\/ (    '-'  ( )\r\n"
				+ "              {/ ''\\_                       /( \\/ | {&}   /\\ \\\r\n"
				+ "              {\\ ,_oo)                        \\  / \\     / _> )\r\n"
				+ "              {/  (_^_@\\_________________      \"`   >:::;-'`\"\"'-.\r\n"
				+ "    .=.      {/ \\___)))*)---------------\"`         /:::/         \\\r\n"
				+ "   (.=.`\\   {/   /=;  @/                          /  /||   {&}   |\r\n"
				+ "       \\ `\\{/(   \\/\\                             (  / (\\         /\r\n"
				+ "        \\  `. `\\  ) )                            / /   \\'-.___.-'\r\n"
				+ "         \\    // /_/_                          _/ /     \\ \\\r\n"
				+ "          '==''---))))                        /___|    /___|");
	}
	
	
	static void potion() {
		System.out.println("                __ \r\n"
				+ "               (__)\r\n"
				+ "              <____>\r\n"
				+ "               )--(\r\n"
				+ "              /\\/\\/\\\r\n"
				+ "             /\\/\\/\\/\\\r\n"
				+ "             \\/\\/\\/\\/\r\n"
				+ "             /\\/\\/\\/\\\r\n"
				+ "            /\\/\\/\\/\\/\\    .----------.\r\n"
				+ "           //\\\\//\\\\//\\\\   \\~~~~~~~~~~/\r\n"
				+ "          /\\\\//\\\\//\\\\//\\   \\/\\/\\/\\/\\/\r\n"
				+ "         |\\//\\\\//\\\\//\\\\/|   \\/\\/\\/\\/\r\n"
				+ "         |/\\\\//\\\\//\\\\//\\|    \\/\\/\\/\r\n"
				+ "          \\/\\/\\/\\/\\/\\/\\/      ~||~\r\n"
				+ "           \\/\\/\\/\\/\\/\\/        ||\r\n"
				+ "            `---------'      __||__\r\n"
				+ "                            `------'");
	}
	
	
	static void win() {
		System.out.println("   .\r\n"
				+ "  / \\\r\n"
				+ "  | |\r\n"
				+ "  |.|\r\n"
				+ "  |.|\r\n"
				+ "  |:|      __\r\n"
				+ ",_|:|_,   /  )\r\n"
				+ "  (Oo    / _I_\r\n"
				+ "   +\\ \\  || __|\r\n"
				+ "      \\ \\||___|\r\n"
				+ "        \\ /.:.\\-\\\r\n"
				+ "         |.:. /-----\\\r\n"
				+ "         |___|::oOo::|\r\n"
				+ "         /   |:<_T_>:|\r\n"
				+ "        |_____\\ ::: /\r\n"
				+ "         | |  \\ \\:/\r\n"
				+ "         | |   | |\r\n"
				+ "         \\ /   | \\___\r\n"
				+ "         / |   \\_____\\\r\n"
				+ "         `-'");
	}
	
	
	static void gameover() {
		System.out.println("\t<--!   Game Over   !-->\n"
							+"           ____ __\r\n"
							+ "          { --.\\  |          .)%%%)%%\r\n"
							+ "           '-._\\\\ | (\\___   %)%%(%%(%%%\r\n"
							+ "               `\\\\|{/ ^ _)-%(%%%%)%%;%%%\r\n"
							+ "           .'^^^^^^^  /`    %%)%%%%)%%%'\r\n"
							+ "          //\\   ) ,  /       '%%%%(%%'\r\n"
							+ "    ,  _.'/  `\\<-- \\< \r\n"
							+ "     `^^^`     ^^   ^^\n"
							+ "__________________________________________________"
						);
	}
	
	
	static void pareggio() {
		System.out.println("         />_________________________________\r\n"
				+ "[########[]_________________________________>\r\n"
				+ "         \\>");
	}
	
}