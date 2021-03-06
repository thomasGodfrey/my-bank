package com.abc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.Math.abs;

public class Customer {
	public static int customerIDAssign = 0;

    private int customerID;
    private String name;
    private List<Account> accounts;

    public Customer(String name) {
    	this.customerID = customerIDAssign++;
        this.name = name;
        this.accounts = new ArrayList<Account>();
    }

    public String getName() {
        return name;
    }

    public Customer openAccount(Account account) {
        accounts.add(account);
        return this;
    }

    public int getNumberOfAccounts() {
        return accounts.size();
    }

    public double totalInterestEarned() {
        double total = 0;
        for (Account a : accounts)
            total += a.interestEarned();
        return total;
    }
    
    /**
     * Transfer money amount between two owned accounts
     * If there was a requirement for accounts to have a positive balance, would include a check
     * however this is not currently a stated requirement
     * @param acc1 Account to withdraw from
     * @param acc2 Account to deposit to
     * @param amount Money amount to transfer
     */
    public void transfer(Account acc1, Account acc2, double amount) {
    	if(!accounts.contains(acc1) || !accounts.contains(acc2)) {
    		throw new IllegalArgumentException("accounts must belong to customer");
    	} else if(amount == 0){
    		throw new IllegalArgumentException("amount must be greater than zero");
    	} else {
    		acc1.withdraw(amount);
    		acc2.deposit(amount);
    	}
    }

    public String getStatement() {
        String statement = null;
        statement = "Statement for " + name + "\n";
        double total = 0.0;
        for (Account a : accounts) {
            statement += "\n" + statementForAccount(a) + "\n";
            total += a.getBalance();
        }
        statement += "\nTotal In All Accounts " + toDollars(total);
        return statement;
    }

    private String statementForAccount(Account a) {
        String s = "";

       //Translate to pretty account type
        switch(a.getAccountType()){
            case Account.CHECKING:
                s += "Checking Account\n";
                break;
            case Account.SAVINGS:
                s += "Savings Account\n";
                break;
            case Account.MAXI_SAVINGS:
                s += "Maxi Savings Account\n";
                break;
        }

        //Now total up all the transactions
        double total = 0.0;
        List<Transaction> transactions = a.getTransactions();
        for (Transaction t : transactions) {
        	double tAmount = t.getAmount();
            s += "  " + (tAmount < 0 ? "withdrawal" : "deposit") + " " + toDollars(tAmount) + "\n";
            total += tAmount;
        }
        s += "  interest " + toDollars(a.interestEarned()) + "\n";
        s += "Total " + toDollars(a.getBalance());
        return s;
    }

    private String toDollars(double d){
        return String.format("$%,.2f", abs(d));
    }
}
