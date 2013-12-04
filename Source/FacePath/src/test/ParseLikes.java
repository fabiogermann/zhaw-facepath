package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseLikes {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// class="_3rbh clearfix" data-referrer="pagelet_timeline_recent_ocm"
		try {
			ParseLikes pl = new ParseLikes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ParseLikes() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("C:/fabio.htm"));
		String everything;
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append('\n');
				line = br.readLine();
			}
			everything = sb.toString();
		} finally {
			br.close();
		}

		String likelisthtml="";

		Document doc = Jsoup.parse(everything);
		Elements elements = doc.select("._3rbh").select(".clearfix");
		for (Element e : elements) {
			if (e.outerHtml().indexOf(
					"data-referrer=\"pagelet_timeline_recent_ocm\"") > 0) {
				likelisthtml = e.html();
			}
		}

		System.out.println(likelisthtml);
	}

}
