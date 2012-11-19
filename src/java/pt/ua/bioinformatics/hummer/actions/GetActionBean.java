package pt.ua.bioinformatics.hummer.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.hummer.services.API;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/{rel=}/{id=}.{event=csv}")
public class GetActionBean implements ActionBean {

    private ActionBeanContext context;
    private String rel;
    private String id;
    private String event = "csv";

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    public ActionBeanContext getContext() {
        return context;
    }

    @DefaultHandler
    public Resolution action() {
        String response = "";
        if(!rel.equals("") && !rel.equals("")) {
            response = API.list(rel, id, event);
            return new StreamingResolution("text", response);
        }  else {
            return new ForwardResolution("/index.jsp");
        }
    }
}
