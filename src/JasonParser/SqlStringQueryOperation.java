/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JasonParser;

/**
 *
 * @author Tarek
 */
public class SqlStringQueryOperation {

    public static String AddQouts(String Query) {
        return "\"" + Query + "\"";
    }
}
