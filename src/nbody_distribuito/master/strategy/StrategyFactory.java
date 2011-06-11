package nbody_distribuito.master.strategy;


public class StrategyFactory {

	public PartitionStrategy getStrategy(){
		return new FixedJobStrategy();
	}
}
