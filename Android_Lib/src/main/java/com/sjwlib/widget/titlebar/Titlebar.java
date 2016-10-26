package com.sjwlib.widget.titlebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sjwlib.R;

public class Titlebar extends RelativeLayout {
    IBack iBack;
    IAction iAction;
    ITitle iTitle;
    private RelativeLayout layout;
    private TextView tv_title = null, tv_action = null;
    private ImageView iv_logo = null, iv_back = null, iv_action = null;

    public Titlebar(Context context) {
        this(context, null);
    }

    public Titlebar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typed = context.obtainStyledAttributes(attrs, R.styleable.Titlebar);
        int tbarType = typed.getInteger(R.styleable.Titlebar_titleType, 0);
        switch(tbarType) {
            case 0 :
                layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.activity_title_logo, this, true);
                iv_logo = (ImageView) layout.findViewById(R.id.iv_logo);
                int logoSrc = typed.getResourceId(R.styleable.Titlebar_titleLogoSrc, -1);
                setLogoSrc(logoSrc);
                break;
            case 1 :
                layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.activity_title_one, this, true);
                break;
            case 2 :
                layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.activity_title_back, this, true);
                initBack();
                break;
            case 3 :
                layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.activity_title_back_action_iv, this, true);
                iv_action = (ImageView) layout.findViewById(R.id.iv_action);
                int actionSrc = typed.getResourceId(R.styleable.Titlebar_titleActionSrc, -1);
                setActionSrc(actionSrc);
                initBack();
                initAction(iv_action);
                break;
            case 4 :
                layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.activity_title_back_action_tv, this, true);
                tv_action = (TextView) layout.findViewById(R.id.tv_action);
                String actionTxt = typed.getString(R.styleable.Titlebar_titleActionText);
                setActionTxt(actionTxt);
                initBack();
                initAction(tv_action);
                break;
            default :
                break;
        }

        tv_title = (TextView) layout.findViewById(R.id.tv_title);
        String title = typed.getString(R.styleable.Titlebar_titleText);
        setTitleTxt(title);
        int titlebg = typed.getColor(R.styleable.Titlebar_titleBackground, -1);
        setTitleBg(titlebg);
        initTitle(tv_title);
        boolean isTitleClick = typed.getBoolean(R.styleable.Titlebar_titleClickable, false);
        setTitleClickable(isTitleClick);
    }

    public void setLogoSrc(int logoSrc) {
        if (iv_logo != null && logoSrc != -1) {
            iv_logo.setImageResource(logoSrc);
        }
    }

    public void setTitleTxt(String title) {
        if (tv_title != null) {
            tv_title.setText(title);
        }
    }

    public void setTitleBg(int titleBg) {
        if (tv_title != null && titleBg != -1) {
            tv_title.setBackgroundColor(titleBg);
        }
    }

    public void setActionSrc(int actionSrc) {
        if (iv_action != null && actionSrc != -1) {
            iv_action.setImageResource(actionSrc);
        }
    }

    public void setActionTxt(String actionTxt) {
        if (tv_action != null) {
            tv_action.setText(actionTxt);
        }
    }

    public void setTitleClickable(boolean isClick) {
        tv_title.setClickable(isClick);
    }

    // interface

    public interface IBack {
        public void onClick();
    }

    public interface IAction {
        public void onClick();
    }

    public interface ITitle {
        public void onClick();
    }

    public void setBackOnClick(IBack interBack) {
        iBack = interBack;
    }

    public void setActionOnClick(IAction interAction) {
        iAction = interAction;
    }

    public void setTitleOnClick(ITitle interTitle) {
        iTitle = interTitle;
    }

    private void initBack() {
        iv_back = (ImageView) layout.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iBack != null) {
                    iBack.onClick();
                }
            }
        });
    }

    private void initAction(View view) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iAction != null) {
                    iAction.onClick();
                }
            }
        });
    }

    private void initTitle(View view) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iTitle != null) {
                    iTitle.onClick();
                }
            }
        });
    }
}