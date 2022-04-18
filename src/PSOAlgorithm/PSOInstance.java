/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PSOAlgorithm;

import FitnessFunction.FitnessFunction;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Random;

/**
 *
 * @author Tarek
 */
public class PSOInstance {

    //PSOInstance PSO = null;
    double[] X;
    double[] V;
    double[] pBest;
    double W, c1, r1, c2, r2;
    public static double[] gBest;
    public static int chromosomeLength = 0;
    double bestLocalFitnessValue = Double.MAX_VALUE;
    public static double bestGlobalFitnessValue = Double.MAX_VALUE;

    public void Iterate() throws SQLException, ParseException {
        for (int i = 0; i < chromosomeLength; i++) {
            V[i] = (W * V[i]) + (c1 * r1 * (pBest[i] - X[i])) + (c2 * r2 * (gBest[i] - X[i]));
            X[i] = V[i] + X[i];
            X[i] = Math.abs(X[i]);
        }

        CalculateInstanceFitnessValue();
    }

    public void CalculateInstanceFitnessValue() throws SQLException, ParseException {

        Double FF = new FitnessFunction(X).CalculateFitnessFunction();
        SetBestLocalFitnessValue(FF);
        PSOInstance.SetBestGlobalFitnessValue(FF, this);

    }

    public void SetBestLocalFitnessValue(double FitnessValue) {
        if (this.bestLocalFitnessValue > FitnessValue) {
            //  System.out.println("FitnessValue : " + FitnessValue + " bestLocalFitnessValue : " + bestLocalFitnessValue);
            this.bestLocalFitnessValue = FitnessValue;
            pBest = X.clone();
        }
    }

    public static void SetBestGlobalFitnessValue(double FitnessValue, PSOInstance PSO) {
        if (bestGlobalFitnessValue > FitnessValue) {
            // System.out.println("FitnessValue : " + FitnessValue + " bestGlobalFitnessValue : " + bestGlobalFitnessValue);
            bestGlobalFitnessValue = FitnessValue;
            gBest = PSO.X.clone();
        }
    }

    PSOInstance() {
        X = new double[chromosomeLength];
        V = new double[chromosomeLength];
        pBest = new double[chromosomeLength];
        W = 0.6;
        c1 = 0.5;
        c2 = 0.5;
        r1 = 0.5;
        r2 = 0.5;

        for (int i = 0; i < chromosomeLength; i++) {
            Random r = new Random();
            double random = r.nextFloat() * (10.0);
            X[i] = random;
            Random r2 = new Random();
            double random2 = r2.nextFloat() * (10.0);
            V[i] = random2;
        }
    }
}
