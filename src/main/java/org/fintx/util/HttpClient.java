/**
 *  Copyright 2017 FinTx
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fintx.util;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author bluecreator(qiang.x.wang@gmail.com)
 *
 */
public final class HttpClient {
    private HttpClient() {
        throw new AssertionError("No HttpClient instances for you!");
    }

    static private final OkHttpClient client;
    
    static{
//        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor(new HttpLogger());
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient().newBuilder().sslSocketFactory(createSSLSocketFactory()).hostnameVerifier(new TrustAllHostnameVerifier()).retryOnConnectionFailure(true).connectTimeout(20, TimeUnit.SECONDS).writeTimeout(40,  TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)/*.addNetworkInterceptor(interceptor)*/.build();
    }

    /**
     * 默认信任所有的证书
     * TODO 最好加上证书认证，主流App都有自己的证书
     *
     * @return
     */
   // @SuppressLint("TrulyRandom")
    private static SSLSocketFactory createSSLSocketFactory() {

        SSLSocketFactory sSLSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()},new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)

                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
    }
    
    
    // public static void postMultipart(String url, MediaType mediaType, File[] files, String[] fileKeys,
    // Param[] params) {
    //
    // MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
    // for (Param param : params) {
    // builder.addFormDataPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
    // RequestBody.create(null, param.value));
    // }
    // if (files != null) {
    // RequestBody fileBody = null;
    // for (int i = 0; i < files.length; i++) {
    // File file = files[i];
    // String fileName = file.getName();
    // fileBody = RequestBody.create(okhttp3.MediaType.parse(mediaType.getValue()), file);
    // builder.addFormDataPart(fileKeys[i], files[i].getName(), fileBody);
    // }
    // }
    //
    // RequestBody requestBody = builder.build();
    // Request request = new Request.Builder().header("Authorization", "Client-ID " +
    // IMGUR_CLIENT_ID).url(url).post(requestBody).tag(TAG).build();
    // Response response = client.newCall(request).execute();
    // if (!response.isSuccessful())
    // throw new IOException("Unexpected code " + response);
    //
    // System.out.println(response.body().string());
    // }

    public static void getSync() throws Exception {
        Request request = new Request.Builder().url("http://publicobject.com/helloworld.txt").build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);

        Headers responseHeaders = response.headers();
        for (int i = 0; i < responseHeaders.size(); i++) {
            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
        }

        System.out.println(response.body().string());
    }

    public static void getAsync() throws Exception {
        Request request = new Request.Builder().url("http://publicobject.com/helloworld.txt").build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                System.out.println(response.body().string());
            }
        });
    }

    public static void getHeaders() throws Exception {
        Request request = new Request.Builder().url("https://api.github.com/repos/square/okhttp/issues").header("User-Agent", "OkHttp Headers.java")
                .addHeader("Accept", "application/json; q=0.5").addHeader("Accept", "application/vnd.github.v3+json").build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);

        System.out.println("Server: " + response.header("Server"));
        System.out.println("Date: " + response.header("Date"));
        System.out.println("Vary: " + response.headers("Vary"));
    }

    public static String postString(URL url, MediaType type, String postBody) throws IOException {

        Request request = new Request.Builder().url(url).post(RequestBody.create(okhttp3.MediaType.parse(type.value), postBody)).build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);

        return response.body().string();
    }

    public static String postForm(URL url,Map<String,String> formParams) throws IOException {
        FormBody.Builder builder=new FormBody.Builder();
        for(String key:formParams.keySet()){
            builder.add(key, formParams.get(key));
        }
        RequestBody formBody =builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);
        String resp= response.body().string();
        response.close();
        return resp;
    }

  
    /**
     * Common media formats: text/html:HTML格式 text/plain:纯文本格式 text/xml: XML格式 image/gif:gif图片格式 image/jpeg:jpg图片格式
     * image/png：png图片格式 Media type begin with application application/xhtml+xml:XHTML格式 application/xml :XML数据格式
     * application/atom+xml :Atom XML聚合格式 application/json :JSON数据格式 application/pdf :pdf格式 application/msword :Word文档格式
     * application/octet-stream:二进制流数据（如常见的文件下载） application/x-www-form-urlencoded:
     * <form encType="">中默认的encType，form表单数据被编码为key/value格式发送到服务器（表单默认的提交数据的格式） Others:
     * multipart/form-data:需要在表单中进行文件上传时，就需要使用该格式
     */
    public enum MediaType {
        APP_XML("application/xml"), APP_JSON("application/json"),APP_FORM("application/x-www-form-urlencoded"), TEXT_PLAIN("text/plain"), TEXT_HTML("text/html"), TEXT_XML("text/xml");
        private String value;

        private MediaType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }
    // TODO https://github.com/square/okhttp/wiki/Recipes
//    public static class HttpLogger implements HttpLoggingInterceptor.Logger {
//        @Override
//        public void log(String message) {
//            System.err.println("HttpLogInfo:\n"+ message);
//        }
//    }
}