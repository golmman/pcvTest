package pcv.test;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

public class RTSCameraAppState extends AbstractAppState {

    private Application app;
    private RTSCamera testCam;

    public RTSCameraAppState(RTSCamera fc) {
    	testCam = fc;
    }    

    /**
     *  This is called by SimpleApplication during initialise().
     */
    void setCamera( RTSCamera cam ) {
        this.testCam = cam;
    }
    
    public RTSCamera getCamera() {
        return testCam;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        this.app = app;

        if (app.getInputManager() != null) {
        
            if (testCam == null) {
                testCam = new RTSCamera(app.getCamera());
            }
            
            testCam.registerWithInput(app.getInputManager());            
        }               
    }
            
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        
        testCam.setEnabled(enabled);
    }
    
    @Override
    public void cleanup() {
        super.cleanup();

        if (app.getInputManager() != null) {        
            testCam.unregisterInput();
        }        
    }
}
