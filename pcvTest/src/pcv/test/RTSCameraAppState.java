package pcv.test;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

public class RTSCameraAppState extends AbstractAppState {

    private Application app;
    private RTSCamera rtsCam;

    public RTSCameraAppState(RTSCamera fc) {
    	rtsCam = fc;
    }    

    /**
     *  This is called by SimpleApplication during initialise().
     */
    void setCamera(RTSCamera cam) {
        this.rtsCam = cam;
    }
    
    public RTSCamera getCamera() {
        return rtsCam;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        this.app = app;

        if (app.getInputManager() != null) {

            if (rtsCam == null) {
            	throw new IllegalStateException("How is this possible?");
                //testCam = new RTSCamera(app.getCamera());
            }
            
            rtsCam.registerApp(app.getInputManager());            
        }               
    }
            
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        
        rtsCam.setEnabled(enabled);
    }
    
    @Override
    public void cleanup() {
        super.cleanup();

        if (app.getInputManager() != null) {        
            rtsCam.unregisterInput();
        }        
    }
}
