package com.darkwhite.weather.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.darkwhite.weather.R;
import com.darkwhite.weather.model.CityInfo;
import com.darkwhite.weather.model.daysweather.ListItem;
import com.darkwhite.weather.model.daysweather.MultipleDaysWeatherResponse;
import com.darkwhite.weather.model.db.MultipleDaysWeather;
import com.darkwhite.weather.service.ApiService;
import com.darkwhite.weather.utils.ApiClient;
import com.darkwhite.weather.utils.AppUtil;
import com.darkwhite.weather.utils.Constants;
import com.darkwhite.weather.utils.DbUtil;
import com.darkwhite.weather.utils.MyApplication;
import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidScheduler;
import io.objectbox.query.Query;
import io.objectbox.reactive.DataObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MultipleDaysFragment extends DialogFragment {
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.swipe_container)
  SwipeRefreshLayout swipeContainer;
  private CompositeDisposable disposable = new CompositeDisposable();
  private ItemAdapter<MultipleDaysWeather> mItemAdapter;
  private Activity activity;
  private Box<MultipleDaysWeather> multipleDaysWeatherBox;
  private Prefser prefser;
  private String apiKey;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_multiple_days,
        container, false);
    ButterKnife.bind(this, view);
    initVariables();
    initSwipeView();
    initRecyclerView();
    showStoredMultipleDaysWeather();
    checkTimePass();
    return view;
  }

  private void initVariables() {
    activity = getActivity();
    prefser = new Prefser(activity);
    BoxStore boxStore = MyApplication.getBoxStore();
    multipleDaysWeatherBox = boxStore.boxFor(MultipleDaysWeather.class);
  }

  private void initSwipeView() {
    swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
        android.R.color.holo_green_light,
        android.R.color.holo_orange_light,
        android.R.color.holo_red_light);
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

      @Override
      public void onRefresh() {
        requestWeather();
      }

    });
  }

  private void requestWeather() {
    long lastUpdate = prefser.get(Constants.LAST_STORED_MULTIPLE_DAYS, Long.class, 0L);
    if (AppUtil.isTimePass(lastUpdate)) {
      checkCityInfoExist();
    } else {
      swipeContainer.setRefreshing(false);
    }
  }

  private void initRecyclerView() {
    LinearLayoutManager layoutManager
        = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    recyclerView.setLayoutManager(layoutManager);
    mItemAdapter = new ItemAdapter<>();
    FastAdapter<MultipleDaysWeather> mFastAdapter = FastAdapter.with(mItemAdapter);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(mFastAdapter);
  }

  private void showStoredMultipleDaysWeather() {
    Query<MultipleDaysWeather> query = DbUtil.getMultipleDaysWeatherQuery(multipleDaysWeatherBox);
    query.subscribe().on(AndroidScheduler.mainThread())
        .observer(new DataObserver<List<MultipleDaysWeather>>() {
          @Override
          public void onData(@NonNull List<MultipleDaysWeather> data) {
            if (data.size() > 0) {
              final Handler handler = new Handler();
              handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                  data.remove(0);
                  mItemAdapter.clear();
                  mItemAdapter.add(data);
                }
              }, 500);
            }
          }
        });
  }

  private void checkTimePass() {
    apiKey = getResources().getString(R.string.open_weather_map_api);
    if (prefser.contains(Constants.LAST_STORED_MULTIPLE_DAYS)) {
      requestWeather();
    } else {
      checkCityInfoExist();
    }
  }

  private void checkCityInfoExist() {
    CityInfo cityInfo = prefser.get(Constants.CITY_INFO, CityInfo.class, null);
    if (cityInfo != null) {
      if (AppUtil.isNetworkConnected()) {
        requestWeathers(cityInfo.getName());
      } else {
        Toast.makeText(activity, getResources().getString(R.string.no_internet_message), Toast.LENGTH_SHORT).show();
        swipeContainer.setRefreshing(false);
      }
    }
  }

  private void requestWeathers(String cityName) {
    ApiService apiService = ApiClient.getClient().create(ApiService.class);
    String defaultLang = "bn";
    disposable.add(
        apiService.getMultipleDaysWeather(
            cityName, Constants.UNITS, defaultLang, 16, apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<MultipleDaysWeatherResponse>() {
              @Override
              public void onSuccess(MultipleDaysWeatherResponse response) {
                handleMultipleDaysResponse(response);
                swipeContainer.setRefreshing(false);
              }

              @Override
              public void onError(Throwable e) {
                swipeContainer.setRefreshing(false);
                Log.e("MainActivity", "onError: " + e.getMessage());
              }
            })
    );
  }

  private void handleMultipleDaysResponse(MultipleDaysWeatherResponse response) {
    multipleDaysWeatherBox.removeAll();
    List<ListItem> listItems = response.getList();
    for (ListItem listItem : listItems) {
      MultipleDaysWeather multipleDaysWeather = new MultipleDaysWeather();
      multipleDaysWeather.setDt(listItem.getDt());
      multipleDaysWeather.setMaxTemp(listItem.getTemp().getMax());
      multipleDaysWeather.setMinTemp(listItem.getTemp().getMin());
      multipleDaysWeather.setWeatherId(listItem.getWeather().get(0).getId());
      multipleDaysWeatherBox.put(multipleDaysWeather);
    }
    prefser.put(Constants.LAST_STORED_MULTIPLE_DAYS, System.currentTimeMillis());
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(true);
    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    lp.copyFrom(dialog.getWindow().getAttributes());
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
    dialog.getWindow().setAttributes(lp);
    return dialog;
  }


  @OnClick(R.id.close_button)
  void close() {
    dismiss();
    if (getFragmentManager() != null) {
      getFragmentManager().popBackStack();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    disposable.dispose();
  }
}
