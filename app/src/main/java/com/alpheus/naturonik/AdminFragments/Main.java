package com.alpheus.naturonik.AdminFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alpheus.naturonik.Fragments.Search;
import com.alpheus.naturonik.R;
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

public class Main extends Fragment
        implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener {

    SliderLayout sliderLayout;
    HashMap<String, String> HashMapForURL;

    ImageView peanutIv, cashewIv, pistachiosIv, walnutIv, macadamiaIv, almondIv, hazelnutIv;
    ImageView contactsEmail, contactsWhatsUp, contactsPhone;

    public Main() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        setHasOptionsMenu(true);

        sliderLayout = (SliderLayout) view.findViewById(R.id.slider);

        peanutIv = view.findViewById(R.id.peanut_iv);
        cashewIv = view.findViewById(R.id.cashew_iv);
        pistachiosIv = view.findViewById(R.id.pistachios_iv);
        walnutIv = view.findViewById(R.id.walnut_iv);
        macadamiaIv = view.findViewById(R.id.macadamia_iv);
        almondIv = view.findViewById(R.id.almond_iv);
        hazelnutIv = view.findViewById(R.id.hazelnut_iv);


        contactsEmail = view.findViewById(R.id.contacts_email_iv);
        contactsWhatsUp = view.findViewById(R.id.contacts_whatsup_iv);
        contactsPhone = view.findViewById(R.id.contacts_phone_iv);

        //---------------------Intent email, whatsup, phone-----------------------------------------

        contactsEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "naturonik.ru@gmail.com"));
                startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
            }
        });

        contactsPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+79250506165"));
                startActivity(intent);

            }
        });

        contactsWhatsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Данная функция пока не доступна. Номер: +7(***)***-**-**", Toast.LENGTH_SHORT).show();
            }
        });


        //---------------------Загрузка картинок "Категории"----------------------------------------

        Glide.with(getActivity()).load("https://naturonik.ru/img/Slider/peanut.jpg").into(peanutIv);
        Glide.with(getActivity()).load( "https://naturonik.ru/img/Slider/cashew.jpg").into(cashewIv);
        Glide.with(getActivity()).load("https://naturonik.ru/img/Slider/pistachio.jpg").into(pistachiosIv);
        Glide.with(getActivity()).load("https://naturonik.ru/img/Slider/walnut.jpg").into(walnutIv);
        Glide.with(getActivity()).load("https://naturonik.ru/img/Slider/macadamia.jpg").into(macadamiaIv);
        Glide.with(getActivity()).load("https://naturonik.ru/img/Slider/almond.jpg").into(almondIv);
        Glide.with(getActivity()).load("https://naturonik.ru/img/Slider/hazelnuts.jpg").into(hazelnutIv);

        //------------------------------------Слайдер-----------------------------------------------
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
        }

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        sliderLayout.setCustomAnimation(new DescriptionAnimation());

        sliderLayout.setDuration(8000);

        sliderLayout.addOnPageChangeListener(com.alpheus.naturonik.AdminFragments.Main.this);

        //--------------------------------Категории товаров-----------------------------------------

        peanutIv.setOnClickListener(this);
        cashewIv.setOnClickListener(this);
        pistachiosIv.setOnClickListener(this);
        walnutIv.setOnClickListener(this);
        macadamiaIv.setOnClickListener(this);
        walnutIv.setOnClickListener(this);
        hazelnutIv.setOnClickListener(this);

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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_add);
        MenuItem item1 = menu.findItem(R.id.action_add_product);
        if (item != null || item1 != null)
            item.setVisible(true);
            item1.setVisible(false);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = new Search();
        Bundle bundle = new Bundle();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        switch (v.getId()) {

            case R.id.peanut_iv:
                bundle.putString("query", "Арахис");
                fragment.setArguments(bundle);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Поиск");
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                        .addToBackStack(null).commit();
                break;

            case R.id.cashew_iv:
                bundle.putString("query", "Кешью");
                fragment.setArguments(bundle);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Поиск");
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                        .addToBackStack(null).commit();
                break;

            case R.id.pistachios_iv:
                bundle.putString("query", "Фисташки");
                fragment.setArguments(bundle);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Поиск");
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                        .addToBackStack(null).commit();
                break;

            case R.id.walnut_iv:
                bundle.putString("query", "Грецкий орех");
                fragment.setArguments(bundle);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Поиск");
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                        .addToBackStack(null).commit();
                break;

            case R.id.macadamia_iv:
                bundle.putString("query", "Макадамия");
                fragment.setArguments(bundle);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Поиск");
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                        .addToBackStack(null).commit();
                break;

            case R.id.almond_iv:
                bundle.putString("query", "Миндаль");
                fragment.setArguments(bundle);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Поиск");
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                        .addToBackStack(null).commit();
                break;

            case R.id.hazelnut_iv:
                bundle.putString("query", "Фундук");
                fragment.setArguments(bundle);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Поиск");
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                        .addToBackStack(null).commit();
                break;

        }
    }
}
