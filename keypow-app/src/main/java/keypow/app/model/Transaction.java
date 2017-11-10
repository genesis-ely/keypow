package keypow.app.model;

import java.math.BigDecimal;

public class Transaction {
	private Transaction() {

	}

	public enum Type {
		DEBIT, CREDIT
	}

	private String transactionId;
	private String currency;
	private BigDecimal amount;
	private BigDecimal balance;
	private Type type;
	
	
	public String getTransactionId() {
		return transactionId;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	
	public Type getType() {
		return type;
	}

	public static class Builder {

		private Transaction transaction;

		public Builder() {
			transaction = new Transaction();
		}

		public Builder setTransactionId(String transactionId) {
			transaction.transactionId = transactionId;
			return this;
		}
		
		public Builder setCurrency(String currency) {
			transaction.currency = currency;
			return this;
		}
		
		public Builder setAmount(BigDecimal amount) {
			transaction.amount = amount;
			return this;
		}
		
		public Builder setBalance(BigDecimal balance) {
			transaction.balance = balance;
			return this;
		}
		
		public Builder setType(Type type) {
			transaction.type = type;
			return this;
		}
		
		public Transaction build() {
			return transaction;
		}
		
	}

	public static Transaction.Builder newBuilder() {
		return new Builder();
	}

}
