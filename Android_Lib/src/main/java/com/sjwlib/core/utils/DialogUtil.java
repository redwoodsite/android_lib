/**
 *
 */
package com.sjwlib.core.utils;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * Description:
 * <br/>网站: <a href="http://www.crazyit.org">疯狂Java联盟</a>
 * <br/>Copyright (C), 2001-2014, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */
public class DialogUtil
{
    private static ProgressDialog myDialog;


    // 定义一个显示消息的对话框
    public static void showDialog(final Context context, String msg)
    {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(msg).setCancelable(false);
        builder.setPositiveButton("确定", null);
        builder.create().show();
    }

    // 定义一个显示指定组件的对话框
    public static void showDialog(Context ctx , View view)
    {
        new AlertDialog.Builder(ctx)
                .setView(view).setCancelable(false)
                .setPositiveButton("确定", null)
                .create()
                .show();
    }

    public void showTips(Context context) {
        showTips(context, "正在加载数据...");
    }

    public static void showTips(Context context,String message) {
        myDialog = new ProgressDialog(context); // 获取对象
        myDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置样式为圆形样式
        //myDialog.setTitle("友情提示"); // 设置进度条的标题信息
        myDialog.setMessage("正在加载数据"); // 设置进度条的提示信息
        //myDialog.setIcon(R.drawable.ic_launcher); // 设置进度条的图标为应用程序图标
        myDialog.setIndeterminate(true); // 设置进度条是否为不明确
        myDialog.setCancelable(true); // 设置进度条是否按返回键取消
        myDialog.show(); // 显示进度条
    }

    public static void display(Context context, String msg){

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void closeTips(){
        if (myDialog.equals(null)== false)
            myDialog.dismiss();
    }

}
