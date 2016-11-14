package com.truemdhq.projectx.model;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.truemdhq.projectx.R;

/**
 * Created by yashvardhansrivastava on 09/11/16.
 */
public class InvoiceParentViewHolder extends ParentViewHolder {

    public TextView mCrimeTitleTextView;
    public ImageView mParentDropDownArrow;
    public RelativeLayout mParent;

    public InvoiceParentViewHolder(View itemView) {
        super(itemView);

        mCrimeTitleTextView = (TextView) itemView.findViewById(R.id.parent_list_item_crime_title_text_view);
        mParentDropDownArrow = (ImageView) itemView.findViewById(R.id.parent_list_item_expand_arrow);
        mParent = (RelativeLayout) itemView.findViewById(R.id.parent_relative_view);

        mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded()) {

                    mParentDropDownArrow.animate().rotationBy(180.0f).start();

                    //mParentDropDownArrow.setRotation(180.0f);
                    collapseView();

                } else {

                    mParentDropDownArrow.animate().rotationBy(180.0f).start();

                    //mParentDropDownArrow.setRotation(0.0f);
                    expandView();

                }
            }
        });



    }
    public void bind(InvoiceParent invoiceParent) {
        InvoiceParent invoice = invoiceParent;
        mCrimeTitleTextView.setText(invoice.getVendorName());
    }

}