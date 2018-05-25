package com.baigu.dms.domain.netservice.response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 22:20
 *
 */
public class BaseResponse<T> {
    public static final String SUCCESS = "success";


    private int code;
    private String status;
    private String description;
    private String message;
    private T data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
