package keypow.app.rest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import keypow.app.model.Keypow;
import keypow.app.model.KeypowException;
import keypow.app.model.ResultHandler;
import keypow.app.model.Transaction;
import keypow.app.model.Keypow.GetLaunchUrl;

public class KeypowRest extends AbstractVerticle {

	private Router router;
	private HttpServer server;
	private Keypow keypow;

	public KeypowRest(Keypow keypow) {
		this.keypow = keypow;
	}

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		server = vertx.createHttpServer();
		router = Router.router(vertx);
		router.get("/game-launcher").handler(this::handle);
		server.requestHandler(router::accept);
		server.listen(8888);
		super.start(startFuture);
	}

	private void handle(RoutingContext ctx) {
		HttpServerRequest request = ctx.request();
		String gameId = request.getParam("gameId");
		String playerId = request.getParam("playerId");
		String partnerToken = request.getParam("partnerToken");
		String secretKey = request.getParam("secretKey");
		String deposit = request.getParam("deposit");
		String currency = request.getParam("currency");
		if (null != deposit) {
			keypow.deposit()
			.setAmount(Integer.parseInt(deposit))
			.setCurrency(currency)
			.setPartnerToken(partnerToken)
			.setPlayerId(playerId)
			.setSecretKey(secretKey)
			.execute(new DepositHandler(ctx));
		}
		keypow.getLaunchUrl()
			.setSecretKey(secretKey)
			.setPartnerToken(partnerToken)
			.setPlayerId(playerId)
			.execute(new ResultHandler<String>() {

				@Override
				public void onSuccess(String url) {
					ctx.response().setStatusCode(302);
					ctx.response().putHeader("location", url);
					ctx.response().end();
				}

				@Override
				public void onError(KeypowException kex) {
					// TODO Auto-generated method stub
					
				}
			});
		
		
		
	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}

	private static class DepositHandler implements ResultHandler<Transaction> {

		private RoutingContext ctx;

		public DepositHandler(RoutingContext ctx) {
			this.ctx = ctx;
		}

		@Override
		public void onSuccess(Transaction result) {
		}

		@Override
		public void onError(KeypowException kex) {
			ctx.response().setStatusCode(500).end(kex.getMessage());
		}
	}
}