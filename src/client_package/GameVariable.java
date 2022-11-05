package client_package;

public class GameVariable {
	//Dati personaggi gioco
    public int player_hp;            //salute giocatore
    public int player_potion;        //livello pozione
    public int monster_hp;           //salute mostro

	public int dec_player;            //valore decremento salute del player
	public int dec_monster;           //valore decremento salute del player

	public GameVariable() {
	    this.player_hp = 0;
	    this.player_potion = 0;
	    this.monster_hp = 0;

		this.dec_player = 0;
		this.dec_monster = 0;
	}

	
	public void setAll(String s) {
		String array[] = s.split("/");
		assert(array.length==5);
	    this.player_hp = Integer.valueOf(array[0]);
	    this.dec_player = Integer.valueOf(array[1]);
	    this.player_potion = Integer.valueOf(array[2]);
	    this.monster_hp = Integer.valueOf(array[3]);
	    this.dec_monster = Integer.valueOf(array[4]);
	}
}
