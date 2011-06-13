package nbody_distribuito.master.strategy;


public class StrategyFactory {
/**
 * Classe di design che restituisce la strategia usata
 * Utile per cambiare strategia velocemente
 * @return
 */
	public PartitionStrategy getStrategy(){
		return new FixedJobStrategy();
	}
}
