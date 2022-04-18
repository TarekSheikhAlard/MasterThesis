package FireFlyAlgorithm;

import java.io.*;
import java.sql.SQLException;
import java.text.*;
import java.util.Locale;

abstract class f_xj {
    // single function  multi independent variable
    // a single value is returned indiced to equation_ref
    // example f[0]=x[0]+sin(x[1])
    //         f[1]=x[0]*x[0]-x[1]
    // func(x,1) returns the value of f[1]
    // func(x,0) returns the value of f[0]

    abstract double func(double x[]) throws SQLException, ParseException;
}

public class FireFlyInstance {

    double gamma, alpha, beta;
    double alpha0, alphan;
    int PopulationNumber;
    int ChromosomeLength;
// m number of fireflies
// n number of variables for each firefly 
// I0 light intensity at source
// gamma absorbtion coefficient
// alpha size of the random step
// beta attractiveness
    double Particles[][];
    double FitnessValue[];
    double ymin;
    double xmin[];
    double xmax[];
    double BEST[];
    int imin;
    int ngeneration;

    public FireFlyInstance(f_xj ff, int PopulationNum, double xxmin[], double xxmax[], double gammai, double alpha0i, double alphani, int ngenerationi) throws SQLException, ParseException {
//initial population set
        ChromosomeLength = xxmin.length;
        PopulationNumber = PopulationNum;
        Particles = new double[PopulationNumber][ChromosomeLength];
        FitnessValue = new double[PopulationNumber];
        xmin = new double[ChromosomeLength];

        gamma = gammai;
        alpha0 = alpha0i;
        alphan = alphani;
        ngeneration = ngenerationi;
        BEST = new double[ngeneration];
        ymin = 1.0e50;
        for (int i = 0; i < PopulationNumber; i++) {
            for (int j = 0; j < ChromosomeLength; j++) {
                Particles[i][j] = xxmin[j] + Math.random() * (xxmax[j] - xxmin[j]);
            }

            FitnessValue[i] = ff.func(Particles[i]);
            if (FitnessValue[i] < ymin) {
                ymin = FitnessValue[i];
                imin = i;
                for (int k = 0; k < ChromosomeLength; k++) {
                    xmin[k] = Particles[i][k];
                }
                alpha = alpha0;
            };
        }
//end of init
        int l = 0;
        double d = 0;
        double t = 0;
        while (l < ngeneration) {

            double fi = 0, fj = 0;
            for (int i = 0; i < PopulationNumber; i++) {
                fi = ff.func(Particles[i]);
                for (int j = 0; j < PopulationNumber; j++) {
                    fj = ff.func(Particles[j]);
                    if (fi < fj) {
                        beta = attractiveness(i, j);
                        for (int k = 0; k < ChromosomeLength; k++) {   //The best one will remain the rest will change
                            if (Particles[i][k] != xmin[k]) {
                                Particles[i][k] = (1.0 - beta) * Particles[i][k] + beta * Particles[j][k] + alpha * (Math.random() - 0.5);
                            }
                        }
                    } else {
                        for (int k = 0; k < ChromosomeLength; k++) {
                            Particles[i][k] = Particles[i][k] + alpha * (Math.random() - 0.5);
                        }
                    }
                }
               // double old = fi;
               // System.out.println("(Old Fi: " + fi);
                fi = ff.func(Particles[i]);
               // System.out.println("new Fi: " + fi +")" + (fi<old));
                FitnessValue[i] = fi;
                if (FitnessValue[i] < ymin) {
                    ymin = FitnessValue[i];
                    imin = i;
                    for (int k = 0; k < ChromosomeLength; k++) {
                        xmin[k] = Particles[i][k];
                    }
                };
            }
//best firefly moves randomly
            for (int k = 0; k < ChromosomeLength; k++) {
                Particles[imin][k] = Particles[imin][k] + alpha * (Math.random() - 0.5);
            }
            FitnessValue[imin] = ff.func(Particles[imin]);
            if (FitnessValue[imin] < ymin) {
                ymin = FitnessValue[imin];
            }
            alpha = alphan + (alpha0 - alphan) * Math.exp(-t);
            t = 0.1 * l;

            BEST[l] = ymin;
           // System.out.println( FitnessValue[imin]);
            l++;
        }

    }

    public double distance(int i, int j) {
        double d = 0;
        for (int k = 0; k < ChromosomeLength; k++) {
            d += (Particles[i][k] - Particles[j][k]) * (Particles[i][k] - Particles[j][k]);
        }
        d = Math.sqrt(d);
        return d;
    }

    public double attractiveness(int i, int j) {
        double d = distance(i, j);
        //return FitnessValue[i] / (1 + gamma * d * d);
        return FitnessValue[i]*Math.exp(-gamma*d*d);
    }

    public String toString() {
        String s = "ymin = " + ymin + "\nxmin = ";
        for (int k = 0; k < ChromosomeLength; k++) {
            s += xmin[k] + " ";
        }
        return s;
    }

    public String toString(f_xj ff, double xx[]) throws SQLException, ParseException {
        String s = "ymin = " + ff.func(xx) + "\nxmin = ";
        for (int k = 0; k < ChromosomeLength; k++) {
            s += xx[k] + " ";
        }
        return s;
    }

    public double[] xmin() {
        return xmin;
    }

    double[][] solution() {

        double out[][] = new double[2][xmin.length];
        out[0][0] = ymin;
        for (int i = 0; i < xmin.length; i++) {
            out[1][i] = xmin[i];
        }
        return out;

    }

    void toStringnew() {
        double[][] out = solution();
        System.out.println("Optimized value = " + out[0][0]);
        for (int i = 0; i < xmin.length; i++) {
            System.out.println("x[" + i + "] = " + out[1][i]);
        }

    }

}
