package nbody_distribuito.master;

public class StrategyFactory {

	public PartitionStrategy getStrategy(){
		return new FixedJobStrategy();
	}
}
