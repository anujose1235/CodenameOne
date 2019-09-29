package com.codename1.samples;


import com.codename1.components.InfiniteProgress;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.components.ToastBar.Status;
import static com.codename1.ui.CN.*;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Dialog;
import com.codename1.ui.Label;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.io.Log;
import com.codename1.ui.Toolbar;
import java.io.IOException;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.io.NetworkEvent;
import com.codename1.io.Util;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename One</a> for the purpose 
 * of building native mobile applications using Java.
 */
public class InfiniteProgressWithMessage {

    private Form current;
    private Resources theme;

    public void init(Object context) {
        // use two network threads instead of one
        updateNetworkThreadCount(2);

        theme = UIManager.initFirstTheme("/theme");

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature
        Log.bindCrashProtection(true);

        addNetworkErrorListener(err -> {
            // prevent the event from propagating
            err.consume();
            if(err.getError() != null) {
                Log.e(err.getError());
            }
            Log.sendLogAsync();
            Dialog.show("Connection Error", "There was a networking error in the connection to " + err.getConnectionRequest().getUrl(), "OK", null);
        });        
    }
    
    public void start() {
        if(current != null){
            current.show();
            return;
        }
        Form hi = new Form("Hi World", BoxLayout.y());
        Button showProgress = new Button("Show InfiniteProgress");
        showProgress.addActionListener(e->{
            callSerially(()->{
                Dialog dlg = new Dialog();
                dlg.setDialogUIID("Container");
                dlg.setTintColor(0x0);
                dlg.setLayout(new BorderLayout());
                SpanLabel message = new SpanLabel("This is a progress message we wish to show");
                $("*", message).setFgColor(0xffffff);
                dlg.add(BorderLayout.CENTER, BoxLayout.encloseYCenter(FlowLayout.encloseCenter(new InfiniteProgress()), FlowLayout.encloseCenter(message)));

                dlg.setTransitionInAnimator(CommonTransitions.createEmpty());
                dlg.setTransitionOutAnimator(CommonTransitions.createEmpty());
                dlg.showPacked(BorderLayout.CENTER, false);

                //dlg.revalidateWithAnimationSafety();
                invokeAndBlock(()->{
                    Util.sleep(5000);
                });
                dlg.dispose();
                
            });
        });
        
        Button showToastProgress = new Button("Show Toast Progress");
        showToastProgress.addActionListener(e->{
            callSerially(()->{
                Status status = ToastBar.getInstance().createStatus();
                status.setMessage("This is a progress message we wish to show");
                status.setShowProgressIndicator(true);
                status.show();
                invokeAndBlock(()->{
                    Util.sleep(5000);
                });
                status.clear();
                        
            });
        });
        hi.addAll(showProgress, showToastProgress);
        hi.show();
    }

    public void stop() {
        current = getCurrentForm();
        if(current instanceof Dialog) {
            ((Dialog)current).dispose();
            current = getCurrentForm();
        }
    }
    
    public void destroy() {
    }

}
