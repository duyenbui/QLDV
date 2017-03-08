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
 * Created by code-engine-studio on 08/03/2017.
 */

public class ListSpeciesByHabitatAdapter extends RecyclerView.Adapter<ListSpeciesByHabitatAdapter.ViewHolderSpecies> {
    @Override
    public ViewHolderSpecies onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.species_card_item_horizontal, parent, false);
        ViewHolderSpecies myViewHolder = new ViewHolderSpecies(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderSpecies holder, int position) {
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

    public interface OnItemSpeciesClickListener{
        void onItemClick(Species speciesItem);
    }

    private Context context;
    private List<Species> listSpecies;
    private final OnItemSpeciesClickListener listener;

    public ListSpeciesByHabitatAdapter(Context context, List<Species> listSpecies, OnItemSpeciesClickListener listener) {
        this.context = context;
        this.listSpecies = listSpecies;
        this.listener = listener;
    }

    public class ViewHolderSpecies extends RecyclerView.ViewHolder{

        ImageView img;
        TextView vietnameseNameSpecies;
        TextView scienceNameSpecies;
        TextView nameGenus;
        TextView nameFamily;

        public ViewHolderSpecies(View itemView) {
            super(itemView);
            this.img = (ImageView) itemView.findViewById(R.id.imageViewSpecies);
            this.vietnameseNameSpecies = (TextView) itemView.findViewById(R.id.tv_vietnameseNameSpecies);
            this.scienceNameSpecies = (TextView) itemView.findViewById(R.id.tv_scienceNameSpecies);
            this.nameGenus = (TextView) itemView.findViewById(R.id.tv_nameGenus);
            this.nameFamily = (TextView) itemView.findViewById(R.id.tv_nameFamily);

        }

        public void blind(final Species listSpeciesItem
                , final OnItemSpeciesClickListener listener
        ){
            if(listSpeciesItem.getOtherName().trim().equals("chưa có")){
                vietnameseNameSpecies.setText(listSpeciesItem.getVietnameseName());
            } else vietnameseNameSpecies.setText(listSpeciesItem.getVietnameseName()+" ("+listSpeciesItem.getOtherName()+" )");

            scienceNameSpecies.setText("Tên khoa học: "+listSpeciesItem.getScienceName());
            nameGenus.setText("Chi: "+listSpeciesItem.getScienceNameGenus());
            nameFamily.setText(listSpeciesItem.getVietnameseNameFamily());
            if(listSpeciesItem.getImage().equals(null) || listSpeciesItem.getImage().equals("pro.jpg")){
                UrlImageViewHelper.setUrlDrawable(img,"http://is.tnu.edu.vn/wp-content/themes/motive/images/no_image.jpg" );
            }else UrlImageViewHelper.setUrlDrawable(img, listSpeciesItem.getImage());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(listSpeciesItem);
                }
            });
        }
    }
}
