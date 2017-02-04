package projekt.swd.drsmartfon;

/**
 * Created by Amadeusz on 2017-01-21.
 */

public class Fact {

    private int number;
    private boolean[] userValues;

    public Fact(int number) {
        this.number = number;
    }

    public Fact(boolean[] userValues) {
        this.userValues = userValues;
        number = 0;
    }

    public boolean evaluate(boolean[] combination) {
        switch(number) {
            case 1: return !(combination[1] && combination[2] || combination[3]) || combination[0];
            case 2: return !(combination[5] && combination[3]) || combination[6];
            case 3: return !(combination[8] && combination[9]) || combination[7];
            case 4: return !combination[4] || (combination[10] || combination[11]);
            case 5: return !(combination[5] && (combination[12] || combination[13])) || combination[8];
            case 6: return !(combination[16] || combination[15]) || !combination[14];
        }

        boolean userFunctionValue = true;
        for(int v = 0; v < userValues.length; v++) {
            if(userValues[v]) {
                userFunctionValue = userFunctionValue && combination[v];
            }
        }
        return userFunctionValue;
    }

}
