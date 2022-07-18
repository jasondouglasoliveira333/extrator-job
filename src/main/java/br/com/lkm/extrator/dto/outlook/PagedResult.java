package br.com.lkm.extrator.dto.outlook;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PagedResult<T> {

    @JsonProperty("@odata.nextLink")
    private String nextLink;
    @JsonProperty("@odata.context")
    private String context;
    private T[] value;

    public String getNextLink() {
        return nextLink;
    }

    public void setNextLink(String nextLink) {
        this.nextLink = nextLink;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public T[] getValue() {
        return value;
    }

    public void setValue(T[] value) {
        this.value = value;
    }
}
