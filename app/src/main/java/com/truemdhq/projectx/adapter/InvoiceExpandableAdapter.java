package com.truemdhq.projectx.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.truemdhq.projectx.R;
import com.truemdhq.projectx.model.Invoice;
import com.truemdhq.projectx.model.InvoiceChild;
import com.truemdhq.projectx.model.InvoiceChildViewHolder;
import com.truemdhq.projectx.model.InvoiceParent;
import com.truemdhq.projectx.model.InvoiceParentViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yashvardhansrivastava on 09/11/16.
 */
public class InvoiceExpandableAdapter extends ExpandableRecyclerAdapter<InvoiceParentViewHolder, InvoiceChildViewHolder> {



    private LayoutInflater mInflater;

    public InvoiceExpandableAdapter(Context context, @NonNull List<? extends ParentListItem> invoiceList) {

        super(invoiceList);

        mInflater = LayoutInflater.from(context);

//        mInflater = ( LayoutInflater )context.
//                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }





    @Override
    public void onBindChildViewHolder(InvoiceChildViewHolder crimeChildViewHolder, int i, Object childObject) {
        InvoiceChild invoiceChild = (InvoiceChild) childObject;
        crimeChildViewHolder.mCrimeDateText.setText(invoiceChild.getVendorEmail()+"\n"+invoiceChild.getComment());

    }


    @Override
    public InvoiceParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {

        View view = mInflater.inflate(R.layout.list_item_invoice_parent_projectx, viewGroup, false);
        return new InvoiceParentViewHolder(view);

    }

    @Override
    public InvoiceChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_item_invoice_child_projectx, viewGroup, false);
        return new InvoiceChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(InvoiceParentViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        InvoiceParent invoiceParent = (InvoiceParent) parentListItem;
       parentViewHolder.mCrimeTitleTextView.setText("Name: "+invoiceParent.getVendorName());

    }
}
