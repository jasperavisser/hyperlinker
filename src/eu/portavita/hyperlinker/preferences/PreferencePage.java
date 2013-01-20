package eu.portavita.hyperlinker.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import eu.portavita.hyperlinker.Activator;

/**
 * Preference page.
 *
 * @author Jasper A. Visser
 */
public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Preferences for act code hyperlinker plugin.");
	}

	@Override
	public void createFieldEditors() {
		Composite parent = getFieldEditorParent();

		// Add editor for pattern.
		StringFieldEditor editor1 = new StringFieldEditor(PreferenceConstants.PATTERN, "Hyperlink pattern:", parent);
		editor1.getTextControl(parent).setToolTipText("Choose the pattern that represents a hyperlink. Use Java regular expression syntax, i.e. \\w+.");
		addField(editor1);

		// Add editor for URL.
		StringFieldEditor editor2 = new StringFieldEditor(PreferenceConstants.TARGET_URL, "Target URL:", parent);
		editor2.getTextControl(parent).setToolTipText("Choose the URL to be opened when you click a hyperlink. Use %s to represent the hyperlink text in the URL.");
		addField(editor2);

		// Add editor for external browser.
		BooleanFieldEditor editor3 = new BooleanFieldEditor(PreferenceConstants.USE_EXTERNAL_BROWSER, "Use external browser.", parent);
		editor3.getDescriptionControl(parent).setToolTipText("Whether to open hyperlinks in an external browser or Eclipse's internal browser.");
		addField(editor3);
	}

	@Override
	public void init(IWorkbench workbench) {
	}
}