package com.example.duyenbui.qldv.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.duyenbui.qldv.R;
import com.example.duyenbui.qldv.activity.member.MemberMainActivity;
import com.example.duyenbui.qldv.object.Species;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

import static com.example.duyenbui.qldv.fragment.LibrarySpeciesFragment.realm;

public class SpeciesDetailActivity extends AppCompatActivity {

    int id;

    TextView et_scienceName;
    TextView et_vietnameseName;
    TextView et_reproductionTraits;
    TextView et_sexualTraits;
    TextView et_ortherTraits;
    TextView et_alertlevel;
    TextView et_biologicalBehavior;
    TextView et_mediumSize;
    TextView et_food;
    TextView et_origin;
    ImageView et_image;
    TextView et_yearDiscover;
    TextView et_scienceNameGenus;
    TextView et_vietnameseNameFamily;
    TextView et_individualQuantity;

    Species species;

    // Tao list slider
    private SliderLayout slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_species_detail);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent i = getIntent();
        id = i.getIntExtra("idItem", 1);

         et_scienceName = (TextView) findViewById(R.id.detail_scienceNameSpecies);
         et_vietnameseName = (TextView) findViewById(R.id.detail_vietnameseNameSpecies);
         et_reproductionTraits = (TextView) findViewById(R.id.reproductionTraits);
         et_sexualTraits = (TextView) findViewById(R.id.sexualTraits);
         et_ortherTraits = (TextView) findViewById(R.id.otherTraits);
         et_alertlevel = (TextView) findViewById(R.id.alertLevel);
         et_biologicalBehavior = (TextView) findViewById(R.id.biologicalBehavior);
         et_mediumSize = (TextView) findViewById(R.id.mediumSize);
         et_food = (TextView) findViewById(R.id.food);
         et_origin = (TextView) findViewById(R.id.origin);
//         et_image = (ImageView) findViewById(R.id.image_species);
         et_yearDiscover = (TextView) findViewById(R.id.yearDiscover);
         et_scienceNameGenus = (TextView) findViewById(R.id.nameGenus);
         et_vietnameseNameFamily = (TextView) findViewById(R.id.speciesNameFamily);
        et_individualQuantity = (TextView) findViewById(R.id.individualQuantity);

        // Slider
        slider = (SliderLayout) findViewById(R.id.slider);

        prepareSlider();

        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setDuration(6000);
        slider.setSliderTransformDuration(3000, null);
        slider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));

        showDetailSpecies();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                onBackPressed();
                break;
            }
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void prepareSlider() {
        if(species.getImage().equals("") || species.getImage().equals("pro.jpg")){
            UrlImageViewHelper.setUrlDrawable(et_image,"http://is.tnu.edu.vn/wp-content/themes/motive/images/no_image.jpg" );
        }else UrlImageViewHelper.setUrlDrawable(et_image, getString(R.string.host_name) + "/resources/images/"+species.getImage());


        String images = species.getImage();
        String[] imageItems = images.split(",");
            for (int i=0; i<imageItems.length; i++) {
                TextSliderView slide = new TextSliderView(this);
                slide.description(species.getVietnameseName());
                slide.image(imageItems[i]);
//                    slide.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//                        @Override
//                        public void onSliderClick(BaseSliderView slider) {
//                            Bundle args = new Bundle();
//                            args.clear();
//                            args.putString("NewID", postSnapshot.child("NewID").getValue().toString());
//                            Fragment fragment = null;
//                            Class fragmentClass = GuestDetailsNewsFragment.class;
//                            try {
//                                fragment = (Fragment) fragmentClass.newInstance();
//                                fragment.setArguments(args);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                            fragmentManager.beginTransaction().replace(R.id.guest_fl_content, fragment).commit();
//                    slide.setOnImageLoadListener(i);
                slider.addSlider(slide);
            }
//                    });
    }


    private void showDetailSpecies(){
        species = realm.where(Species.class).equalTo("id", id).findFirst();

//        if(species.getImage().equals("") || species.getImage().equals("pro.jpg")){
//            UrlImageViewHelper.setUrlDrawable(et_image,"http://is.tnu.edu.vn/wp-content/themes/motive/images/no_image.jpg" );
//        }else UrlImageViewHelper.setUrlDrawable(et_image, getString(R.string.host_name) + "/resources/images/"+species.getImage());

        if(species.getOtherName().trim().equals("chưa có")){
            et_vietnameseName.setText(species.getVietnameseName());
        } else et_vietnameseName.setText(species.getVietnameseName()+" ("+species.getOtherName()+" )");

        et_scienceName.setText("Tên khoa học: "+species.getScienceName());

        if(species.getVietnameseNameGenus().equals("chưa có")){
            et_scienceNameGenus.setText("Chi: "+species.getScienceNameGenus());
        } else et_scienceNameGenus.setText("Chi: "+species.getScienceNameGenus()+ " - "+species.getVietnameseNameGenus());

        if(species.getVietnameseNameFamily().equals("chưa có")){
            et_vietnameseNameFamily.setText(null);
        } else et_vietnameseNameFamily.setText("Họ: "+species.getVietnameseNameFamily());

        if(species.getAlertlevel().trim().equals("chưa có")){
            et_alertlevel.setText("");
        } else et_alertlevel.setText(species.getAlertlevel());

        if(species.getIndividualQuantity().trim().equals("chưa có")){
            et_individualQuantity.setText("");
        } else et_individualQuantity.setText(species.getIndividualQuantity());



        if(species.getReproductionTraits().trim().equals("chưa có") && species.getSexualTraits().trim().equals("chưa có")
         && species.getOrtherTraits().trim().equals("chưa có") && species.getBiologicalBehavior().trim().equals("chưa có")
                && species.getMediumSize().trim().equals("chưa có") && species.getFood().trim().equals("chưa có")
                && species.getOrigin().trim().equals("chưa có")){

            et_reproductionTraits.setText(species.getReproductionTraits());
            et_sexualTraits.setText("");
            et_ortherTraits.setText("");
            et_biologicalBehavior.setText("");
            et_mediumSize.setText("");
            et_food.setText("");
            et_origin.setText("");

        }
        if(species.getReproductionTraits().trim().equals("chưa có")){
            et_reproductionTraits.setText("");
        } else et_reproductionTraits.setText("  "+species.getReproductionTraits());

        if(species.getSexualTraits().trim().equals("chưa có")){
            et_sexualTraits.setText("");
        } else et_sexualTraits.setText("  "+species.getSexualTraits());

        if(species.getOrtherTraits().trim().equals("chưa có")){
            et_ortherTraits.setText("");
        } else et_ortherTraits.setText("  "+species.getOrtherTraits());

        if(species.getBiologicalBehavior().trim().equals("chưa có")){
            et_biologicalBehavior.setText("");
        } else et_biologicalBehavior.setText("  "+species.getBiologicalBehavior());

        if(species.getMediumSize().trim().equals("chưa có")){
            et_mediumSize.setText("");
        } else et_mediumSize.setText("  "+species.getMediumSize());

        if(species.getFood().trim().equals("chưa có")){
            et_food.setText("");
        } else et_food.setText("  "+species.getFood());

        if(species.getOrigin().trim().equals("chưa có")){
            et_origin.setText("");
        } else et_origin.setText("  "+species.getOrigin());

        et_yearDiscover.setText("  "+species.getDiscovererName()+" ("+species.getYearDiscover()+")");
    }
}
