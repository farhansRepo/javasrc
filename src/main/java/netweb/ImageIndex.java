import java.io.*;
import java.util.*;

import com.darwinsys.util.FileProperties;

/** ImageIndex -- make an index.html for an image directory
 * based on some images and a "captions.txt" file.
 * <pre>
 * For each image x, assume there is the following:
 *		a file x+THUMB_SUFFIX which is the thumbnail
 * 		a file x+IMAGE_SUFFIX which is the large image
 *		an entry in the "captions.txt" file, of the form
 *			x	Group shot in front of statue
 * <pre>
 * @author	Ian F. Darwin, http://www.darwinsys.com/
 * @Version $Id$
 */
public class ImageIndex {
	protected String image_suffix;
	protected String thumb_suffix;
	/** The captions file (created by hand) */
	public static final String CAPTIONS_FILE = "captions.txt";
	/** A reader for the captions file */
	protected BufferedReader cap;
	/** The map from filename to caption */
	protected HashMap map = new HashMap();
	/** Vector for listing names for sorting */
	Vector names = new Vector();
	/** The output file that we create */
	public static final String OUTPUTFILE = "index.html";
	/** The main output stream */
	PrintWriter out;
	/** Properties */
	Properties pprops;
	/** Properties file */
	String PROPS_FILE = "ImageIndex.properties";

	/** The background color for the page */
	public String bgcolor;
	/** The title of this show */
	String title;

	/** Make an index */
	public static void main(String[] av) {
		ImageIndex mi = new ImageIndex();
		try {
			mi.open(CAPTIONS_FILE, OUTPUTFILE);		// open files
			mi.read();		// read caption file
			mi.BEGIN();		// print HTML header
			mi.process();		// do bulk of work
			mi.END();		// print trailer.
			mi.close();		// close files
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/** Open the files */
	void open(String captions, String outFile) throws IOException {
		cap = new BufferedReader(new FileReader(captions));
		out = new PrintWriter(new FileWriter(outFile));
		pprops = new FileProperties(PROPS_FILE);
		title = pprops.getProperty("title");
		thumb_suffix = pprops.getProperty("thumb_suffix");
		image_suffix = pprops.getProperty("image_suffix");
	}

	/** read the captions file */
	void read() throws IOException {

		String line;
		while ((line = cap.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, "\t");
			String name = st.nextToken();
			String desc = st.nextToken();
			names.addElement(name);
			map.put(name, desc);
		}
		cap.close();
	}

	/** Write the HTML headers */
	void BEGIN() {
		println("<HTML>");
		println("<HEAD>");
		println("    <META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=iso-8859-1\">");
		println("    <META NAME=\"GENERATOR\" CONTENT=\"Java ImageIndex\">");
		println("    <TITLE>" + title + "</TITLE>");
		println("</HEAD>");
		println("<BODY BGCOLOR=\"" + pprops.get("bgcolor") + "\">");
		println("<H1>" + title + "</H1>");
		println("<P>All files are Copyright &copy;: All rights reserved.");
		println("</P>");
		println("<HR>");
	}

	/** Do the bulk of the work */
	void process() {

		System.out.println("Generating HTML...");
		println("<table>");

		String fn;
		for (int i=0; i<names.size(); i++) {
			fn = (String)names.elementAt(i);
			mkLink(fn, (String)map.get(fn));
		}
		println("</table>");
		System.out.println("*** process - done ***");
	}

	void mkLink(String href, String descrip) {
		// System.out.println(href + "==>" + descrip);
		String thumbnail = href + thumb_suffix;
		String image = href + image_suffix;
		print("<tr><td>");
		print("<a href=\"" + image + "\">");
		print("<img src=\"" + thumbnail + "\"></a>");
		print("</td><td>");
		print("<a href=\"" + image + "\">");
		print(descrip + "</a>");
		print("</td></tr>");
		println();
	}

	/** Write the trailers and a signature */
	void END() {
		System.out.println("Finishing the HTML");
		println("</UL>");
		println("<P>This file generated by the Java program ");
		print("<A HREF=\"ImageIndex.java\">ImageIndex</A> at ");
		println(new Date().toString());
		println("</P>");
		println("</BODY>");
		println("</HTML>");
	}

	/** Close open files */
	void close() {
		System.out.println("Closing output files...");
		if (out != null)
			out.close();
	}

	/** Convenience routine for out.print */
	void print(String s) {
		out.print(s);
	}

	/** Convenience routine for out.println */
	void println(String s) {
		out.println(s);
	}

	void println() {
		out.println();
	}

}
