package retrofit.callback;

/***
 * 功能描述:
 * 作者:qiujialiu
 * 时间:2017/5/31
 ***/

public class BaseResult<T> {
    public static final int SUCCESS = 00;
    public String code;
    public String msg;
    public T data;

    public static int getSUCCESS() {
        return SUCCESS;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return code.equals("200");
    }


    public boolean isConnectError(){

        return  code.equals("4401") || code.equals("4402") || code.equals("4403") || code.equals("500");

    }



}
