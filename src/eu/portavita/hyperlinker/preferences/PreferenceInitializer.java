package eu.portavita.hyperlinker.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import eu.portavita.hyperlinker.Activator;

/**
 * Class used to initialize default preference values.
 *
 * @author Jasper A. Visser
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.PATTERN, "\\w+");
		store.setDefault(PreferenceConstants.TARGET_URL, "http://localhost/#q=%s");
		store.setDefault(PreferenceConstants.USE_EXTERNAL_BROWSER, false);
	}
}
