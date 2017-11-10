package keypow.app.model;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

import keypow.app.model.Transaction.Type;

public class Keypow {
	private String walletUrl;
	private String gameUrl;

	public Keypow(String walletUrl, String gameUrl) {
		this.walletUrl = walletUrl;
		this.gameUrl = gameUrl;
		
	}
	
	public Deposit deposit() {
		return new Deposit(walletUrl);
	}
	
	public GetLaunchUrl getLaunchUrl() {
		return new GetLaunchUrl(walletUrl, gameUrl);
	}
	
	public static class Deposit {
		private HttpRequestWithBody post;
		private JSONObject body;
		private Deposit(String walletUrl) {
			post = Unirest.post(String.format("%s/m4/wallet/transfer", walletUrl))
					.header("Content-Type", "application/json");
			body = new JSONObject();
			body.put("action", "Withdraw");
		}
		
		public Deposit setPartnerToken(String partnerToken) {
			post.header("X-Genesis-PartnerToken", partnerToken);
			body.put("partner_id", partnerToken);
			return this;
		}
		
		public Deposit setSecretKey(String secretKey) {
			post.header("X-Genesis-Secret", secretKey);
			return this;
		}
		
		public Deposit setPlayerId(String playerId) {
			body.put("user_id", playerId);
			return this;
		}
		
		public Deposit setAmount(int amount) {
			body.put("credits", amount);
			return this;
		}
		
		public Deposit setCurrency(String currency) {
			body.put("currency", currency);
			return this;
		}
		
		public void execute(ResultHandler<Transaction> callback) {
			post.body(body);
			post.asJsonAsync(new Callback<JsonNode>() {
				
				@Override
				public void failed(UnirestException e) {
					callback.onError(new KeypowException(e));
				}
				
				@Override
				public void completed(HttpResponse<JsonNode> response) {
					JSONObject respBody = response.getBody().getObject();
					Transaction tx = Transaction.newBuilder()
								.setAmount(BigDecimal.valueOf(respBody.getLong("credits_transferred")))
								.setBalance(BigDecimal.valueOf(respBody.getLong("internal_balance")))
								.setCurrency(respBody.getString("currency"))
								.setTransactionId(respBody.getString("transaction_id"))
								.setType(Type.CREDIT)
								.build();
					callback.onSuccess(tx);
				}
				
				@Override
				public void cancelled() {
					callback.onError(new KeypowException("Cancelled!"));
				}
			});
		}
	}
	
	public static class GetLaunchUrl {
		private GetRequest getRequest;
		private String gameUrl;
		private String partnerToken;
		
		private GetLaunchUrl(String walletUrl, String gameUrl) {
			getRequest = Unirest.get(String.format("%s/m4/wallet/balance/{playerId}", walletUrl))
					.header("Content-Type", "application/json");
			this.gameUrl = gameUrl;
			
		}
		
		public GetLaunchUrl setPartnerToken(String partnerToken) {
			getRequest.header("X-Genesis-PartnerToken", partnerToken);
			this.partnerToken = partnerToken;
			return this;
		}
		
		public GetLaunchUrl setSecretKey(String secretKey) {
			getRequest.header("X-Genesis-Secret", secretKey);
			return this;
		}
		
		public GetLaunchUrl setPlayerId(String playerId) {
			getRequest.routeParam("playerId", playerId);
			return this;
		}
		
		
		public void execute(ResultHandler<String> callback) {
			getRequest.asJsonAsync(new Callback<JsonNode>() {
				
				@Override
				public void failed(UnirestException e) {
					callback.onError(new KeypowException(e));
				}
				
				@Override
				public void completed(HttpResponse<JsonNode> response) {
					JSONObject respBody = response.getBody().getObject();
					System.out.println("respBody:" + respBody);
					String token = respBody.getString("session_token");
					String url = String.format("%s?partner=%s&session=%s&mode=real", gameUrl, partnerToken, token);
					callback.onSuccess(url);
					
					
				}
				
				@Override
				public void cancelled() {
					callback.onError(new KeypowException("Cancelled!"));
				}
			});
		}
	}
}
