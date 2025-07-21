package com.example.finance.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.finance.MainActivity;
import com.example.finance.R;
import com.example.finance.sampledata.AppDatabase;
import com.example.finance.sampledata.FinanceAdapter;
import com.example.finance.sampledata.FinanceModel;
import com.example.finance.sampledata.FinanceModelDAO;
import com.example.finance.sampledata.viewmodels.MyViewModelInc;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class IncomeFragment extends Fragment {
    private FinanceAdapter adapter;
    private MyViewModelInc viewModel;
    private List<Double> mas = new ArrayList<>();
    public IncomeFragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView rubles = view.findViewById(R.id.textView5_inc);
        AppDatabase db = MainActivity.getDatabase_inc();
        FinanceModelDAO transactionDao = db.financeModelDao();

        CreateStats(Arrays.asList(0d, 0d, 0d, 0d, 0d), view);

        RecyclerView recyclerView = view.findViewById(R.id.rv_income);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new FinanceAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(
                    @NonNull RecyclerView recyclerView,
                    @NonNull RecyclerView.ViewHolder viewHolder,
                    @NonNull RecyclerView.ViewHolder target
            ) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    List<FinanceModel> mData = adapter.getData();
                    FinanceModel deletedItem = mData.get(position);
                    mData.remove(position);
                    new Thread(() -> {
                        transactionDao.delete(deletedItem.getId());
                    }).start();
                    adapter.notifyItemRemoved(position);
                }
            }
        }).attachToRecyclerView(recyclerView);





        viewModel = new ViewModelProvider(this).get(MyViewModelInc.class);
        viewModel.getAllTransactions().observe(getViewLifecycleOwner(), transactions -> {
            adapter.updateData(transactions);
            rubles.setText(HomeFragment.updateMany(transactions, 1) +"");
            mas = updateChart(transactions);
            CreateStats(mas, view);
        });


        ImageButton btn = view.findViewById(R.id.imageButton_inc);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog dialog = new CustomDialog(requireContext(), transaction -> {
                    new Thread(() -> transactionDao.insert(transaction)).start();
                }, 2);
                dialog.show();
            }
        });
    }

    private List<Double> updateChart(List<FinanceModel> transactions) {
        double cat1 = 0;
        double cat2 = 0;
        double cat3 = 0;
        double cat4 = 0;
        double cat5 = 0;
        for (int j = 0; j < transactions.size(); j++){
            switch (transactions.get(j).getCategory()){
                case "Аванс":
                    cat1 += transactions.get(j).getAmount();
                    break;
                case "Зарплата":
                    cat2 += transactions.get(j).getAmount();
                    break;
                case "Переводы":
                    cat3 += transactions.get(j).getAmount();
                    break;
                case "Кешбек":
                    cat4 += transactions.get(j).getAmount();
                    break;
                case "Другое":
                    cat5 += transactions.get(j).getAmount();
                    break;
            }
        }
        double sum = 0;
        if (cat1+cat2+cat3+cat4+cat5 != 0)
            sum = 100 / (cat1+cat2+cat3+cat4+cat5);
        else sum = 0;
        List<Double> mas = Arrays.asList(sum * cat1, sum * cat2,sum * cat3,sum * cat4,sum * cat5);
        return mas;
    }

    private void CreateStats(List<Double> mas, @NonNull View view){
        PieChart pieChart = view.findViewById(R.id.pieChart_inc);
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(mas.get(0).floatValue(), "Аванс"));
        entries.add(new PieEntry(mas.get(1).floatValue(), "Зарплата"));
        entries.add(new PieEntry(mas.get(2).floatValue(), "Переводы"));
        entries.add(new PieEntry(mas.get(3).floatValue(), "Кешбек"));
        entries.add(new PieEntry(mas.get(4).floatValue(), "Другое"));
        PieDataSet dataSet = new PieDataSet(entries, null);
        dataSet.setDrawValues(false);
        int[] colors2 = {
                Color.parseColor("#0C26F4"),
                Color.parseColor("#07730E"),
                Color.parseColor("#808080"),
                Color.parseColor("#6F4A23"),
                Color.parseColor("#090303")

        };
        int[] colors = new int[0];
        boolean aBoolean = getResources().getBoolean(R.bool.is_dark_theme);
        if (aBoolean) {
            colors = new int[]{
                    -0xeebb22,
                    -0x8844ff,
                    -8355712,
                    -0x66aaee,
                    -256
            };
        } else {
            colors = new int[]{
                    -0xeebb22,
                    -0x887eff,
                    -8355712,
                    -0x66aaee,
                    -0xfffffff
            };
        }
        dataSet.setColors(colors2);
        List<LegendEntry> legendEntries = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors[i];
            entry.label = entries.get(i).getLabel();
            legendEntries.add(entry);
        }
        Legend legend = pieChart.getLegend();
        legend.setCustom(legendEntries);
        pieChart.notifyDataSetChanged();
        PieData pieData = new PieData(dataSet);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setDrawEntryLabels(false);
        pieChart.setHoleColor(Color.parseColor("#058ECA"));
        pieChart.setEntryLabelTextSize(12f);
        pieChart.getDescription().setEnabled(false);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
}