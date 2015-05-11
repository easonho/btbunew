package com.meicai.util.ui;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by woo on 12/29/14.
 */



public class BarcodeTextView extends TextView {

    public interface OnBarCodeListener {
        void OnBarCode(String str);
        String OnInput(int keyCode);
    };


    private InputMethodManager input;
    private String barcode;
    private OnBarCodeListener mL;

    public String getBarcode() {
        return barcode;
    }

    public BarcodeTextView(Context context) {
        super(context);
        if (isInEditMode()) {
            return;
        }
        Button bt;

    }

    public void setOnBarCodeEvent(OnBarCodeListener l) {
        mL = l;
    }


    public BarcodeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        input = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        setFocusable(true);
        setFocusableInTouchMode(true);
        input.showSoftInput(this,0);
    }

    public BarcodeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            return;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            return super.onKeyUp(keyCode,event);
        }
        if (keyCode == 134) {
            if (barcode != null && barcode != "") {
                mL.OnBarCode(barcode);
                return true;
            }
            Toast.makeText(getContext(),"无效的输入",Toast.LENGTH_SHORT).show();
            return  true;
        }

        this.setText(mL.OnInput(keyCode));
        return true;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.actionLabel = null;
        outAttrs.label = "Test text";
        outAttrs.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;


        return new BarcodeInputConnection(this, true);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }



    class BarcodeInputConnection extends BaseInputConnection {

        public BarcodeInputConnection(View targetView, boolean fullEditor) {
            super(targetView, fullEditor);
        }


        public boolean commitText(CharSequence text, int newCursorPosition) {
            barcode = (String) text;
            return true;
        }
    }


}



