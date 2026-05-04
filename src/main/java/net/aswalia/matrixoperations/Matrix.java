
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.aswalia.matrixoperations;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author arvin
 */

public class Matrix {
    // the amount to test difference in equals
    public static double EPSILON = 1e-15;
    
    private static String STRING_FORMAT = "%.3f";
    // the states defined within the state transition matrix
    private enum State {
        value,
        plusSign,
        minusSign,
        integerDigits,
        decimalPoint,
        decimalDigits,
        e_unexp,
        e_unkno
    }

    // the character types expected in parsing Matrix files
    private enum CharType {
        ws,
        plus,
        minus,
        digit,
        decimal,
        nl,
        unknown
    }

/*       0    1   2     3     4   5    6     nl    = 'n' | 'r'
       | ws | + | - | digit | . | nl | uk  | ws    = ' ' | '\t |
       ------------------------------------- digit = '0' | '1' | ... |'9'
                                             uk = unknown (any but the defined)
     0 | 0  | 1 | 2 |   3   | - | 0  |  -  | 0 = beginRow            - = <error state>  
     1 | -  | - | - |   3   | - | -  |  -  | 1 = nonDigits       
     2 | -  | - | - |   3   | - | -  |  -  | 2 = afterMinusSign
     3 | 0  | - | - |   3   | 4 | 0  |  -  | 3 = digits
     4 | -  | - | - |   5   | - | -  |  -  | 4 = afterDecimalPoint
     5 | 0  | - | - |   5   | - | 0  |  -  | 5 = decimalDigits
*/
    private static final State[][] STATE_MACHINE = {
//          ws                +               -               digit                .                 nl             uk
       {State.value,   State.plusSign, State.minusSign, State.integerDigits, State.e_unexp,      State.value,   State.e_unkno}, //  value
       {State.e_unexp, State.e_unexp,  State.e_unexp,   State.integerDigits, State.e_unexp,      State.e_unexp, State.e_unkno}, //  plusSign
       {State.e_unexp, State.e_unexp,  State.e_unexp,   State.integerDigits, State.e_unexp,      State.e_unexp, State.e_unkno}, //  minusSign
       {State.value,   State.e_unexp,  State.e_unexp,   State.integerDigits, State.decimalPoint, State.value,   State.e_unkno}, //  integerDigits
       {State.e_unexp, State.e_unexp,  State.e_unexp,   State.decimalDigits, State.e_unexp,      State.e_unexp, State.e_unkno}, //  decimalPoint
       {State.value,   State.e_unexp,  State.e_unexp,   State.decimalDigits, State.e_unexp,      State.value,   State.e_unkno}  //  decimalDigits
    };

    private BufferedReader br;
    private State state;
    private CharType tokenType;
    private char nextToken;
    private boolean newRow;
    private String value;
    private int indexi, indexj;
    private final List<MatrixElement> ml;
    private final double[][] ma;
    
    public Matrix(String filename) throws Exception {
        ml = new LinkedList<>();
        state = State.value;
        indexi = indexj = 1;
        newRow = false;
        value = "";
        buildMatrix(filename);
        ma = listToArray();
    }
    
    public Matrix(List<MatrixElement> om) throws Exception {
        ml = om;
        ma = listToArray();
    }
    
    public Matrix(double[][] om) {
        ma = om;
        ml = arrayToList();
    }
    
    public List<MatrixElement> getMatrixAsList() {
        return ml;
    }
    
    public double[][] getMatrixAsArray() {
        return ma;
    }
    
    public double[] row(int r) {
        return ma[r];
    }
    
    public double[] column(int c) {
        int col = getColumns();
        double[] ret = new double[col];
        for (int i=0; i<col; i++) {
            ret[i] = ma[i][c];
        }
        return ret;
    }
    
    public int r() {
        return getRows();
    }
    
    public int c() {
        return getColumns();
    }
    
    @Override
    public String toString() {
        String ret = "";
        for (var row : ma) {
            for (double val : row) {
                String v = String.format(STRING_FORMAT, val);
                ret += v + " ";
            }
            ret += "\n";
        }
        return ret;
    }
    
    @Override
    public boolean equals(Object om) {
        if ((om == null) || (!(om instanceof Matrix))) {
            return false;
        } else {
            int s = ma.length;
            double[][] oma = ((Matrix)om).getMatrixAsArray();
            if (oma.length != s) {
                return false;
            }
            for (int i=0; i<s; i++) {
                for (int j=0; j<s; j++) {
                    // should this be an absolut or relative measurement?
                    // currently going with absolut.
                    if ((ma[i][j]-oma[i][j]) > Matrix.EPSILON) {
                        return false;
                    }
                }
            }
            return true;
        }       
    } 

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Arrays.deepHashCode(this.ma);
        return hash;
    }
    
    private List<MatrixElement> arrayToList() {
        List<MatrixElement> ret = new LinkedList<>();
        int row = ma.length;
        int col = row;
        for (int i=0; i<row; i++) {
            for (int j=0; j<col; j++) {
                // Matrix in list form uses index from 1
                // Matrix in array form uses index from 0
                ret.add(new MatrixElement(i+1,j+1,ma[i][j]));
            }
        }
        return ret;
    }
    
    private double[][] listToArray() throws Exception {
        int rows = getRows();
        int columns = getColumns();
        if (rows != columns) {
            throw new Exception("Not a square matrix: " + rows + "," + columns);
        }
        double[][] mat = new double[rows][columns];
        for(MatrixElement e: ml) {
            // Matrix in list form uses index from 1
            // Matrix in array form uses index from 0
            mat[e.row()-1][e.col()-1] = e.value();
        }
        return mat;
    }
    
    private int getRows() {
        int row = 0;
        for(MatrixElement e: ml) {
            if (row < e.row()) {
                row = e.row();
            }
        }
        return row;
    }
    
    private int getColumns() {
        int col = 0;
        for(MatrixElement e: ml) {
            if (col < e.col()) {
                col = e.col();
            }
        }
        return col;
    }
    
    private void buildMatrix(String filename) throws Exception {
        FileReader fr = new FileReader(filename);
        br = new BufferedReader(fr);
        parseMatrix();
    }
    
    private void error(char oc) throws Exception {
        throw new Exception("Parse error - expected \'" + tokenType + "\' Got: " + oc);
    }
    
    private void nextTokenType() {
        switch(nextToken) {
            case ' ', '\t'  -> tokenType = CharType.ws;
            case '\n', '\r' -> tokenType = CharType.nl;
            case '+'        -> tokenType = CharType.plus;
            case '-'        -> tokenType = CharType.minus;
            case '0', '1', '2', 
                 '3', '4', '5', 
                 '6', '7', '8', 
                 '9'        -> tokenType = CharType.digit;
            case '.'        -> tokenType = CharType.decimal;
            default         -> tokenType = CharType.unknown;
        }
    }
    
    private void setElement() {
        double val = Double.parseDouble(value);
        ml.add(new MatrixElement(indexi, indexj, val));
        value = "";
        if (newRow) {
            indexi++; // next row
            indexj = 1;
        } else {
            indexj++; // next column
        }
    }
    
    private void element() {
        switch(tokenType) {
            case plus, minus, digit -> value += nextToken;
        }
    }
    
    private void nonDigits() {
        switch(tokenType) {
            case digit -> value += nextToken;
        }
    }   
    
    private void digits() {
        switch(tokenType) {
            case ws             -> {newRow = false; setElement();} 
            case nl             -> {newRow = true; setElement();}
            case digit, decimal -> value += nextToken;
        }
    }    
    
    private void parseMatrix() throws Exception {
        int ch;
        while ((ch = br.read()) != -1) {
            char oldChar = nextToken;
            nextToken = (char) ch;
            nextTokenType();
            switch (state) {
                case value                             -> element();
                case plusSign, minusSign, decimalPoint -> nonDigits();
                case integerDigits, decimalDigits      -> digits();
                default                                -> error(oldChar);
            }
            state = STATE_MACHINE[state.ordinal()][tokenType.ordinal()];            
        }
        if (!tokenType.equals(CharType.ws) && !tokenType.equals(CharType.nl)) {
            throw new Exception("Unexpected EOF: " + tokenType + " " + nextToken);
        } 
    }

    public static void main(String[] args) throws Exception {
        System.out.println("End to end test");
        String filename = args[0];
//        System.out.println("Before matrix build: " + System.currentTimeMillis());
        Matrix m = new Matrix(filename);
//        System.out.println("After matrix build:  " + System.currentTimeMillis());
        System.out.println("Matrix m:\n" + m);
        System.out.println();
//        System.out.println("Before matrix determinant: " + System.currentTimeMillis());
//        System.out.println("Determinant: " + MatOps.det(m));
//        System.out.println("After matrix determinant:  " + System.currentTimeMillis());
//        System.out.println();
//        System.out.println("Before inverse matrix: " + System.currentTimeMillis());
        Matrix mm1 = MatOps.inv(m);
//        System.out.println("After inverse matrix:  " + System.currentTimeMillis());
        System.out.println("Inverse of m:\n" + mm1);
        if (mm1 != null) {
            System.out.println();
//            System.out.println("Before matrix mult: " + System.currentTimeMillis());
            System.out.println("m X inv(m):\n" + MatOps.mult(m, mm1));
//            System.out.println("After matrix mult:  " + System.currentTimeMillis());
        }
    }
}