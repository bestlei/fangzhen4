package embed;

//记录映射结果的bean类
public class SimulateResult {
    private int virtual_net_num;
    private int succ_num;
    private int fail_nul;
    private double accept_rate;
    private double profit;
    private double cost;
    
	public SimulateResult(int virtual_net_num, int succ_num, int fail_nul,
			double accept_rate, double profit, double cost) {
		super();
		this.virtual_net_num = virtual_net_num;
		this.succ_num = succ_num;
		this.fail_nul = fail_nul;
		this.accept_rate = accept_rate;
		this.profit = profit;
		this.cost = cost;
	}

	public SimulateResult() {
		super();
	}

	public int getVirtual_net_num() {
		return virtual_net_num;
	}

	public void setVirtual_net_num(int virtual_net_num) {
		this.virtual_net_num = virtual_net_num;
	}

	public int getSucc_num() {
		return succ_num;
	}

	public void setSucc_num(int succ_num) {
		this.succ_num = succ_num;
	}

	public int getFail_nul() {
		return fail_nul;
	}

	public void setFail_nul(int fail_nul) {
		this.fail_nul = fail_nul;
	}

	public double getAccept_rate() {
		return accept_rate;
	}

	public void setAccept_rate(double accept_rate) {
		this.accept_rate = accept_rate;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
    
    
	
}
