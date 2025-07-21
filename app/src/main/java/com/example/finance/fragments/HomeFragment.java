package com.example.finance.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.finance.R;
import com.example.finance.sampledata.FinanceModel;
import com.example.finance.sampledata.viewmodels.MyViewModelExp;
import com.example.finance.sampledata.viewmodels.MyViewModelInc;
import com.google.android.material.tabs.TabLayout;

import java.util.List;


public class HomeFragment extends Fragment {
    private MyViewModelInc viewModelInc;
    private MyViewModelExp viewModelExp;
    long rub_inc = 0L;
    long rub_exp = 0L;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.switchLayoutFragment, new ExpensesFragment())
                    .commit();
        ImageButton btn_acc = view.findViewById(R.id.accButton);
        btn_acc.setOnClickListener(v->
                requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainLayout, new AccFragment())
                .addToBackStack(null)
                .commit());
        TextView rubles = view.findViewById(R.id.textView3);
        long stuff = -rub_exp + rub_inc;
        rubles.setText(stuff + "");
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = new ExpensesFragment();
                        break;
                    case 1:
                        fragment = new IncomeFragment();
                        break;
                }
                if (fragment != null) {
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.switchLayoutFragment, fragment)
                            .commit();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });


        viewModelInc = new ViewModelProvider(this).get(MyViewModelInc.class);
        viewModelExp = new ViewModelProvider(this).get(MyViewModelExp.class);
        viewModelInc.getAllTransactions().observe(getViewLifecycleOwner(), transactions1 -> {
            long stuf_rub = Long.parseLong(rubles.getText().toString()) - rub_inc;
            rub_inc = updateMany(transactions1, 1);
            stuf_rub += rub_inc;
            rubles.setText(stuf_rub +"");
        });
        viewModelExp.getAllTransactions().observe(getViewLifecycleOwner(), transactions2 -> {
            long stuf_rub = Long.parseLong(rubles.getText().toString()) + rub_exp;
            rub_exp = updateMany(transactions2, 1);
            stuf_rub -= rub_exp;
            rubles.setText(stuf_rub +"");
        });
    }

    public static long updateMany(List<FinanceModel> transactions, int i) {
        long cost = 0;
        if (i == 1){
            for (int j = 0; j < transactions.size(); j++){
                cost += transactions.get(j).getAmount();
            }
        }
        else{
            for (int j = 0; j < transactions.size(); j++){
                cost -= transactions.get(j).getAmount();
            }
        }
        return cost;
    }

}