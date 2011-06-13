package nbody_distribuito;

public class Constants {

    // Costanti di rete

    public final static String WORKER_IP = "127.0.0.1";
    public final static String SERVER_IP = "127.0.0.1";


    // Costanti per lo scambio di messaggi che conivolgono il Master 
    public final static String ASSOCIATE = "associate";
    public final static String ACK_ASSOCIATE = "ackAssociate";
    public final static String DISSOCIATE = "dissociate";
    public final static String ACK_DISSOCIATE = "ackDissociate";
    public final static String CLIENT_QUEUE = "clientQueue";
    public final static String CLIENT_QUEUE_RESP = "clientQueueResp";
    public static final String WAIT_ASSOCIATE = "waitAssociate";
    public final static String DO_JOB = "doJob";
    public final static String JOB_RESULT = "jobResult";

    // Costanti per lo scambio di messaggi che coinvolgono il FlagActor
//    public final static String IS_SET = "isSet";
//    public final static String IS_SET_RESULT = "isSetResult";
//    public final static String SET_FLAG = "setFlag";
//    public final static String RESET_FLAG = "resetFlag";

    // COstanti per lo scambio di messaggi che coinvolgono gli eventi per la
    // gestione dell'avvio, dello stop e della pausa della computazione
    public final static String START_EVENT = "startEvent";
    public final static String STOP_EVENT = "stopEvent";
    public final static String PAUSE_EVENT = "pauseEvent";
    public static final String STEP_EVENT = "singleStepEvent";
    public final static String RANDOMIZE_EVENT = "randomizeEvent";
    public final static String CHANGE_PARAM = "changeParamEvent";
    public static final String OPEN_FILE_EVENT = "openFileEvent";

 //   public final static String STOP_ACTOR = "stopActor";
    public final static String EVENT_CONTROLLER_ACTOR = "eventControllerActor";
    public final static String COMPUTE_ACTOR = "computeActor";
    public final static String WORKER_HANDLER_ACTOR = "workerHandler";
    public final static String WORKER_ACTOR = "workerActor";
	
	

}
