package keypow.app.model;

public interface ResultHandler<T> {
	void onSuccess(T result);
	void onError(KeypowException kex);
}
