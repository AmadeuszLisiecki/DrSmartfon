package projekt.swd.drsmartfon;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private boolean findDiseases = true;
    private String[] alphas;
    private String[] alphaU;
    private String[] alphaY;
    private Fact[] facts;
    private CheckBoxAdapter adapter;
    private ArrayList<Tuple> sy;
    private ArrayList<Tuple> su1;
    private ArrayList<Tuple> su2;
    private ArrayList<Tuple> su;
    private ArrayList<String> fy;
    private ArrayList<String> fu;
    private ListView list;
    private Button button;
    private TextView prompt;

    private class Tuple {

        boolean[] values;

        public Tuple(boolean[] values) {
            this.values = values;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alphas = new String[]{"achalazja", "problem z połykaniem", "zarzucanie treści pokarmowej", "ból w klatce piersiowej", "zgaga",
                "kaszel", "krztuszenie się", "utrata masy ciała", "zapalenie płuc", "rak przełyku", "palenie w przełyku", "odbijanie",
                "gorączka", "dreszcze", "badanie USG", "złamanie kości", "poparzenie"};
        alphaU = new String[]{alphas[1], alphas[2], alphas[3], alphas[5], alphas[6], alphas[7], alphas[10], alphas[11], alphas[12],
                alphas[13], alphas[16]};
        alphaY = new String[]{alphas[0], alphas[4], alphas[8], alphas[9], alphas[14], alphas[15]};

        sy = new ArrayList<>();
        su1 = new ArrayList<>();
        su2 = new ArrayList<>();
        su = new ArrayList<>();

        facts = new Fact[6];
        for(int f  = 0; f < facts.length; f++) {
            facts[f] = new Fact(f + 1);
        }

        button = (Button)findViewById(R.id.button);
        prompt = (TextView)findViewById(R.id.textView);

        list = (ListView) findViewById(R.id.list);
        adapter = new CheckBoxAdapter(this, alphaU);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.diseases) {
            findDiseases = true;
            adapter = new CheckBoxAdapter(this, alphaU);
            prompt.setText("Zaznacz objawy pacjenta.");
            button.setText("Znajdź choroby");
        }
        else {
            findDiseases = false;
            adapter = new CheckBoxAdapter(this, alphaY);
            prompt.setText("Zaznacz choroby pacjenta.");
            button.setText("Znajdź objawy");
        }
        list.setAdapter(adapter);

        return super.onOptionsItemSelected(item);
    }

    public void evaluate(View button) {
        boolean[] userValues = adapter.getChecked();
        boolean userChecked = false;
        for(int v = 0; !userChecked && v < userValues.length; v++) {
            if(userValues[v]) {
                userChecked = true;
            }
        }
        if(userChecked) {
            boolean[] currentCombination = new boolean[alphas.length];
            for(int c = 0; c < Math.pow(2, alphas.length); c++) {
                boolean foundFalseFact = false;
                for(int f = 0; !foundFalseFact && f < facts.length; f++) {
                    if(!facts[f].evaluate(currentCombination)) {
                        foundFalseFact = true;
                    }
                    else if(f == facts.length - 1) {
                        if(new Fact(userValues).evaluate(currentCombination)) {
                            if(findDiseases) {
                                boolean[] ayCombination = new boolean[alphaY.length];
                                ayCombination[0] = currentCombination[0];
                                ayCombination[1] = currentCombination[4];
                                ayCombination[2] = currentCombination[8];
                                ayCombination[3] = currentCombination[9];
                                ayCombination[4] = currentCombination[14];
                                ayCombination[5] = currentCombination[15];
                                Tuple tuple = new Tuple(ayCombination);
                                if(!contains(sy, tuple)) {
                                    sy.add(tuple);
                                }
                            }
                            else {
                                boolean[] auCombination = new boolean[alphaU.length];
                                auCombination[0] = currentCombination[1];
                                auCombination[1] = currentCombination[2];
                                auCombination[2] = currentCombination[3];
                                auCombination[3] = currentCombination[5];
                                auCombination[4] = currentCombination[6];
                                auCombination[5] = currentCombination[7];
                                auCombination[6] = currentCombination[10];
                                auCombination[7] = currentCombination[11];
                                auCombination[8] = currentCombination[12];
                                auCombination[9] = currentCombination[13];
                                auCombination[10] = currentCombination[16];
                                Tuple tuple = new Tuple(auCombination);
                                if(!contains(su1, tuple)) {
                                    su1.add(tuple);
                                }
                            }
                        }
                        else if(!findDiseases) {
                            boolean[] auCombination = new boolean[alphaU.length];
                            auCombination[0] = currentCombination[1];
                            auCombination[1] = currentCombination[2];
                            auCombination[2] = currentCombination[3];
                            auCombination[3] = currentCombination[5];
                            auCombination[4] = currentCombination[6];
                            auCombination[5] = currentCombination[7];
                            auCombination[6] = currentCombination[10];
                            auCombination[7] = currentCombination[11];
                            auCombination[8] = currentCombination[12];
                            auCombination[9] = currentCombination[13];
                            auCombination[10] = currentCombination[16];
                            Tuple tuple = new Tuple(auCombination);
                            if(!contains(su2, tuple)) {
                                su2.add(tuple);
                            }
                        }
                    }
                }
                currentCombination = generateNextCombination(currentCombination);
            }
            if(!findDiseases) {
                for(int t = 0; t < su2.size(); t++) {
                    if(contains(su1, su2.get(t))) {
                       su1.remove(su2.get(t));
                    }
                }
            }
            su = su1;
            String message = showF();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {}

                });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            Toast.makeText(this, "Zaznacz przynajmniej jedną pozycję.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean[] generateNextCombination(boolean[] combination) {
        boolean endGenerating = false;
        for(int a = combination.length - 1; !endGenerating && a >= 0; a--) {
            if(combination[a]) {
                combination[a] = false;
            }
            else {
                combination[a] = true;
                endGenerating = true;
            }
        }
        return combination;
    }

    private boolean contains(ArrayList<Tuple> s, Tuple checked) {
        for(int t = 0; t < s.size(); t++) {
            if(Arrays.equals(s.get(t).values, checked.values)) {
                return true;
            }
        }
        return false;
    }

    private String showF() {
        String message = "";
        if(findDiseases) {
            fy = new ArrayList<>();
            int tupleIndex = (int)(Math.random() * sy.size());
            Tuple choosenTuple = sy.get(tupleIndex);
            boolean[] tupleValues = choosenTuple.values;
            for(int v = 0; v < tupleValues.length; v++) {
                if(tupleValues[v]) {
                    switch(v) {
                        case 0:
                            fy.add(alphas[0]);
                            break;
                        case 1:
                            fy.add(alphas[4]);
                            break;
                        case 2:
                            fy.add(alphas[8]);
                            break;
                        case 3:
                            fy.add(alphas[9]);
                            break;
                        case 4:
                            fy.add(alphas[14]);
                            break;
                        case 5:
                            fy.add(alphas[15]);
                            break;
                    }
                }
            }
            if(fy.isEmpty()) {
                message = "Nie można zidentyfikować choroby ani zaproponować badania";
            }
            else {
                message = "Sugerowane choroby albo badania:\n";
                for(int a = 0; a < fy.size(); a++) {
                    message += fy.get(a) + (a == (fy.size() - 1) ? "." : ",\n");
                }
            }
        }
        else {
            fu = new ArrayList<>();
            int tupleIndex = (int)(Math.random() * su.size());
            Tuple choosenTuple = su.get(tupleIndex);
            boolean[] tupleValues = choosenTuple.values;
            for(int v = 0; v < tupleValues.length; v++) {
                if(tupleValues[v]) {
                    switch(v) {
                        case 0:
                            fu.add(alphas[1]);
                            break;
                        case 1:
                            fu.add(alphas[2]);
                            break;
                        case 2:
                            fu.add(alphas[3]);
                            break;
                        case 3:
                            fu.add(alphas[5]);
                            break;
                        case 4:
                            fu.add(alphas[6]);
                            break;
                        case 5:
                            fu.add(alphas[7]);
                            break;
                        case 6:
                            fu.add(alphas[10]);
                            break;
                        case 7:
                            fu.add(alphas[11]);
                            break;
                        case 8:
                            fu.add(alphas[12]);
                            break;
                        case 9:
                            fu.add(alphas[13]);
                            break;
                        case 10:
                            fu.add(alphas[16]);
                            break;
                    }
                }
            }
            if(fu.isEmpty()) {
                message = "Nie można zidentyfikować objawów.";
            }
            else {
                message = "Sugerowane objawy:\n";
                for(int a = 0; a < fu.size(); a++) {
                    message += fu.get(a) + (a == (fu.size() - 1) ? "." : ", ");
                }
            }
        }
        return message;
    }

}
