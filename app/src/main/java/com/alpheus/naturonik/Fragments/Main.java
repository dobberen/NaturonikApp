package com.alpheus.naturonik.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alpheus.naturonik.R;
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

public class Main extends Fragment
        implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    SliderLayout sliderLayout;
    HashMap<String, String> HashMapForURL;
    ImageView hitImageView, hitImageView2, offerImageView, recipesImageView, contactsEmailImageView,
    contactsWhatsUpImageView;

    public Main() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        sliderLayout = (SliderLayout) view.findViewById(R.id.slider);

        hitImageView = view.findViewById(R.id.hit1_iv);
        hitImageView2 = view.findViewById(R.id.hit2_iv);
        offerImageView = view.findViewById(R.id.offer_iv);
        recipesImageView = view.findViewById(R.id.recipes_iv);
        contactsEmailImageView = view.findViewById(R.id.contacts_email_iv);
        contactsWhatsUpImageView = view.findViewById(R.id.contacts_whatsup_iv);

        Glide.with(getActivity()).load("https://naturonik.ru/img/Nuts/arahis/arahis_och_sir_3842.jpg").into(hitImageView);
        Glide.with(getActivity()).load("https://naturonik.ru/img/Nuts/keshu/keshu_ww240.jpg").into(hitImageView2);
        Glide.with(getActivity()).load("https://i.pinimg.com/originals/d0/e9/5c/d0e95cbdcc61272c480759f17fd7228e.jpg").into(offerImageView);
        Glide.with(getActivity()).load("https://img.povar.ru/main/b9/85/fa/37/keks_quotlakomkaquot_s_iziumom_i_orehami-401786.JPG").into(recipesImageView);
        Glide.with(getActivity()).load("https://cdn3.iconfinder.com/data/icons/email-51/48/25-512.png").into(contactsEmailImageView);
        Glide.with(getActivity()).load("https://cdn3.iconfinder.com/data/icons/social-network-30/512/social-01-512.png").into(contactsWhatsUpImageView);

        AddImagesUrlOnline();

        for (String name : HashMapForURL.keySet()) {

            final TextSliderView textSliderView = new TextSliderView(getActivity());

            textSliderView
                    .description(name)
                    .image(HashMapForURL.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                        }
                    });

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);

            sliderLayout.addSlider(textSliderView);
            /*top_nuts_slider.addSlider(textSliderView);
            top_nuts_slider2.addSlider(textSliderView);*/
        }

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        sliderLayout.setCustomAnimation(new DescriptionAnimation());

        sliderLayout.setDuration(8000);

        sliderLayout.addOnPageChangeListener(Main.this);

        return view;
    }

    @Override
    public void onStop() {

        super.onStop();
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public void AddImagesUrlOnline() {

        HashMapForURL = new HashMap<String, String>();

        HashMapForURL.put(getString(R.string.slider_peanut), "https://naturonik.ru/img/Slider/peanut.jpg");
        HashMapForURL.put(getString(R.string.slider_pistachio), "https://naturonik.ru/img/Slider/pistachio.jpg");
        HashMapForURL.put(getString(R.string.slider_hazelnuts), "https://naturonik.ru/img/Slider/hazelnuts.jpg");
        HashMapForURL.put(getString(R.string.slider_almond), "https://naturonik.ru/img/Slider/almond.jpg");
        HashMapForURL.put(getString(R.string.slider_walnut), "https://naturonik.ru/img/Slider/walnut.jpg");
        HashMapForURL.put(getString(R.string.slider_cashew), "https://naturonik.ru/img/Slider/cashew.jpg");
        HashMapForURL.put(getString(R.string.slider_macadamia), "https://naturonik.ru/img/Slider/macadamia.jpg");
    }

}
