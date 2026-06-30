package mg.itu.tsimbina.dto;

public class UrlMethodDTO {
    public UrlMethodDTO(String url, String methodName) {
        this.url = url;
        this.methodName = methodName;
    }

    String url;
    String methodName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UrlMethodDTO other = (UrlMethodDTO) obj;
        return url.equals(other.url) && methodName.equals(other.methodName);
    }
    @Override
    public String  toString() {
        return "UrlMethodDTO{" +
                "url='" + url + '\'' +
                ", methodName='" + methodName + '\'' +
                '}';
    }
    @Override
    public int hashCode(){
        return java.util.Objects.hash(url, methodName);
    }
}
