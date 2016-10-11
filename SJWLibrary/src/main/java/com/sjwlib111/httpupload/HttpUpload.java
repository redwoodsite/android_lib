package com.sjwlib111.httpupload;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.typedef.UploadModel;

/**
 * Created by Administrator on 2015/11/5.
 */
public class HttpUpload {
    /* 将手机文件上传至服务器 注：支持任意格式
        调用示例，文件名仅限13598870467的手机：
            UploadModel model = new UploadModel();
            model.file = new File("/storage/emulated/0/DCIM/Camera/1446685622064.jpg");;
            model.phone = edt_phone.getText().toString();
            model.action = edt_action.getText().toString();
            model.field = edt_field.getText().toString();
            model.url = App_Const.SERVICE_URL_UPLOAD;
            model.ex1 = "YD151116055303";
            HttpUpload.upload(model);
        manifest.xml配置：
             <uses-permission android:name="android.permission.INTERNET" />
        x_layout.xml，加入进度
            <ProgressBar
            android:id="@+id/progressBar1"
            style="?android:attr/progressBarStyleLarge"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:visibility="gone"
            android:layout_alignRight="@+id/textView1"
            android:layout_below="@+id/textView1" />
    */

    /**
     * 异步加载图片完毕的回调接口
     */
    public interface UploadCallback{
        /**
         * 回调函数
         * @param msg: 服务器回执
         */
        public void onUpload(String msg);
    }

    public static void upload(final UploadModel model, final UploadCallback callback){
        if(model.progressBar != null)
            model.progressBar.setVisibility(View.VISIBLE);
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... arg0) {
                //异步加载，千万不能把网络请求放在UI主线程中，不然会发生异常android.os.NetworkOnMainThreadException
                String end = "\r\n";//回车换行
                String twoHyphens = "--";//参数分隔符ps:与boundary分割传入的参数
                String boundary = "***********";//分界线可以任意分配

                try {
                    model.url = model.url
                            + "?phone="+model.phone + "&action=" + model.action + "&field="
                            + model.field + "&ex1=" + model.ex1 + "&ex2=" + model.ex2;
                    URL url = new URL(model.url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setUseCaches(false);//不适用缓存
                    httpURLConnection.setRequestMethod("POST");//post请求
                    httpURLConnection.setRequestProperty("filetype","100");//一直保持链接状态
                    httpURLConnection.setRequestProperty("Connection","keep-Alive");//一直保持链接状态
                    httpURLConnection.setRequestProperty("Charset", "utf-8");//字符集编码为utf-8
                    httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
                    //请求数据为多元性数据，这里只用分界线不用分隔符表示，必须严格按照这样写，不然服务器无法识别

                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    //获取输出流
                    String newName = model.field ;;//临时文件名字。可任意改.我的服务器端存储的不是它，以为我用了全球唯一标识符（Guid）来命名的
                    dataOutputStream.writeBytes(twoHyphens+boundary+end);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; "+
                            "name=\"MyHeadPicture\";filename=\""+
                            newName +"\""+ end);
						/*注意，千万注意这的MyHeadPicture与浏览器端的<input type="file" name="MyHeadPicture"/>name对应的属性一致
						，记住不能少了回车换行结束标志*/
                    dataOutputStream.writeBytes(end);

                    FileInputStream fStream =new FileInputStream(model.file);//获取本地文件输入流
				          /* 设置每次写入1024bytes */
                    int bufferSize =1024;
                    byte[] buffer =new byte[bufferSize];
                    int length =-1;
                    //      StringBuffer sb = new StringBuffer();
				          /* 从文件读取数据至缓冲区 */
                    while((length = fStream.read(buffer)) !=-1)
                    {
                        //     	  sb.append(length);
				            /* 将资料写入DataOutputStream中 */
                        dataOutputStream.write(buffer, 0, length);//将文件一字节流形式输入到输出流中
                    }
                    dataOutputStream.writeBytes(end);
                    dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + end);
				          /* close streams */
                    fStream.close();
                    dataOutputStream.flush();
				          /* 取得Response内容 */
                    InputStream is = httpURLConnection.getInputStream();//服务器端响应
                    int ch;
                    StringBuffer b =new StringBuffer();
                    while( ( ch = is.read() ) !=-1 )
                    {
                        b.append( (char)ch );
                    }
                    System.err.println(b.toString());
                    dataOutputStream.close();

                    return b.toString();//返回响应内容
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(String result) {
                if(model.progressBar != null) {
                    model.progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(model.progressBar.getContext(), result, Toast.LENGTH_LONG).show();//显示出响应内容
                }

                if(callback!=null)
                    callback.onUpload(result);

                super.onPostExecute(result);
            }

        }.execute("");
    }
}
