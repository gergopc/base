package hu.bme.mit.train.controller;

public class TrainControlThread extends Thread {

    private static final int STEP_INTERVAL = 100;
    private boolean running = false;
    private TrainControllerImpl trainControllerImpl;

    public TrainControlThread(TrainControllerImpl trainControllerImpl){
            super();
            this.trainControllerImpl = trainControllerImpl;
    }

    @Override
    public void run(){
        running = true;
        while(running){
            trainControllerImpl.followSpeed();
            try {
                Thread.sleep(STEP_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stopSimulation(){
        running = false;
    }

}
