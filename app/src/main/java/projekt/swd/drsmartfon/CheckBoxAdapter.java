package projekt.swd.drsmartfon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * Created by Amadeusz on 2017-01-16.
 */

public class CheckBoxAdapter extends ArrayAdapter<String> {

    private String[] elements;
    private boolean[] checked;

    public CheckBoxAdapter(Context context, String[] elements) {
        super(context, 0, elements);
        this.elements = elements;
        checked = new boolean[elements.length];
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final String element = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.element_listy, parent, false);
        }
        // Lookup view for data population
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked[position] = isChecked;
            }
        });
        checkBox.setText(element);
        // Return the completed view to render on screen
        checkBox.setChecked(checked[position]);
        return convertView;
    }

    public boolean[] getChecked() {
        return checked;
    }

}
