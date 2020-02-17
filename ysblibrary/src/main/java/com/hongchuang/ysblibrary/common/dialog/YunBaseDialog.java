package com.hongchuang.ysblibrary.common.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongchuang.hclibrary.utils.DevicesUtil;
import com.hongchuang.hclibrary.utils.TextUtil;
import com.hongchuang.ysblibrary.R;


public class YunBaseDialog extends Dialog {
    private CharSequence message;

    public YunBaseDialog(Context context, int theme) {
        super(context, theme);
    }

    public YunBaseDialog(Context context) {
        super(context);
    }

//    @Override
//    public void onBackPressed() {
//
//    }

    public String getMessage() {
        return message.toString();
    }

    public void setMessage(CharSequence message) {
        this.message = message;
    }

    @Override
    public void show() {
        super.show();
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST);
    }

    public void showCanceledOnTouchOutside(boolean cancel) {
        show();
        setCanceledOnTouchOutside(cancel);
    }

    public interface AfterToDo {
        void after(String content);
    }

    public static class Builder {
        public static final int LEFT_BUTTON = DialogInterface.BUTTON_NEGATIVE;
        public static final int RIGHT_BUTTON = DialogInterface.BUTTON_POSITIVE;
        protected OnClickListener onClickListener;
        private Context context;
        private String title;
        private CharSequence content;
        private String leftButtonText;
        private String rightButtonText;
        private Drawable icon;
        private boolean haveTitle = true;

        private Button mButtonLeft;
        private Button mButtonRight;
        private ImageView mImageViewIcon;
        private TextView mTexViewTitle;
        private TextView mTextViewContent;
        private View mView;


        private DialogType type = DialogType.TYPE_BLUE;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder isHaveTitle(boolean haveTitle) {
            this.haveTitle = haveTitle;
            return this;
        }

        public Builder setIcon(Drawable icon) {
            this.icon = icon;
            return this;
        }

        public Builder setMessage(CharSequence content) {
            this.content = content;
            return this;
        }

        public Builder setButtonType(DialogType type) {
            this.type = type;
            return this;
        }


        public Builder setLeftButtonText(String leftButtonText) {
            this.leftButtonText = leftButtonText;
            return this;
        }

        public Builder setOnClickListener(OnClickListener listener) {
            this.onClickListener = listener;
            return this;
        }

        public Builder setRightButtonText(String rightButtonText) {
            this.rightButtonText = rightButtonText;
            return this;
        }

        public Builder setContentView(View view) {
            this.mView = view;
            return this;
        }


        @SuppressLint("Override")
        public YunBaseDialog create() {
            if (context == null) return null;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final YunBaseDialog dialog = new YunBaseDialog(context, R.style.Dialog);
            dialog.setMessage(content);
            if (mView == null) {
                mView = inflater.inflate(R.layout.dialog_base_two_button, null);
                dialog.setContentView(mView);
                mButtonLeft = (Button) mView.findViewById(R.id.btn_left);
                mButtonRight = (Button) mView.findViewById(R.id.btn_right);
                mTexViewTitle = (TextView) mView.findViewById(R.id.tv_title);
                mTextViewContent = (TextView) mView.findViewById(R.id.tv_content);
                mImageViewIcon = (ImageView) mView.findViewById(R.id.iv_icon);

                setOnLeftListener(dialog);
                if (mButtonLeft != null) {
                    if (mButtonRight != null) {
                        mButtonRight.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onClickListener != null) {
                                    onClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                                }

                            }
                        });
                    }

                    if (haveTitle) {
                        mTexViewTitle.setVisibility(View.VISIBLE);
                    } else {
                        mTexViewTitle.setVisibility(View.GONE);
                    }

                    if (TextUtil.isNotEmpty(title)) {
                        mTexViewTitle.setText(title);
                    }

                    mTextViewContent.setText(content);

                    if (TextUtil.isNotEmpty(leftButtonText) && mButtonLeft != null) {
                        mButtonLeft.setVisibility(View.VISIBLE);
                        mButtonLeft.setText(leftButtonText);
                    } else if (mButtonLeft != null) {
                        mButtonLeft.setVisibility(View.GONE);
                    }


                    if (rightButtonText != null && mButtonRight != null) {
                        mButtonRight.setText(rightButtonText);
                        mButtonRight.setVisibility(View.VISIBLE);
                    } else if (mButtonRight != null) {
                        mButtonRight.setVisibility(View.GONE);
                    }

                    if (icon != null) {
                        mImageViewIcon.setVisibility(View.VISIBLE);
                        mImageViewIcon.setImageDrawable(icon);
                    } else {
                        mImageViewIcon.setVisibility(View.GONE);
                    }

//                    if (type == DialogType.TYPE_BLUE) {
//                        mButtonLeft.setBackgroundResource(R.drawable.lib_selector_common_btn_blue_radius2);
//                        mButtonRight.setBackgroundResource(R.drawable.lib_selector_common_btn_blue_radius2);
//                    } else if (type == DialogType.TYPE_YELLOW) {
//                        mButtonLeft.setBackgroundResource(R.drawable.lib_select_common_btn_yellow_radius);
//                        mButtonRight.setBackgroundResource(R.drawable.lib_select_common_btn_yellow_radius);
//
//                    }
                }
            } else {
                dialog.setContentView(mView);
            }
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = DevicesUtil.getWidth(context) - DevicesUtil.dip2px(60);//宽高可设置具体大小
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            dialog.setContentView(mView);
            return dialog;
        }

        private void setOnLeftListener(final YunBaseDialog dialog) {
            if (mButtonLeft != null) {
                mButtonLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickListener != null)
                            onClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                });
            }
        }

        public enum DialogType {
            TYPE_BLUE, TYPE_YELLOW
        }
    }
}
