# Dungeon_Adventures
Minigioco implementato con il costrutto Client e Server.


### Descrizione Gioco
> Ad ogni giocatore viene assegnato a inizio del gioco un livello X di salute e una quantità Y di una pozione (X e Y). Per ogni giocatore viene generato e associato un mostro (all'inizio del gioco) con un livello Z di salute (generato casualmente).<br><br> Il gioco si svolge in round, a ogni round un giocatore può:<br>**1) Combattere col mostro:**<br>il combattimento si conclude decrementando il livello di salute del mostro e del giocatore ognuno con un valore casuale.<br>**2) Bere una parte della pozione**:<br>il livello di salute del giocatore viene incrementato di un valore proporzionale alla quantità di pozione bevuta (valore generato casualmente).<br>**3) Uscire dal gioco**:<br>In questo caso la partita viene considerata persa per il giocatore.<br><br>Il combattimento si conclude quando il giocatore o il mostro o entrambi hanno un valore di salute pari a 0, in tal caso:
> *  se il giocatore ha vinto o pareggiato, può chiedere di giocare nuovamente
> * se invece ha perso deve uscire dal gioco.

### Descrizione Applicazione
>Mediante il costrutto Client-Server:
>* **Server:**
> <br>Riceve richieste di gioco da parte dei clients e gestisce ogni connessione in un diverso thread. Ogni thread riceve i comandi passati dall'utente al client e li esegue. Dopo aver eseguito ogni comando ne comunica al client l'esito ed l'eventuale terminazione del  gioco
>* **Client:**
> <br> Si connette con il server e chiede iterativamente all'utente il comando da eseguire e lo invia al server. I comandi sono i seguenti 1) combatti, 2) bevi pozione, 3) esci del gioco.<br> Attende un messaggio che segnala l'esito del comando nel caso di gioco concluso vittoriosamente, chiede all'utente se intende continuare a giocare, in caso affermativo instaura una nuova connessione con il serve e inizia una nuova partita.
