package eu.portavita.hyperlinker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;

import eu.portavita.hyperlinker.preferences.PreferenceConstants;

/**
 * Detects hyperlinks that match a configurable pattern.
 *
 * @author Jasper A. Visser
 */
public class HyperlinkDetector extends AbstractHyperlinkDetector {

	/**
	 * Document where previous hyperlink was found.
	 */
	private static IDocument previousDocument = null;

	/**
	 * Previous hyperlink that was found.
	 */
	private static IHyperlink previousResult = null;

	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region,
			boolean canShowMultipleHyperlinks) {

		final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();

		// No region, no glory.
		if (region == null || textViewer == null) {
			previousResult = null;
			return null;
		}

		// Get document.
		IDocument document = textViewer.getDocument();
		if (document == null) {
			previousResult = null;
			return null;
		}

		// If user hasn't moved outside word, return previous result.
		if (document.equals(previousDocument)
				&& previousResult != null
				&& region.getOffset() >= previousResult.getHyperlinkRegion().getOffset()
				&& region.getOffset() < previousResult.getHyperlinkRegion().getOffset()
						+ previousResult.getHyperlinkRegion().getLength()) {
			return new IHyperlink[] { previousResult };
		}

		// Get line, and offset therein.
		int offset = region.getOffset();
		final IRegion lineInfo;
		final String line;
		try {
			lineInfo = document.getLineInformationOfOffset(offset);
			line = document.get(lineInfo.getOffset(), lineInfo.getLength());
		} catch (org.eclipse.jface.text.BadLocationException e) {
			e.printStackTrace();
			previousResult = null;
			return null;
		}
		int offsetInLine = offset - lineInfo.getOffset();

		// Match hyperlink.
		Hyperlink result = null;
		String pattern = preferenceStore.getString(PreferenceConstants.PATTERN);
		String description = preferenceStore.getString(PreferenceConstants.DESCRIPTION);
		final Matcher matcher = Pattern.compile(pattern).matcher(line);
		while (matcher.find() && result == null) {
			final int group = 0;
			final int start = matcher.start(group);
			final int end = matcher.end(group);
			final String text = matcher.group(group);
			if (start <= offsetInLine && end > offsetInLine) {
				result = new Hyperlink(new Region(start + lineInfo.getOffset(), end - start), text, description);
				break;
			}
		}

		// No result, no glory.
		if (result == null) {
			previousResult = null;
			return null;
		}

		// Remember this document, region & result, because Eclipse calls this method a lot.
		previousDocument = document;
		previousResult = result;

		// Return.
		return new IHyperlink[] { result };
	}
}
