package keypow.app.main;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="keypow")
public class KeypowConfig {
	private String walletUrl;
	private String gameUrl;

	public String getWalletUrl() {
		return walletUrl;
	}

	public void setWalletUrl(String walletUrl) {
		this.walletUrl = walletUrl;
	}

	public String getGameUrl() {
		return gameUrl;
	}

	public void setGameUrl(String gameUrl) {
		this.gameUrl = gameUrl;
	}
}
