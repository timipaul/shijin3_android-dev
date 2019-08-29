package retrofit;


import com.hongchuang.hclibrary.utils.DeviceUuidFactory;
import com.hongchuang.hclibrary.utils.DevicesUtil;
import com.hongchuang.hclibrary.utils.LogUtils;
import com.hongchuang.hclibrary.utils.TextUtil;
import com.hongchuang.ysblibrary.YSBLibrary;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit.callback.FailOrExceptionType;
import retrofit.callback.NetworkException;

/***
 * 功能描述:
 * 作者:chenwei
 * 时间:2016/12/23
 * 版本:
 ***/
class TokenInterceptor implements Interceptor {
    private RequestStatusListener listener;

    private static void sendTokenInvalid(Response response) throws IOException {
        if (TextUtil.equals(response.header("code"), "3")) {

        }
    }

    private static ResponseBody printResponse(Response response) {
        try {
            String s = new String(response.body().bytes());
            LogUtils.w(s);
            ResponseBody body = ResponseBody.create(null, s);
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.body();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = getRequest(chain);
        long t1 = System.nanoTime();
        printParams(chain, request);
        Response response;
        try {
            if (listener != null) {
                listener.onStart(request.url().toString());
            }
            response = chain.proceed(getRequest(chain));
        } catch (ConnectException e) {
            if (listener != null) {
                listener.onError(request.url().toString());
            }
            throw new NetworkException(FailOrExceptionType.CONNECT_FAIL, "连接服务器失败!");
        } catch (ConnectTimeoutException e2) {
            if (listener != null) {
                listener.onError(request.url().toString());
            }
            throw new NetworkException(FailOrExceptionType.CONNECT_FAIL, "请求服务器超时了!");
        } catch (SocketTimeoutException e3) {
            if (listener != null) {
                listener.onError(request.url().toString());
            }
            throw new NetworkException(FailOrExceptionType.NO_RESPONSE, "服务器响应超时了!");
        } catch (UnknownHostException e4) {
            if (listener != null) {
                listener.onError(request.url().toString());
            }
            throw new NetworkException(FailOrExceptionType.CONNECT_FAIL, "无法连接到服务器!");
        } catch (SocketException e3) {
            if (listener != null) {
                listener.onError(request.url().toString());
            }
            throw new NetworkException(FailOrExceptionType.NO_RESPONSE, "连接服务器失败!");
        }
        long t2 = System.nanoTime();
        String resStr = String.format("Received response for %s in %.1fms", response.request().url(), (t2 - t1) / 1e6d);
        LogUtils.w("OkHttpManager", resStr);
        if (response.code() == 3003 || response.code() == 3002) {
            HttpUrl url = request.url();
            String s = url.toString();
            sendTokenInvalid(response);
            if (listener != null) {
                listener.onError(request.url().toString());
            }
            throw new NetworkException(FailOrExceptionType.NO_LOGIN, "3003");
        }
        if (response.code() == 500 ){
            if (listener != null) {
                listener.onError(request.url().toString());
            }
            throw new NetworkException(FailOrExceptionType.CONNECT_ERROR, response.message());
        }
        if (listener != null) {
            response.header("token");
            listener.onComplete(request.url().toString());
        }

        return createResponseBody(request, response, printResponse(response));
    }

    private Request getRequest(Chain chain) {
        String token = ToKenUtil.getToken();
        if (TextUtil.isEmpty(token)) {
            return chain.request().newBuilder().addHeader("app_version", "Android_" + DevicesUtil.getVersion(YSBLibrary.getLibrary().getContext())).build();
        }
        return chain.request()
                .newBuilder()
                .addHeader("token", token)
                .addHeader("_platform", "android7d6802d01b")
                .addHeader("_uuid","android7d6802d01b  "+new DeviceUuidFactory(YSBLibrary.getLibrary().getContext()).getDeviceUuid().toString())
                .addHeader("app_version", "Android_" + DevicesUtil.getVersion(YSBLibrary.getLibrary().getContext()))
                .build();
    }

    private Response createResponseBody(Request request, Response response, ResponseBody body) {
        return new Response.Builder()
                .body(body)
                .code(response.code())
                .headers(response.headers())
                .message(response.message())
                .request(request)
                .protocol(response.protocol())
                .sentRequestAtMillis(response.sentRequestAtMillis())
                .build();
    }
    private final Charset UTF8 = Charset.forName("UTF-8");
    private void printParams(Chain chain, Request request) {
        LogUtils.w("OkHttpManager", String.format("Sending request %s", request.url()));
        LogUtils.w("OkHttpManager", String.format("headers %s", request.headers()));
        LogUtils.w("OkHttpManager", String.format("header %s", request.header("user-agent")));
        if (request.body() != null) {
            //LogUtils.w("OkHttpManager", request.body().toString());
            try {
                //RequestBody body =
                String body = null;
                RequestBody requestBody = request.body();
//                FormBody body = (FormBody) request.body();
                if(requestBody != null) {
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);

                    Charset charset = UTF8;
                    MediaType contentType = requestBody.contentType();
                    if (contentType != null) {
                        charset = contentType.charset(UTF8);
                    }
                    body = buffer.readString(charset);
                }

                LogUtils.w("OkHttpManager", "发送请求"+body);
            } catch (Exception e) {
                LogUtils.w("OkHttpManager", "OkHttpManager Exception msg=" + e.getMessage());
            }
        }
    }

    void setListener(RequestStatusListener listener) {
        this.listener = listener;
    }

    interface RequestStatusListener {
        void onStart(String url);

        void onComplete(String url);

        void onError(String url);
    }
}
