package Class5.sync;

public class SharedObject {
	// Bank Account
	int money;
	
	public SharedObject (int initialValue) {
		this.money = initialValue;
		
	}
	
	public void addMoney (int value) {
		System.out.println("ADD MONEY *BEF*. ADD: "+this.money);
		int aux = this.money;
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		aux+=value;
		this.money = aux;
		System.out.println("ADD MONEY **AFTER**. ADD: "+this.money);
	}
	
	public void discMoney (int value) {
		System.err.println("DISC MONEY -BEF-. EXTR: "+this.money);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.money-=value;
		System.err.println("DISC MONEY --AFTER--. EXTR: "+this.money);
	}
	
	public int getCurrentBalance () {
		return this.money;
	}
}
