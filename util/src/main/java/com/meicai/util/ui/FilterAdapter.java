package com.meicai.util.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

/**
 * Created by woo on 1/13/15.
 */



public class FilterAdapter<T>  {
    private ArrayAdapter<T> mImpl;
    private Filter mFilter;
    private ArrayList<T> mItems;
    private ArrayList<T> mSource;


    public ArrayAdapter<T> getImpl() {
        return mImpl;
    }


    public interface  itemFilter {
        boolean OnFilter(CharSequence contraint);
        void OnView(View view);

        ;
    }

    public void  init(Context ctx,int resourceId,ArrayList<T> org) {
        mSource = org;
        mItems = org;
        mImpl = new ArrayAdapter<T>(ctx,resourceId,mItems) {
            @Override
            public Filter getFilter() {
                if (mFilter == null) {
                    mFilter = new textFilter(this);
                }

                return mFilter;
            }

            @Override
            public int getCount() {
                return mItems.size();
            }

            @Override
            public T getItem(int arg0) {
                return mItems.get(arg0);
            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                super.getView(position, convertView, parent);
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                T item = (T)this.getItem(position);
                if (item instanceof itemFilter) {
                    ((itemFilter)item).OnView(text);
                }else {
                    text.setText(item.toString());
                }
                return view;
            }
        };
    }

    public FilterAdapter(Context ctx,int resourceId,ArrayList<T> org) {
        init(ctx,resourceId,org);
    }

    private class textFilter extends  Filter {
        ArrayAdapter mAdapter;
        textFilter(ArrayAdapter adp) {
            mAdapter = adp;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                ArrayList<T> filter = new ArrayList<T>();
                for (T o : mSource) {
                    if (o instanceof itemFilter) {
                        if ( ((itemFilter) o).OnFilter(constraint)) {
                            filter.add(o);
                        }
                    }
                }
                result.count = filter.size();
                result.values = filter;
            }
            else
            {
                synchronized(this)
                {
                    result.values = mSource;
                    result.count = mSource.size();
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mItems = (ArrayList<T>) results.values;
            mAdapter.notifyDataSetChanged();
        }
    }

}

