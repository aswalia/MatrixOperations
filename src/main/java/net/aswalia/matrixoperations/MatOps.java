/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package net.aswalia.matrixoperations;
/**
 *
 * @author arvin
 */
public class MatOps {

    private static void checkForSizeMismatch(int r, int c) throws Exception {
        if (r != c) {
            throw new Exception("Row and column must have same size: " + r + " != " + c);
        }
    }

    private static void checkSquareMatrix(Matrix m) throws Exception {
        int row = m.r();
        int col = m.c();
        checkForSizeMismatch(row, col);
    }

    private static void checkSameSizeMat(Matrix m, Matrix om) throws Exception {
        checkSquareMatrix(m);
        checkSquareMatrix(om);
        int row = m.r();
        int orow = om.r();
        checkForSizeMismatch(row, orow);
    }

    public static double vectMult(double[] row, double[] col) throws Exception {
        int r = row.length;
        int c = col.length;
        checkForSizeMismatch(r, c);
        // verified size of vectors match
        double ret = 0;
        for (int i = 0; i < r; i++) {
            ret += row[i] * col[i];
        }
        return ret;
    }

    public static Matrix subMatrix(Matrix m, int r, int c) throws Exception {
        // exclude elements from m with row r and colum c
        int rowS = m.r();
        int colS = m.c();
        double[][] ma = m.getMatrixAsArray();
        int ns = rowS - 1;
        double[][] ret = new double[ns][ns];
        int ri = 0;
        int cj = 0;
        for (int i = 0; i < rowS; i++) {
            for (int j = 0; j < colS; j++) {
                if ((i != (r - 1)) && (j != (c - 1))) {
                    ret[ri][cj] = ma[i][j];
                    cj = (cj + 1) % ns;
                    if (cj == 0) {
                        ri++;
                    }
                }
            }
        }
        return new Matrix(ret);
    }

    private static double simpleDeterminant(Matrix m) {
        double[][] mat = m.getMatrixAsArray();
        return mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0];
    }

    public static double det(Matrix m) throws Exception {
        checkSquareMatrix(m);
        // verified square matrix
        int rowS = m.r();
        int colS = m.c();
        double ret;
        if (rowS == 2) {
            ret = simpleDeterminant(m);
        } else {
            // recursiv implementation with det as a sum of
            // products between element and sub-matrix det
            double[][] mat = m.getMatrixAsArray();
            double elemContribution = 0;
            int sign;
            for (int j = 0; j < colS; j++) {
                sign = (j % 2 == 0) ? 1 : -1;
                elemContribution += sign * mat[0][j] * det(MatOps.subMatrix(m, 1, j + 1));
            }
            ret = elemContribution;
        }
        return ret;
    }

    public static Matrix trans(Matrix m) throws Exception {
        checkSquareMatrix(m);
        // verified square matrix
        int rowS = m.r();
        int colS = m.c();
        double[][] mat = m.getMatrixAsArray();
        var om = new double[rowS][colS];
        for (int i = 0; i < rowS; i++) {
            for (int j = 0; j < colS; j++) {
                om[i][j] = mat[j][i];
            }
        }
        return new Matrix(om);
    }

    public static Matrix mult(Matrix m, Matrix om) throws Exception {
        checkSameSizeMat(m, om);
        // verified m and om of same size
        int row = m.r();
        int ocol = om.c();
        double[][] ret = new double[row][ocol];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < ocol; j++) {
                ret[i][j] = MatOps.vectMult(m.row(i), om.column(j));
            }
        }
        return new Matrix(ret);
    }
    
    public static boolean isDetZero(double det) throws Exception {
        return (Math.abs(det) < Matrix.EPSILON);
    }
    
    public static boolean isDetZero(Matrix m) throws Exception {
        double det = MatOps.det(m);
        return MatOps.isDetZero(det);      
    }

    public static Matrix inv(Matrix m) throws Exception {
        double det = MatOps.det(m);
        if (MatOps.isDetZero(det)) {
            // no inv exists
            return null;
        } else {
            // verified square matrix in det-operation
            double[][] mat = m.getMatrixAsArray();
            int s = mat.length;
            double[][] ret = new double[s][s];
            int sign;
            for (int i = 0; i < s; i++) {
                for (int j = 0; j < s; j++) {
                    sign = ((i + j) % 2 == 0) ? 1 : -1;
                    ret[i][j] = sign * MatOps.det(MatOps.subMatrix(m, i + 1, j + 1)) / det;
                }
            }
            return MatOps.trans(new Matrix(ret));
        }
    }

    public static Matrix add(Matrix m, Matrix om) throws Exception {
        checkSameSizeMat(m, om);
        // verified m and om of same size
        int row = m.r();
        int col = m.c();
        double[][] mat = m.getMatrixAsArray();
        double[][] ret = new double[row][col];
        double[][] ommat = om.getMatrixAsArray();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                ret[i][j] = mat[i][j] + ommat[i][j];
            }
        }
        return new Matrix(ret);

    }

    public static Matrix sub(Matrix m, Matrix om) throws Exception {
        checkSameSizeMat(m, om);
        // verified m and om of same size
        int row = m.r();
        int col = m.c();
        double[][] mat = m.getMatrixAsArray();
        double[][] ret = new double[row][col];
        double[][] ommat = om.getMatrixAsArray();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                ret[i][j] = mat[i][j] - ommat[i][j];
            }
        }
        return new Matrix(ret);
    }
}
