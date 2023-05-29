package top.frnks.chatroomjavafx.common.model.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Request implements Serializable {
//    @Serial
//    private static final long serialVersionUID = 3L;
    private ResponseType responseType;
    private ActionType action;
    private Map<String, Object> attributeMap;
    public Request() {
        this.attributeMap = new HashMap<>();
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public Map<String, Object> getAttributeMap() {
        return attributeMap;
    }

    public void setAttributeMap(Map<String, Object> attributeMap) {
        this.attributeMap = attributeMap;
    }

    public Object getAttribute(String key) {
        return this.attributeMap.get(key);
    }

    public void setAttribute(String key, Object value) {
        this.attributeMap.put(key, value);
    }

    public void removeAttribute(String key) {
        this.attributeMap.remove(key);
    }

    public void clearAttributeMap() {
        this.attributeMap.clear();
    }
}
