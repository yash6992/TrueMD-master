package com.truemdhq.projectx.model;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.truemdhq.projectx.R;

/**
 * Created by yashvardhansrivastava on 09/11/16.
 */
public class InvoiceChildViewHolder extends ChildViewHolder{

        public TextView mCrimeDateText;
        public CheckBox mCrimeSolvedCheckBox;

        public InvoiceChildViewHolder(View itemView) {
            super(itemView);

            mCrimeDateText = (TextView) itemView.findViewById(R.id.child_list_item_crime_date_text_view);
            mCrimeSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.child_list_item_crime_solved_check_box);
        }

    public void bind(InvoiceChild invoiceChild) {
        mCrimeDateText.setText(invoiceChild.getVendorEmail()+"\n"+invoiceChild.getComment());
        mCrimeSolvedCheckBox.setChecked(true);
    }

    }