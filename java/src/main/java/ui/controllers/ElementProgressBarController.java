package ui.controllers;

import javax.swing.JProgressBar;

import ui.container.AppContainer;
import ui.container.Base;
import ui.container.InjectableObject;

public class ElementProgressBarController extends InjectableObject {
	// Fields
	JProgressBar progressBar;
	
	public ElementProgressBarController(AppContainer _appContainer, Base parent) {
		super(_appContainer, parent);
	}
	
	public void attach(
			JProgressBar progressBar) {
		this.progressBar = progressBar;
		progressBar.revalidate();
		progressBar.repaint();
	}
	
	public void setProgress(float x) {
		progressBar.setValue((int)x * 100);
	}
}
