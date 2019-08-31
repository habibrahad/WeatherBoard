package com.darkwhite.weather.model.db;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.darkwhite.weather.R;
import com.darkwhite.weather.utils.AppUtil;
import com.darkwhite.weather.utils.Constants;
import com.google.android.material.card.MaterialCardView;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class FourDayWeather extends AbstractItem<FourDayWeather, FourDayWeather.MyViewHolder> {
  @Id
  private long id;
  private int dt;
  private double temp;
  private double minTemp;
  private double maxTemp;
  private int weatherId;
  private long timestampStart;
  private long timestampEnd;
  private @ColorInt
  int color;
  private @ColorInt
  int colorAlpha;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getDt() {
    return dt;
  }

  public void setDt(int dt) {
    this.dt = dt;
  }

  public double getTemp() {
    return temp;
  }

  public void setTemp(double temp) {
    this.temp = temp;
  }

  public double getMinTemp() {
    return minTemp;
  }

  public void setMinTemp(double minTemp) {
    this.minTemp = minTemp;
  }

  public double getMaxTemp() {
    return maxTemp;
  }

  public void setMaxTemp(double maxTemp) {
    this.maxTemp = maxTemp;
  }

  public int getWeatherId() {
    return weatherId;
  }

  public void setWeatherId(int weatherId) {
    this.weatherId = weatherId;
  }

  public long getTimestampStart() {
    return timestampStart;
  }

  public void setTimestampStart(long timestampStart) {
    this.timestampStart = timestampStart;
  }

  public long getTimestampEnd() {
    return timestampEnd;
  }

  public void setTimestampEnd(long timestampEnd) {
    this.timestampEnd = timestampEnd;
  }

  public int getColor() {
    return color;
  }

  public void setColor(int color) {
    this.color = color;
  }

  int getColorAlpha() {
    return colorAlpha;
  }

  public void setColorAlpha(int colorAlpha) {
    this.colorAlpha = colorAlpha;
  }

  @NonNull
  @Override
  public MyViewHolder getViewHolder(@NonNull View v) {
    return new MyViewHolder(v);
  }

  @Override
  public int getType() {
    return R.id.fastadapter_item_adapter;
  }

  @Override
  public int getLayoutRes() {
    return R.layout.weather_day_item;
  }

  protected static class MyViewHolder extends FastAdapter.ViewHolder<FourDayWeather> {
    Context context;
    View view;
    @BindView(R.id.day_name_text_view)
    AppCompatTextView dayNameTextView;
    @BindView(R.id.temp_text_view)
    AppCompatTextView tempTextView;
    @BindView(R.id.min_temp_text_view)
    AppCompatTextView minTempTextView;
    @BindView(R.id.max_temp_text_view)
    AppCompatTextView maxTempTextView;
    @BindView(R.id.weather_image_view)
    AppCompatImageView weatherImageView;
    @BindView(R.id.card_view)
    MaterialCardView cardView;
    @BindView(R.id.shadow_view)
    View shadowView;

    MyViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
      this.view = view;
      this.context = view.getContext();
    }

    @Override
    public void bindView(@NonNull FourDayWeather item, @NonNull List<Object> payloads) {
      cardView.setCardBackgroundColor(item.getColor());
      int[] colors = {
          Color.TRANSPARENT,
          item.getColorAlpha(),
          Color.TRANSPARENT
      };
      Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
      calendar.setTimeInMillis(item.getDt() * 1000L);
      if (AppUtil.isRTL(context)) {
        dayNameTextView.setText(Constants.DAYS_OF_WEEK_bangla[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
      } else {
        dayNameTextView.setText(Constants.DAYS_OF_WEEK_english[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
      }
      tempTextView.setText(String.format(Locale.getDefault(), "%.0f°", item.getTemp()));
      minTempTextView.setText(String.format(Locale.getDefault(), "%.0f°", item.getMinTemp()));
      maxTempTextView.setText(String.format(Locale.getDefault(), "%.0f°", item.getMaxTemp()));
      AppUtil.setWeatherIcon(context, weatherImageView, item.weatherId);
      GradientDrawable shape = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors);
      shape.setShape(GradientDrawable.OVAL);
      shadowView.setBackground(shape);
    }

    @Override
    public void unbindView(@NonNull FourDayWeather item) {

    }

  }
}
