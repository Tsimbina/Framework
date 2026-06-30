package mg.itu.tsimbina.exception;

import mg.itu.tsimbina.dto.UrlMethodDTO;

public class DupicatedUrl extends RuntimeException{

    public DupicatedUrl(UrlMethodDTO urlMethodDTO) {
        super(urlMethodDTO.getUrl() + " avec method " + urlMethodDTO.getMethodName() + " est deja dans un autre method.");
    }
    
}
