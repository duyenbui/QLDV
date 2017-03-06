package com.example.duyenbui.qldv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duyenbui.qldv.R;
import com.example.duyenbui.qldv.object.Species;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.List;

/**
 * Created by code-engine-studio on 22/02/2017.
 */

public class ListSpeciesAdapter extends RecyclerView.Adapter<ListSpeciesAdapter.MyViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(Species speciesItem);
    }

    private Context context;
    private List<Species> listSpecies;
    private final OnItemClickListener listener;

    public ListSpeciesAdapter(Context context, List<Species> listSpecies, OnItemClickListener listener) {
        this.context = context;
        this.listSpecies = listSpecies;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView txtVietnameseNameSpecies;
        TextView txtScienceNameSpecies;
        TextView txtNameFamily;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.imageView);
            this.txtVietnameseNameSpecies = (TextView) itemView.findViewById(R.id.vietnameseNameSpecies);
            this.txtScienceNameSpecies = (TextView) itemView.findViewById(R.id.scienceNameSpecies);
            this.txtNameFamily = (TextView) itemView.findViewById(R.id.nameFamily);

        }

        public void blind(final Species listSpeciesItem
                , final OnItemClickListener listener
        ){

            txtVietnameseNameSpecies.setText(listSpeciesItem.getVietnameseName());
            txtScienceNameSpecies.setText(listSpeciesItem.getScienceName());
            txtNameFamily.setText(listSpeciesItem.getVietnameseNameFamily());
            if(listSpeciesItem.getImage().equals(null) || listSpeciesItem.getImage().equals("pro.jpg")){
                UrlImageViewHelper.setUrlDrawable(image,"http://is.tnu.edu.vn/wp-content/themes/motive/images/no_image.jpg" );
            }else UrlImageViewHelper.setUrlDrawable(image, listSpeciesItem.getImage());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(listSpeciesItem);
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.species_card, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.blind(listSpecies.get(position)
                , listener
        );
    }

    @Override
    public int getItemCount() {
        if (listSpecies == null)
            return 0;
        else return listSpecies.size();
    }

}
