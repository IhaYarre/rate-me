package com.androidsx.rateme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.androidsx.libraryrateme.R;

public class DialogRateMe extends DialogFragment {

	private String appPackageName;
	private View mView;
	private View tView;
	private Button close;
	private RatingBar ratingBar;
	private LayerDrawable stars;
	private Button RateMe;
	private Button NoThanks;

	public static DialogRateMe newInstance(String appName) {
		DialogRateMe dialogo = new DialogRateMe();
		Bundle args = new Bundle();
		args.putString("name", appName);
		dialogo.setArguments(args);
		return dialogo;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		appPackageName = getArguments().getString("name");
		setupUI();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		stars.getDrawable(2).setColorFilter(Color.YELLOW,
				PorterDuff.Mode.SRC_ATOP);
		ratingBar
				.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						Toast.makeText(getActivity(),
								"Puntuacion: " + String.valueOf(rating),
								Toast.LENGTH_SHORT).show();
						if (rating >= 4.0) {
							RateMe.setVisibility(View.VISIBLE);
							NoThanks.setVisibility(View.GONE);
							clickButton(true);

						} else {
							NoThanks.setVisibility(View.VISIBLE);
							RateMe.setVisibility(View.GONE);
							clickButton(false);
						}
					}
				});

		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();

			}
		});
		return builder.setView(mView).setCustomTitle(tView)
				.setCancelable(false).create();

	}

	private void setupUI() {
		mView = getActivity().getLayoutInflater().inflate(R.layout.library,
				null);
		tView = getActivity().getLayoutInflater().inflate(R.layout.title, null);
		close = (Button) tView.findViewById(R.id.cerrar);
		RateMe = (Button) mView.findViewById(R.id.buttonRateMe);
		NoThanks = (Button) mView.findViewById(R.id.buttonThanks);

		ratingBar = (RatingBar) mView.findViewById(R.id.ratingBar);
		stars = (LayerDrawable) ratingBar.getProgressDrawable();

	}

	private void rateApp() {
		try {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://details?id=" + appPackageName)));
		} catch (android.content.ActivityNotFoundException anfe) {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://play.google.com/store/apps/details?id="
							+ appPackageName)));
		}
	}

	private void clickButton(boolean flag) {

		if (flag) {
			RateMe.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					rateApp();
				}
			});

		} else {
			NoThanks.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("plain/text");
					intent.putExtra(Intent.EXTRA_EMAIL,
							new String[] { "some@email.com" });
					intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
					intent.putExtra(Intent.EXTRA_TEXT, "mail body");
					try {
						startActivity(Intent.createChooser(intent, ""));
					} catch (android.content.ActivityNotFoundException ex) {
						Toast.makeText(getActivity(),
								"No email client installed.", Toast.LENGTH_LONG)
								.show();
					}
				}
			});
		}
	}

}
