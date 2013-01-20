package eu.portavita.hyperlinker;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

import eu.portavita.hyperlinker.preferences.PreferenceConstants;

/**
 * Represents a hyperlink that matches a configurable pattern and links to a configurable URL.
 *
 * @author Jasper A. Visser
 */
public class Hyperlink implements IHyperlink {

	/**
	 * Id of the internal browser to re-use.
	 */
	private static final String INTERNAL_BROWSER_ID = "3490328034";

	/**
	 * Region of the hyperlink.
	 */
	private IRegion region;

	/**
	 * Text of the hyperlink.
	 */
	private String text;

	/**
	 * Creates a new hyperlink.
	 *
	 * @param region Region of the hyperlink.
	 * @param text Text of the hyperlink.
	 */
	public Hyperlink(IRegion region, String text) {
		this.region = region;
		this.text = text;
	}

	@Override
	public IRegion getHyperlinkRegion() {
		return region;
	}

	@Override
	public String getHyperlinkText() {
		return "Open in reflection";
	}

	@Override
	public String getTypeLabel() {
		return "Open in reflection";
	}

	@Override
	public void open() {

		// Get preferences.
		final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		final String targetUrl = preferenceStore.getString(PreferenceConstants.TARGET_URL);
		final Boolean useExternalBrowser = preferenceStore
				.getBoolean(PreferenceConstants.USE_EXTERNAL_BROWSER);

		// Get browser.
		IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
		IWebBrowser browser;
		try {
			if (useExternalBrowser) {
				browser = support.getExternalBrowser();
			} else {
				browser = support.createBrowser(INTERNAL_BROWSER_ID);
			}
		} catch (PartInitException e) {
			e.printStackTrace();
			return;
		}

		// Open URL.
		try {
			browser.openURL(new URL(targetUrl.replace("%s", text)));
		} catch (PartInitException e) {
			e.printStackTrace();
			return;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return;
		}
	}
}
