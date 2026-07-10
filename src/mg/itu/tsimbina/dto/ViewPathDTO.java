package mg.itu.tsimbina.dto;

import mg.itu.tsimbina.view.ModelAndView;

public class ViewPathDTO {
    private  String prefix;
    private  String suffix;

    public ViewPathDTO(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String formatView(ModelAndView mav){
        return formatStringView(mav.getViewName());
    }

    public String formatStringView(String viewName) {
        return prefix + viewName + suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }
}
