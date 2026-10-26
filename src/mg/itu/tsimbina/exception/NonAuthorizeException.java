package mg.itu.tsimbina.exception;

public class NonAuthorizeException extends RuntimeException {

    public NonAuthorizeException(String authKey) {
        super("Methode non autoriser causer par absence de authKey value : "+ authKey);
    }

}