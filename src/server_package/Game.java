package server_package;
import java.io.*;
import java.net.*;
import java.util.concurrent.ThreadLocalRandom;

public class Game implements Runnable{
	private Socket socket;
	private int id;
    private int player_hp;
    private int player_potion;
    private int monster_hp;
	
	public Game(Socket socket, int id) {
		this.socket = socket;       	//prende il socket da gestire
		this.id = id;					//prende l'id del game
	    this.player_hp = 0;
	    this.player_potion = 0;
	    this.monster_hp = 0;
	}
	
	public void run() {
        System.out.println(" -> Game "+this.id+":  Connected: [ip: "+this.socket.getRemoteSocketAddress()+"]");
		

        try(
        	BufferedReader input_socket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	PrintWriter output_socket = new PrintWriter(socket.getOutputStream(), true)
        	) {
        	Boolean game = true;        //true  -->  continua a giocare
			int r1 = 0;                 //valore random generato per player_hp
			int r2 = 0;                 //valore random generato per monster_hp


			//Inizio comunicazione con il client  -----------
			if(input_socket.readLine().compareTo("start")==0){
				output_socket.println(this.id);                        //invia valore IdGame
				
				//Inizio gioco --> setta valori personaggi
				this.player_hp = ThreadLocalRandom.current().nextInt(10, 100 + 1);           //valore casuale tra [10,100]
				this.player_potion = ThreadLocalRandom.current().nextInt(10, 100 + 1);       //valore casuale tra [10,100]
				this.monster_hp = ThreadLocalRandom.current().nextInt(10, 100 + 1);          //valore casuale tra [10,100]
				
				//invia al client la stringa contenente: salute_giocatore, livello_pozione, salute_mostro
				output_socket.println(this.player_hp+"/"+0+"/"+this.player_potion+"/"+this.monster_hp+"/"+0);
			}else{
				System.out.println("\n\tCommunication error with customer!\n");
				System.out.println(" -> Game "+this.id+":  Terminated: [ip: "+this.socket.getRemoteSocketAddress()+"]");
				socket.close();
				return ;
			}


        	while(game) {
        		//lettura valore client
				r1 = 0;
				r2 = 0;
        		switch(input_socket.readLine()) {

					//combattimento
					case "1":
						output_socket.println("1");           //comunica al client l'operazione da svolgere

						r1 = random_value(this.player_hp);      //genera valore casuale
						this.player_hp += r1;                   //decrementa vita player
						r2 = random_value(this.monster_hp);     //genera valore casuale    
        				this.monster_hp += r2;                  //decrementa vita monster
        			break;


					//utilizzo pozione
        			case "2":
						output_socket.println("2");           //comunica al client l'operazione da svolgere
						if(this.player_potion>0){
							r1 = - random_value(this.player_potion-1) +1;     	//genera valore casuale
							this.player_potion -= r1;                 			//decrementa la pozione
							this.player_hp += r1;                   			//incrementa salute player
						}
        			break;
        			
					case "3":
						output_socket.println("3");
        				game = false;
					continue;

        			default:
						output_socket.println("invalid");
					break;
        		}
				//Stringa inviata al client: "salute_giocatore/decremento_giocatore/livello_pozione/salute_mostro/decremento_mostro"
				output_socket.println(this.player_hp+"/"+r1+"/"+this.player_potion+"/"+this.monster_hp+"/"+r2);

				//Controllo fine partita
				if(this.player_hp<=0 || this.monster_hp<=0){
	
					//Caso Sconfitta
					if(this.player_hp<=0 && this.monster_hp>0){
						output_socket.println("gameover");

					//Caso Pareggio
					}else if(this.monster_hp<=0 && this.player_hp<=0){
						output_socket.println("pareggio");
					}
					//Caso vittori
					else if(this.monster_hp<=0 && this.player_hp>0){
						output_socket.println("win");
					}
					game = false;
				}else{
					output_socket.println("continue");
				}
        	}
        	
        	System.out.println(" -> Game "+this.id+":  Terminated: [ip: "+this.socket.getRemoteSocketAddress()+"]");
        }
		catch (Exception e) {
            System.out.println("Error:" + socket);
        }
    }
	
	
	
	
	
	//ritorna una valore casuale compreso [0, max]
	public int random_value(int max) {
		return (- ThreadLocalRandom.current().nextInt(0, max + 1));
	}
}