package com.mappn.sdk.pay.weight;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

/* loaded from: classes.dex */
final class a implements ListAdapter, SpinnerAdapter {
    private SpinnerAdapter a;
    private ListAdapter b;

    public a(SpinnerAdapter spinnerAdapter) {
        this.a = spinnerAdapter;
        if (spinnerAdapter instanceof ListAdapter) {
            this.b = (ListAdapter) spinnerAdapter;
        }
    }

    @Override // android.widget.ListAdapter
    public final boolean areAllItemsEnabled() {
        ListAdapter listAdapter = this.b;
        if (listAdapter != null) {
            return listAdapter.areAllItemsEnabled();
        }
        return true;
    }

    @Override // android.widget.Adapter
    public final int getCount() {
        if (this.a == null) {
            return 0;
        }
        return this.a.getCount();
    }

    @Override // android.widget.SpinnerAdapter
    public final View getDropDownView(int i, View view, ViewGroup viewGroup) {
        if (this.a == null) {
            return null;
        }
        return this.a.getDropDownView(i, view, viewGroup);
    }

    @Override // android.widget.Adapter
    public final Object getItem(int i) {
        if (this.a == null) {
            return null;
        }
        return this.a.getItem(i);
    }

    @Override // android.widget.Adapter
    public final long getItemId(int i) {
        if (this.a == null) {
            return -1L;
        }
        return this.a.getItemId(i);
    }

    @Override // android.widget.Adapter
    public final int getItemViewType(int i) {
        return 0;
    }

    @Override // android.widget.Adapter
    public final View getView(int i, View view, ViewGroup viewGroup) {
        return getDropDownView(i, view, viewGroup);
    }

    @Override // android.widget.Adapter
    public final int getViewTypeCount() {
        return 1;
    }

    @Override // android.widget.Adapter
    public final boolean hasStableIds() {
        return this.a != null && this.a.hasStableIds();
    }

    @Override // android.widget.Adapter
    public final boolean isEmpty() {
        return getCount() == 0;
    }

    @Override // android.widget.ListAdapter
    public final boolean isEnabled(int i) {
        ListAdapter listAdapter = this.b;
        if (listAdapter != null) {
            return listAdapter.isEnabled(i);
        }
        return true;
    }

    @Override // android.widget.Adapter
    public final void registerDataSetObserver(DataSetObserver dataSetObserver) {
        if (this.a != null) {
            this.a.registerDataSetObserver(dataSetObserver);
        }
    }

    @Override // android.widget.Adapter
    public final void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        if (this.a != null) {
            this.a.unregisterDataSetObserver(dataSetObserver);
        }
    }
}
