import org.ejml.ops.CommonOps;
import org.ejml.ops.NormOps;
import org.ejml.simple.*;
import org.ejml.ops.CommonOps.*;
import org.ejml.ops.NormOps.*;
import sun.java2d.pipe.SpanShapeRenderer;

public class Main {

    public static void main(String[] args) {

        //array of Beacon objects
        Beacon[] beacons = new Beacon[3]; //set the size to 3 at first
        int numBeacons = beacons.length;

        //create Beacons
        Beacon beacon1 = new Beacon(10, 10);
        Beacon beacon2 = new Beacon(0, 15);
        Beacon beacon3 = new Beacon(-5, 5);

        //add Beacons to array
        beacons[0] = beacon1;
        beacons[1] = beacon2;
        beacons[2] = beacon3;

        //set intial guess
        double guessX = 2;
        double guessY = 2;
        double[][] station = new double[][]{{ guessX, guessY }};
        SimpleMatrix matStation = new SimpleMatrix(station);
        double[][] mo = new double[][]{{ -1.0, -1.0}};
        SimpleMatrix minusOne = new SimpleMatrix(mo);

        //calculate the estimation error
        double estimationError = 0;
        double[] distances = {15,16,5};
        for (int i=0; i<numBeacons; i++) {
            Beacon thisBeacon = beacons[i];
//            double d = getDistance(thisBeacon.getX(), thisBeacon.getY(), guessX, guessY);
            double d = distances[i];
            double f = Math.abs(Math.pow(thisBeacon.getX() - guessX, 2) + Math.pow(thisBeacon.getY() - guessY, 2) - Math.pow(d, 2));
            estimationError = estimationError + f;
        }

        //create a Jacobian matrix of size [number_of_beacons][2]
        double[][] jacobianMatrix = new double[numBeacons][2];
        double[][] matF = new double[numBeacons][1];
        while (estimationError > 0.01){
            //for loop happens here
            //the condition for, for loop ->
            for (int i=0; i<numBeacons; i++){ //3 is the number of beacons
                //we calculate the jacobian matrix here
                Beacon b = beacons[i];
                for (int j=0; j<2; j++){
                    if (j==0){
                        jacobianMatrix[i][j] = -2*(b.getX()-guessX);
                    }
                    else {
                        jacobianMatrix[i][j] = -2 * (b.getY() - guessY);
                    }
                }
                matF[i][0] = Math.pow(b.getX() - guessX, 2) + Math.pow(b.getY() - guessY, 2) - Math.pow(distances[i], 2);
            }
            SimpleMatrix matrixJacobian = new SimpleMatrix(jacobianMatrix);
            SimpleMatrix matrixF = new SimpleMatrix(matF);
            //here goes the matrix inverse operation
            // estimationError = -inv(jacobianMatrix' * jacobianMatrix) * (jacobianMatrix') * F'
            SimpleMatrix first = (matrixJacobian.transpose().mult(matrixJacobian)).invert();
            SimpleMatrix second = (matrixJacobian.transpose().mult(matrixF));
            SimpleMatrix matrixError = first.mult(second);
            matrixError = matrixError.negative();
            matStation = matStation.plus(matrixError.transpose());
            System.out.println(matStation);
            estimationError = matrixError.elementSum();
        }
    }

    //coordinate 1: x1, y1; coordinate 2: x2, y2
    public static double getDistance(int x1, int y1, int x2, int y2) {
        double d = Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
        return d;
    }
}