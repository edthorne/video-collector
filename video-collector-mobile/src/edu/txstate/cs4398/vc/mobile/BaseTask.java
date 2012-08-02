package edu.txstate.cs4398.vc.mobile;

import android.os.AsyncTask;

public abstract class BaseTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	
	protected static final String NAMESPACE = "http://services.desktop.vc.cs4398.txstate.edu/";
	protected EventHandler event;

}
