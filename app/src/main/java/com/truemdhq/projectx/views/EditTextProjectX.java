package com.truemdhq.projectx.views;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.truemdhq.projectx.R;

/**
 * Created by yashvardhansrivastava on 13/11/16.
 */
public class EditTextProjectX extends RelativeLayout
{
    LayoutInflater inflater = null;
    RelativeLayout custom_et;
   public EditText edit_text;
    ImageView btn_clear;ImageView error_image;TextView error_text;
    LinearLayout error_ll;
    public TextInputLayout text_input_layout;
    public EditTextProjectX(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
// TODO Auto-generated constructor stub
        initViews();
    }
    public EditTextProjectX(Context context, AttributeSet attrs)
    {
        super(context, attrs);
// TODO Auto-generated constructor stub
        initViews();
    }
    public EditTextProjectX(Context context)
    {
        super(context);
// TODO Auto-generated constructor stub
        initViews();
    }
    void initViews()
    {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.clearable_edit_text, this, true);
        custom_et = (RelativeLayout) findViewById(R.id.custom_et);
        edit_text = (EditText) findViewById(R.id.edit_text);
        error_ll = (LinearLayout) findViewById(R.id.errorLL);
        text_input_layout = (TextInputLayout) findViewById(R.id.text_input_layout);
        btn_clear = (ImageView) findViewById(R.id.clearable_button_clear);
        error_image = (ImageView) findViewById(R.id.error_image);
        error_text = (TextView) findViewById(R.id.error_text);
        btn_clear.setVisibility(RelativeLayout.INVISIBLE);
        error_ll.setVisibility(LinearLayout.INVISIBLE);
        edit_text.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "MarkOffcPro.ttf"));
        edit_text.clearFocus();

        edit_text.setOnFocusChangeListener(new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus) {
                custom_et.setBackground(getResources().getDrawable(R.drawable.shape_steel_ghost_squ));
            }
            else if(hasFocus) {
                custom_et.setBackground(getResources().getDrawable(R.drawable.shape_steel_ghost_squ_pressed));
            }
        }
    });

        clearText();
        showHideClearButton();
    }
    void clearText()
    {
        btn_clear.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
// TODO Auto-generated method stub
                edit_text.setText("");
            }
        });
    }
    void showHideClearButton()
    {
        edit_text.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
// TODO Auto-generated method stub
                if (s.length() > 0) {
                    btn_clear.setVisibility(RelativeLayout.VISIBLE);
                    error_ll.setVisibility(RelativeLayout.VISIBLE);
                    error_text.setText(edit_text.getHint());
                    error_image.setImageDrawable(getResources().getDrawable(R.drawable.icic_textfield_valid));
                }
                else {
                    btn_clear.setVisibility(RelativeLayout.INVISIBLE);
                    error_ll.setVisibility(RelativeLayout.INVISIBLE);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
// TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s)
            {
// TODO Auto-generated method stub
            }
        });
    }
    public Editable getText()
    {
        Editable text = edit_text.getText();
        return text;
    }
    public void setError(String error)
    {
        error_image.setImageDrawable(getResources().getDrawable(R.drawable.icic_textfield_error));
        error_text.setText(error);
        error_text.setTextColor(getResources().getColor(R.color.primary_violet));

        error_ll.setVisibility(RelativeLayout.VISIBLE);
    }
    public void removeError()
    {
        error_image.setImageDrawable(getResources().getDrawable(R.drawable.icic_textfield_valid));
        error_text.setText("");
        error_ll.setVisibility(RelativeLayout.INVISIBLE);
    }
    public void allFine()
    {
        error_image.setImageDrawable(getResources().getDrawable(R.drawable.icic_textfield_valid));
        error_text.setText(edit_text.getHint());
        error_text.setTextColor(getResources().getColor(R.color.primary_green));
        error_ll.setVisibility(RelativeLayout.VISIBLE);
    }
}

