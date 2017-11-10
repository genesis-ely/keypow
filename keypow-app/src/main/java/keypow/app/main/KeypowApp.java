package keypow.app.main;

import io.vertx.core.Vertx;
import keypow.app.model.Keypow;
import keypow.app.rest.KeypowRest;

public class KeypowApp  {
	private Vertx vertx;
	private Keypow keypow;
	
	public KeypowApp(Vertx vertx, Keypow keypow) {
		this.vertx = vertx;
		this.keypow = keypow;
	}
	
	public void init() {
		vertx.deployVerticle(new KeypowRest(keypow));
    }
}
