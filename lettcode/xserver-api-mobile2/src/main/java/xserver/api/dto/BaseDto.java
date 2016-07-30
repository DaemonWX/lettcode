package xserver.api.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
@SuppressWarnings("serial")
public class BaseDto implements Serializable{
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
