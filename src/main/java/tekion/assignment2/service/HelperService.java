package tekion.assignment2.service;

import java.util.*;

public abstract class HelperService {

    // Convert Balls into Overs
    public static String convertToOvers(int balls) {
        String answer;
        int overs = balls / 6;
        int overBalls = balls % 6;
        answer = overs + "." + overBalls;
        return answer;
    }

    // Generate Ball Result such that batsman hits more than bowlers
    public static int generateRandomNumberBias(String type) {
        if(Objects.equals(type, "Batsman"))
            return generateRandomNumberBiasBatsman() ;
        return generateRandomNumberBiasBowler() ;
    }

    private static int generateRandomNumberBiasBatsman() {
        double[] prob = {0.10,0.15,0.20,0.18,0.11,0.09,0.07,0.1} ;
        return func(prob) ;
    }

    private static int generateRandomNumberBiasBowler() {
        double[] prob = {0.20,0.25,0.15,0.10,0.05,0.03,0.02,0.20} ;
        return func(prob) ;
    }

    private static int func(double[] prob)
    {
        double d = Math.random() ;
        double sum = 0.0 ;
        for(int i=0;i<8;i++)
        {
            sum += prob[i] ;
            if(sum>d)
            {
                return i ;
            }
        }
        return 7 ;
    }


}
