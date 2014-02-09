package com.example.mobiledev_hw1;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Point;
import android.text.InputType;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * @author Shawn P Neuman
 *
 */
public class MainActivity extends Activity implements OnClickListener {

	private RelativeLayout rl;
	private EditText et1;
	private Button startButton;
	private ArrayList<Button> buttonList;
	private int width;
	private int numButtons = 0;
	private boolean visible = false;
	private boolean scrambled = false;
	private LayoutParams sbLP;
	private LayoutParams etLP;
	private LayoutParams butLP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Display dsp = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		dsp.getSize(size);
		width = size.x;

		etLP = new LayoutParams(250, 120);
		sbLP = new LayoutParams(250, 120);

		ScrollView sv = new ScrollView(this);
		this.setContentView(sv);
		rl = new RelativeLayout(this);

		et1 = new EditText(this);
		et1.setHint("num > 3");
		et1.setInputType(InputType.TYPE_CLASS_NUMBER);

		etLP.leftMargin = ((width / 2) - 125);
		etLP.topMargin = 60;
		et1.setLayoutParams(etLP);

		startButton = new Button(this);
		startButton.setText("Start");
		sbLP.leftMargin = ((width / 2) - 125);
		sbLP.topMargin = 240;
		startButton.setLayoutParams(sbLP);
		startButton.setId(0);
		rl.addView(et1);
		rl.addView(startButton);

		startButton.setOnClickListener(this);
		buttonList = new ArrayList<Button>();

		sv.addView(rl);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
/**
 * adds buttons to layout
 */
	private void createButtons() {

		if ("".equals(et1.getText().toString())) {
			Toast dummy = Toast.makeText(this,
					"HEY DUMMY,  Enter a damn number!!", Toast.LENGTH_LONG);
			dummy.show();

		}
		else {
			numButtons = Integer.parseInt(et1.getText().toString());

			if (numButtons < 3) {
				Toast dummy = Toast.makeText(this,
						"HEY DUMMY,  Enter a larger number!!",
						Toast.LENGTH_LONG);
				dummy.show();
			}
			else {
				removeButtons();

				for (int i = 0; i < numButtons; i++) {
					butLP = new LayoutParams(250, 120);
					Button b = new Button(this);
					b.setId(i + 1);
					b.setText("" + i);
					butLP.topMargin = 420 + (180 * i);
					butLP.leftMargin = ((width / 2) - 125);
					b.setLayoutParams(butLP);
					b.setOnClickListener(this);
					rl.addView(b);
					buttonList.add(b);
				}

				et1.setText("");
			}
		}
	}
/**
 * removes buttons to start from scratch
 */
	private void removeButtons() {

		for (int i = 0; i < buttonList.size(); i++) {
			Button b = buttonList.get(i);
			((ViewGroup) b.getParent()).removeView(b);
		}
		buttonList.clear();

	}
/**
 * hides or unhides the buttons if 0 is pressed
 * @param toggle true if hidden, false if not
 */
	private void makeInvisible(boolean toggle) {
		if (toggle) {

			et1.setVisibility(View.VISIBLE);
			startButton.setVisibility(View.VISIBLE);
			for (int i = 0; i < buttonList.size(); i++) {

				Button b = buttonList.get(i);
				b.setVisibility(View.VISIBLE);

			}
		}
		else {
			et1.setVisibility(View.INVISIBLE);
			startButton.setVisibility(View.INVISIBLE);
			for (int i = 0; i < buttonList.size(); i++) {
				Button b = buttonList.get(i);
				if (!(b.getId() == 1)) {
					b.setVisibility(View.INVISIBLE);
				}
			}
		}

		if (toggle) {
			visible = false;
		}
		else {
			visible = true;
		}
	}
/**
 * if scrambled is true, then this unscrambles the butons
 * if false, it scrambles them
 */
	private void scramble() {
		
		// it is scramble, this straightens it
		if (isScrambled()) {
			et1.setLayoutParams(etLP);
			startButton.setLayoutParams(sbLP);
			for (int i = 0; i < buttonList.size(); i++) {
				Button b = buttonList.get(i);
				LayoutParams btScram = new LayoutParams(250, 120);
				btScram.topMargin = 420 + (180 * i);
				btScram.leftMargin = ((width / 2) - 125);
				b.setLayoutParams(btScram);
			}

		}

		// it is not scrambled, this scrambles it using a random number generator
		// to place the left margin.  top margin stays constant
		else {
			LayoutParams sbScram = new LayoutParams(
					250, 120);
			LayoutParams etScram = new LayoutParams(
					250, 120);
			etScram.topMargin = 60;
			etScram.leftMargin = genNumber();
			et1.setLayoutParams(etScram);
			
			sbScram.topMargin = 240;
			sbScram.leftMargin = genNumber();
			startButton.setLayoutParams(sbScram);
			
			for (int i = 0; i < buttonList.size(); i++) {
				Button b = buttonList.get(i);
				LayoutParams btScram = new LayoutParams(250, 120);
				btScram.topMargin = 420 + (180 * i);
				btScram.leftMargin = genNumber();
				b.setLayoutParams(btScram);
			}
			

		}

		setScrambled(!isScrambled());
	}

	// handles the button clicks
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
// the start button,
		case 0: {

			createButtons();
			break;
		}
//the 0 button
		case 1: {
			makeInvisible(visible);
			break;
		}
// the 1 button
		case 2: {
			scramble();
			break;
		}
// the two button
		case 3: {
			removeButtons();
			setScrambled(true);
			scramble();
			break;
		}

		}

	}
/**
 * used to determine if buttons are scrambled or not
 * @return true or false
 */
	private boolean isScrambled() {
		return scrambled;
	}
/**
 * set true if buttons are scrambled
 * set false if not
 * @param setter true or false
 */
	private void setScrambled(boolean setter) {
		this.scrambled = setter;
	}
	/**
	 * generates a random number as a factor for left margin placement
	 * @return random integer from 0 - 500
	 */
	private int genNumber() {

		return (int) (Math.random() * 500);

	}

}
