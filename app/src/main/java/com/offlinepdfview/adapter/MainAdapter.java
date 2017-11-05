package com.offlinepdfview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.offlinepdfview.common.Utils;
import com.offlinepdfview.data.MainData;
import com.offlinepdfview.listener.OnRecyclerItemClick;
import com.offlinepdfview.sit60.fragment_21_09.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by sit78 on 31-10-2017.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainRowHolder> {

    private Context context;
    private List<MainData> mainData;
    private int position;
    OnRecyclerItemClick itemClick;


    public MainAdapter(Context context, List<MainData> mainData, int position, OnRecyclerItemClick itemClick) {
        this.context = context;
        this.mainData = mainData;
        this.position = position;
        this.itemClick = itemClick;
    }

    @Override
    public MainRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_book_list, parent, false);
        MainRowHolder mainRowHolder = new MainRowHolder(view);
        return mainRowHolder;
    }

    @Override
    public void onBindViewHolder(MainRowHolder holder, final int position) {

        holder.txtBookTitle.setText(mainData.get(position).getTitle());
        holder.txtBookAuthor.setText(mainData.get(position).getAuthor());

        Picasso.with(context)
                .load(mainData.get(position).getImageUrl())
                .placeholder(R.drawable.ic_notebook)
                .fit()
                .into(holder.imgBookImage);

        String Local_File_Path = Utils.getMaterialPath(context) + mainData.get(position).getFileName();
        File file = new File(Local_File_Path);

        if (file.exists()) {
            holder.btnDownload.setText(R.string.view);
            //holder.btnDownload.setCompoundDrawablesWithIntrinsicBounds();
        } else {
            holder.btnDownload.setText(R.string.download);
        }

        holder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.ItemClick(position, "click", v, 0);
            }
        });


    }

    @Override
    public int getItemCount() {
        return (mainData != null ? mainData.size() : 0);
    }

    public class MainRowHolder extends RecyclerView.ViewHolder {

        private ImageView imgBookImage;
        private TextView txtBookTitle, txtBookAuthor;
        private Button btnDownload;


        public MainRowHolder(View itemView) {
            super(itemView);
            imgBookImage = (ImageView) itemView.findViewById(R.id.imgBookImage);
            txtBookTitle = (TextView) itemView.findViewById(R.id.txtBookTitle);
            txtBookAuthor = (TextView) itemView.findViewById(R.id.txtBookAuthor);
            btnDownload = (Button) itemView.findViewById(R.id.btnDownload);
        }
    }
}
