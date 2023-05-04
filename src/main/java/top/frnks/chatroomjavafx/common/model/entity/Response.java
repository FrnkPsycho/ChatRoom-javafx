package top.frnks.chatroomjavafx.common.model.entity;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Response {
    private ResponseType responseType;
    private ResponseStatus responseStatus;
    private Map<String, Object> dataMap;
    private OutputStream outputStream;

    public Response() {
        responseStatus = ResponseStatus.OK;
        dataMap = new HashMap<>();
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setData(String key, Object value) {
        this.dataMap.put(key, value);
    }

    public Object getData(String key) {
        return this.dataMap.get(key);
    }

    public void removeData(String key) {
        this.dataMap.remove(key);
    }

    public void clearDataMap() {
        this.dataMap.clear();
    }
}
