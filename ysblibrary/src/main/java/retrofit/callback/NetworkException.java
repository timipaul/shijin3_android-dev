package retrofit.callback;

import java.io.IOException;

/***
 * 功能描述:请求数据时的exception
 * 作者:qiujialiu
 * 时间:2017/5/31
 ***/

public class NetworkException extends IOException {
    private int type;
    private int code;

    public NetworkException(int type, String message) {
        super(message);
        this.type = type;
    }

    public NetworkException(int type, int code, String message) {
        super(message);
        this.type = type;
        this.code = code;
    }

    public NetworkException(String detailMessage) {
        super(detailMessage);
        this.type = FailOrExceptionType.NORMAL_TYPE;
    }

    public int getCode() {
        return code;
    }

    public int getType() {
        return type;
    }
}
